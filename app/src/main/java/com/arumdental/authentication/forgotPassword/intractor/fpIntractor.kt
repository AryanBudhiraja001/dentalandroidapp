package com.arumdental.authentication.forgotPassword.intractor

import com.arumdental.authentication.forgotPassword.fpContract
import com.arumdental.base.BaseApplication
import com.arumdental.web.RetrofitClient
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class fpIntractor:fpContract.fpIntractor {
    override fun fp(email: String, fpCallback: fpContract.fpCallback) {
        if(BaseApplication.instance!!.hasNetwork())
        {

            RetrofitClient.getInstance().getapi().forgotPassword(email).enqueue(object :
                Callback<ResponseBody>
            {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if(response.isSuccessful && response.code()==200)
                    {
                        val jsonObject: JSONObject? = JSONObject(response.body()!!.string())
                            var code=jsonObject?.getString("code")

                        if(code.equals("200"))
                        {
                            var message=jsonObject?.getString("msg")
                            fpCallback.onSucess(message!!)
                        }
                        else
                        {
                            fpCallback.onError("Not found")
                        }



                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    fpCallback.onError(t.message!!)
                }

            })
        }

        else
        {
            fpCallback.onError("No Internet connection")
        }
    }

}