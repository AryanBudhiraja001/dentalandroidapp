package com.arumdental.home

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.CompoundButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.arumdental.R
import com.arumdental.home.changePassword.ChangePasswordResponse
import com.arumdental.home.shipping.ShippingContract
import com.arumdental.home.shipping.adapter.ShippingCountriesAdapter
import com.arumdental.home.shipping.model.ShippingBodyItem
import com.arumdental.home.shipping.model.ShippingResponseModel
import com.arumdental.home.shipping.presenter.ShippingPresenter
import com.arumdental.profile.model.ContactParamModel
import com.arumdental.profile.model.ContactResponseModel
import com.arumdental.utils.*
import com.arumdental.web.RetrofitClient
import kotlinx.android.synthetic.main.activity_contact_form.*
import kotlinx.android.synthetic.main.address_layout.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ContactFormActivity : Fragment(), View.OnClickListener, ShippingContract.ShippingView {

    var errorMessage: String? = null
    var shippingpresenter: ShippingPresenter? = null
    var country: String? = "Select your Country"
    var isSubmitClicked: Boolean? = false
    var options:String?=null


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_contact_form, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ivBackHome.setOnClickListener(this)
        tvSendMessage.setOnClickListener(this)
        init()


    }

    private fun init() {
        shippingpresenter = ShippingPresenter(this)
        shippingpresenter?.getShippingRated()
        showProgress(context!!)

        rvDentalLab.setOnCheckedChangeListener(object:CompoundButton.OnCheckedChangeListener{
            override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
                if(p1)
                {
                    options= rvDentalLab.text.toString()
                }
            }

        })
        rvSupplier.setOnCheckedChangeListener(object:CompoundButton.OnCheckedChangeListener{
            override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
                if(p1)
                {
                    options= rvSupplier.text.toString()
                }
            }

        })
        rvSurgery.setOnCheckedChangeListener(object:CompoundButton.OnCheckedChangeListener{
            override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
                if(p1)
                {
                    options= rvSurgery.text.toString()
                }
            }

        })
        rvOther.setOnCheckedChangeListener(object:CompoundButton.OnCheckedChangeListener{
            override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
                if(p1)
                {
                    options= rvOther.text.toString()
                }
            }

        })

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

    }

    override fun onClick(p0: View?) {
        when (p0) {
            ivBackHome -> {
                activity!!.supportFragmentManager.popBackStack()
            }

            tvSendMessage -> {
                if (validateCreateAccountData(country!!,
                                etCompany.text.toString().trim(),
                                etName.text.toString().trim(),
                                etEmail.text.toString().trim(),
                                etSubject.text.toString().trim(),
                                etTelephone.text.toString().trim(),
                                etMessage.text.toString().trim())) {

                    val body = ContactParamModel()
                    body.country = country
                    body.company = etCompany.text.toString().trim()
                    body.your_name = etName.text.toString().trim()
                    body.email = etEmail.text.toString().trim()
                    body.subject = etSubject.text.toString().trim()
                    body.telephone = etTelephone.text.toString().trim()
                    body.message = etMessage.text.toString().trim()
                    showProgress(context!!)
                    RetrofitClient.getInstance().getapi().contact(country!!,
                            etCompany.text.toString().trim(), options!!, etName.text.toString().trim(), etEmail.text.toString().trim(),
                            etSubject.text.toString().trim(), etTelephone.text.toString().trim(), etMessage.text.toString().trim()
                    ).enqueue(object : Callback<ContactResponseModel> {
                        override fun onResponse(call: Call<ContactResponseModel>, response: Response<ContactResponseModel>) {

                            hideProgress()
                            if (response.isSuccessful && response.code() == 200) {
                                Toast.makeText(context, response.body()!!.message, Toast.LENGTH_SHORT).show()
                                activity!!.supportFragmentManager.popBackStack()
                            }
                        }

                        override fun onFailure(call: Call<ContactResponseModel>, t: Throwable) {
                            hideProgress()
                        }

                    })

                } else {
                    Toast.makeText(context!!, errorMessage.toString(), Toast.LENGTH_SHORT).show()
                }
            }

        }
    }


    fun validateCreateAccountData(
            country: String,
            company: String,
            your_name: String,
            email: String,
            subject: String,
            telephone: String,
            message: String

    ): Boolean {

        if (TextUtils.isEmpty(country)) {
            errorMessage = "Please enter country"
            return false

        } else if (country.equals("Select your Country")) {
            errorMessage = "Please select country"
            return false
        } else if (TextUtils.isEmpty(company)) {
            errorMessage = "Please enter company"
            return false
        } else if (TextUtils.isEmpty(your_name)) {
            errorMessage = "Please enter your name"
            return false
        } else if (TextUtils.isEmpty(email)) {
            errorMessage = "Please select email"
            return false
        } else if (!isValidEmailId(email)) {
            errorMessage = "Please enter valid email"
            return false
        } else if (TextUtils.isEmpty(subject)) {
            errorMessage = "Please enter subject"
            return false
        } else if (TextUtils.isEmpty(telephone)) {
            errorMessage = "Please enter telephone"
            return false
        } else if (TextUtils.isEmpty(message)) {
            errorMessage = "Please enter message"
            return false
        } else {
            return true
        }


    }

    override fun onSucessShipping(shippingResponseModel: ShippingResponseModel) {
        hideProgress()
        shippingResponseModel.body?.sortBy { it.country }
        val shippingBodyItem = ShippingBodyItem()
        shippingBodyItem.country = "Select your Country"
        shippingResponseModel.body?.add(0, shippingBodyItem)
        val shippingCountriesAdapter = ShippingCountriesAdapter(context!!, shippingResponseModel.body)
        CountryList.adapter = shippingCountriesAdapter
        CountryList.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {


            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                if (position != 0) {
                    val selectedItem: ShippingBodyItem =
                            parent!!.selectedItem as ShippingBodyItem

                    country = selectedItem.country

                } else {

                    if (isSubmitClicked!!) {
                        //Toast.makeText(context,"Please select country", Toast.LENGTH_SHORT).show()
                        isSubmitClicked = false
                    }


                }


            }

        }
    }

    override fun onErrorShipping(erroe: String) {
        hideProgress()
        //showSnackBar(AddressLayout, erroe)
    }
}

