package com.arumdental.coupons

import com.arumdental.coupons.model.CouponsResponseModel

interface CouponsContract {

    interface  CouponPresenter{
        fun getCoupns()
    }

    interface  CouponCallBack{
        fun onSucess(couponsResponseModel: ArrayList<CouponsResponseModel>)
        fun onError(error:String)
    }

    interface CouponView{
        fun onSucess(couponsResponseModel: ArrayList<CouponsResponseModel>)
        fun onErrorCoupons(error:String)
    }

    interface  CouponIntractor{
        fun getCoupons(couponCallBack: CouponCallBack)
    }
}