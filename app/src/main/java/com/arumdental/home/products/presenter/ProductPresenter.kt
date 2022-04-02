package com.arumdental.home.products.presenter

import com.arumdental.home.products.ProductContract
import com.arumdental.home.products.intractor.ProductIntractor
import com.arumdental.home.products.model.ProductResponseModel

class ProductPresenter(val productView: ProductContract.ProductView):ProductContract.ProductPresenter,
ProductContract.ProductCallBack{

    var productIntractor:ProductIntractor= ProductIntractor()
    override fun getProducts(id:Int, search:String) {
        productIntractor.getProducts(id,this,search)
    }

    override fun onSucess(productResponseModel: ArrayList<ProductResponseModel>) {
        productView.onSucess(productResponseModel)

    }

    override fun onError(error: String) {
        productView.onError(error)

    }
}