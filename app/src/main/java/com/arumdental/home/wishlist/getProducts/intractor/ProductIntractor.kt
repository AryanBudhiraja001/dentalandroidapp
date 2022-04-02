package com.arumdental.home.wishlist.getProducts.intractor

import com.arumdental.base.BaseApplication
import com.arumdental.home.wishlist.addProduct.model.AddProductResponse
import com.arumdental.home.wishlist.getProducts.ProductsContract
import com.arumdental.home.wishlist.getProducts.model.WishlistProductsResponseModel
import com.arumdental.web.RetrofitClient
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class ProductIntractor:ProductsContract.ProductIntractor {


    override fun getProducts(userId: Int, productCallBack: ProductsContract.ProductCallBack) {
        if(BaseApplication.instance!!.hasNetwork())
        {
            RetrofitClient.getInstance().getapi().getProducts(userId).enqueue(object :
                retrofit2.Callback<WishlistProductsResponseModel>
            {
                override fun onFailure(call: Call<WishlistProductsResponseModel>, t: Throwable) {
                    productCallBack.onErrorWishlist(t.message!!)
                }

                override fun onResponse(
                    call: Call<WishlistProductsResponseModel>,
                    response: Response<WishlistProductsResponseModel>
                ) {
                    if(response.isSuccessful && response.code()==200)
                    {
                        if(response.body()!!.success==200)
                        {
                            productCallBack.onSucessWishlist(response.body()!!)
                        }
                        else
                        {
                            productCallBack.onErrorWishlist("Error")
                        }
                    }
                    else{
                        productCallBack.onErrorWishlist("Error")
                    }

                }

            })

        }
        else{
            productCallBack.onErrorWishlist("No Internet Connection")
        }
    }
}