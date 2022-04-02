package com.arumdental.home.filter.presenter

import com.arumdental.home.filter.FilterContract
import com.arumdental.home.filter.intractor.FilterIntractor
import com.arumdental.home.filter.model.FilterResponseModel
import com.arumdental.home.products.model.ProductResponseModel
import retrofit2.http.Url

class FilterPresenter(var filterView: FilterContract.FilterView):
    FilterContract.FilterPresenter,
    FilterContract.FilterCallback{

    var filterIntractor:FilterIntractor= FilterIntractor()


    override fun getFilters(string: String) {
        filterIntractor.getFilters(string, this)

    }

    override fun onSucessFilter(productList: ArrayList<ProductResponseModel>) {
        filterView.onSucessFilter(productList)

    }

    override fun onErrorFilter(error: String) {
        filterView.onErrorFilter(error)

    }
}