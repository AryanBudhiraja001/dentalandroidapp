package com.arumdental.home.filter.model

import java.io.Serializable

class FilterResponseModel:Serializable {

    var id:Int?=null
    var name:String?=null
    var price:String?=null
    var images:ArrayList<Images>?=null
    var stock_status:String?=null
    var stock_quantity:String?=null


    inner  class Images: Serializable {
        var src:String?=null
    }
}