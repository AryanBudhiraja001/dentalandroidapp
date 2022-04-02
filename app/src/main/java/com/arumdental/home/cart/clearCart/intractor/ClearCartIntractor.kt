package com.arumdental.home.cart.clearCart.intractor

import com.arumdental.base.BaseApplication
import com.arumdental.home.cart.clearCart.ClearCartContract
import com.arumdental.web.RetrofitClient
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ClearCartIntractor:ClearCartContract.ClearCartIntractor {
    override fun clearCart(clearCartCallBack: ClearCartContract.ClearCartCallBack) {
        if(BaseApplication.instance!!.hasNetwork())
        {
            RetrofitClient.getInstance().getapi().clearCart().enqueue(object: Callback<ResponseBody>
            {
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    clearCartCallBack.onErrorClearCart(t.localizedMessage)
                }

                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                   if(response.isSuccessful && response.code()==200)
                   {
                       clearCartCallBack.onSucessClearCart(response.body()!!)
                   }
                    else{
                       val jObjError = response.errorBody()!!.string()
                       clearCartCallBack.onErrorClearCart(jObjError)
                   }
                }

            })
        }
        else
        {
            clearCartCallBack.onErrorClearCart("No internet connection")
        }
    }
}