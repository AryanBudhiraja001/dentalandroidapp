package com.arumdental.home.shipping.presenter

import com.arumdental.home.shipping.ShippingContract
import com.arumdental.home.shipping.intractor.ShippingIntractor
import com.arumdental.home.shipping.model.ShippingResponseModel

class ShippingPresenter(val shippingView: ShippingContract.ShippingView):ShippingContract.ShippingPresenter,ShippingContract.ShippingCallBack {


    var shippingIntractor=ShippingIntractor()
    override fun getShippingRated() {
        shippingIntractor.getShipping(this)

    }

    override fun onSucessShipping(shippingResponseModel: ShippingResponseModel) {
       shippingView.onSucessShipping(shippingResponseModel)
    }

    override fun onErrorShipping(erroe: String) {
        shippingView.onErrorShipping(erroe)
    }
}