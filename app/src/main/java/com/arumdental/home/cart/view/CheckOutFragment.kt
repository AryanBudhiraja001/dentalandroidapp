package com.arumdental.home.cart.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.arumdental.R
import com.arumdental.home.HomeActivity
import com.arumdental.home.address.AddressFragment
import com.arumdental.home.address.AllAddressFragment
import com.arumdental.home.cart.CartContract
import com.arumdental.home.cart.adapter.CartAdapter
import com.arumdental.home.cart.model.AddToCartResponse
import com.arumdental.home.cart.model.CartResponse
import com.arumdental.home.cart.model.UpdateCartResponse
import com.arumdental.home.cart.presenter.CartPresenter
import com.arumdental.home.order.createOrder.view.PlaceOrderFragment
import com.arumdental.utils.*
import com.google.gson.Gson
import kotlinx.android.synthetic.main.checkout_layout.*
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject


class CheckOutFragment : Fragment(), CartContract.CartView, CartAdapter.updateCart, View.OnClickListener {

    var showToolbar: ShowToolbar? = null
    var cartPresenter: CartPresenter? = null

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.checkout_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        showToolbar = context as HomeActivity
    }

    private fun init() {
        tvProceed.setOnClickListener(this)
        ivBackCheckOut.setOnClickListener(View.OnClickListener { v -> activity!!.supportFragmentManager.popBackStack() })
        showToolbar!!.changeToolbar(false, "Check Out Frag", false, true)
        cartPresenter = CartPresenter(this)
        cartPresenter!!.getCart()
        showProgress(context!!)


    }

    override fun onSucessAddTOCart(addToCartResponse: AddToCartResponse) {

    }

    override fun onErrorAddTOCart(error: String) {

    }

    override fun onSucessUpdateCart(updateCartResponse: UpdateCartResponse) {
        if (isAdded) {
            cartPresenter!!.getCart()
            showProgress(context!!)
        }

    }

    override fun onErrorUpdateCart(error: String) {
        if (isAdded) {
            showSnackBar(CheckOutView, error)
        }

    }

    override fun onSucessCart(responseBody: ResponseBody) {
        if (isAdded) {
            hideProgress()
            llPrices.visibility = View.VISIBLE
            tvProceed.visibility = View.VISIBLE

            val json: String = responseBody.string()

            if (!json.isEmpty()) {

                val newRes = json.replace("\\u00a3", "")
                val newRes2 = json.replace("\\u20ac", "")
                // var newRes2=json?.replace("\\u20b9","")
                var newRes3 = json.replace("\\£", "")

                Log.d("CheckoutFragment", "Json: $newRes")

                if (newRes2.equals("[]")) {
                    hideProgress()
                    scrollView.visibility = View.GONE
                    tvNoProductsCart.visibility = View.VISIBLE
                } else {


                    val jsonObject: JSONObject? = JSONObject(newRes2)

                    if (jsonObject != null) {
                        val keys: Iterator<*> = jsonObject.keys()
                        val list: ArrayList<CartResponse> = ArrayList()

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
                                list.add(cartModel)


                            } catch (e: JSONException) {
                                e.printStackTrace()
                            }

                            println("jdghdjkhgk" + Gson().toJson((list)))

                            val cartAdapter = CartAdapter(context!!, list!!)
                            cartAdapter.setOnClickListener(this)
                            rvCart.adapter = cartAdapter
                            var total: Double? = 0.0

                            for (i in list.indices) {
                                val price = list.get(i).product_price!!.replace("€", "")
                                //  var spilt=price.split(".")
                                //var result=spilt[0]
                                val newresult = price.toDouble() * list.get(i).quantity!!
                                println("result  " + newresult)
                                total = total!! + newresult


                            }


                            tvSubtotal.setText("€" + total)
                            tvTotal.setText("€" + total)
                        }
                    }
                }


            } else {
                hideProgress()
                scrollView.visibility = View.GONE
                tvNoProductsCart.visibility = View.VISIBLE
            }

        }


    }

    override fun onError(error: String) {
        if (isAdded) {
            hideProgress()
            showSnackBar(CheckOutView, error)
        }

    }

    override fun updatevalue(quantity: Int, cart_key: String, i: Int) {
        cartPresenter!!.updateCart(quantity, cart_key)
    }

    override fun onClick(v: View?) {
        when (v) {
            tvProceed -> {

                if (UserSharedPrefences.getInstance().isaddressselected) {
                    if (UserSharedPrefences.getInstance()!!.billingModel != null) {

                        if (UserSharedPrefences.getInstance()!!.billingModel.address_1 != "") {
                            (context as HomeActivity).replaceFragmentWithOutBackStack(PlaceOrderFragment(), "place order frag")
                        }

                    } else {
                        (context as HomeActivity).replaceFragmentWithOutBackStack(AllAddressFragment(), "address frag")
                    }
                } else {
                    (context as HomeActivity).replaceFragmentWithOutBackStack(AllAddressFragment(), "address frag")
                }


            }
        }
    }


}