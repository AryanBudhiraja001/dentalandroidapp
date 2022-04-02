package com.arumdental.home.payment.presenter

import com.arumdental.home.payment.PaymentContract
import com.arumdental.home.payment.intractor.PaymentIntractor
import okhttp3.ResponseBody

class PaymentPresenter(var paymentView: PaymentContract.PaymentView):PaymentContract.PaymentCallBack,
            PaymentContract.PaymentPresenter{

    var paymentIntractor=PaymentIntractor()
    override fun makePayment(token: String, amount: Double, orderId: Int) {
        paymentIntractor.makePayment(token, amount, orderId, this)
    }

    override fun onSucess(responseBody: ResponseBody) {
       paymentView.onSucess(responseBody)
    }

    override fun onError(error: String) {
        paymentView.onError(error)
    }
}