package com.arumdental.authentication.signUp.model.response

import com.arumdental.profile.updateProfile.model.Billing
import okhttp3.ResponseBody

class SignUpResponse {
    var  email:String?=null
    var first_name:String?=null
    var last_name:String?=null
    var password:String?=null
    var username:String?=null
    var id:Int?=null
    var billing:Billing?=null
    var shipping:Billing?=null
    var metadata:ArrayList<ResponseBody>?=null




}