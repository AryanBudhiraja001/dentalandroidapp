package com.arumdental.home.filter.intractor

import com.arumdental.base.BaseApplication
import com.arumdental.home.brands.model.CategoriesResponse
import com.arumdental.home.filter.SizeContract
import com.arumdental.web.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SizeIntractor:SizeContract.SizeIntractor {


    override fun getSizes(sizeCallBack: SizeContract.SizeCallBack) {

        if(BaseApplication.instance!!.hasNetwork())
        {
            RetrofitClient.getInstance().getapi().gettags().enqueue(object: Callback<ArrayList<CategoriesResponse>>
            {
                override fun onFailure(call: Call<ArrayList<CategoriesResponse>>, t: Throwable) {
                    sizeCallBack.onErrorSize(t.message!!)
                }

                override fun onResponse(
                    call: Call<ArrayList<CategoriesResponse>>,
                    response: Response<ArrayList<CategoriesResponse>>
                ) {
                  if(response.isSuccessful && response.code()==200)
                  {
                      if(response.body()!!.size>0)
                      {
                          sizeCallBack.onSucessSize(response.body()!!)
                      }
                  }
                }

            })

        }
        else
        {
            sizeCallBack.onErrorSize("No Internrt Connection")
        }

    }

    override fun getTypes(sizeCallBack: SizeContract.SizeCallBack) {
        if(BaseApplication.instance!!.hasNetwork())
        {
            RetrofitClient.getInstance().getapi().getTypes().enqueue(object: Callback<ArrayList<CategoriesResponse>>
            {
                override fun onFailure(call: Call<ArrayList<CategoriesResponse>>, t: Throwable) {
                    sizeCallBack.onErrorType(t.message!!)
                }

                override fun onResponse(
                    call: Call<ArrayList<CategoriesResponse>>,
                    response: Response<ArrayList<CategoriesResponse>>
                ) {
                    if(response.isSuccessful && response.code()==200)
                    {
                        if(response.body()!!.size>0)
                        {
                            sizeCallBack.onSucessType(response.body()!!)
                        }
                    }
                    else
                    {
                        sizeCallBack.onErrorType(response.message())
                    }
                }

            })

        }
        else
        {
            sizeCallBack.onErrorType("No Internrt Connection")
        }
    }

    override fun getVolume(sizeCallBack: SizeContract.SizeCallBack) {
        if(BaseApplication.instance!!.hasNetwork())
        {
            RetrofitClient.getInstance().getapi().getVolume().enqueue(object: Callback<ArrayList<CategoriesResponse>>
            {
                override fun onFailure(call: Call<ArrayList<CategoriesResponse>>, t: Throwable) {
                    sizeCallBack.onErrorVolume(t.message!!)
                }

                override fun onResponse(
                    call: Call<ArrayList<CategoriesResponse>>,
                    response: Response<ArrayList<CategoriesResponse>>
                ) {
                    if(response.isSuccessful && response.code()==200)
                    {
                        if(response.body()!!.size>0)
                        {
                            sizeCallBack.onSucessVolume(response.body()!!)
                        }
                        else
                        {
                            sizeCallBack.onErrorVolume(response.message())
                        }
                    }
                }

            })

        }
        else
        {
            sizeCallBack.onErrorVolume("No Internrt Connection")
        }
    }
}