package com.arumdental.profile


import com.arumdental.profile.model.ProfileResponseModel
import okhttp3.ResponseBody

interface ProfileContract {

    interface  ProfilePresenter{
        fun getUserProfile(email:String)
        fun getUserProfileWP()
    }

    interface  ProfileView{
        fun onSucess(profileResponseModel: ProfileResponseModel)
        fun onError(error:String)

        fun onSucessWP(responseBody: ResponseBody)
        fun onErrorWP(error:String)
    }

    interface  ProfileCallBack{
        fun onSucess(profileResponseModel: ProfileResponseModel)
        fun onError(error:String)

        fun onSucessWP(responseBody: ResponseBody)
        fun onErrorWP(error:String)
    }

    interface  ProfileIntrator{
        fun getUserProfile(email:String,profileCallBack: ProfileCallBack)
        fun getUserProfileWP(profileCallBack: ProfileCallBack)
    }
}