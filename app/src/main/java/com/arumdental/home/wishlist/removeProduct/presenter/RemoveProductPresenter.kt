package com.arumdental.home.wishlist.removeProduct.presenter

import com.arumdental.home.wishlist.addProduct.model.AddProductResponse
import com.arumdental.home.wishlist.removeProduct.RemoveProductContract
import com.arumdental.home.wishlist.removeProduct.intractor.RemoveProductIntractor

class RemoveProductPresenter(val removeProductView: RemoveProductContract.RemoveProductView):RemoveProductContract.RemoveProductPresenter,
            RemoveProductContract.RemoveProductCallBack{

    val removeProductIntractor: RemoveProductIntractor= RemoveProductIntractor()
    override fun removeProduct(productID: Int) {
        removeProductIntractor.removeProduct(productID, this)

    }

    override fun onSucessRemoveProduct(addProductResponse: AddProductResponse) {
        removeProductView.onSucessRemoveProduct(addProductResponse)
    }

    override fun onErrorRemoveProduct(error: String) {
       removeProductView.onErrorRemoveProduct(error)
    }
}