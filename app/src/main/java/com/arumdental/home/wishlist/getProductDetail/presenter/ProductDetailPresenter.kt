package com.arumdental.home.wishlist.getProductDetail.presenter

import com.arumdental.home.products.model.ProductResponseModel
import com.arumdental.home.wishlist.getProductDetail.ProductDetailContract
import com.arumdental.home.wishlist.getProductDetail.intractor.ProductDetailIntractor

class ProductDetailPresenter(val productDetailView: ProductDetailContract.ProductDetailView):ProductDetailContract.ProductDetailCallBack,
            ProductDetailContract.ProductDetailPresenter{

    val productDetailInttractor:ProductDetailIntractor= ProductDetailIntractor()
    override fun getProductDetail(id: String) {
        productDetailInttractor.getProductDetail(id, this)

    }

    override fun onSucessProductDetail(productResponseModellist: ArrayList<ProductResponseModel>) {
        productDetailView.onSucessProductDetail(productResponseModellist)
    }

    override fun onErrorProductDetail(error: String) {
       productDetailView.onErrorProductDetail(error)
    }
}