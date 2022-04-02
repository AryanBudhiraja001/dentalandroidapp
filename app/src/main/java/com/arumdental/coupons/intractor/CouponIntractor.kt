package com.arumdental.coupons.intractor

import com.arumdental.base.BaseApplication
import com.arumdental.coupons.CouponsContract
import com.arumdental.coupons.model.CouponsResponseModel
import com.arumdental.web.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CouponIntractor:
CouponsContract.CouponIntractor{
    override fun getCoupons(couponCallBack: CouponsContract.CouponCallBack) {
        if(BaseApplication.instance!!.hasNetwork())
        {
            RetrofitClient.getInstance().getapi().getcoupons().enqueue(object:
                Callback<ArrayList<CouponsResponseModel>>{
                override fun onFailure(call: Call<ArrayList<CouponsResponseModel>>, t: Throwable) {
                    couponCallBack.onError(t.message!!)
                }

                override fun onResponse(
                    call: Call<ArrayList<CouponsResponseModel>>,
                    response: Response<ArrayList<CouponsResponseModel>>
                ) {
                  if(response.isSuccessful && response.code()==200)
                  {
                      couponCallBack.onSucess(response.body()!!)

                  }
                    else{
                      couponCallBack.onError(response.message())
                  }
                }

            })
        }
        else
        {
            couponCallBack.onError("No Internet connection")
        }
    }
}