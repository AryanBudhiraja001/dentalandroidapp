package com.arumdental.profile.updateProfile

import com.arumdental.authentication.signUp.model.response.SignUpResponse
import com.arumdental.home.address.model.AddMultipleAddressModel
import com.arumdental.profile.updateProfile.model.UpdateProfileModel
import okhttp3.ResponseBody

interface UpdateProfileContract {


    interface  UpdateProfilePresenter{
        fun updateProfile(updateProfileModel: AddMultipleAddressModel)
    }
    interface  UpdateProfileView{
        fun onSucess(signUpResponse: ResponseBody)
        fun onError(error:String)
    }

    interface  UpdatePrpfileCallBack{
        fun onSucess(signUpResponse: ResponseBody)
        fun onError(error:String)
    }

    interface  UpdateProfileIntractor{
        fun updateProfile(updateProfileModel: AddMultipleAddressModel, updatePrpfileCallBack: UpdatePrpfileCallBack)
    }


}