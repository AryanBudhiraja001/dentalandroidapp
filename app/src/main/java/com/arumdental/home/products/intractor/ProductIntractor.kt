package com.arumdental.home.products.intractor

import com.arumdental.base.BaseApplication
import com.arumdental.home.products.ProductContract
import com.arumdental.home.products.model.ProductResponseModel
import com.arumdental.web.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductIntractor :ProductContract.ProductIntractor{
    override fun getProducts(id:Int,productCallBack: ProductContract.ProductCallBack, search:String) {

        if(BaseApplication.instance!!.hasNetwork())
        {
            RetrofitClient.getInstance().getapi().getProductsFromCategory(id,search).enqueue(object :
                Callback<ArrayList<ProductResponseModel>>
            {
                override fun onFailure(call: Call<ArrayList<ProductResponseModel>>, t: Throwable) {
                    productCallBack.onError(t.message!!)
                }

                override fun onResponse(
                    call: Call<ArrayList<ProductResponseModel>>,
                    response: Response<ArrayList<ProductResponseModel>>
                ) {
                    if(response.isSuccessful && response.code()==200)
                    {
                        productCallBack.onSucess(response.body()!!)

                    }
                    else{
                        productCallBack.onError(response.message()!!)
                    }
                }

            })
        }

    }

}

