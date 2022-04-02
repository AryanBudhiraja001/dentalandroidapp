package com.arumdental.home.order.createOrder

import com.arumdental.home.order.createOrder.model.CreateOrderModel
import com.arumdental.home.order.createOrder.model.CreateOrderResponseModel

interface CreateOrderContract {

    interface  CreateOrderPresenter{
        fun createOrder(createOrderModel: CreateOrderModel)
    }

    interface  CreateOrderView{
        fun onSucess(createOrderResponseModel: CreateOrderResponseModel)
        fun onErrorCreateOrder(error:String)
    }

    interface  CreateOrderCallBack{
        fun onSucess(createOrderResponseModel: CreateOrderResponseModel)
        fun onErrorCreateOrder(error:String)
    }

    interface  CreateOrderIntractor{
        fun createOrder(createOrderModel: CreateOrderModel,
                        createOrderCallBack: CreateOrderCallBack)
    }


}