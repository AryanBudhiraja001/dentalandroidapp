package com.arumdental.home.filter.intractor

import com.arumdental.base.BaseApplication
import com.arumdental.home.filter.FilterContract
import com.arumdental.home.filter.model.FilterResponseModel
import com.arumdental.home.products.model.ProductResponseModel
import com.arumdental.web.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Url

class FilterIntractor:FilterContract.FilterIntractor {
    override fun getFilters(string: String, filterCallback: FilterContract.FilterCallback) {
        if(BaseApplication.instance!!.hasNetwork())
        {

            RetrofitClient.getInstance().getapi().applyFilter(string).enqueue(object: Callback<ArrayList<ProductResponseModel>>{
                override fun onFailure(call: Call<ArrayList<ProductResponseModel>>, t: Throwable) {
                    filterCallback.onErrorFilter(t.message!!)
                }

                override fun onResponse(
                    call: Call<ArrayList<ProductResponseModel>>,
                    response: Response<ArrayList<ProductResponseModel>>
                ) {
                   if(response.isSuccessful && response.code()==200)
                   {


                           filterCallback.onSucessFilter(response.body()!!)


                   }
                    else
                   {
                       filterCallback.onErrorFilter(response.message())
                   }
                }

            })
        }
        else{
            filterCallback.onErrorFilter("No Internet Connection")
        }
    }
}