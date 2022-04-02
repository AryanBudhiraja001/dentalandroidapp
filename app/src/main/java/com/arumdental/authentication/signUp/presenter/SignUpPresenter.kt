package com.arumdental.authentication.signUp.presenter

import android.text.TextUtils
import com.arumdental.authentication.signUp.SignUpContract
import com.arumdental.authentication.signUp.intractor.SignUpIntractor
import com.arumdental.authentication.signUp.model.paramModel.SignUpParamModel
import com.arumdental.authentication.signUp.model.response.SignUpResponse
import com.arumdental.utils.isValidEmailId

class SignUpPresenter(val signUpView:SignUpContract.SignUpView):
        SignUpContract.SigUpPresenter,
            SignUpContract.SignUpCallBack{

    var errorMessage:String?=null

    var signUpIntractor:SignUpContract.SignUpIntractor=SignUpIntractor()



    override fun signUp(signUpParamModel: SignUpParamModel) {

        signUpIntractor.signUp(signUpParamModel, this)

    }

    override fun onSucessSignUp(signUpResponse: SignUpResponse) {
        signUpView.onSucessSignUp(signUpResponse)

    }

    override fun onErrorSignUp(error: String) {
        signUpView.onErrorSignUp(error)

    }

    fun validateCreateAccountData(
        firstName:String,
        lastName:String,
        businessName:String,
        email:String,
        phone:String,
        password:String,
        confirmPassword:String

    ):Boolean
    {

        if(TextUtils.isEmpty(firstName))
        {
            errorMessage="Please enter first name"
            return false

        }
        else if(TextUtils.isEmpty(lastName))
        {
            errorMessage="Please enter last name"
            return false
        }
        else if(TextUtils.isEmpty(businessName))
        {
            errorMessage="Please enter business name"
            return false
        }

        else if(TextUtils.isEmpty(email))
        {
            errorMessage="Please select email"
            return false
        }
        else if(!isValidEmailId(email))

            {
                errorMessage="Please enter valid email"
                return false
            }

        else if(TextUtils.isEmpty(phone))
        {
            errorMessage="Please enter phone number"
            return false
        }
        else if(phone.length!=10)
        {
            errorMessage="Please enter valid phone number"
            return false
        }

        else if(TextUtils.isEmpty(password))
        {
            errorMessage="Please enter password"
            return false
        }
        else if(password.length<6)
        {
            errorMessage="Please enter  valid password"
            return false
        }
        else if(TextUtils.isEmpty(confirmPassword))
        {
            errorMessage="Please enter confirm password"
            return false
        }
        else if(!password.equals(confirmPassword))
        {
            errorMessage="Password and confirm password do not match"
            return false
        }
        else{
            return true
        }


    }


}