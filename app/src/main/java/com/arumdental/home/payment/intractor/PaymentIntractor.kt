package com.arumdental.home.payment.intractor

import com.arumdental.base.BaseApplication
import com.arumdental.home.payment.PaymentContract
import com.arumdental.web.RetrofitClient
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PaymentIntractor:PaymentContract.PaymentIntractor {
    override fun makePayment(
        token: String,
        amount: Double,
        orderId: Int,
        paymentCallBack: PaymentContract.PaymentCallBack
    ) {
        if(BaseApplication.instance!!.hasNetwork())
        {
            RetrofitClient.getInstance().getapi().makePayment(token, amount.toString(),
                orderId.toString()
            ).enqueue(object: Callback<ResponseBody>
            {
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    paymentCallBack.onError(t.message!!)
                }

                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {

                    if(response.isSuccessful && response.code()==200)
                    {
                        paymentCallBack.onSucess(response!!.body()!!)
                    }
                    else if(response.code()==500)
                    {
                        paymentCallBack.onError("Error in payment")
                    }
                    else
                    {
                        val jObjError = JSONObject(response.errorBody()!!.string())
                        var message=jObjError.getString("message")
                        paymentCallBack.onError(message)
                    }

                }

            })



        }
        else
        {
            paymentCallBack.onError("No Internet connection")
        }
    }
}