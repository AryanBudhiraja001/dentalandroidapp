package com.arumdental.authentication.login.presenter

import android.text.TextUtils
import com.arumdental.authentication.login.LoginContract
import com.arumdental.authentication.login.intractor.LoginIntractor
import com.arumdental.authentication.login.model.LoginResponseModel
import com.arumdental.authentication.login.model.ValidateTokenResponse
import com.arumdental.utils.isValidEmailId
import kotlin.math.log

class LoginPresenter(val loginView: LoginContract.LoginView)
    :LoginContract.LoginPresenter,
        LoginContract.LoginCallBack{

    var loginIntractor:LoginContract.LoginIntractor?=LoginIntractor()
    var errorMessage:String?=null

    override fun login(username: String, password: String) {
        loginIntractor!!.onlogin(username, password, this)

    }

    override fun ontokenValidate() {
        loginIntractor!!.ontokenValidate(this)

    }

    override fun onSucesslogin(loginResponseModel: LoginResponseModel) {

        loginView.onSucesslogin(loginResponseModel)

    }

    override fun onfailure(error: String) {
        loginView.onfailure(error)

    }

    override fun onSucessValidateToken(validateTokenResponse: ValidateTokenResponse) {
        loginView.onSucessValidateToken(validateTokenResponse)

    }

    override fun onfailureValidateToken(error: String) {
        loginView.onfailureValidateToken(error)

    }

    fun validateLoginData(
        email:String,
        password:String

    ):Boolean
    {



         if(TextUtils.isEmpty(email))
        {
            errorMessage="Please select email"
            return false
        }
        else if(!isValidEmailId(email))

        {
            errorMessage="Please enter valid email"
            return false
        }


        else if(TextUtils.isEmpty(password))
        {
            errorMessage="Please enter password"
            return false
        }
        else if(password.contains(" "))
         {
             errorMessage="Please enter  valid password"
             return false
         }
        else if(password.length<6)
        {
            errorMessage="Please enter  valid password"
            return false
        }

        else{
            return true
        }


    }
}