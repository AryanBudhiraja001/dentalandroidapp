package com.arumdental.home.cart.clearCart

import okhttp3.ResponseBody

interface ClearCartContract {

    interface  ClearCartPresenter{
        fun clearCart()
    }

    interface ClearCartView{
        fun onSucessClearCart(responseBody: ResponseBody)
        fun onErrorClearCart(error:String)
    }

    interface  ClearCartCallBack{
        fun onSucessClearCart(responseBody: ResponseBody)
        fun onErrorClearCart(error:String)
    }

    interface  ClearCartIntractor{
        fun clearCart(clearCartCallBack: ClearCartCallBack)
    }
}