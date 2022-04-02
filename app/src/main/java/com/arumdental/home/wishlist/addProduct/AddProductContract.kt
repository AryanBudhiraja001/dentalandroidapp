package com.arumdental.home.wishlist.addProduct

import com.arumdental.home.wishlist.addProduct.model.AddProductResponse

interface AddProductContract {

    interface  AddProductPresenter{
        fun addProduct(userID:Int, productID:Int, price:String)
    }

    interface  AddProductCallBack{
        fun onSucessAddProduct(addProductResponse: AddProductResponse)
        fun onErrorAddProduct(error:String)
    }

    interface  AddProductView{
        fun onSucessAddProduct(addProductResponse: AddProductResponse)
        fun onErrorAddProduct(error:String)
    }
    interface  AddProductIntrator{
        fun addProduct(userID:Int, productID:Int, price:String,addProductCallBack: AddProductCallBack)
    }
}