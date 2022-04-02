package com.arumdental.home.address

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.arumdental.R
import com.arumdental.home.HomeActivity
import com.arumdental.home.address.adapter.AllAddressAdapter
import com.arumdental.home.address.model.AddMultipleAddressModel
import com.arumdental.home.address.model.MetaData1
import com.arumdental.home.shipping.ShippingContract
import com.arumdental.home.shipping.model.ShippingResponseModel
import com.arumdental.home.shipping.presenter.ShippingPresenter
import com.arumdental.profile.updateProfile.UpdateProfileContract
import com.arumdental.profile.updateProfile.model.Billing
import com.arumdental.profile.updateProfile.presenter.UpdateProfilePresenter
import com.arumdental.utils.*
import com.google.gson.Gson
import kotlinx.android.synthetic.main.address_layout.*
import kotlinx.android.synthetic.main.all_address_fragment.*
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class AllAddressFragment:Fragment(), View.OnClickListener, AllAddressAdapter.onClick , ShippingContract.ShippingView,
    UpdateProfileContract.UpdateProfileView{
    var showToolbar: ShowToolbar?=null
    var shippingpresenter: ShippingPresenter?=null
    var country:String?=null
    var updateProfilePresenter: UpdateProfilePresenter?=null



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.all_address_fragment, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        showToolbar=context as HomeActivity
    }
    private  fun init()
    {
        ivBackAllAddress.setOnClickListener(this)
        llAddAddress.setOnClickListener(this)
        tvSubmit.setOnClickListener(this)
        shippingpresenter= ShippingPresenter(this)
        updateProfilePresenter= UpdateProfilePresenter(this)
        showToolbar!!.changeToolbar(false, "profile",false, true)
        if(UserSharedPrefences.getInstance().addresslist!=null && UserSharedPrefences.getInstance().addresslist.size>0) {
            tvNoAddressFound.visibility=View.GONE
            rvAddress.visibility=View.VISIBLE

            var adapter=AllAddressAdapter(context!!, UserSharedPrefences.getInstance().addresslist)
            adapter.setOnClickListener(this)
            rvAddress.adapter=adapter
        }
        else{
            tvNoAddressFound.visibility=View.VISIBLE
            rvAddress.visibility=View.GONE
        }
    }

    override fun onClick(v: View?) {
        when(v)
        {
            ivBackAllAddress->
            {
                activity!!.supportFragmentManager.popBackStack()
            }
            llAddAddress->
            {
                var args=Bundle()
                var fragment= AddressFragment()
                fragment.arguments=args
                (context as HomeActivity).replaceFragment(fragment,"address frag")
            }
            tvSubmit->
            {
                activity!!.supportFragmentManager.popBackStack()
            }

        }
    }

    override fun onItemClicListner(position: Int, list: ArrayList<Billing>) {
        UserSharedPrefences.getInstance().saveBillingModel(list.get(position))
        UserSharedPrefences.getInstance().isaddressselected = true
        UserSharedPrefences.getInstance().positon = position
        country=UserSharedPrefences.getInstance().billingModel.country
        shippingpresenter!!.getShippingRated()


        println("billing model"+ Gson().toJson(UserSharedPrefences.getInstance().billingModel))
        //activity!!.supportFragmentManager.popBackStack()
    }

    override fun onItemClicListnerEdit(position: Int, list: ArrayList<Billing>) {
        var args=Bundle()
        args.putSerializable("coming","edit")
        args.putInt("position", position)
        var fragment= AddressFragment()
        fragment.arguments=args
        (context as HomeActivity).replaceFragmentWithOutBackStack(fragment!!,"address frag")
    }

    override fun onItemClicListnerDelete(position: Int, list: ArrayList<Billing>) {
        if(position!=null)
        {
            val addMultipleAddressModel = AddMultipleAddressModel()
           var  addresslist = UserSharedPrefences.getInstance().addresslist
            val billing = Billing()
            val metadata = MetaData1()
            var metaaData: ArrayList<MetaData1>? = ArrayList()

            metadata.key = "_wcmca_additional_addresses"

            addresslist.removeAt(position)
            metadata.value = addresslist
            metaaData!!.add(metadata)
            addMultipleAddressModel.meta_data = metaaData!!


            updateProfilePresenter!!.updateProfile(addMultipleAddressModel)
            showProgress(context!!)
        }

    }

    override fun onSucessShipping(shippingResponseModel: ShippingResponseModel) {
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

    }

    override fun onSucess(signUpResponse: ResponseBody) {
        hideProgress()

        var addresslist:ArrayList<Billing>?= ArrayList()

        var json : String? = signUpResponse.string()
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
                                println("dhsjkvhdks "+Gson().toJson( UserSharedPrefences.getInstance()!!.addresslist))
                            }



                        }



                    }
                }
            }
            catch (e: JSONException)
            {
                e.printStackTrace()
            }
            var adapter=AllAddressAdapter(context!!, UserSharedPrefences.getInstance().addresslist)
            adapter.setOnClickListener(this)
            rvAddress.adapter=adapter


        }


        /* UserSharedPrefences.getInstance().saveBillingModel(signUpResponse.billing)
         println("jfhsjfhksdfs"+Gson().toJson(UserSharedPrefences.getInstance().billingModel))
         showSnackBar(AddressLayout, "Address added")
         activity!!.supportFragmentManager.popBackStack()*/

    }

    override fun onError(error: String) {
        hideProgress()
        showSnackBar(AddressLayout,error)
    }
}