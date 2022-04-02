package com.arumdental.home.brands

import com.arumdental.home.brands.model.CategoriesResponse
import com.arumdental.home.brands.view.BrandsFragment

interface BrandsContract {

    interface  BrandsPresenter{
        fun getBrands(string:String)
        fun getCategories()
        fun searchBrands(search:String)
    }
    interface  BrandsCallback{
        fun onSucess(categoriesResponse: ArrayList<CategoriesResponse>)
        fun onError(error:String)

        fun onSucessSearch(categoriesResponse: ArrayList<CategoriesResponse>)
        fun onErrorSearch(error: String)
    }

    interface  BrandsView{
        fun onSucess(categoriesResponse:  ArrayList<CategoriesResponse>)
        fun onError(error:String)


        fun onSucessSearch(categoriesResponse: ArrayList<CategoriesResponse>)
        fun onErrorSearch(error: String)
    }

    interface  BrandsIntractor{
        fun getBrands(string:String,brandsCallback: BrandsCallback)
        fun getcategories(brandsCallback: BrandsCallback)
        fun searchBrands(search:String,brandsCallback: BrandsCallback)

    }



}