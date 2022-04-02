package com.arumdental.home.order.createOrder.view

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.arumdental.R
import com.arumdental.coupons.CouponsContract
import com.arumdental.coupons.model.CouponsResponseModel
import com.arumdental.coupons.presenter.CouponPresenter
import com.arumdental.home.HomeActivity
import com.arumdental.home.payment.view.PaymentFragment
import com.arumdental.home.address.AddressFragment
import com.arumdental.home.address.AllAddressFragment
import com.arumdental.home.cart.CartContract
import com.arumdental.home.cart.adapter.CartAdapter
import com.arumdental.home.cart.adapter.CartAdapter2
import com.arumdental.home.cart.clearCart.ClearCartContract
import com.arumdental.home.cart.clearCart.presenter.ClearCartPresenter
import com.arumdental.home.cart.model.AddToCartResponse
import com.arumdental.home.cart.model.CartResponse
import com.arumdental.home.cart.model.UpdateCartResponse
import com.arumdental.home.cart.presenter.CartPresenter
import com.arumdental.home.order.createOrder.CreateOrderContract
import com.arumdental.home.order.createOrder.model.*
import com.arumdental.home.order.createOrder.presenter.CreateOrderPresenter
import com.arumdental.utils.*
import com.google.gson.Gson
import kotlinx.android.synthetic.main.checkout_layout.*
import kotlinx.android.synthetic.main.placeorder_layout.*
import kotlinx.android.synthetic.main.placeorder_layout.rvCart
import kotlinx.android.synthetic.main.placeorder_layout.scrollView
import kotlinx.android.synthetic.main.placeorder_layout.tvNoProductsCart
import kotlinx.android.synthetic.main.placeorder_layout.tvSubtotal
import kotlinx.android.synthetic.main.placeorder_layout.tvTotal
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList

class PlaceOrderFragment :Fragment(),CartContract.CartView, CartAdapter2.updateCart, View.OnClickListener,
    CouponsContract.CouponView, CreateOrderContract.CreateOrderView, ClearCartContract.ClearCartView{
    var showToolbar: ShowToolbar?=null
    var cartPresenter: CartPresenter?=null
    var couponPresenter:CouponPresenter?=null
    var isRemove:Boolean?=false
    var couponmount:Int?=null
    var total: Double? = 0.0
    var clearCartPresenter:ClearCartPresenter?=null
    var userSharedPrefences:UserSharedPrefences?=null
    var couponlist:ArrayList<CouponsResponseModel>?=null
    var codeId:Int?=null
    var list: ArrayList<CartResponse>? = ArrayList()
    var codeName:String?=null
    var createOrderPresenter:CreateOrderPresenter?=null
    var totalAmount:Int?=0

    var orderID:Int?=null
    var orderTotal:Double?=null
    var shippingprice:Double?=null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.placeorder_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private  fun init(){

        createOrderPresenter= CreateOrderPresenter(this)
        clearCartPresenter= ClearCartPresenter(this)
        tvApply.setOnClickListener(this)
        tvAddAddress.setOnClickListener(this)
       // tvEdit.setOnClickListener(this)
        tvPlaceOrder.setOnClickListener(this)
        ivBackPlaceOrder.setOnClickListener(this)
        couponPresenter= CouponPresenter(this)
        userSharedPrefences=UserSharedPrefences.getInstance()

        println("billing model"+Gson().toJson(UserSharedPrefences.getInstance().billingModel))
        if(userSharedPrefences!!.billingModel!=null)
        {
           // tvAddress1.setText(userSharedPrefences!!.billingModel.address_1+"  "+userSharedPrefences!!.billingModel.address_2)
            tvAddress2.setText(userSharedPrefences!!.billingModel.address_1+" "+userSharedPrefences!!.billingModel.address_2 +" \n"+userSharedPrefences!!.billingModel.city+","+userSharedPrefences!!.billingModel.state
            +" "+userSharedPrefences!!.billingModel.country)
            tvAddress3.setText(userSharedPrefences!!.billingModel.postcode)
            tvAddress1.setText(userSharedPrefences!!.name)
            tvAddress5.setText("Mobile:"+userSharedPrefences!!.phone)
        }

        showToolbar!!.changeToolbar(false, "Place Order Frag", false, true)
        cartPresenter=CartPresenter(this)
        cartPresenter!!.getCart()
        showProgress(context!!)


    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        showToolbar=context as HomeActivity
    }

    override fun onSucessAddTOCart(addToCartResponse: AddToCartResponse) {

    }

    override fun onErrorAddTOCart(error: String) {

    }

    override fun onSucessUpdateCart(updateCartResponse: UpdateCartResponse) {
        if(isAdded)
        {
            cartPresenter!!.getCart()
            showProgress(context!!)
        }

    }

    override fun onErrorUpdateCart(error: String) {
        if(isAdded)
        {
            showSnackBar(CheckOutView,error)
        }

    }

    override fun onSucessCart(responseBody: ResponseBody) {
        if(isAdded)
        {
            hideProgress()
            list?.clear()

            var json : String? = responseBody.string()

            if(!json.isNullOrEmpty()){
                var newRes =json?.replace("\\u00a3","")
                Log.d("CheckoutFragment","Json: $newRes")
                var newRes2=json?.replace("\\u20ac","")
                var newRes3=json?.replace("£","")

                if(newRes2.equals("[]")){
                    Toast.makeText(context, "No data found", Toast.LENGTH_SHORT).show()
                    hideProgress()
                    scrollView.visibility=View.GONE
                    tvNoProductsCart.visibility=View.VISIBLE
                }
                else {


                    val jsonObject: JSONObject? = JSONObject(newRes2)

                    if (jsonObject != null) {
                        val keys: Iterator<*> = jsonObject.keys()


                        while (keys.hasNext()) {

                            val key = keys.next() as String
                            try {
                                var value: JSONObject = jsonObject.getJSONObject(key)
                                println("value " + value)
                                var cartModel = CartResponse()

                                cartModel.product_id = value.getInt("product_id")
                                cartModel.key = value.getString("key")
                                cartModel.product_name = value.getString("product_name")
                                cartModel.product_title = value.getString("product_title")
                                cartModel.product_price = value.getString("product_price")
                                cartModel.quantity = value.getInt("quantity")
                                list!!.add(cartModel!!)


                            } catch (e: JSONException) {
                                e.printStackTrace()
                            }

                            println("jdghdjkhgk" + Gson().toJson((list)))

                            var cartAdapter = CartAdapter2(context!!, list!!)
                            cartAdapter.setOnClickListener(this)
                            rvCart.adapter = cartAdapter
                            total=0.0
                            shippingprice=0.0
                            orderTotal=0.0


                            for( i in list!!.indices)
                            {
                                var price=list!!.get(i).product_price!!.replace("€","")
                             //   var spilt=price.split(".")
                               // var result=spilt[0]
                                var newresult=price.toDouble()* list!!.get(i).quantity!!
                                println("result  "+price  )
                                total=total!!+newresult


                            }


                            tvSubtotal.setText("€"+total)

                            if(UserSharedPrefences.getInstance().price!=null)
                            {
                                tvShippingPrice.setText("€"+userSharedPrefences!!.price)
                                var double:Double=userSharedPrefences!!.price.toDouble()
                                var result=String.format("%.2f", double);
                                var newresult=result.toDouble()
                                shippingprice=newresult
                                var sfkskf=newresult+total!!
                                orderTotal=sfkskf
                                tvTotal.setText("€ ${String.format(Locale.getDefault(),"%.2f",orderTotal)}")


                            }
                            else
                            {

                                tvShippingPrice.setText("€"+0)
                                var double:Double=userSharedPrefences!!.price.toDouble()

                                var sfkskf=total!!
                                orderTotal=sfkskf.toDouble()
                                tvTotal.setText("€ ${String.format(Locale.getDefault(),"%.2f",orderTotal)}")
                            }



                        }
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

    override fun onSucess(couponsResponseModel: ArrayList<CouponsResponseModel>) {

        if(isAdded)
        {
            hideProgress()
            couponlist=couponsResponseModel;
            for (i in couponlist!!.indices)
            {
                var coupon=etCoupon.text.toString().trim()
                if(coupon.equals(couponlist!!.get(i).code))
                {
                    tvCouponApplied.visibility=View.VISIBLE
                    tvCouponApplied.setText("Coupon Applied")
                    tvApply.setText("Remove")
                    isRemove=true
                    etCoupon.isEnabled=false
                    codeId=couponlist!!.get(i).id
                    codeName=couponlist!!.get(i).code
                    tvCouponAmount.setText("€"+couponlist!!.get(i).amount)
                    var spilt=couponlist!!.get(i).amount!!.split(".")
                    var result=spilt[0]
                    couponmount=result.toInt()
                    if(couponmount!! < total!!)
                    {
                        var newAmount=orderTotal!!-couponmount!!

                        orderTotal=newAmount
                        tvTotal.setText("€ ${String.format(Locale.getDefault(),"%.2f",newAmount)}")

                    }
                    else
                    {
                        tvCouponApplied.setText("Coupon Cannot be Applied")
                    }

                    break
                }
                else
                {
                    tvCouponApplied.visibility=View.VISIBLE
                    tvCouponApplied.setText("Coupon is invalid")
                    tvCouponApplied.setTextColor(ContextCompat.getColor(context!!, R.color.colorRed))
                    tvApply.setText("Remove")
                    isRemove=true

                }
            }
        }


    }

    override fun onErrorCoupons(error: String) {
        hideProgress()

    }

    override fun onSucess(createOrderResponseModel: CreateOrderResponseModel) {
       hideProgress()
        orderID= createOrderResponseModel.id

        hideProgress()
        var args=Bundle()
        args.putSerializable("orderId",orderID)
        args.putSerializable("total",orderTotal)
        var fragment= PaymentFragment()
        fragment.arguments=args
        (context as HomeActivity).replaceFragmentWithOutBackStack(fragment!!,"payment frag")


    }

    override fun onErrorCreateOrder(error: String) {
        hideProgress()
        showSnackBar(PlaceOrderView,error)

    }

    override fun onError(error: String) {
        if(isAdded)
        {
            hideProgress()
            showSnackBar(PlaceOrderView, error)
        }
    }

    override fun updatevalue(quantity: Int, cart_key: String, i: Int) {
        cartPresenter!!.updateCart(quantity,cart_key!! )
    }

    override fun onClick(v: View?) {
        when(v)
        {
            tvApply->
            {
                if(!isRemove!!)
                {
                    if(!TextUtils.isEmpty(etCoupon.text.toString().trim()))
                    {
                        couponPresenter!!.getCoupns()
                        showProgress(context!!)
                    }else{
                        tvCouponApplied.setText("Please enter coupon code")


                }

                }
                else{
                    etCoupon.setText("")
                    tvApply.setText("Apply")
                    etCoupon.isEnabled=true
                    tvCouponApplied.visibility=View.GONE
                    isRemove=false
                    tvCouponAmount.setText("€0.00")
                    tvTotal.setText("€ ${String.format(Locale.getDefault(),"%.2f",orderTotal)}")
                }

            }
            /*tvEdit->
            {
                var args=Bundle()
                args.putSerializable("coming","edit")
                var fragment= AddressFragment()
                fragment.arguments=args
                (context as HomeActivity).replaceFragmentWithOutBackStack(fragment!!,"address frag")

            }*/
            tvAddAddress->
            {
             /*   var args=Bundle()
                args.putSerializable("coming","new")
                var fragment= AddressFragment()
                fragment.arguments=args*/
                (context as HomeActivity).replaceFragmentWithOutBackStack(AllAddressFragment(),"address frag")
            }
            tvPlaceOrder->
            {

                val itemprice=userSharedPrefences!!.ofitems
                val spilt=itemprice.split(".")
                if(spilt[0].toInt()==0)
                {
                        placeOrder()
                }else{

                    if(orderTotal!!>=spilt[0].toInt())
                    {
                        placeOrder()
                    }
                    else{

                        Toast.makeText(context!!,"Please increase the item in your cart, the total should be more than "+spilt[0], Toast.LENGTH_SHORT).show()
                    }
                }







            }
            ivBackPlaceOrder->
            {
                activity!!.supportFragmentManager.popBackStack()
            }
        }
    }

    override fun onSucessClearCart(responseBody: ResponseBody) {

       // showSnackBar(PlaceOrderView,"Order Placed")

    }

    override fun onErrorClearCart(error: String) {
      hideProgress()
    }

    private  fun placeOrder()
    {
        var createOrderModel=CreateOrderModel()
        var couponLines:ArrayList<CouponLines>?= ArrayList()
        if(codeName!=null)
        {
            couponLines!!.add(CouponLines(codeName!!, codeId!!))
        }


        var shippinglinelist:ArrayList<ShippingLines>?= ArrayList()
        shippinglinelist!!.add(ShippingLines(
            "sdas",
            "kjgdlkg",
            shippingprice.toString()
        ))

        var linesItemslist:ArrayList<LinesItems>?= ArrayList()

        for(i in list!!.indices)
        {
            var linesItems=LinesItems()
            linesItems.product_id= list!!.get(i).product_id.toString()
            linesItems.quantity= list!!.get(i).quantity.toString()
            var spilt=list!!.get(i).product_price!!.split(".")
            var result=spilt[0]
            var price=result.replace("€","")
            var  totalAmount1=price.toInt() * list!!.get(i).quantity!!
            totalAmount=totalAmount!!+totalAmount1

            linesItemslist!!.add(linesItems)


        }
        println("line ywjfj"+Gson().toJson(linesItemslist))
        println("toatsl amnlksjd "+totalAmount)

        createOrderModel.payment_method="stripe"
        createOrderModel.payment_method_title="stripe"
        createOrderModel.coupon_lines=couponLines
        createOrderModel.shipping_lines=shippinglinelist
        createOrderModel.set_paid=false
        createOrderModel.billing=UserSharedPrefences.getInstance().billingModel
        createOrderModel.shipping=UserSharedPrefences.getInstance().billingModel
        createOrderModel.line_items=linesItemslist
        createOrderModel.customer_id=userSharedPrefences!!.id

        createOrderPresenter!!.createOrder(createOrderModel)
        showProgress(context!!)
    }


}