package com.arumdental.home.changePassword

import com.arumdental.home.products.model.ProductResponseModel
import okhttp3.Call
import okhttp3.ResponseBody
import retrofit2.Response

interface changePasswordView {
    fun onSucessChangePassword(response: Response<ChangePasswordResponse>)
    fun onErrorChangePassword(t:Throwable)
}