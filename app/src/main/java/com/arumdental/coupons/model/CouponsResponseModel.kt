package com.arumdental.coupons.model

class CouponsResponseModel {

    var id:Int?=null
    var code:String?=null
    var amount:String?=null
    var discount_type:String?=null
    var usage_count:Int?=null
    var usage_limit_per_user:Int?=null
    var limit_usage_to_x_items:Int?=null
    var free_shipping:String?=null
    var minimum_amount:String?=null
    var maximum_amount:String?=null
    var product_categories:ArrayList<Int>?=null
    var date_expires:String?=null



}