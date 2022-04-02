package com.arumdental.profile.updateProfile.presenter

import android.text.TextUtils
import com.arumdental.authentication.signUp.model.response.SignUpResponse
import com.arumdental.home.address.model.AddMultipleAddressModel
import com.arumdental.profile.updateProfile.UpdateProfileContract
import com.arumdental.profile.updateProfile.intractor.UpdateProfileIntarctor
import com.arumdental.profile.updateProfile.model.UpdateProfileModel
import com.arumdental.utils.isValidEmailId
import okhttp3.ResponseBody

class UpdateProfilePresenter(val updateProfileView: UpdateProfileContract.UpdateProfileView)

    :UpdateProfileContract.UpdatePrpfileCallBack,
    UpdateProfileContract.UpdateProfilePresenter{


    var updateProfileIntractor=UpdateProfileIntarctor()
    var errorMessage:String?=null

    override fun updateProfile(updateProfileModel: AddMultipleAddressModel) {
        updateProfileIntractor.updateProfile(updateProfileModel, this)

    }

    override fun onSucess(signUpResponse: ResponseBody) {
        updateProfileView.onSucess(signUpResponse)
    }

    override fun onError(error: String) {
        updateProfileView.onError(error)
    }


    fun validateAddressDate(
        name:String,
        address1:String,
        address2:String,
        phone:String,
        postalocde:String,
        state:String,
        city:String,
        country:String

    ):Boolean {

        if (TextUtils.isEmpty(name)) {
            errorMessage = "Please enter  name"
            return false

        } else if (TextUtils.isEmpty(address1)) {
            errorMessage = "Please enter address1"
            return false
        } else if (TextUtils.isEmpty(address2)) {
            errorMessage = "Please enter address2"
            return false
        } else if (TextUtils.isEmpty(phone)) {
            errorMessage = "Please enter phone number"
            return false
        } else if (phone.length != 10) {
            errorMessage = "Please enter valid phone number"
            return false
        } else if (TextUtils.isEmpty(postalocde)) {
            errorMessage = "Please enter postalCode"
            return false
        } else if (TextUtils.isEmpty(city)) {
            errorMessage = "Please enter city"
            return false

        }
        else if (TextUtils.isEmpty(country)) {
            errorMessage = "Please enter country"
            return false

        }
        else if(country.equals("Select your Country"))
        {
            errorMessage="Please select country"
            return false
        }
        else {
            return true
        }
    }

    }