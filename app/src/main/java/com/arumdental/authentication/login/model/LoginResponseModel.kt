package com.arumdental.authentication.login.model

class LoginResponseModel {


    var statusCode: Int? = 0
    var data: Data? = null

    inner class Data {
        var token: String? = null
        var id: Int? = 0
        var email: String? = null
        var nicename: String? = null
        var firstName: String? = null
        var lastName: String? = null
        var displayName: String? = null
    }


}