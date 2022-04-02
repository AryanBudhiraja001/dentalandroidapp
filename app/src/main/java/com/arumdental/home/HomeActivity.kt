package com.arumdental.home

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.arumdental.R
import com.arumdental.authentication.BaseActivity
import com.arumdental.home.address.AddressFragment
import com.arumdental.home.brands.view.BrandsFragment
import com.arumdental.home.cart.view.CheckOutFragment
import com.arumdental.home.order.view.OrdersFragment
import com.arumdental.home.products.view.ProductFragment
import com.arumdental.home.shipping.ShippingContract
import com.arumdental.home.shipping.model.ShippingResponseModel
import com.arumdental.home.shipping.presenter.ShippingPresenter
import com.arumdental.home.wishlist.WishlistFragment
import com.arumdental.profile.ProfileContract
import com.arumdental.profile.model.ProfileResponseModel
import com.arumdental.profile.presenter.ProfilePresenter
import com.arumdental.profile.updateProfile.model.Billing
import com.arumdental.profile.view.ProfileFragment
import com.arumdental.utils.*
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.common_toolbar.*
import kotlinx.android.synthetic.main.drawer_layout.*
import kotlinx.android.synthetic.main.filter_fragment.*
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class HomeActivity : AppCompatActivity(),ProfileContract.ProfileView, View.OnClickListener, ShowToolbar,
    ShippingContract.ShippingView {
    private  var currentFrag: Fragment?=null
    private  var string:CharSequence?=null
    var shippingpresenter: ShippingPresenter?=null
    var country:String?=null



    var profilePresenter:ProfilePresenter?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        init()
    }

    private  fun init()
    {
        replaceFragmentWithOutBackStack2(BrandsFragment(), "brands frag")
        profilePresenter=ProfilePresenter(this)
        profilePresenter?.getUserProfileWP()
        showProgress(this)
   /*    profilePresenter!!.getUserProfile(UserSharedPrefences.getInstance().email)
        showProgress(this)*/
        shippingpresenter= ShippingPresenter(this)


        ivMenu.setOnClickListener(this)
        clProfileLayout.setOnClickListener(this)
        ivBackHome.setOnClickListener(this)
        ivCart.setOnClickListener(this)
        llLogout.setOnClickListener(this)
        llOrder.setOnClickListener(this)
        tvLogout.setOnClickListener(this)
        tvCart.setOnClickListener(this)
        ivSearch.setOnClickListener(this)
        ivCancel.setOnClickListener(this)
        ivSearchInner.setOnClickListener(this)
        llMyWishList.setOnClickListener(this)
        llHome.setOnClickListener(this)

        if(UserSharedPrefences.getInstance()?.name!=null)
        {
            tvUserName.setText(UserSharedPrefences.getInstance()?.name)
        }
        if(UserSharedPrefences.getInstance()?.email!=null)
        {
            tvUserEmail.setText(UserSharedPrefences.getInstance()?.email)
        }

        edSearch.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(s!!.length>0)
                {
                    string=s

                }
            }

        })
    }

    override fun onSucess(profileResponseModel: ProfileResponseModel) {
      // hideProgress()
        UserSharedPrefences.getInstance().id=profileResponseModel.body!!.get(0).ID!!.toInt()

        if(UserSharedPrefences.getInstance().id!=null)
        {
            profilePresenter!!.getUserProfileWP()
            showProgress(this)
        }

        /*if(profileResponseModel.body!!.get(0).metadata!=null)
        {
            if(profileResponseModel.body!!.get(0).metadata!!.billing_address_1!="")
            {
                var billing=Billing()
                billing.state=profileResponseModel.body!!.get(0).metadata!!.billing_state
                billing.address_1=profileResponseModel.body!!.get(0).metadata!!.billing_address_1
                billing.address_2=profileResponseModel.body!!.get(0).metadata!!.billing_address_2
                billing.country=profileResponseModel.body!!.get(0).metadata!!.billing_country
                billing.phone=profileResponseModel.body!!.get(0).metadata!!.billing_phone
                billing.postcode=profileResponseModel.body!!.get(0).metadata!!.billing_postcode
                billing.city=profileResponseModel.body!!.get(0).metadata!!.billing_city
                country=profileResponseModel.body!!.get(0).metadata!!.billing_country


                UserSharedPrefences.getInstance().saveBillingModel(billing)
                shippingpresenter!!.getShippingRated()
                showProgress(this)
            }
            else
            {
              //  Toast.makeText(this,"address not added", Toast.LENGTH_SHORT).show()
            }


        }*/


    }


    override fun onError(error: String) {
       // hideProgress()
        showSnackBar(HomeView,error)

    }

    override fun onSucessWP(responseBody: ResponseBody) {
        var addresslist:ArrayList<Billing>?= ArrayList()

        var json : String? = responseBody.string()
        println("json "+json)

        if(!json.isNullOrEmpty()){


            try{
                val jsonObject: JSONObject = JSONObject(json)
                val metaDataArray=jsonObject!!.getJSONArray("meta_data")

                println("meta day aarrya"+metaDataArray)
                if(metaDataArray!=null && metaDataArray.length()>0)
                {
                    for (i in 0 until metaDataArray.length())
                    {
                        val item: JSONObject = metaDataArray[i] as JSONObject

                        val key=item.getString("key")
                        if(key.equals("_wcmca_additional_addresses"))
                        {
                            var array: JSONArray =item.getJSONArray("value") as JSONArray

                            println("sfhkjahsfkjf "+array)

                            for( i in 0 until array.length())
                            {
                                var Innerarray: JSONObject =array[i]  as JSONObject
                                val billing=Billing()
                                billing.address_1=Innerarray.getString("address_1")
                                billing.address_2=Innerarray.getString("address_2")
                                billing.city=Innerarray.getString("city")
                                billing.state=Innerarray.getString("state")
                                billing.country=Innerarray.getString("country")
                                billing.postcode=Innerarray.getString("postcode")
                                billing.phone=Innerarray.getString("phone")
                                addresslist!!.add(billing)
                                UserSharedPrefences.getInstance().saveAddresslist(addresslist)
                                println("dhsjkvhdks "+ Gson().toJson(  UserSharedPrefences.getInstance().addresslist))
                            }



                        }



                    }
                }
            }
            catch (e: JSONException)
            {
                e.printStackTrace()
            }


        }
    }

    override fun onErrorWP(error: String) {
        hideProgress()

    }

    fun replaceFragment(frag: Fragment, TAG: String) {
        val manager = supportFragmentManager
        if (manager != null) {
            val t = manager.beginTransaction()
            currentFrag = manager.findFragmentByTag(TAG)
            if (currentFrag != null) {
                t.replace(R.id.container1, frag, TAG).commit()
                if (currentFrag != null && currentFrag == frag.javaClass) {
                    llEdittext.visibility=View.GONE
                    lltoolbar.visibility=View.VISIBLE
                    t.replace(R.id.container1, frag, TAG).addToBackStack(TAG).commit()


                }
            } else {
                llEdittext.visibility=View.GONE
                lltoolbar.visibility=View.VISIBLE
                t.replace(R.id.container1, frag, TAG).addToBackStack(TAG).commit()

            }
        }
    }
    fun replaceFragmentWithOutBackStack(frag: Fragment, TAG: String) {
        val manager = supportFragmentManager
        if (manager != null) {
            val t = manager.beginTransaction()
            currentFrag = manager.findFragmentByTag(TAG)
            if (currentFrag != null) {
                t.replace(R.id.container1, frag, TAG).commit()
                if (currentFrag != null && currentFrag == frag.javaClass) {
                    llEdittext.visibility=View.GONE
                    lltoolbar.visibility=View.VISIBLE
                    t.replace(R.id.container1, frag, TAG).addToBackStack(null).commit()
                }
            } else {
                llEdittext.visibility=View.GONE
                lltoolbar.visibility=View.VISIBLE
                t.replace(R.id.container1, frag, TAG).addToBackStack(null).commit()
            }
        }


    }
    fun replaceFragmentWithOutBackStack2(frag: Fragment, TAG: String) {
        val manager = supportFragmentManager
        if (manager != null) {
            val t = manager.beginTransaction()
            currentFrag = manager.findFragmentByTag(TAG)
            if (currentFrag != null) {
                t.replace(R.id.container1, frag, TAG).commit()
                if (currentFrag != null && currentFrag == frag.javaClass) {
                    llEdittext.visibility=View.GONE
                    lltoolbar.visibility=View.VISIBLE
                    t.replace(R.id.container1, frag, TAG).commit()
                }
            } else {
                llEdittext.visibility=View.GONE
                lltoolbar.visibility=View.VISIBLE
                t.replace(R.id.container1, frag, TAG).commit()
            }
        }
    }

    @SuppressLint("WrongConstant")
    override fun onClick(v: View?) {
        when(v)
        {
            ivMenu->
            {
                HomeView.openDrawer(Gravity.START)
            }
            clProfileLayout->
            {
                replaceFragmentWithOutBackStack(ProfileFragment(), "profile frag")
                HomeView.closeDrawers()
            }
            ivBackHome->
            {
                onBackPressed()
            }

            ivCart->
            {
                replaceFragmentWithOutBackStack(CheckOutFragment(), "checkout frag")
                HomeView.closeDrawers()

            }
            llLogout->
            {
                Logout()
                HomeView.closeDrawers()
            }
            tvLogout->
            {
                Logout()
                HomeView.closeDrawers()
            }
            tvCart->
            {
                replaceFragmentWithOutBackStack(CheckOutFragment(), "cart frag")
                HomeView.closeDrawers()
            }
            llOrder->
            {
                replaceFragmentWithOutBackStack(OrdersFragment(), "Order frag")
                HomeView.closeDrawers()
            }
            ivSearch->
            {
               lltoolbar.visibility=View.GONE
                llEdittext.visibility=View.VISIBLE

            }
            ivCancel->
            {
                lltoolbar.visibility=View.VISIBLE
                llEdittext.visibility=View.GONE
                Search("me",2)
                edSearch.setText("")
                ivSearchInner.visibility=View.VISIBLE
                ivCancel.visibility=View.GONE
            }

            ivSearchInner->
            {
                ivCancel.visibility=View.VISIBLE
                ivSearchInner.visibility=View.GONE
                if(string.isNullOrEmpty())
                {
                    Search("me",2)
                }else{
                    Search(string!!,1)
                }

            }
            llMyWishList->
            {
                replaceFragmentWithOutBackStack(WishlistFragment(), "wishlist frag")
                HomeView.closeDrawers()
            }
            llHome->
            {
                val fragment =
                    supportFragmentManager.findFragmentById(R.id.container1)
                if(fragment is BrandsFragment )
                {
                    HomeView.closeDrawers()

                }
                else
                {
                    replaceFragmentWithOutBackStack2(BrandsFragment(), "brands frag")
                    supportFragmentManager.popBackStack()
                    HomeView.closeDrawers()
                }
            }
        }
    }

    override fun changeToolbar(isshow: Boolean, name:String,showSearch:Boolean, showBack:Boolean) {
        if(!isshow)
        {
            common_toolbar.visibility=View.GONE

        }
        else{
            common_toolbar.visibility=View.VISIBLE

        }
        if(!showSearch)
        {
            ivSearch.visibility=View.GONE
        }
        else
        {
            ivSearch.visibility=View.VISIBLE
        }
        if(!showBack)
        {
            ivBackHome.visibility=View.GONE
        }
        else
        {
            ivBackHome.visibility=View.VISIBLE
        }
        if(name.equals("filter"))
        {


        }




    }

    override fun onBackPressed() {
        super.onBackPressed()
        supportFragmentManager.popBackStack()
        llEdittext.visibility=View.GONE
        lltoolbar.visibility=View.VISIBLE

    }

    private  fun Logout()
    {

        val builder =
            AlertDialog.Builder(this)
        builder.setMessage("Are you sure you want to Logout?")
            .setCancelable(false)
            .setPositiveButton("Yes", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {

                    dialog!!.dismiss()
                    UserSharedPrefences.getInstance().islogin="no"
                    UserSharedPrefences.getInstance().token=null
                    UserSharedPrefences.getInstance().saveBillingModel(null)
                    UserSharedPrefences.getInstance().saveAddresslist(null)
                    UserSharedPrefences.getInstance().isaddressselected=false
                    UserSharedPrefences.getInstance().positon=0
                     val intent= Intent(this@HomeActivity, BaseActivity::class.java)
                    startActivity(intent)
                    finish()

                }



            })
            .setNegativeButton("No", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    dialog!!.dismiss()

                }

            })
        val alert = builder.create()
        alert.show()
    }

    private  fun Search(string: CharSequence, i:Int)
    {
        val fragment =
            supportFragmentManager.findFragmentById(R.id.container1)
        if(fragment is BrandsFragment )
        {
            fragment.onSearch(string,i)

        }
        else if(fragment is ProductFragment)
        {
            fragment.onSearchProduct(string,i)
        }

    }

    override fun onSucessShipping(shippingResponseModel: ShippingResponseModel) {
      //  hideProgress()
        if(shippingResponseModel!=null)
        {
            if(shippingResponseModel.body!!.size>0)
            {
                for( i in shippingResponseModel.body!!.indices)
                {
                    if(shippingResponseModel.body!!.get(i).country.equals(country))
                    {
                        UserSharedPrefences.getInstance()!!.countrycode=shippingResponseModel.body!!.get(i).country
                        UserSharedPrefences.getInstance()!!.price=shippingResponseModel.body!!.get(i).shipping_price
                        UserSharedPrefences.getInstance()!!.ofitems=shippingResponseModel.body!!.get(i).of_items
                    }

                }
            }
        }

    }

    override fun onErrorShipping(erroe: String) {
        hideProgress()
        showSnackBar(HomeView,erroe)

    }
}