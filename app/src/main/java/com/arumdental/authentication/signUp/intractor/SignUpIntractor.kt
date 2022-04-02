package com.arumdental.authentication.signUp.intractor

import com.arumdental.R
import com.arumdental.authentication.signUp.SignUpContract
import com.arumdental.authentication.signUp.model.paramModel.SignUpParamModel
import com.arumdental.authentication.signUp.model.response.SignUpResponse
import com.arumdental.base.BaseApplication
import com.arumdental.utils.Constants
import com.arumdental.web.ApiInterface
import com.arumdental.web.Retrofit3
import com.arumdental.web.RetrofitClient
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import kotlin.math.sign

class SignUpIntractor:SignUpContract.SignUpIntractor {

    var retrofit3=Retrofit3()





    override fun signUp(
        signUpParamModel: SignUpParamModel,
        signUpCallBack: SignUpContract.SignUpCallBack
    ) {

        if(BaseApplication.instance!!.hasNetwork())
        {
          retrofit3.api.signUp(
               signUpParamModel).enqueue(object : Callback<SignUpResponse>
            {
                override fun onFailure(call: Call<SignUpResponse>, t: Throwable) {
                    signUpCallBack.onErrorSignUp(t.message!!)
                }

                override fun onResponse(
                    call: Call<SignUpResponse>,
                    response: Response<SignUpResponse>
                ) {
                  if(response.isSuccessful  &&response.code()==201)
                  {
                        signUpCallBack.onSucessSignUp(response.body()!!)
                  }
                    else
                  {
                      try {
                          val jObjError = JSONObject(response.errorBody()!!.string())
                          var data=jObjError.getJSONObject("data")
                          var code=data.getInt("status")
                          if(code==400)
                          {
                              signUpCallBack.onErrorSignUp("Email already exists.")
                          }
                          else
                          {
                              signUpCallBack.onErrorSignUp("Please check your credentails.")
                          }



                      } catch (e: Exception) {
                          e.printStackTrace()
                      }
                  }
                }

            })

        }
        else
        {
            signUpCallBack.onErrorSignUp(R.string.No_internet_Connection.toString())
        }



    }

}