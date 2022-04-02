package com.arumdental.home.wishlist.getProductDetail

import com.arumdental.home.products.model.ProductResponseModel

interface ProductDetailContract {

    interface  ProductDetailPresenter{
        fun getProductDetail(id:String)
    }

    interface  ProductDetailView{
        fun onSucessProductDetail(productResponseModellist: ArrayList<ProductResponseModel>)
        fun onErrorProductDetail(error:String)
    }

    interface ProductDetailCallBack{
        fun onSucessProductDetail(productResponseModellist: ArrayList<ProductResponseModel>)
        fun onErrorProductDetail(error:String)
    }

    interface  ProductDetailIntractor{
        fun getProductDetail(id:String,productDetailCallBack: ProductDetailCallBack)
    }


}