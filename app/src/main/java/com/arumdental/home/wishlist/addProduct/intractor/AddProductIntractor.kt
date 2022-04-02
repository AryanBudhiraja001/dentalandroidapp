package com.arumdental.home.wishlist.addProduct.intractor

import com.arumdental.base.BaseApplication
import com.arumdental.home.wishlist.addProduct.AddProductContract
import com.arumdental.home.wishlist.addProduct.model.AddProductResponse
import com.arumdental.web.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddProductIntractor:AddProductContract.AddProductIntrator {
    override fun addProduct(
        userID: Int,
        productID: Int,
        price: String,
        addProductCallBack: AddProductContract.AddProductCallBack
    ) {
      if(BaseApplication.instance!!.hasNetwork())
      {
          RetrofitClient.getInstance().getapi().addToWishlist(userID, productID, price).enqueue(object :
              Callback<AddProductResponse>
          {
              override fun onFailure(call: Call<AddProductResponse>, t: Throwable) {
                  addProductCallBack.onErrorAddProduct(t.message!!)
              }

              override fun onResponse(
                  call: Call<AddProductResponse>,
                  response: Response<AddProductResponse>
              ) {
                  if(response.isSuccessful && response.code()==200)
                  {
                      if(response.body()!!.success==200)
                      {
                          addProductCallBack.onSucessAddProduct(response.body()!!)
                      }
                      else
                      {
                          addProductCallBack.onErrorAddProduct(response.body()!!.message.toString())
                      }
                  }
                  else{
                      addProductCallBack.onErrorAddProduct(response.body()!!.message.toString())
                  }

              }

          })

      }
        else
      {
          addProductCallBack.onErrorAddProduct("No Internet Connection")
      }
    }
}