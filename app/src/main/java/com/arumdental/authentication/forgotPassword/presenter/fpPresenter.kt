package com.arumdental.authentication.forgotPassword.presenter

import com.arumdental.authentication.forgotPassword.fpContract

class fpPresenter(val fpView: fpContract.fpView):fpContract.fpPresenter,
        fpContract.fpCallback
{

    val fpIntractor=com.arumdental.authentication.forgotPassword.intractor.fpIntractor()
    override fun fp(email: String) {
        fpIntractor.fp(email, this)
    }

    override fun onSucess(string: String) {
        fpView.onSucess(string)
    }

    override fun onError(error: String) {
       fpView.onError(error)
    }

}