package com.arumdental.home.wishlist.getProducts.model

class WishlistProductsResponseModel {

    var success:Int?=null
    var message:String?=null
    var body:ArrayList<Data>?=null


    inner  class  Data{
        var ID:String?=null
        var wishlist_id:String?=null
        var product_id:Int?=null

    }

}