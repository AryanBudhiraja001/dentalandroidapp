package com.arumdental.home.products.model

import java.io.Serializable

class ProductResponseModel:Serializable {
    var id:Int?=null
    var name:String?=null
    var price:String?=null
    var images:ArrayList<Images>?=null
    var stock_status:String?=null
    var stock_quantity:String?=null
    var description:String?=null
    var isFav:Boolean?=false
    var productID:Int?=null
    var sku:String?=null


    inner  class Images:Serializable{
        var src:String?=null
    }
}