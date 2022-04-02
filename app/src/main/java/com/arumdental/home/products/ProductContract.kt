package com.arumdental.home.products

import com.arumdental.home.products.model.ProductResponseModel

interface ProductContract {

    interface  ProductPresenter{
        fun getProducts(id:Int, search:String)
    }

    interface ProductView{
        fun onSucess(productResponseModel: ArrayList<ProductResponseModel>)
        fun onError(error: String)
    }

    interface ProductCallBack{
        fun onSucess(productResponseModel: ArrayList<ProductResponseModel>)
        fun onError(error: String)
    }

    interface  ProductIntractor{
        fun getProducts(id:Int,productCallBack: ProductCallBack, search:String)
    }
}