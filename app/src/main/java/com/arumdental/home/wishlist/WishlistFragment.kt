package com.arumdental.home.wishlist

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.arumdental.R
import com.arumdental.home.HomeActivity
import com.arumdental.home.cart.CartContract
import com.arumdental.home.cart.model.AddToCartResponse
import com.arumdental.home.cart.model.UpdateCartResponse
import com.arumdental.home.cart.presenter.CartPresenter
import com.arumdental.home.cart.view.CheckOutFragment
import com.arumdental.home.products.model.ProductResponseModel
import com.arumdental.home.wishlist.adapter.WishListAdapter
import com.arumdental.home.wishlist.addProduct.model.AddProductResponse
import com.arumdental.home.wishlist.getProductDetail.ProductDetailContract
import com.arumdental.home.wishlist.getProductDetail.presenter.ProductDetailPresenter
import com.arumdental.home.wishlist.getProducts.ProductsContract
import com.arumdental.home.wishlist.getProducts.model.WishlistProductsResponseModel
import com.arumdental.home.wishlist.getProducts.presenter.ProductsPresenter
import com.arumdental.home.wishlist.removeProduct.RemoveProductContract
import com.arumdental.home.wishlist.removeProduct.presenter.RemoveProductPresenter
import com.arumdental.utils.*
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.brands_fragment.*
import kotlinx.android.synthetic.main.product_fragment.*
import kotlinx.android.synthetic.main.wishlist_layout.*
import okhttp3.ResponseBody

class WishlistFragment :Fragment(),ProductsContract.ProductView,
    ProductDetailContract.ProductDetailView, View.OnClickListener, WishListAdapter.onClick, RemoveProductContract.RemoveProductView,CartContract.CartView{

    var productPresenter:ProductsPresenter?=null
    var productDetailPresenter:ProductDetailPresenter?=null
    var productIdlist:ArrayList<Int>?=null
    var showToolbar:ShowToolbar?=null
    var removeProductPresenter:RemoveProductPresenter?=null
    var cartpresenter: CartPresenter?=null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.wishlist_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private  fun init()
    {
        cartpresenter= CartPresenter(this)
        removeProductPresenter= RemoveProductPresenter(this)
        ivBackWishlist.setOnClickListener(this)
        ivCartWishlist.setOnClickListener(this)
        showToolbar!!.changeToolbar(false,"Wishlist", false, true)
        productPresenter=ProductsPresenter(this)


        productDetailPresenter= ProductDetailPresenter(this)
        productPresenter?.getProducts(UserSharedPrefences.getInstance().id)
        showProgress(context!!)

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        showToolbar=context as HomeActivity
    }

    override fun onSucessWishlist(productResponseModel: WishlistProductsResponseModel) {
        hideProgress()

        if(isAdded)
        {
            if(!productResponseModel.body.isNullOrEmpty())
            {
                rvWishlist.visibility=View.VISIBLE
                tvNoProductsWishlist.visibility=View.GONE
                productIdlist= ArrayList()
                for(i in productResponseModel.body!!.indices)
                {
                    productIdlist!!.add(productResponseModel.body!!.get(i).product_id!!.toInt())

                }

                val commaSeperatedString = productIdlist!!.joinToString()
                println("commmaa "+commaSeperatedString)
                productDetailPresenter?.getProductDetail(commaSeperatedString)
                showProgress(context!!)
            }
            else
            {
                rvWishlist.visibility=View.GONE
                tvNoProductsWishlist.visibility=View.VISIBLE

            }
        }



    }

    override fun onErrorWishlist(error: String) {
        hideProgress()

    }

    override fun onSucessProductDetail(productResponseModellist: ArrayList<ProductResponseModel>) {
      hideProgress()

        if(isAdded)
        {
            if(!productResponseModellist.isNullOrEmpty())
            {
                for(i in productResponseModellist.indices)
                {
                    productResponseModellist.get(i).isFav=true
                }

                rvWishlist.visibility=View.VISIBLE
                println("new list wishlidt"+Gson().to(productResponseModellist) )
                val wishListAdapter=WishListAdapter(context!!, productResponseModellist)
                val layoutmanger=GridLayoutManager(context,2)
                wishListAdapter.setOnClickListener(this)
                rvWishlist.layoutManager=layoutmanger
                rvWishlist.adapter=wishListAdapter

            }
            else{
                
        }




        }
    }

    override fun onErrorProductDetail(error: String) {
        rvWishlist.visibility=View.GONE
       hideProgress()
        showSnackBar(WishlistView,error)

    }

    override fun onClick(v: View?) {
        when(v)
        {
            ivBackWishlist->
            {
                activity!!.supportFragmentManager.popBackStack()
            }
            ivCartWishlist->
            {
                (context as HomeActivity).replaceFragmentWithOutBackStack(CheckOutFragment(), "cart frag")

            }

        }
    }

    override fun onFavClickListener(productID: Int) {
        removeProductPresenter?.removeProduct(productID)
        showProgress(context!!)

    }

    override fun onAddToCart(product_id: Int, qunatity: Int) {
        cartpresenter?.addToCart(qunatity, product_id)
        showProgress(context!!)

    }

    override fun onSucessRemoveProduct(addProductResponse: AddProductResponse) {
        hideProgress()
        productPresenter?.getProducts(UserSharedPrefences.getInstance().id)
        showProgress(context!!)

    }

    override fun onErrorRemoveProduct(error: String) {
        hideProgress()

    }

    override fun onSucessAddTOCart(addToCartResponse: AddToCartResponse) {
      hideProgress()
        showSnackBar(WishlistView, "Added to Cart")
    }

    override fun onErrorAddTOCart(error: String) {
        hideProgress()

    }

    override fun onSucessUpdateCart(updateCartResponse: UpdateCartResponse) {

    }

    override fun onErrorUpdateCart(error: String) {

    }

    override fun onSucessCart(responseBody: ResponseBody) {

    }

    override fun onError(error: String) {

    }

                                     }