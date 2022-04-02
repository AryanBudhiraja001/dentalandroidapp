package com.arumdental.home.wishlist.getProductDetail.intractor

import com.arumdental.base.BaseApplication
import com.arumdental.home.products.model.ProductResponseModel
import com.arumdental.home.wishlist.getProductDetail.ProductDetailContract
import com.arumdental.web.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductDetailIntractor:ProductDetailContract.ProductDetailIntractor {
    override fun getProductDetail(
        id: String,
        productDetailCallBack: ProductDetailContract.ProductDetailCallBack
    ) {
        if(BaseApplication.instance!!.hasNetwork())
        {
            RetrofitClient.getInstance().getapi().getProductDetail(id).enqueue(object:
                Callback<ArrayList<ProductResponseModel>>
            {
                override fun onFailure(call: Call<ArrayList<ProductResponseModel>>, t: Throwable) {
                   productDetailCallBack.onErrorProductDetail(t.message!!)
                }

                override fun onResponse(
                    call: Call<ArrayList<ProductResponseModel>>,
                    response: Response<ArrayList<ProductResponseModel>>
                ) {
                    if (response.isSuccessful && response.code() == 200)
                    {
                        if(!response.body().isNullOrEmpty())
                        {
                            productDetailCallBack.onSucessProductDetail(response.body()!!)
                        }
                        else
                        {
                            productDetailCallBack.onErrorProductDetail("No Data found")
                        }
                    }
                    else
                    {
                        productDetailCallBack.onErrorProductDetail("No Data found")
                    }
                }

            })
        }
        else
        {
            productDetailCallBack.onErrorProductDetail("No Internet Connection")
        }

    }
}