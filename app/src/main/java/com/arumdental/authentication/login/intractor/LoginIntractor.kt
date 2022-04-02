package com.arumdental.authentication.login.intractor

import com.arumdental.R
import com.arumdental.authentication.login.LoginContract
import com.arumdental.authentication.login.model.LoginResponseModel
import com.arumdental.authentication.login.model.ValidateTokenResponse
import com.arumdental.base.BaseApplication
import com.arumdental.utils.UserSharedPrefences
import com.arumdental.web.RetrofitClient
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.log

class LoginIntractor:LoginContract.LoginIntractor {

    override fun onlogin(
        username: String,
        password: String,
        loginCallBack: LoginContract.LoginCallBack
    ) {
        if(BaseApplication.instance!!.hasNetwork())
        {



            RetrofitClient.getInstance().getapi().login(username,password).enqueue(object :
                Callback<LoginResponseModel>
            {
                override fun onFailure(call: Call<LoginResponseModel>, t: Throwable) {
                    loginCallBack.onfailure("Please check your email and password")
                }

                override fun onResponse(
                    call: Call<LoginResponseModel>,
                    response: Response<LoginResponseModel>
                ) {
                    if(response.isSuccessful && response.code()==200)
                    {


                                loginCallBack.onSucesslogin(response.body()!!)





                    }
                    else
                    {
                        loginCallBack.onfailure("Please check your email and password")
                    }

                }


            })

        }
        else
        {
            loginCallBack.onfailure(R.string.No_internet_Connection.toString())
        }
    }



    override fun ontokenValidate(loginCallBack: LoginContract.LoginCallBack) {
        if(BaseApplication.instance!!.hasNetwork())
        {

            RetrofitClient.getInstance().getapi().validateToken().enqueue(object :Callback<ValidateTokenResponse>
            {
                override fun onFailure(call: Call<ValidateTokenResponse>, t: Throwable) {
                    loginCallBack.onfailure(t.message.toString())
                }

                override fun onResponse(
                    call: Call<ValidateTokenResponse>,
                    response: Response<ValidateTokenResponse>
                ) {
                   if(response.isSuccessful && response.code()==200)
                   {
                       if(response.body()!!.statusCode==200)
                       {

                               loginCallBack.onSucessValidateToken(response.body()!!)



                       }
                       else{
                           loginCallBack.onfailure(response.body()!!.code!!)
                       }

                   }
                    else
                   {
                       loginCallBack.onfailure(response.message())
                   }
                }

            })

        }
        else
        {
            loginCallBack.onfailure(R.string.No_internet_Connection.toString())
        }
    }


}