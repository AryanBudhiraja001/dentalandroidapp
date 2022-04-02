package com.arumdental.home.changePassword

import com.arumdental.web.RetrofitClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChangePasswordPresenter (val view: changePasswordView, val retrofit: RetrofitClient){


    fun ChangePassword(login_id: Int, oldPassword: String, newPassword:String) {
        val call: Call<ChangePasswordResponse> = retrofit.getapi().resetPassword(login_id, oldPassword, newPassword)
        call!!.enqueue(object : Callback<ChangePasswordResponse> {
            override fun onFailure(call: Call<ChangePasswordResponse>, t: Throwable) {
                view.onErrorChangePassword(t)
            }

            override fun onResponse(call: Call<ChangePasswordResponse>, response: Response<ChangePasswordResponse>) {
                view.onSucessChangePassword(response)
            }


        })

    }
}