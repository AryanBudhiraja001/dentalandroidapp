package com.arumdental.home.payment

import okhttp3.ResponseBody

interface PaymentContract {

    interface  PaymentPresenter{
        fun makePayment(token:String, amount:Double, orderId:Int)
    }

    interface  PaymentView{
        fun onSucess(responseBody: ResponseBody)
        fun onError(error:String)
    }
    interface  PaymentCallBack{
        fun onSucess(responseBody: ResponseBody)
        fun onError(error:String)
    }

    interface  PaymentIntractor{
        fun makePayment(token:String, amount:Double, orderId:Int, paymentCallBack: PaymentCallBack)
    }


}