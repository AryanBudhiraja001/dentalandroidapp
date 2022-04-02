package com.arumdental.home.filter

import com.arumdental.home.filter.model.FilterResponseModel
import com.arumdental.home.products.model.ProductResponseModel
import retrofit2.http.Url

interface FilterContract {

    interface  FilterPresenter{
        fun getFilters(string:String)
    }

    interface  FilterView{
         fun onSucessFilter(productList:ArrayList<ProductResponseModel>)
        fun onErrorFilter(error:String)
    }

    interface  FilterCallback{
        fun onSucessFilter(productList:ArrayList<ProductResponseModel>)
        fun onErrorFilter(error:String)
    }
    interface  FilterIntractor{
        fun  getFilters( string:String,filterCallback: FilterCallback)
    }


}