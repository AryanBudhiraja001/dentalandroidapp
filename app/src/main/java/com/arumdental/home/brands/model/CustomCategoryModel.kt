package com.arumdental.home.brands.model

 data class CustomCategoryModel(
     var alphabet:String,
    var data:List<CategoriesResponse>)  {


    inner  class  Data{
        var id:Int?=null
        var name:String?=null
        var slug:String?=null
        var parent:Int?=null
        var description:String?=null
        var display:String?=null
        var image:String?=null
        var count:Int?=null
    }
}