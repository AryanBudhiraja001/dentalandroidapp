package com.arumdental.home.address

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.arumdental.R
import com.arumdental.authentication.signUp.model.response.SignUpResponse
import com.arumdental.home.HomeActivity
import com.arumdental.home.address.model.AddMultipleAddressModel
import com.arumdental.home.address.model.MetaData1
import com.arumdental.home.shipping.ShippingContract
import com.arumdental.home.shipping.adapter.ShippingCountriesAdapter
import com.arumdental.home.shipping.model.ShippingBodyItem
import com.arumdental.home.shipping.model.ShippingResponseModel
import com.arumdental.home.shipping.presenter.ShippingPresenter
import com.arumdental.profile.updateProfile.UpdateProfileContract
import com.arumdental.profile.updateProfile.model.Billing
import com.arumdental.profile.updateProfile.model.UpdateProfileModel
import com.arumdental.profile.updateProfile.presenter.UpdateProfilePresenter
import com.arumdental.utils.*
import com.google.gson.Gson
import com.google.gson.JsonArray
import kotlinx.android.synthetic.main.address_layout.*
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class AddressFragment :Fragment(), View.OnClickListener,UpdateProfileContract.UpdateProfileView, ShippingContract.ShippingView {

    var updateProfilePresenter:UpdateProfilePresenter?=null
    var showToolbar:ShowToolbar?=null
    var userSharedPrefences:UserSharedPrefences?=null
    var countrylist:ArrayList<String>?=null
    var shippingpresenter:ShippingPresenter?=null
    var isSubmitClicked:Boolean?=false
    var isComingFromEdit:Boolean?=false
    var addressPosition:Int?=null
    var country:String?="Select your Country"


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.address_layout, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private  fun init()
    {

            shippingpresenter= ShippingPresenter(this)
        shippingpresenter?.getShippingRated()
        showProgress(context!!)

        userSharedPrefences=UserSharedPrefences.getInstance();
        updateProfilePresenter= UpdateProfilePresenter(this)
        tvSubmitAddress.setOnClickListener(this)
        //showToolbar!!.changeToolbar(false,"address frag", false, true)
        ivBackAddress.setOnClickListener(this)
        if(userSharedPrefences!!.name!=null)
        {
            etYourName.setText(userSharedPrefences!!.name)
        }

        if(arguments!=null)
        {

            if(arguments!!.getString("coming").equals("edit"))
            {
                isComingFromEdit=true
                addressPosition=arguments!!.getInt("position")
                if(userSharedPrefences!!.addresslist!=null)
                {
                    if(addressPosition!=null)
                    {
                        etAddress1.setText(userSharedPrefences!!.addresslist.get(addressPosition!!).address_1)
                        etAddress2.setText(userSharedPrefences!!.addresslist.get(addressPosition!!).address_2)
                        etPostalCode.setText(userSharedPrefences!!.addresslist.get(addressPosition!!).postcode)
                        etPhoneNumber.setText(userSharedPrefences!!.addresslist.get(addressPosition!!).phone)
                        etCity.setText(userSharedPrefences!!.addresslist.get(addressPosition!!).city)
                        etState.setText(userSharedPrefences!!.addresslist.get(addressPosition!!).state)
                    }

                }


            }
            else
            {
                isComingFromEdit=false
            }
        }


    }



    override fun onAttach(context: Context) {
        super.onAttach(context)
        showToolbar=context as HomeActivity
    }

    override fun onClick(v: View?) {
      when(v)
      {
          tvSubmitAddress -> {
              isSubmitClicked = true
              if (updateProfilePresenter!!.validateAddressDate(
                      etYourName.text.toString().trim(),
                      etAddress1.text.toString().trim(),
                      etAddress2.text.toString().trim(),
                      etPhoneNumber.text.toString().trim(),
                      etPostalCode.text.toString().trim(),
                      etState.text.toString().trim(),
                      etCity.text.toString().trim(),
                   country!!
                  )
              ) {


                  val addMultipleAddressModel = AddMultipleAddressModel()

                  var addresslist: ArrayList<Billing>? = ArrayList()
                  var metaaData: ArrayList<MetaData1>? = ArrayList()

                  if (userSharedPrefences?.addresslist != null && userSharedPrefences?.addresslist!!.size > 0) {

                      if (isComingFromEdit!!)
                      {
                          addresslist = userSharedPrefences?.addresslist
                          val billing = Billing()
                          billing.address_1 = etAddress1.text.toString().trim()
                          billing.address_2 = etAddress2.text.toString().trim()
                          billing.country = userSharedPrefences!!.countrycode
                          billing!!.city = etCity.text.toString().trim()
                          billing!!.state = etState.text.toString().trim()
                          billing!!.postcode = etPostalCode.text.toString().trim()
                          billing!!.phone = etPhoneNumber.text.toString().trim()
                          addresslist!!.removeAt(addressPosition!!)
                          addresslist!!.add(billing)


                       /*   userSharedPrefences?.addresslist?.get(addressPosition!!)!!.address_1 =
                              etAddress1.text.toString().trim()
                          userSharedPrefences?.addresslist?.get(addressPosition!!)!!.address_2 =
                              etAddress2.text.toString().trim()
                          userSharedPrefences?.addresslist?.get(addressPosition!!)!!.country =
                              userSharedPrefences!!.countrycode
                          userSharedPrefences?.addresslist?.get(addressPosition!!)!!.city =
                              etCity.text.toString().trim()
                          userSharedPrefences?.addresslist?.get(addressPosition!!)!!.state =
                              etState.text.toString().trim()
                          userSharedPrefences?.addresslist?.get(addressPosition!!)!!.phone =
                              etPhoneNumber.text.toString().trim()
                          userSharedPrefences?.addresslist?.get(addressPosition!!)!!.postcode =
                              etPostalCode.text.toString().trim()*/

                          println("dfdskjfsf"+Gson().toJson(addresslist))
                          val metadata = MetaData1()
                          metadata.key = "_wcmca_additional_addresses"
                          metadata.value =addresslist
                          metaaData!!.add(metadata)
                          addMultipleAddressModel.meta_data = metaaData!!

                          userSharedPrefences?.phone = etPhoneNumber.text.toString().trim()

                          updateProfilePresenter!!.updateProfile(addMultipleAddressModel)
                          showProgress(context!!)
                      } else {
                          addresslist = userSharedPrefences?.addresslist
                          val billing = Billing()
                          val metadata = MetaData1()
                          metadata.key = "_wcmca_additional_addresses"


                          billing.address_1 = etAddress1.text.toString().trim()
                          billing!!.address_2 = etAddress2.text.toString().trim()
                          billing!!.country = userSharedPrefences!!.countrycode
                          billing!!.city = etCity.text.toString().trim()
                          billing!!.state = etState.text.toString().trim()
                          billing!!.postcode = etPostalCode.text.toString().trim()
                          billing!!.phone = etPhoneNumber.text.toString().trim()
                          addresslist!!.add(billing)
                          metadata.value = addresslist
                          metaaData!!.add(metadata)
                          addMultipleAddressModel.meta_data = metaaData!!

                          userSharedPrefences?.phone = etPhoneNumber.text.toString().trim()

                          updateProfilePresenter!!.updateProfile(addMultipleAddressModel)
                          showProgress(context!!)
                      }

                  }
                  else
                  {
                      addresslist = ArrayList()
                      val billing = Billing()
                      val metadata = MetaData1()
                      metadata.key = "_wcmca_additional_addresses"


                      billing.address_1 = etAddress1.text.toString().trim()
                      billing!!.address_2 = etAddress2.text.toString().trim()
                      billing!!.country = userSharedPrefences!!.countrycode
                      billing!!.city = etCity.text.toString().trim()
                      billing!!.state = etState.text.toString().trim()
                      billing!!.postcode = etPostalCode.text.toString().trim()
                      billing!!.phone = etPhoneNumber.text.toString().trim()
                      addresslist!!.add(billing)
                      metadata.value = addresslist
                      metaaData!!.add(metadata)
                      addMultipleAddressModel.meta_data = metaaData!!

                      userSharedPrefences?.phone = etPhoneNumber.text.toString().trim()

                      updateProfilePresenter!!.updateProfile(addMultipleAddressModel)
                      showProgress(context!!)
                  }

              }
              else {
                  showSnackBar(AddressLayout, updateProfilePresenter!!.errorMessage!!)
              }

          }


          ivBackAddress->
          {
              activity!!.supportFragmentManager.popBackStack()
          }
      }
    }

    override fun onSucess(signUpResponse: ResponseBody) {
      hideProgress()

        var addresslist:ArrayList<Billing>?= ArrayList()

            var json : String? = signUpResponse.string()
            println("json "+json)

        if(!json.isNullOrEmpty()){


            try{
                val jsonObject: JSONObject= JSONObject(json)
                val metaDataArray=jsonObject!!.getJSONArray("meta_data")

                println("meta day aarrya"+metaDataArray)
                if(metaDataArray!=null && metaDataArray.length()>0)
                {
                    for (i in 0 until metaDataArray.length())
                    {
                     val item:JSONObject = metaDataArray[i] as JSONObject

                        val key=item.getString("key")
                        if(key.equals("_wcmca_additional_addresses"))
                        {
                            var array:JSONArray=item.getJSONArray("value") as JSONArray

                            println("sfhkjahsfkjf "+array)

                            for( i in 0 until array.length())
                            {
                                var Innerarray:JSONObject=array[i]  as JSONObject
                                val billing=Billing()
                                billing.address_1=Innerarray.getString("address_1")
                                billing.address_2=Innerarray.getString("address_2")
                                billing.city=Innerarray.getString("city")
                                billing.state=Innerarray.getString("state")
                                billing.country=Innerarray.getString("country")
                                billing.postcode=Innerarray.getString("postcode")
                                billing.phone=Innerarray.getString("phone")
                                addresslist!!.add(billing)
                                userSharedPrefences!!.saveAddresslist(addresslist)
                                println("dhsjkvhdks "+Gson().toJson(userSharedPrefences!!.addresslist))
                            }



                        }



                    }
                }
            }
            catch (e:JSONException)
            {
                e.printStackTrace()
            }


        }
        activity!!.supportFragmentManager.popBackStack()

           /* UserSharedPrefences.getInstance().saveBillingModel(signUpResponse.billing)
            println("jfhsjfhksdfs"+Gson().toJson(UserSharedPrefences.getInstance().billingModel))
            showSnackBar(AddressLayout, "Address added")
            activity!!.supportFragmentManager.popBackStack()*/

    }

    override fun onError(error: String) {
      hideProgress()
        showSnackBar(AddressLayout,error)
    }

    override fun onSucessShipping(shippingResponseModel: ShippingResponseModel) {
        hideProgress()
        var shippinglist:ArrayList<ShippingBodyItem>?= ArrayList()

        shippingResponseModel.body?.sortBy { it.country }
        var shippingBodyItem=ShippingBodyItem()
        shippingBodyItem.country="Select your Country"
        shippingResponseModel.body?.add(0,shippingBodyItem)
        var shippingCountriesAdapter=ShippingCountriesAdapter(context!!,shippingResponseModel.body)
        spCountry.adapter=shippingCountriesAdapter
       spCountry.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {



            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                if(position!=0)
                {
                    val selectedItem: ShippingBodyItem =
                        parent!!.selectedItem as ShippingBodyItem
                    userSharedPrefences!!.countrycode=selectedItem.country
                    userSharedPrefences!!.price=selectedItem.shipping_price
                    userSharedPrefences!!.ofitems=selectedItem.of_items
                    country=selectedItem.country

                }
                else {

                    if(isSubmitClicked!!)
                    {
                        //Toast.makeText(context,"Please select country", Toast.LENGTH_SHORT).show()
                        isSubmitClicked=false
                    }


                }


            }

        }


    }

    override fun onErrorShipping(erroe: String) {
        hideProgress()
        showSnackBar(AddressLayout, erroe)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }


}