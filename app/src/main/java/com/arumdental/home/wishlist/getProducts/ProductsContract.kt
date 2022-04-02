package com.arumdental.home.wishlist.getProducts

import com.arumdental.home.wishlist.getProducts.model.WishlistProductsResponseModel

interface ProductsContract {

    interface  ProductPresenter{
        fun getProducts(userId:Int)
    }

    interface  ProductCallBack{
        fun onSucessWishlist(productResponseModel:WishlistProductsResponseModel)
        fun onErrorWishlist(error:String)
    }
    interface  ProductView{
        fun onSucessWishlist(productResponseModel:WishlistProductsResponseModel)
        fun onErrorWishlist(error:String)
    }
    interface  ProductIntractor{
        fun getProducts(userId:Int, productCallBack:ProductCallBack)
    }


}