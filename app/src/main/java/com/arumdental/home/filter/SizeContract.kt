package com.arumdental.home.filter

import com.arumdental.home.brands.model.CategoriesResponse

interface SizeContract {

    interface  SizePresenter{
        fun getSizes()
       fun getTypes()
        fun getVolume()
    }

    interface  SizeCallBack{
        fun onSucessSize(sizelist:ArrayList<CategoriesResponse>)
        fun onErrorSize(error:String)

        fun onSucessType(typelist:ArrayList<CategoriesResponse>)
        fun onErrorType(error:String)

        fun onSucessVolume(volumelist:ArrayList<CategoriesResponse>)
        fun onErrorVolume(error:String)
    }
    interface  SizeView{
        fun onSucessSize(sizelist:ArrayList<CategoriesResponse>)
        fun onErrorSize(error:String)

        fun onSucessType(typelist:ArrayList<CategoriesResponse>)
        fun onErrorType(error:String)

        fun onSucessVolume(volumelist:ArrayList<CategoriesResponse>)
        fun onErrorVolume(error:String)
    }

    interface SizeIntractor{
        fun getSizes(sizeCallBack: SizeCallBack)
        fun getTypes(sizeCallBack: SizeCallBack)
        fun getVolume(sizeCallBack: SizeCallBack)


    }

}