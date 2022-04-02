package com.arumdental.home.brands.model

import java.io.Serializable

class CategoriesResponse :Serializable{
    var id:Int?=null
    var name:String?=null
    var slug:String?=null
    var parent:Int?=null
    var description:String?=null
    var display:String?=null
    var image:Image?=null
    var count:Int?=null


    inner  class  Image{
        var id:Int?=null
        var src:String?=null
    }

}