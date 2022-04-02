package com.arumdental.coupons.presenter

import com.arumdental.coupons.CouponsContract
import com.arumdental.coupons.intractor.CouponIntractor
import com.arumdental.coupons.model.CouponsResponseModel

class CouponPresenter(val couponView: CouponsContract.CouponView):CouponsContract.CouponCallBack,
CouponsContract.CouponPresenter{

    var couponIntractor=CouponIntractor()
    override fun getCoupns() {
        couponIntractor.getCoupons(this)
    }

    override fun onSucess(couponsResponseModel: ArrayList<CouponsResponseModel>) {
        couponView.onSucess(couponsResponseModel)
    }

    override fun onError(error: String) {
        couponView.onErrorCoupons(error)
    }
}