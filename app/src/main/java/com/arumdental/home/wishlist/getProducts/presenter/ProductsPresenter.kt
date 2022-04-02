package com.arumdental.home.wishlist.getProducts.presenter

import com.arumdental.home.wishlist.getProducts.ProductsContract
import com.arumdental.home.wishlist.getProducts.intractor.ProductIntractor
import com.arumdental.home.wishlist.getProducts.model.WishlistProductsResponseModel

class ProductsPresenter (val productView: ProductsContract.ProductView):ProductsContract.ProductPresenter,
                    ProductsContract.ProductCallBack{

    var productIntractor:ProductIntractor= ProductIntractor()

    override fun getProducts(userId: Int) {
        productIntractor.getProducts(userId, this)

    }

    override fun onSucessWishlist(productResponseModel: WishlistProductsResponseModel) {
       productView.onSucessWishlist(productResponseModel)
    }

    override fun onErrorWishlist(error: String) {
        productView.onErrorWishlist(error)
    }


}