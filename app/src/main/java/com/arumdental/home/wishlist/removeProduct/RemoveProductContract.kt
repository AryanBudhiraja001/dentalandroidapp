package com.arumdental.home.wishlist.removeProduct

import com.arumdental.home.wishlist.addProduct.model.AddProductResponse

interface RemoveProductContract {
    interface  RemoveProductPresenter{
        fun removeProduct(productID:Int)
    }

    interface  RemoveProductCallBack{
        fun onSucessRemoveProduct(addProductResponse: AddProductResponse)
        fun onErrorRemoveProduct(error:String)
    }

    interface RemoveProductView{
        fun onSucessRemoveProduct(addProductResponse: AddProductResponse)
        fun onErrorRemoveProduct(error:String)
    }
    interface  RemoveProductIntrator{
        fun removeProduct(productID:Int,removeProductCallBack: RemoveProductCallBack)
    }

}