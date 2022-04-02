package com.arumdental.home.cart.intractor

import com.arumdental.base.BaseApplication
import com.arumdental.home.cart.CartContract
import com.arumdental.home.cart.model.AddToCartResponse
import com.arumdental.home.cart.model.UpdateCartResponse
import com.arumdental.web.Retrofit2
import com.arumdental.web.RetrofitClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CartIntractor:CartContract.CartIntractor {
    override fun addToCart(
        quantity: Int,
        product_id: Int,
        cartCallBack: CartContract.CartCallBack
    ) {
        if(BaseApplication.instance!!.hasNetwork())
        {
            RetrofitClient.getInstance().getapi().addToCart(product_id,quantity).enqueue(object : Callback<AddToCartResponse>
            {
                override fun onFailure(call: Call<AddToCartResponse>, t: Throwable) {
                    cartCallBack.onErrorAddTOCart(t.message!!)
                }

                override fun onResponse(
                    call: Call<AddToCartResponse>,
                    response: Response<AddToCartResponse>
                ) {
                    if(response.isSuccessful && response.code()==200)
                    {
                        cartCallBack.onSucessAddTOCart(response.body()!!)
                    }
                    else if(response.code()==404)
                    {
                        cartCallBack.onErrorAddTOCart("Product out of stock")
                    }
                    else{
                        cartCallBack.onErrorAddTOCart(response.message())
                    }
                }

            })
        }
        else
        {
            cartCallBack.onErrorAddTOCart("No Internet Connection")
        }
    }

    override fun updateCart(
        quantity: Int,
        cart_item_key: String,
        cartCallBack: CartContract.CartCallBack
    ) {
        if(BaseApplication.instance!!.hasNetwork())
        {
            RetrofitClient.getInstance().getapi().updateCart(cart_item_key,quantity).enqueue(object :
                Callback<UpdateCartResponse>
            {
                override fun onFailure(call: Call<UpdateCartResponse>, t: Throwable) {
                    cartCallBack.onErrorUpdateCart(t.message!!)
                }

                override fun onResponse(
                    call: Call<UpdateCartResponse>,
                    response: Response<UpdateCartResponse>
                ) {
                    if(response.isSuccessful && response.code()==200)
                    {
                        if(response.body()!=null)
                        {
                            cartCallBack.onSucessUpdateCart(response.body()!!)
                        }
                        else
                        {
                            cartCallBack.onErrorUpdateCart(response.message()!!)
                        }

                    }
                    else
                    {
                        cartCallBack.onErrorUpdateCart(response.message())
                    }
                }

            })
        }
        else
        {
            cartCallBack.onErrorUpdateCart("No Internet Connection")
        }
    }

    override fun getCart(cartCallBack: CartContract.CartCallBack) {
        if(BaseApplication.instance!!.hasNetwork())
        {
            Retrofit2.getInstance().getapi().getCart().enqueue(object:Callback<ResponseBody>
            {
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    cartCallBack.onError(t.message!!)
                }

                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if(response.isSuccessful && response.code()==200)
                    {
                        if(response.body()!=null)
                        {
                            cartCallBack.onSucessCart(response.body()!!)
                        }


                    }
                    else{

                        cartCallBack.onError(response.message()!!)
                    }
                }

            })

        }
    }

}