package com.arumdental.home.brands.presenter

import com.arumdental.home.brands.BrandsContract
import com.arumdental.home.brands.intractor.BrandsIntractor
import com.arumdental.home.brands.model.CategoriesResponse
import com.arumdental.home.brands.view.BrandsFragment

class BrandsPresenter (val brandsView: BrandsContract.BrandsView)
    :BrandsContract.BrandsPresenter, BrandsContract.BrandsCallback{


    var brandsIntractor:BrandsIntractor= BrandsIntractor()


    override fun getBrands(string:String) {
        brandsIntractor.getBrands(string,this)

    }

    override fun getCategories() {
        brandsIntractor.getcategories(this)
    }

    override fun searchBrands(search: String) {
        brandsIntractor.searchBrands(search, this)
    }

    override fun onSucess(categoriesResponse:  ArrayList<CategoriesResponse>) {
        brandsView.onSucess(categoriesResponse)

    }

    override fun onError(error: String) {
        brandsView.onError(error)

    }

    override fun onSucessSearch(categoriesResponse: ArrayList<CategoriesResponse>) {
        brandsView.onSucessSearch(categoriesResponse)
    }

    override fun onErrorSearch(error: String) {
       brandsView.onErrorSearch(error)
    }

}