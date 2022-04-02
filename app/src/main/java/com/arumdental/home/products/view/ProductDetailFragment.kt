package com.arumdental.home.products.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.arumdental.R
import com.arumdental.home.HomeActivity
import com.arumdental.home.cart.CartContract
import com.arumdental.home.cart.model.AddToCartResponse
import com.arumdental.home.cart.model.UpdateCartResponse
import com.arumdental.home.cart.presenter.CartPresenter
import com.arumdental.home.products.adapter.ProductImageSliderAdpater
import com.arumdental.home.products.adapter.ProductViewPagerAdapter
import com.arumdental.home.products.model.ProductResponseModel
import com.arumdental.utils.ShowToolbar
import com.arumdental.utils.hideProgress
import com.arumdental.utils.showProgress
import com.arumdental.utils.showSnackBar
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.product_detail.*
import okhttp3.ResponseBody

class ProductDetailFragment :Fragment(), CartContract.CartView, View.OnClickListener{
    var productResponseModel:ProductResponseModel?=null
    var showToolbar: ShowToolbar?=null
    var cartPresenter:CartPresenter?=null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.product_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private  fun init()
    {

        cartPresenter=CartPresenter(this)
        tvAddToCartProduct.setOnClickListener(this)

        showToolbar!!.changeToolbar(true, "Product Detail",false, true)
        if(arguments!=null)
        {

             productResponseModel= arguments!!.getSerializable("product Detail") as ProductResponseModel?
            var productViewPagerAdapter=ProductImageSliderAdpater(context!!,productResponseModel!!.images!!)
            ProductviewPager.adapter=productViewPagerAdapter

            if(productResponseModel!!.images!!.size==1)
            {
                indicator.visibility=View.GONE
            }
            else{
                indicator.visibility=View.VISIBLE
                indicator.setupWithViewPager(ProductviewPager)
            }


            tvProductName.setText(productResponseModel?.name)
            tvProductPrice.setText("€"+productResponseModel?.price)

            if(productResponseModel?.stock_quantity.equals("null"))
            {
                llNotify.visibility=View.VISIBLE
                tvAddToCartProduct.setText("Out of Stock")
            }
        }


    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        showToolbar=context as HomeActivity
    }

    override fun onSucessAddTOCart(addToCartResponse: AddToCartResponse) {
        hideProgress()
        showSnackBar(ProductDetail, "Item added to cart")


    }

    override fun onErrorAddTOCart(error: String) {
        hideProgress()
        showSnackBar(ProductDetail, error)

    }

    override fun onSucessUpdateCart(updateCartResponse: UpdateCartResponse) {

    }

    override fun onErrorUpdateCart(error: String) {

    }

    override fun onSucessCart(responseBody: ResponseBody) {

    }

    override fun onError(error: String) {

    }

    override fun onClick(v: View?) {
        when(v)
        {
            tvAddToCartProduct->
            {
                if(!productResponseModel?.stock_quantity.equals("null"))
                {
                    cartPresenter!!.addToCart(1, productResponseModel?.id!!)
                    showProgress(context!!)
                }
            }
        }
    }

}