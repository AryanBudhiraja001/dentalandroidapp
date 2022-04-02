package com.arumdental.authentication.forgotPassword

import okhttp3.ResponseBody

interface fpContract {

    interface fpPresenter{
        fun fp(email:String)
    }
    interface  fpView{
        fun onSucess(string: String)
        fun onError(error:String)
    }

    interface  fpCallback{
        fun onSucess(string: String)
        fun onError(error:String)
    }

    interface  fpIntractor{
        fun fp(email:String,fpCallback: fpCallback)
    }
}