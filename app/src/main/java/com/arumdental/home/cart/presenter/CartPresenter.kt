package com.arumdental.home.cart.presenter

import com.arumdental.home.cart.CartContract
import com.arumdental.home.cart.intractor.CartIntractor
import com.arumdental.home.cart.model.AddToCartResponse
import com.arumdental.home.cart.model.UpdateCartResponse
import okhttp3.ResponseBody

class CartPresenter(var cartView: CartContract.CartView):CartContract.CartPresenter,
        CartContract.CartCallBack{

    var cartIntractor:CartIntractor?= CartIntractor()
    override fun addToCart(quantity: Int, product_id: Int) {
        cartIntractor!!.addToCart(quantity,product_id,this)

    }

    override fun updateCart(quantity: Int, cart_item_key: String) {
        cartIntractor!!.updateCart(quantity,cart_item_key,this)

    }

    override fun getCart() {
        cartIntractor!!.getCart(this)

    }

    override fun onSucessAddTOCart(addToCartResponse: AddToCartResponse) {
        cartView.onSucessAddTOCart(addToCartResponse)

    }

    override fun onErrorAddTOCart(error: String) {
        cartView.onErrorAddTOCart(error)

    }

    override fun onSucessUpdateCart(updateCartResponse: UpdateCartResponse) {
        cartView.onSucessUpdateCart(updateCartResponse)

    }

    override fun onErrorUpdateCart(error: String) {
        cartView.onErrorAddTOCart(error)

    }

    override fun onSucessCart(responseBody: ResponseBody) {
        cartView.onSucessCart(responseBody)

    }

    override fun onError(error: String) {
        cartView.onError(error)

    }
}