package com.arumdental.home.cart.clearCart.presenter

import com.arumdental.home.cart.clearCart.ClearCartContract
import com.arumdental.home.cart.clearCart.intractor.ClearCartIntractor
import okhttp3.ResponseBody

class ClearCartPresenter(var clearCartView: ClearCartContract.ClearCartView):ClearCartContract.ClearCartCallBack,
            ClearCartContract.ClearCartPresenter{

    var clearCartIntractor:ClearCartIntractor= ClearCartIntractor()
    override fun clearCart() {
        clearCartIntractor.clearCart(this)

    }

    override fun onSucessClearCart(responseBody: ResponseBody) {
        clearCartView.onSucessClearCart(responseBody)
    }

    override fun onErrorClearCart(error: String) {
        clearCartView.onErrorClearCart(error)
    }
}