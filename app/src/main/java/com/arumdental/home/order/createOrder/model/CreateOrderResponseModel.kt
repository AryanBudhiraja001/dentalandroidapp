package com.arumdental.home.order.createOrder.model

import com.arumdental.profile.updateProfile.model.Billing

class CreateOrderResponseModel {

    var payment_method:String?=null
    var payment_method_title:String?=null
    var billing:Billing?=null
    var shipping:Billing?=null
    var shipping_lines:ArrayList<ShippingLines>?=null
    var line_items: ArrayList<LinesItems>?=null
    var coupon_lines:ArrayList<CouponLines>?=null
    var set_paid:Boolean?=null
    var id:Int?=null

}