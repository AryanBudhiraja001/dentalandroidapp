package com.arumdental.home.payment.view

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.arumdental.R
import com.arumdental.home.HomeActivity
import com.arumdental.home.OrderPlacedFragment
import com.arumdental.home.cart.clearCart.ClearCartContract
import com.arumdental.home.cart.clearCart.presenter.ClearCartPresenter
import com.arumdental.home.payment.PaymentContract
import com.arumdental.home.payment.presenter.PaymentPresenter
import com.arumdental.utils.hideProgress
import com.arumdental.utils.showProgress
import com.arumdental.utils.showSnackBar
import com.google.android.material.textfield.TextInputEditText
import com.stripe.android.ApiResultCallback
import com.stripe.android.Stripe
import com.stripe.android.StripeTextUtils.removeSpacesAndHyphens
import com.stripe.android.model.Card
import com.stripe.android.model.Token
import com.stripe.android.view.CardMultilineWidget
import kotlinx.android.synthetic.main.new_payment_layout.*
import kotlinx.android.synthetic.main.payment.Payment
import kotlinx.android.synthetic.main.payment.edName
import kotlinx.android.synthetic.main.payment.ivBackPayment
import kotlinx.android.synthetic.main.payment.tvAmount
import kotlinx.android.synthetic.main.payment.tvMakePayment
import okhttp3.ResponseBody
import java.util.*

class PaymentFragment:Fragment(), View.OnClickListener, PaymentContract.PaymentView, ClearCartContract.ClearCartView{

    var cardMultilineWidget:CardMultilineWidget?=null
    var cardtoSave:Card?=null
    var stripe:Stripe?=null
        var orderID:Int?=null
    var total:Double?=null
    var paymentPrsenter: PaymentPresenter?=null
    var clearCartPresenter:ClearCartPresenter?=null
    var isClicked:Boolean?=false

    private val mIsCardNumberValid = false


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.new_payment_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }
    private  fun init()
    {
        clearCartPresenter=ClearCartPresenter(this)
        paymentPrsenter=PaymentPresenter(this)
        if(arguments!=null)
        {
            orderID=arguments!!.getInt("orderId", 0)
            total=arguments!!.getDouble("total")
            tvAmount.setText("€" + String.format(Locale.getDefault(), "%.2f", total))


        }
        if(stripe ==null)
        {
              // stripe = Stripe(context!!, "pk_live_2j9E8hbbWTMCFGGUZN3rqT9X")
               stripe = Stripe(context!!, "pk_test_BPxGU0cRupCVOSN2MPMFEwPv")
             //  stripe = Stripe(context!!, "pk_test_51I7d9THjmb53yEyqXqvCp42gG7oFI3y2bWExWxkjIeKxOMcNn113W6rvj3pM2kPXgnl7rbxGAHM6l7cVuNuzUrrT00FQ3y51pJ")
        }

        tvMakePayment.setOnClickListener(this)
        ivBackPayment.setOnClickListener(this)

       edValidThr!!.addTextChangedListener(object :TextWatcher{
            val TOTAL_SYMBOLS:Int=19
            val TOTAL_DIGITS:Int=19
            val DIVIDER_MODULO:Int=19
            val DIVIDER_POSITION:Int=19
            val DIVIDER:String="-"

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(s!!.length>0)
                {
                    if(s.length==2)
                    {
                        edValidThr!!.setText(edValidThr!!.text.toString().trim()+"/")
                        edValidThr.setSelection(3)
                    }
                }

            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        edCardNumber!!.addTextChangedListener(object:TextWatcher{

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {


              if(s!!.length>0)
              {
                  if(s.length==4){
                      edCardNumber.setText(edCardNumber.text.toString().trim()+"-")
                      edCardNumber.setSelection(5)
                  }
                 else if(s.length==9){
                      edCardNumber.setText(edCardNumber.text.toString().trim()+"-")
                      edCardNumber.setSelection(10)
                  }
                 else if(s.length==14){
                      edCardNumber.setText(edCardNumber.text.toString().trim()+"-")
                      edCardNumber.setSelection(15)
                  }


              }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

    }

    private  fun generatePaymentToken()
    {
        println("asssdsds " + cardtoSave)

        stripe!!.createToken(cardtoSave!!, object : ApiResultCallback<Token> {
            override fun onError(e: Exception) {
                hideProgress()
                showSnackBar(Payment,e.message!!)
                Log.d("errroee", e.message)
            }

            override fun onSuccess(result: Token) {
                Log.d("sucessssssssssssss", result.id)
                paymentPrsenter!!.makePayment(result.id, total!!, orderID!!)


            }

        })
    }

    private  fun checkCardValidation():Boolean{
        cardtoSave=getCard()



        if(TextUtils.isEmpty(edName.text.toString().trim()))
        {
            Toast.makeText(context, "Please enter name", Toast.LENGTH_SHORT).show()
            return false
        }
        else if(cardtoSave==null)
        {
            Toast.makeText(context, "Please enter valid details", Toast.LENGTH_SHORT).show()
            return false
        }
        else
        {
            return true

        }


    }
    fun getCard(): Card? {
        val builder = getCardBuilder()
        return builder?.build()
    }


    public fun  getCardBuilder (): Card.Builder? {

        var cardNumber=getCardNumber()
        var valiDate=getDate()

        val cvcValue: String = Objects.requireNonNull(edCVC.getText())
            .toString().trim()

         if (cardNumber == null || valiDate == null || valiDate.size != 2) {
           return null
        }
        if (!isCvcLengthValid(cvcValue)) {
            return null
        }

         return Card.Builder(cardNumber, valiDate[0].toInt(), valiDate[1].toInt(), cvcValue)


    }
    fun getCardNumber(): String? {
        return  removeSpacesAndHyphens(edCardNumber.text.toString().trim())
    }

    fun getDate():List<String>{

        var date=edValidThr.text.toString().trim().split("/")
        return  date

    }

    fun isCvcLengthValid(string: String):Boolean{
        if(string.length==3)
        {
            return true
        }
        return false
    }


    override fun onClick(v: View?) {
        when(v)
        {
            tvMakePayment -> {
                if (!isClicked!!) {
                    if (checkCardValidation()) {
                        generatePaymentToken()
                        showProgress(context!!)
                        isClicked = true
                    }
                }

            }
            ivBackPayment -> {
                activity!!.supportFragmentManager.popBackStack()
            }
        }
    }

    override fun onSucess(responseBody: ResponseBody) {
       hideProgress()

      clearCartPresenter!!.clearCart()
        showProgress(context!!)
    }

    override fun onError(error: String) {
      hideProgress()
        showSnackBar(Payment, error)
    }

    override fun onSucessClearCart(responseBody: ResponseBody) {
      hideProgress()
        isClicked=false
        (context as HomeActivity).replaceFragmentWithOutBackStack(
            OrderPlacedFragment(),
            "order place fragment"
        )
    }

    override fun onErrorClearCart(error: String) {
        hideProgress()
    }
}