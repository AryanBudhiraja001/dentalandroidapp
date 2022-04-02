package com.arumdental.home.brands.intractor

import com.arumdental.base.BaseApplication
import com.arumdental.home.brands.BrandsContract
import com.arumdental.home.brands.model.CategoriesResponse
import com.arumdental.home.brands.view.BrandsFragment
import com.arumdental.web.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BrandsIntractor:BrandsContract.BrandsIntractor {
    override fun getBrands(string:String,brandsCallback: BrandsContract.BrandsCallback) {

        if(BaseApplication!!.instance!!.hasNetwork())
        {
            RetrofitClient.getInstance().getapi().getBrands(string).enqueue(object:
                Callback<ArrayList<CategoriesResponse>>
            {
                override fun onFailure(call: Call<ArrayList<CategoriesResponse>>, t: Throwable) {
                    brandsCallback.onError(t.message!!)

                }

                override fun onResponse(
                    call: Call<ArrayList<CategoriesResponse>>,
                    response: Response<ArrayList<CategoriesResponse>>
                ) {

                    if(response.isSuccessful && response.code()==200)
                    {
                        brandsCallback.onSucess(response.body()!!)
                    }
                    else
                    {
                        brandsCallback!!.onError(response.message())
                    }

                }

            })


        }
        else
        {
            brandsCallback.onError("No Internet Connection")
        }

    }

    override fun getcategories(brandsCallback: BrandsContract.BrandsCallback) {
        if(BaseApplication!!.instance!!.hasNetwork())
        {
            RetrofitClient.getInstance().getapi().getCategories(100).enqueue(object:
                Callback<ArrayList<CategoriesResponse>>
            {
                override fun onFailure(call: Call<ArrayList<CategoriesResponse>>, t: Throwable) {
                    brandsCallback.onError(t.message!!)

                }

                override fun onResponse(
                    call: Call<ArrayList<CategoriesResponse>>,
                    response: Response<ArrayList<CategoriesResponse>>
                ) {

                    if(response.isSuccessful && response.code()==200)
                    {
                        brandsCallback.onSucess(response.body()!!)
                    }
                    else
                    {
                        brandsCallback!!.onError(response.message())
                    }

                }

            })


        }
        else
        {
            brandsCallback.onError("No Internet Connection")
        }

    }

    override fun searchBrands(search: String, brandsCallback: BrandsContract.BrandsCallback) {
        if(BaseApplication!!.instance!!.hasNetwork())
        {
            RetrofitClient.getInstance().getapi().searchBrands(search).enqueue(object:
                Callback<ArrayList<CategoriesResponse>>
            {
                override fun onFailure(call: Call<ArrayList<CategoriesResponse>>, t: Throwable) {
                    brandsCallback.onError(t.message!!)

                }

                override fun onResponse(
                    call: Call<ArrayList<CategoriesResponse>>,
                    response: Response<ArrayList<CategoriesResponse>>
                ) {

                    if(response.isSuccessful && response.code()==200)
                    {
                        brandsCallback.onSucessSearch(response.body()!!)
                    }
                    else
                    {
                        brandsCallback!!.onError(response.message())
                    }

                }

            })


        }
        else
        {
            brandsCallback.onError("No Internet Connection")
        }

    }
}