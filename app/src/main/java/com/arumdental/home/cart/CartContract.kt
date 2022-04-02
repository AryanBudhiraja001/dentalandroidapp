package com.arumdental.home.cart

import com.arumdental.home.cart.model.AddToCartResponse
import com.arumdental.home.cart.model.UpdateCartResponse
import okhttp3.ResponseBody

class CartContract{

    interface  CartPresenter{
        fun addToCart(quantity:Int,product_id:Int)
        fun updateCart(quantity:Int,cart_item_key:String)
        fun getCart()
    }

    interface CartView{
        fun onSucessAddTOCart(addToCartResponse: AddToCartResponse)
        fun onErrorAddTOCart(error:String)

        fun onSucessUpdateCart(updateCartResponse: UpdateCartResponse)
        fun onErrorUpdateCart(error:String)

        fun onSucessCart(responseBody: ResponseBody)
        fun onError(error: String)

    }

    interface CartCallBack{
        fun onSucessAddTOCart(addToCartResponse: AddToCartResponse)
        fun onErrorAddTOCart(error:String)

        fun onSucessUpdateCart(updateCartResponse: UpdateCartResponse)
        fun onErrorUpdateCart(error:String)

        fun onSucessCart(responseBody: ResponseBody)
        fun onError(error: String)

    }

    interface  CartIntractor{
        fun addToCart(quantity:Int,product_id:Int, cartCallBack: CartCallBack)
        fun updateCart(quantity:Int,cart_item_key:String, cartCallBack: CartCallBack)
        fun getCart(cartCallBack: CartCallBack)
    }


}