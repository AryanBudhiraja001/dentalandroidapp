package com.arumdental.home.order.presenter

import com.arumdental.home.order.OrderContract
import com.arumdental.home.order.intractor.OrderIntractor
import com.arumdental.home.order.model.OrdersModel

class OrderPresenter (var orderView: OrderContract.OrderView):OrderContract.OrderPresenter,
            OrderContract.OrderCallBack{

    var orderIntractor:OrderIntractor= OrderIntractor()
    override fun getOrders(customer:Int) {

        orderIntractor.getOrders(customer, this)

    }

    override fun onSucessOrder(ordersModel: ArrayList<OrdersModel>) {
        orderView.onSucessOrder(ordersModel)
    }

    override fun onErrorOrder(error: String) {
       orderView.onErrorOrder(error)
    }
}