package com.arumdental.home.wishlist.addProduct.presenter

import com.arumdental.home.wishlist.addProduct.AddProductContract
import com.arumdental.home.wishlist.addProduct.intractor.AddProductIntractor
import com.arumdental.home.wishlist.addProduct.model.AddProductResponse

class AddProductPresenter(val  addProductView: AddProductContract.AddProductView):AddProductContract.AddProductCallBack,
                AddProductContract.AddProductPresenter{

    var addProductIntractor:AddProductIntractor= AddProductIntractor()
    override fun addProduct(userID: Int, productID: Int, price: String) {
        addProductIntractor.addProduct(userID, productID, price, this)
    }

    override fun onSucessAddProduct(addProductResponse: AddProductResponse) {
        addProductView.onSucessAddProduct(addProductResponse)
    }

    override fun onErrorAddProduct(error: String) {
       addProductView.onErrorAddProduct(error)
    }
}