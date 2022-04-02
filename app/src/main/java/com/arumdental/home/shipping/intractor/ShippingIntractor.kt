package com.arumdental.home.shipping.intractor

import com.arumdental.base.BaseApplication
import com.arumdental.home.shipping.ShippingContract
import com.arumdental.home.shipping.model.ShippingResponseModel
import com.arumdental.web.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShippingIntractor:ShippingContract.ShippingIntractor {
    override fun getShipping(shippingCallBack: ShippingContract.ShippingCallBack) {
        if(BaseApplication.instance!!.hasNetwork())
        {

            RetrofitClient.getInstance().getapi().getShipingRated().enqueue(object: Callback<ShippingResponseModel>
            {
                override fun onFailure(call: Call<ShippingResponseModel>, t: Throwable) {
                    shippingCallBack.onErrorShipping(t.message!!)
                }

                override fun onResponse(
                    call: Call<ShippingResponseModel>,
                    response: Response<ShippingResponseModel>
                ) {
                   if(response .isSuccessful && response.code()==200)
                   {
                       if(response.body()!!.success==200)
                       {
                           if(response.body()!!.body!=null)
                           {
                               shippingCallBack.onSucessShipping(response.body()!!)
                           }
                       }
                   }
                }

            })


        }
        else
        {
            shippingCallBack.onErrorShipping("No Internet Connection")
        }
    }
}