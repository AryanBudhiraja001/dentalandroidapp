package com.arumdental.home.wishlist.removeProduct.intractor

import com.arumdental.base.BaseApplication
import com.arumdental.home.wishlist.addProduct.model.AddProductResponse
import com.arumdental.home.wishlist.removeProduct.RemoveProductContract
import com.arumdental.utils.UserSharedPrefences
import com.arumdental.web.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RemoveProductIntractor:RemoveProductContract.RemoveProductIntrator {
    override fun removeProduct(
        productID: Int,
        removeProductCallBack: RemoveProductContract.RemoveProductCallBack
    ) {
        if(BaseApplication.instance!!.hasNetwork())
        {
            RetrofitClient.getInstance().getapi().removeProduct(productID, UserSharedPrefences.getInstance().id).enqueue(object :
                Callback<AddProductResponse>
            {
                override fun onFailure(call: Call<AddProductResponse>, t: Throwable) {
                    removeProductCallBack.onErrorRemoveProduct(t.message!!)
                }

                override fun onResponse(
                    call: Call<AddProductResponse>,
                    response: Response<AddProductResponse>
                ) {
                    if(response.isSuccessful && response.code()==200)
                    {
                        if(response.body()!!.success==200)
                        {
                            removeProductCallBack.onSucessRemoveProduct(response.body()!!)
                        }
                        else
                        {
                            removeProductCallBack.onErrorRemoveProduct(response.body()!!.message.toString())
                        }
                    }
                    else{
                        removeProductCallBack.onErrorRemoveProduct(response.body()!!.message.toString())
                    }

                }

            })

        }
        else
        {
            removeProductCallBack.onErrorRemoveProduct("No Internet Connection")
        }
    }
}