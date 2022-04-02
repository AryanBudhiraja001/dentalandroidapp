package com.arumdental.authentication.signUp

import com.arumdental.authentication.signUp.model.paramModel.SignUpParamModel
import com.arumdental.authentication.signUp.model.response.SignUpResponse

interface SignUpContract {

    interface  SigUpPresenter{
        fun signUp(signUpParamModel: SignUpParamModel)
    }
    interface  SignUpView{
        fun onSucessSignUp(signUpResponse:SignUpResponse)
        fun onErrorSignUp(error:String)
    }
    interface SignUpCallBack{
        fun onSucessSignUp(signUpResponse:SignUpResponse)
        fun onErrorSignUp(error:String)
    }

    interface SignUpIntractor{
        fun signUp(signUpParamModel: SignUpParamModel,
                   ResponseBody: SignUpCallBack)
    }

}