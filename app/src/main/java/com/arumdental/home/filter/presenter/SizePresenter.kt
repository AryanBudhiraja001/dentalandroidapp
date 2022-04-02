package com.arumdental.home.filter.presenter

import com.arumdental.home.brands.model.CategoriesResponse
import com.arumdental.home.filter.SizeContract
import com.arumdental.home.filter.intractor.SizeIntractor

class SizePresenter(val sizeView:SizeContract.SizeView):SizeContract.SizePresenter,SizeContract.SizeCallBack {


    var sizeIntractor:SizeIntractor= SizeIntractor()
    override fun getSizes() {

        sizeIntractor.getSizes(this)

    }

    override fun getTypes() {
        sizeIntractor.getTypes(this)
    }

    override fun getVolume() {
       sizeIntractor.getVolume(this)
    }

    override fun onSucessSize(sizelist: ArrayList<CategoriesResponse>) {
        sizeView.onSucessSize(sizelist)
    }

    override fun onErrorSize(error: String) {
        sizeView.onErrorSize(error)
    }

    override fun onSucessType(typelist: ArrayList<CategoriesResponse>) {
        sizeView.onSucessType(typelist)
    }

    override fun onErrorType(error: String) {
       sizeView.onErrorType(error)
    }

    override fun onSucessVolume(volumelist: ArrayList<CategoriesResponse>) {
       sizeView.onSucessVolume(volumelist)
    }

    override fun onErrorVolume(error: String) {
        sizeView.onErrorType(error)
    }


}