package com.arumdental.home.order.model

import com.arumdental.home.order.createOrder.model.ShippingLines
import java.util.*
import kotlin.collections.ArrayList

class OrdersModel {

    var id:Int?=null
    var number:String?=null
    var status:String?=null
    var order_key:String?=null
    var total:String?=null
    var date_created_gmt: String?=null
    var shipping_total:String?=null
    var line_items:ArrayList<OrderInnerModel>?=null
    var shipping_lines:ArrayList<ShippingLines>?=null

    inner  class ShippingLines{
        var method_id:String?=null
        var method_title:String?=null
        var total:Double?=null
    }

}