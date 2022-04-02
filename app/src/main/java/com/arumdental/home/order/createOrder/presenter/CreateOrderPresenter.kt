package com.arumdental.home.order.createOrder.presenter

import com.arumdental.home.order.createOrder.CreateOrderContract
import com.arumdental.home.order.createOrder.intractor.CreateOrderIntractor
import com.arumdental.home.order.createOrder.model.CreateOrderModel
import com.arumdental.home.order.createOrder.model.CreateOrderResponseModel

class CreateOrderPresenter(val createOrderView: CreateOrderContract.CreateOrderView):
CreateOrderContract.CreateOrderPresenter,
CreateOrderContract.CreateOrderCallBack{

    var createOrderIntractor=CreateOrderIntractor()
    override fun createOrder(createOrderModel: CreateOrderModel) {
        createOrderIntractor.createOrder(createOrderModel,this)

    }

    override fun onSucess(createOrderResponseModel: CreateOrderResponseModel) {
        createOrderView.onSucess(createOrderResponseModel)
    }

    override fun onErrorCreateOrder(error: String) {
        createOrderView.onErrorCreateOrder(error)

    }
}