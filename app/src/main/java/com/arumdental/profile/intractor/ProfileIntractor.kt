package com.arumdental.profile.intractor

import com.arumdental.authentication.login.model.LoginResponseModel
import com.arumdental.authentication.signUp.model.response.SignUpResponse
import com.arumdental.base.BaseApplication
import com.arumdental.profile.ProfileContract
import com.arumdental.profile.model.ProfileResponseModel
import com.arumdental.utils.UserSharedPrefences
import com.arumdental.web.RetrofitClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileIntractor:ProfileContract.ProfileIntrator {


    override fun getUserProfile(email:String,profileCallBack: ProfileContract.ProfileCallBack) {

        if(BaseApplication.instance!!.hasNetwork())
        {
         RetrofitClient.getInstance().getapi().getUserInfo(UserSharedPrefences.getInstance().email).enqueue(object :
                Callback<ProfileResponseModel>
            {
                override fun onFailure(call: Call<ProfileResponseModel>, t: Throwable) {
                    profileCallBack.onError(t.message.toString())
                }

                override fun onResponse(
                    call: Call<ProfileResponseModel>,
                    response: Response<ProfileResponseModel>
                ) {
                    if(response.isSuccessful && response.code()==200)
                    {

                        if(response.body()!=null)
                        {
                            if(response.body()!!.body!=null && response.body()!!.body!!.size>0)
                            {
                                profileCallBack.onSucess(response.body()!!)
                            }
                            else
                            {
                                profileCallBack.onError(response.message()!!)
                            }

                        }
                        else
                        {
                            profileCallBack.onError(response.message()!!)
                        }

                    }
                    else
                    {
                        profileCallBack.onError(response.message()!!)
                    }
                }

            })

        }
        else
        {
            profileCallBack.onError("No Internet Connection")
        }

    }

    override fun getUserProfileWP(profileCallBack: ProfileContract.ProfileCallBack) {
        if(BaseApplication.instance!!.hasNetwork())
        {
            RetrofitClient.getInstance().getapi().getUserInfo(UserSharedPrefences.getInstance().id).enqueue(object :
                Callback<ResponseBody>
            {
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    profileCallBack.onError(t.message.toString())
                }

                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if(response.isSuccessful && response.code()==200)
                    {
                        profileCallBack.onSucessWP(response.body()!!)
                    }
                    else
                    {
                        profileCallBack.onErrorWP(response.message()!!)
                    }
                }

            })

        }
        else
        {
            profileCallBack.onError("No Internet Connection")
        }
    }
}