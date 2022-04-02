package com.arumdental.home.order.intractor

import com.arumdental.base.BaseApplication
import com.arumdental.home.order.OrderContract
import com.arumdental.home.order.model.OrdersModel
import com.arumdental.web.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrderIntractor:OrderContract.OrderIntractor {
    override fun getOrders(customer:Int,orderCallBack: OrderContract.OrderCallBack) {
        if(BaseApplication.instance!!.hasNetwork())
        {
            RetrofitClient.getInstance().getapi().getOrders(customer).enqueue(object :
                Callback<ArrayList<OrdersModel>> {
                override fun onFailure(call: Call<ArrayList<OrdersModel>>, t: Throwable) {
                    orderCallBack.onErrorOrder(t.message!!)
                }

                override fun onResponse(call: Call<ArrayList<OrdersModel>>, response: Response<ArrayList<OrdersModel>>) {
                  if(response.isSuccessful && response.code()==200)
                  {
                      orderCallBack.onSucessOrder(response.body()!!)

                  }else{
                      orderCallBack.onErrorOrder(response.message())
                  }
                }

            })
        }
        else{
            orderCallBack.onErrorOrder("No Internet Conection")
        }
    }
}