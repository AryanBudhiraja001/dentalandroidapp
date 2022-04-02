package com.arumdental.profile.presenter

import com.arumdental.authentication.login.model.LoginResponseModel
import com.arumdental.authentication.signUp.model.response.SignUpResponse
import com.arumdental.profile.ProfileContract
import com.arumdental.profile.intractor.ProfileIntractor
import com.arumdental.profile.model.ProfileResponseModel
import okhttp3.ResponseBody

class ProfilePresenter(val profileView: ProfileContract.ProfileView)
    :ProfileContract.ProfileCallBack,
        ProfileContract.ProfilePresenter{

    var profileIntrator:ProfileIntractor= ProfileIntractor()
    override fun getUserProfile(email:String) {
        profileIntrator.getUserProfile(email,this)

    }

    override fun getUserProfileWP() {
        profileIntrator.getUserProfileWP(this)
    }

    override fun onSucess(profileResponseModel: ProfileResponseModel) {
        profileView.onSucess(profileResponseModel)

    }

    override fun onError(error: String) {
        profileView.onError(error)

    }

    override fun onSucessWP(responseBody: ResponseBody) {
        profileView.onSucessWP(responseBody)
    }

    override fun onErrorWP(error: String) {
       profileView.onErrorWP(error)
    }
}