package com.arumdental.home.order

import com.arumdental.home.order.model.OrdersModel

interface OrderContract {

    interface  OrderPresenter{
        fun getOrders(customer:Int)
    }

    interface OrderCallBack{
        fun  onSucessOrder(ordersModel: ArrayList<OrdersModel>)
        fun onErrorOrder(error:String)
    }

    interface OrderView{
        fun  onSucessOrder(ordersModel: ArrayList<OrdersModel>)
        fun onErrorOrder(error:String)

    }

    interface  OrderIntractor{
        fun getOrders(customer:Int,orderCallBack: OrderCallBack)
    }


}