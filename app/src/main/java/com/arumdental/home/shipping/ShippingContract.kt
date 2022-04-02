package com.arumdental.home.shipping

import com.arumdental.home.shipping.model.ShippingResponseModel

class ShippingContract {

    interface  ShippingPresenter{
        fun getShippingRated()
    }

    interface  ShippingView {
        fun onSucessShipping(shippingResponseModel: ShippingResponseModel)
        fun onErrorShipping(erroe:String)
    }

    interface  ShippingCallBack{
        fun onSucessShipping(shippingResponseModel: ShippingResponseModel)
        fun onErrorShipping(erroe:String)
    }

    interface  ShippingIntractor{
        fun getShipping(shippingCallBack: ShippingCallBack)
    }

}