package com.arumdental.authentication.login

import com.arumdental.authentication.login.model.LoginResponseModel
import com.arumdental.authentication.login.model.ValidateTokenResponse

interface LoginContract {

    interface  LoginPresenter{
        fun login(username:String, password:String)
        fun ontokenValidate( )

    }

    interface  LoginView{
        fun onSucesslogin(loginResponseModel: LoginResponseModel)
        fun onfailure(error:String)

        fun onSucessValidateToken(validateTokenResponse: ValidateTokenResponse)
        fun onfailureValidateToken(error:String)
    }

    interface  LoginCallBack{
        fun onSucesslogin(loginResponseModel: LoginResponseModel)
        fun onfailure(error:String)
        fun onSucessValidateToken(validateTokenResponse: ValidateTokenResponse)
        fun onfailureValidateToken(error:String)
    }

    interface  LoginIntractor{
        fun onlogin(username:String, password:String, loginCallBack: LoginCallBack)
        fun ontokenValidate(loginCallBack: LoginCallBack)
    }
}