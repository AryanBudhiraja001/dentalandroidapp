package com.arumdental.home.order.createOrder.intractor

import com.arumdental.base.BaseApplication
import com.arumdental.home.order.createOrder.CreateOrderContract
import com.arumdental.home.order.createOrder.model.CreateOrderModel
import com.arumdental.home.order.createOrder.model.CreateOrderResponseModel
import com.arumdental.web.RetrofitClient
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateOrderIntractor:CreateOrderContract.CreateOrderIntractor {
    override fun createOrder(
        createOrderModel: CreateOrderModel,
        createOrderCallBack: CreateOrderContract.CreateOrderCallBack
    ) {
        if(BaseApplication.instance!!.hasNetwork())
        {

            RetrofitClient.getInstance().getapi().createOrder(createOrderModel).enqueue(object:
                Callback<CreateOrderResponseModel>
            {
                override fun onFailure(call: Call<CreateOrderResponseModel>, t: Throwable) {
                    createOrderCallBack.onErrorCreateOrder(t.message!!)
                }

                override fun onResponse(
                    call: Call<CreateOrderResponseModel>,
                    response: Response<CreateOrderResponseModel>
                ) {
                  if(response.isSuccessful && response.code()==201)
                  {
                        createOrderCallBack.onSucess(response.body()!!)
                  }
                    else
                  {
                      val jObjError = JSONObject(response.errorBody()!!.string())
                      var data=jObjError.getJSONObject("data")
                      var code=data.getString("message")
                      createOrderCallBack.onErrorCreateOrder(code)

                  }
                }

            })

        }
        else
        {
            createOrderCallBack.onErrorCreateOrder("No Internet Connection")
        }
    }
}