package com.arumdental.home.products.view

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arumdental.R
import com.arumdental.home.HomeActivity
import com.arumdental.home.cart.CartContract
import com.arumdental.home.cart.model.AddToCartResponse
import com.arumdental.home.cart.model.CartResponse
import com.arumdental.home.cart.model.UpdateCartResponse
import com.arumdental.home.cart.presenter.CartPresenter
import com.arumdental.home.products.ProductContract
import com.arumdental.home.products.adapter.ProductAdapter
import com.arumdental.home.products.model.ProductResponseModel
import com.arumdental.home.products.presenter.ProductPresenter
import com.arumdental.home.wishlist.addProduct.AddProductContract
import com.arumdental.home.wishlist.addProduct.model.AddProductResponse
import com.arumdental.home.wishlist.addProduct.presenter.AddProductPresenter
import com.arumdental.home.wishlist.getProducts.ProductsContract
import com.arumdental.home.wishlist.getProducts.model.WishlistProductsResponseModel
import com.arumdental.home.wishlist.getProducts.presenter.ProductsPresenter
import com.arumdental.home.wishlist.removeProduct.RemoveProductContract
import com.arumdental.home.wishlist.removeProduct.presenter.RemoveProductPresenter
import com.arumdental.utils.*
import com.bumptech.glide.util.Util
import com.google.gson.Gson
import com.google.gson.JsonArray
import kotlinx.android.synthetic.main.checkout_layout.*
import kotlinx.android.synthetic.main.product_fragment.*
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject


class ProductFragment:Fragment(),
    ProductContract.ProductView ,
    View.OnClickListener,
    ProductAdapter.onAddToCart,
    CartContract.CartView,
    AddProductContract.AddProductView,
    ProductsContract.ProductView,
        RemoveProductContract.RemoveProductView{

    var productPresenter:ProductPresenter?=null
    var cartpresenter:CartPresenter?=null
    var addProductPresenter: AddProductPresenter?=null
    var whishlistPresenter: ProductsPresenter?=null
    var removeProductPresenter:RemoveProductPresenter?=null
    var alertDialog: Dialog?=null
    var layoutmanager:RecyclerView.LayoutManager?=null
    var productResponseModellist: ArrayList<ProductResponseModel>?=null
    var productResponseModellistSearch: ArrayList<ProductResponseModel>?=null
    var showToolbar: ShowToolbar?=null
    var cartlist:ArrayList<CartResponse>?=null
    var menuInflater :MenuInflater?=null
    var mView:View?=null
    var categoryId:Int?=null
    var isSearch:Boolean?=false
    var iComingFromFilter:Boolean?=false

    var isRemove:Boolean?=false


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

           return  inflater.inflate(R.layout.product_fragment, container, false)





    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }
    private  fun init(){
        layoutmanager=LinearLayoutManager(context)


        removeProductPresenter=RemoveProductPresenter(this)
        whishlistPresenter=ProductsPresenter(this)
        productPresenter= ProductPresenter(this)
        cartpresenter= CartPresenter(this)
        addProductPresenter=AddProductPresenter(this)
        cartpresenter!!.getCart()
        showToolbar!!.changeToolbar(true, "Product", true, true)
        if(arguments!=null)
        {

            if(arguments!!.getString("comingFrom").equals("filter"))
            {
                iComingFromFilter=true
                showToolbar?.changeToolbar(true,"Product", false, true)
                productResponseModellist= arguments!!.getSerializable("list") as ArrayList<ProductResponseModel>?
                if(productResponseModellist!!.size>0)
                {
                    rvProducts.visibility=View.VISIBLE
                    tvNoProducts.visibility=View.GONE
                    relarrange.visibility=View.VISIBLE
                    whishlistPresenter?.getProducts(UserSharedPrefences.getInstance()!!.id)
                    showProgress(context!!)

               /*  var productAdapter=ProductAdapter(context!!,productResponseModellist!!, R.layout.item_product)
                    rvProducts.layoutManager=layoutmanager
                    productAdapter.setOnClickListener(this)
                    rvProducts.adapter=productAdapter*/
                }
                else{
                    rvProducts.visibility=View.GONE
                    tvNoProducts.visibility=View.VISIBLE
                    relarrange.visibility=View.GONE
                }


            }
            else
            {
                iComingFromFilter=false
                categoryId=arguments!!.getInt("id")
                productPresenter!!.getProducts(arguments!!.getInt("id"),"")
                showProgress(context!!)
            }

        }

        ivGridView.setOnClickListener(this)
        ivListView.setOnClickListener(this)

        llPrice.setOnClickListener{
          showPopup(llPrice)
        }

        txtPrice.setOnClickListener{
            showPopup(llPrice)

        }
        ivArrowDown.setOnClickListener{
            showPopup(llPrice)

        }




    }

    override fun onSucess(productResponseModel: ArrayList<ProductResponseModel>) {

        if(isAdded)
        {
           // hideProgress()

            if(productResponseModel.size>0)
            {
                if(isSearch!!)
                {
                    productResponseModellistSearch=productResponseModel
                }
                else
                {
                    productResponseModellist=productResponseModel
                }

                whishlistPresenter?.getProducts(UserSharedPrefences.getInstance()!!.id)
                showProgress(context!!)
             /*   rvProducts.visibility=View.VISIBLE
                tvNoProducts.visibility=View.GONE
                var productAdapter=ProductAdapter(context!!,productResponseModellist!!, R.layout.item_product)
                productAdapter.setOnClickListener(this)
                rvProducts.adapter=productAdapter*/
            }
            else{
                rvProducts.visibility=View.GONE
                tvNoProducts.visibility=View.VISIBLE
            }
        }




    }

    override fun onSucessAddTOCart(addToCartResponse: AddToCartResponse) {
        hideProgress()
        showSnackBar(ProductView, "Added to Cart")

    }

    override fun onErrorAddTOCart(error: String) {
        hideProgress()
        showSnackBar(ProductView, error)

    }

    override fun onSucessUpdateCart(updateCartResponse: UpdateCartResponse) {
        hideProgress()
        showSnackBar(ProductView, "Cart Updated")

    }

    override fun onErrorUpdateCart(error: String) {
       hideProgress()
        showSnackBar(ProductView, error)
    }

    override fun onSucessCart(responseBody: ResponseBody) {
        if(isAdded)
        {


            val json : String = responseBody.string()
            var json1:JsonArray

            if(!json.isNullOrEmpty()){

                val newRes =json.replace("\\u00a3","")
                Log.d("CheckoutFragment","Json: $newRes")

                if(newRes.equals("[]")){
                  //  Toast.makeText(context, "No data found", Toast.LENGTH_SHORT).show()
                }
                else{
                    val jsonObject : JSONObject = JSONObject(newRes)

                    val keys: Iterator<*> = jsonObject.keys()
                    cartlist= ArrayList()

                    while (keys.hasNext()) {

                        val key = keys.next() as String
                        try {
                            val value: JSONObject = jsonObject.getJSONObject(key)
                            println("value " + value)
                            val cartModel = CartResponse()

                            cartModel.product_id = value.getInt("product_id")
                            cartModel.key = value.getString("key")
                            cartModel.product_name = value.getString("product_name")
                            cartModel.product_title = value.getString("product_title")
                            cartModel.product_price = value.getString("product_price")
                            cartModel.quantity = value.getInt("quantity")
                            cartlist!!.add(cartModel)


                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }

                        println("jdghdjkhgk" + Gson().toJson((cartlist)))


                    }
                }

            }

            else {
                hideProgress()
                scrollView.visibility = View.GONE
                tvNoProductsCart.visibility = View.VISIBLE
            }

        }
    }

    override fun onError(error: String) {
        hideProgress()

    }

    override fun onClick(v: View?) {
     when(v?.id)
     {
         R.id.ivGridView->
         {
             ivGridView.setImageDrawable(ContextCompat.getDrawable(context!!, R.drawable.category_blk))
             ivListView.setImageDrawable(ContextCompat.getDrawable(context!!, R.drawable.list_view))

             layoutmanager=GridLayoutManager(context,2)
             if(productResponseModellist!=null)
             {
                 val productAdapter=ProductAdapter(context!!,productResponseModellist!!, R.layout.item_product_grid)
                 productAdapter.setOnClickListener(this)
                 rvProducts.layoutManager=layoutmanager
                 rvProducts.adapter=productAdapter

             }


         }
         R.id.ivListView->
         {
             ivGridView.setImageDrawable(ContextCompat.getDrawable(context!!, R.drawable.category))
             ivListView.setImageDrawable(ContextCompat.getDrawable(context!!, R.drawable.list_view_blk))
             layoutmanager=LinearLayoutManager(context)
             if(productResponseModellist!=null)
             {
                 val productAdapter=ProductAdapter(context!!,productResponseModellist!!, R.layout.item_product)
                 productAdapter.setOnClickListener(this)
                 rvProducts.layoutManager=layoutmanager
                 rvProducts.adapter=productAdapter
             }

         }

         R.id.llPrice->
         {
             registerForContextMenu(llPrice)
         }
         R.id.txtPrice->{
             registerForContextMenu(txtPrice)
         }
         R.id.ivArrowDown->
         {
             registerForContextMenu(llPrice)
         }
     }
    }


    private fun showPopup(view: View) {
        val popup = PopupMenu(context!!, view)
        popup.inflate(R.menu.sort_list)

        popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item: MenuItem? ->

            when (item!!.itemId) {
                R.id.highToLow -> {
                    productResponseModellist!!.sortBy { it.price }
                    val productAdapter=ProductAdapter(context!!,productResponseModellist!!, R.layout.item_product)
                    productAdapter.setOnClickListener(this)
                    rvProducts.adapter=productAdapter

                }
                R.id.LowToHigh->
                {
                    productResponseModellist!!.sortByDescending { it.price }
                   // productResponseModellist!!.sortBy { it.price }
                    val productAdapter=ProductAdapter(context!!,productResponseModellist!!, R.layout.item_product)
                    productAdapter.setOnClickListener(this)
                    rvProducts.adapter=productAdapter
                }
                R.id.AtoZ->
                {
                    productResponseModellist!!.sortBy { it.name!!.toLowerCase() }

                    val productAdapter=ProductAdapter(context!!,productResponseModellist!!, R.layout.item_product)
                    productAdapter.setOnClickListener(this)
                    rvProducts.adapter=productAdapter

                }
                R.id.ZtoA->
                {
                    productResponseModellist!!.sortByDescending { it.name!!.toLowerCase() }

                    val productAdapter=ProductAdapter(context!!,productResponseModellist!!, R.layout.item_product)
                    productAdapter.setOnClickListener(this)
                    rvProducts.adapter=productAdapter
                }


            }

            true
        })

        popup.show()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        showToolbar=context as HomeActivity
    }

    override fun onAddToCart(product_id: Int, qunatity: Int) {

        cartpresenter!!.addToCart(qunatity,product_id)
        showProgress(context!!)

    }

    override fun onAddFav(product_id: Int, price: String, i:Int) {

        if(i==1)
        {
            addProductPresenter?.addProduct(UserSharedPrefences.getInstance().id,product_id, price)
            showProgress(context!!)
        }
        else
        {
            removeProductPresenter?.removeProduct(product_id)
            showProgress(context!!)
        }



    }


      fun onSearchProduct(string: CharSequence, i:Int)
    {
        if(i==1)
        {
            isSearch=true
            productPresenter!!.getProducts(categoryId!!,string.toString())
            showProgress(context!!)
        }
        else
        {

            isSearch=false
            if(productResponseModellist!=null)
            {
                val productAdapter=ProductAdapter(context!!,productResponseModellist!!, R.layout.item_product)
                productAdapter.setOnClickListener(this)
                rvProducts.adapter=productAdapter
                relarrange.visibility=View.VISIBLE
                tvNoProducts.visibility=View.GONE
                rvProducts.visibility=View.VISIBLE
            }


        }
    }

    override fun onSucessAddProduct(addProductResponse: AddProductResponse) {

        showSnackBar(ProductView, addProductResponse.message.toString())

        if(iComingFromFilter!!)
        {
                 hideProgress()
            isSearch=false
            whishlistPresenter?.getProducts(UserSharedPrefences.getInstance()!!.id)
            showProgress(context!!)
        }
        else
        {
            productPresenter!!.getProducts(categoryId!!,"")
            showProgress(context!!)
        }


    }

    override fun onErrorAddProduct(error: String) {
        hideProgress()
       showSnackBar(ProductView, error)

    }

    override fun onSucessWishlist(productResponseModel: WishlistProductsResponseModel) {
        hideProgress()
        if(!productResponseModel.body.isNullOrEmpty())
        {

            if(isSearch!!)
            {
                for(i in productResponseModellistSearch!!.indices)
                {
                    for(j in productResponseModel.body!!.indices)
                    {
                        if(productResponseModellistSearch!!.get(i).id!!.equals(productResponseModel.body!!.get(j).product_id))
                        {
                            println("nksdfkjfkw s  "+productResponseModellistSearch!!.get(i).id +"  "+productResponseModel.body!!.get(j).product_id)
                            productResponseModellistSearch!!.get(i).isFav=true
                            //    productResponseModellist!!.get(i).productID=productResponseModel.body!!.get(j).ID!!.toInt()
                        }


                    }


                }
                println("new list "+Gson().toJson(productResponseModellistSearch))

                rvProducts.visibility=View.VISIBLE
                tvNoProducts.visibility=View.GONE
                relarrange.visibility=View.VISIBLE
                val productAdapter=ProductAdapter(context!!,productResponseModellistSearch!!, R.layout.item_product)
                rvProducts.layoutManager=layoutmanager
                productAdapter.setOnClickListener(this)
                rvProducts.adapter=productAdapter
            }
            else
            {


                    for(i in productResponseModellist!!.indices)
                    {
                        for(j in productResponseModel.body!!.indices)
                        {
                            if(productResponseModellist!!.get(i).id!!.equals(productResponseModel.body!!.get(j).product_id))
                            {
                                println("nksdfkjfkw s  "+productResponseModellist!!.get(i).id +"  "+productResponseModel.body!!.get(j).product_id)
                                productResponseModellist!!.get(i).isFav=true
                                //    productResponseModellist!!.get(i).productID=productResponseModel.body!!.get(j).ID!!.toInt()
                            }



                        }


                    }
                    println("new list "+Gson().toJson(productResponseModellist))

                    rvProducts.visibility=View.VISIBLE
                    tvNoProducts.visibility=View.GONE
                    relarrange.visibility=View.VISIBLE
                    val productAdapter=ProductAdapter(context!!,productResponseModellist!!, R.layout.item_product)
                    rvProducts.layoutManager=layoutmanager
                    productAdapter.setOnClickListener(this)
                    rvProducts.adapter=productAdapter



                }






        }
        else
        {
            rvProducts.visibility=View.VISIBLE
            tvNoProducts.visibility=View.GONE
            relarrange.visibility=View.VISIBLE

            for( i in productResponseModellist!!.indices){
                productResponseModellist!![i].isFav=false
            }

            val productAdapter=ProductAdapter(context!!,productResponseModellist!!, R.layout.item_product)
            rvProducts.layoutManager=layoutmanager
            productAdapter.setOnClickListener(this)
            rvProducts.adapter=productAdapter
        }

    }

    override fun onErrorWishlist(error: String) {
        hideProgress()
        rvProducts.visibility=View.VISIBLE
        tvNoProducts.visibility=View.GONE
        relarrange.visibility=View.VISIBLE

        val productAdapter=ProductAdapter(context!!,productResponseModellist!!, R.layout.item_product)
        rvProducts.layoutManager=layoutmanager
        productAdapter.setOnClickListener(this)
        rvProducts.adapter=productAdapter
    }

    override fun onSucessRemoveProduct(addProductResponse: AddProductResponse) {
        hideProgress()
        whishlistPresenter?.getProducts(UserSharedPrefences.getInstance()!!.id)
        showProgress(context!!)
        showSnackBar(ProductView, addProductResponse.message.toString())
        isRemove=true

    }

    override fun onErrorRemoveProduct(error: String) {
        hideProgress()
        showSnackBar(ProductView, error)


    }

    override fun onResume() {
        super.onResume()
        init()
    }

}