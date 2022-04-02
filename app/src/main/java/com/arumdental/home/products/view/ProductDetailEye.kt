package com.arumdental.home.products.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.arumdental.R
import com.arumdental.home.HomeActivity
import com.arumdental.home.cart.CartContract
import com.arumdental.home.cart.model.AddToCartResponse
import com.arumdental.home.cart.model.UpdateCartResponse
import com.arumdental.home.cart.presenter.CartPresenter
import com.arumdental.home.products.adapter.ProductImageSliderAdpater
import com.arumdental.home.products.model.ProductResponseModel
import com.arumdental.home.wishlist.addProduct.AddProductContract
import com.arumdental.home.wishlist.addProduct.model.AddProductResponse
import com.arumdental.home.wishlist.addProduct.presenter.AddProductPresenter
import com.arumdental.home.wishlist.removeProduct.RemoveProductContract
import com.arumdental.home.wishlist.removeProduct.presenter.RemoveProductPresenter
import com.arumdental.utils.*
import com.google.gson.Gson
import kotlinx.android.synthetic.main.product_detail_eye.*
import okhttp3.ResponseBody
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class ProductDetailEye :Fragment(), View.OnClickListener,
    CartContract.CartView,
    AddProductContract.AddProductView,
RemoveProductContract.RemoveProductView{

    var showToolbar:ShowToolbar?=null
    var productResponseModel:ProductResponseModel?=null
    var cartPresenter:CartPresenter?=null
    var addProductPresenter:AddProductPresenter?=null
    var removeProductPresenter:RemoveProductPresenter?=null
    private var countDownTimer: CountDownTimer? = null
    var tvhour:TextView?=null
    var tvDays:TextView?=null
    var tvMins:TextView?=null
    var tvSeconds:TextView?=null
    var imageUrl:String?=null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.product_detail_eye, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvhour=view.findViewById(R.id.tvHour)
        tvDays=view.findViewById(R.id.tvDay)
        tvMins=view.findViewById(R.id.tvMins)
        tvSeconds=view.findViewById(R.id.tvSeconds)
        init()

    }

    private  fun init()
    {

        addProductPresenter=AddProductPresenter(this)
        removeProductPresenter=RemoveProductPresenter(this)
        ivbackProductDetail.setOnClickListener(this)
        ivAdd.setOnClickListener(this)
        ivRemove.setOnClickListener(this)
        ivShare.setOnClickListener(this)
        tvAddToCartEye.setOnClickListener(this)
        ivHeartDescription.setOnClickListener(this)
        showToolbar!!.changeToolbar(false, "Product Detail", false, true)
        if(arguments!=null)
        {

            productResponseModel= arguments!!.getSerializable("product Detail") as ProductResponseModel?

            if(productResponseModel!!.images!=null && productResponseModel!!.images!!.size>0)
            {
                imageUrl= productResponseModel!!.images!!.get(0).src
            }

            println("product id "+Gson().toJson(productResponseModel))

            var productViewPagerAdapter= ProductImageSliderAdpater(
                context!!,
                productResponseModel!!.images!!
            )
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
            tvProductPrice.setText("€" + productResponseModel?.price)
            tvDescription.setText(
                android.text.Html.fromHtml(productResponseModel!!.description).toString()
            )
            if(productResponseModel!!.isFav!!)
            {
                ivHeartDescription.setImageDrawable(
                    ContextCompat.getDrawable(
                        context!!,
                        R.drawable.heart_fill
                    )
                )

            }
            else{
                ivHeartDescription.setImageDrawable(
                    ContextCompat.getDrawable(
                        context!!,
                        R.drawable.heart_grey
                    )
                )
            }

            if(productResponseModel!!.sku!=null)
            {
                tvSKUCOde.setText("PRODUCT CODE : "+productResponseModel!!.sku)
            }

            if(productResponseModel!!.stock_status!=null)
            {
                tvAvailiability.setText("In Stock")
            }
            else{
                tvAvailiability.setText("Out of Stock")
                tvAvailiability.setTextColor(ContextCompat.getColor(context!!, R.color.colorRed))
            }


        }

        cartPresenter= CartPresenter(this)
      //  calculateTime()
        startNewTimer()


    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        showToolbar=context as HomeActivity

    }


    override fun onClick(v: View?) {
        when(v)
        {
            ivbackProductDetail -> {
                activity!!.supportFragmentManager.popBackStack()
                countDownTimer!!.onFinish()
            }

            ivAdd -> {
                var quant = tvQunatity.text.toString().trim()
                var newqunat = quant.toInt() + 1
                tvQunatity.setText("" + newqunat)

            }
            ivRemove -> {
                var quant = tvQunatity.text.toString().trim()
                if (quant.toInt() > 0) {
                    var newqunat = quant.toInt() - 1
                    tvQunatity.setText("" + newqunat)
                }
            }

            tvAddToCartEye -> {
                if (tvQunatity.text.toString().trim().toInt() > 0) {
                    cartPresenter!!.addToCart(
                        tvQunatity.text.toString().trim().toInt(),
                        productResponseModel!!.id!!
                    )

                    showProgress(context!!)
                } else {
                    showSnackBar(ProductDetailView, "Please increase the qunatity")
                }
            }

            ivHeartDescription -> {
                if (productResponseModel!!.isFav!!) {
                    removeProductPresenter!!.removeProduct(productResponseModel!!.id!!)
                    showProgress(context!!)


                } else {
                    addProductPresenter!!.addProduct(
                        UserSharedPrefences.getInstance().id,
                        productResponseModel!!.id!!, productResponseModel!!.price!!
                    )
                    showProgress(context!!)
                    init()
                }

            }
            ivShare -> {
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.type = "text/plain"
                shareIntent.putExtra(Intent.EXTRA_TEXT, productResponseModel!!.name)
                startActivity(Intent.createChooser(shareIntent, "Share link using"))
            }

        }
    }

    override fun onSucessAddTOCart(addToCartResponse: AddToCartResponse) {
       if(isAdded)
       {
           hideProgress()
           activity!!.supportFragmentManager.popBackStack()
       }
    }

    override fun onErrorAddTOCart(error: String) {
        if(isAdded)
        {
            hideProgress()
            showSnackBar(ProductDetailView, error)
        }
    }

    override fun onSucessUpdateCart(updateCartResponse: UpdateCartResponse) {
    }

    override fun onErrorUpdateCart(error: String) {
    }

    override fun onSucessCart(responseBody: ResponseBody) {

    }

    override fun onError(error: String) {

    }

    override fun onSucessAddProduct(addProductResponse: AddProductResponse) {
        hideProgress()
        showSnackBar(ProductDetailView, addProductResponse.message.toString())
        ivHeartDescription.setImageDrawable(
            ContextCompat.getDrawable(
                context!!,
                R.drawable.heart_fill
            )
        )

    }

    override fun onErrorAddProduct(error: String) {
        hideProgress()
        showSnackBar(ProductDetailView, error)

    }

    override fun onSucessRemoveProduct(addProductResponse: AddProductResponse) {
        hideProgress()
        showSnackBar(ProductDetailView, addProductResponse.message.toString())
        ivHeartDescription.setImageDrawable(
            ContextCompat.getDrawable(
                context!!,
                R.drawable.heart_grey
            )
        )

    }

    override fun onErrorRemoveProduct(error: String) {
        hideProgress()
        showSnackBar(ProductDetailView, error)

    }

    private  fun calculateTime()
    {

        val calender=Calendar.getInstance()
        val calender2=Calendar.getInstance()
        val date=calender.time
        val simpleDateFormat=SimpleDateFormat("EEEE", Locale.ENGLISH)
        var currentDay=simpleDateFormat.format(date.time)

        val simpleDateFormat2=SimpleDateFormat("hh:mm a", Locale.ENGLISH)
        var currentTime=simpleDateFormat2.format(date.time)

        println("current time " + currentTime)
        var currentTimeMilli=getMilliFromDate(currentTime)
        var eveningtime=getMilliFromDate("03:00 PM")




        if(currentDay.equals("Friday"))
        {
            if(currentTimeMilli>=eveningtime)
            {
                calender.add(Calendar.DAY_OF_WEEK, 4)
                tvDay.visibility=View.VISIBLE
                txtDays.visibility=View.VISIBLE
            }
            else{
                calender.add(Calendar.DAY_OF_WEEK, 3)
                tvDay.visibility=View.VISIBLE
                txtDays.visibility=View.VISIBLE
            }

        }
        else if(currentDay.equals("Saturday"))
        {
            calender.add(Calendar.DAY_OF_WEEK, 2)
            tvDay.visibility=View.VISIBLE
            txtDays.visibility=View.VISIBLE
        }
        else if(currentDay.equals("Sunday"))
        {
            calender.add(Calendar.DAY_OF_WEEK, 2)
            tvDay.visibility=View.VISIBLE
            txtDays.visibility=View.VISIBLE
        }
        else{
            calender.add(Calendar.DAY_OF_WEEK, 1)
            tvDay.visibility=View.VISIBLE

            txtDays.visibility=View.VISIBLE
        }

        val formatter = SimpleDateFormat("dd.MM.yyyy, HH:mm a", Locale.ENGLISH)

        val nextDay :String=formatter.format(calender2.time)
        val onlyday=nextDay.split(",")
        val nextDate=onlyday[0]+", 03:00 PM"


        val formatter2 = SimpleDateFormat("dd.MM.yyyy, hh:mm a", Locale.ENGLISH)
        val nextTime = formatter2.parse(nextDate)
        println("old datr " + nextTime + "  " + nextTime!!.time+"  "+nextDate+"  "+System.currentTimeMillis())


        val nextTimeInMillis=nextTime.time
        val currentTimeMillis=System.currentTimeMillis()

        val diff=nextTimeInMillis-currentTimeMillis
        countDownTimer = object : CountDownTimer(diff, 1000) {
            override fun onFinish() {
            }

            override fun onTick(millisUntilFinished: Long) {
                val seconds = (millisUntilFinished / 1000) % 60
                val minutes = (millisUntilFinished / (1000 * 60)) % 60
                val hours =
                    (millisUntilFinished / (1000 * 60 * 60)) % 24
                val Days = (millisUntilFinished / (24 * 60 * 60 * 1000))
                println("days " + Days)
                val text =
                    "$hours : $minutes : $seconds"
                tvhour!!.setText(hours.toString())
                tvMins!!.setText(minutes.toString())
                tvSeconds!!.setText(seconds.toString())
                tvDays!!.setText(Days.toString())
            }

        }.start()

        var df = SimpleDateFormat("d", Locale.ENGLISH)

        var day:String?=null
        day=df.format(calender.time)


        if(day.endsWith("1") && !day!!.endsWith("11")) {

            df = SimpleDateFormat("EEEE d'st' MMM", Locale.ENGLISH);
        }
        else if(day.endsWith("2") && !day.endsWith("12")) {

            df = SimpleDateFormat("EEEE d'nd' MMM", Locale.ENGLISH);
        }
        else if(day.endsWith("3") && !day.endsWith("13")) {

            df = SimpleDateFormat("EEEE d'rd' MMM", Locale.ENGLISH);
        }
        else {

            df = SimpleDateFormat("EEEE d'th' MMM", Locale.ENGLISH);
        }


        println("my day " + df.format(calender.time))
        tvDate.setText(df.format(calender.time))


    }

    private  fun startNewTimer(){

        var isFixed:Boolean?=true

        val calenderDay=Calendar.getInstance()
        val calenderTimer=Calendar.getInstance()
        val date=calenderDay.time
        val simpleDateFormatDay=SimpleDateFormat("EEEE", Locale.ENGLISH)
        val formatter = SimpleDateFormat("dd.MM.yyyy, HH:mm", Locale.ENGLISH)
        var currentDay=simpleDateFormatDay.format(date.time)
        var currentDate=formatter.format((calenderDay.time))
        var currentTimeInMillis=System.currentTimeMillis()
        println("cuwnfgsasgf date "+currentDate)
        var onlydate=currentDate.split(",")
        var timeToBeCompared=onlydate[0]+", 15:00"




        var timeToComapredInMillis=getMilliFromDate(timeToBeCompared)



        println("cuwnfgsasgf date "+currentDate+"   "+timeToBeCompared+"  "+timeToComapredInMillis +"  "+currentDay)


        if(currentDay.equals("Friday"))
        {
            if(currentTimeInMillis<=timeToComapredInMillis) {

                calenderDay.add(Calendar.DAY_OF_WEEK, 3)
                tvDay.visibility=View.VISIBLE
                txtDays.visibility=View.VISIBLE
            }
            else{
                isFixed=false

                calenderDay.add(Calendar.DAY_OF_WEEK, 4)
                calenderTimer.add(Calendar.DAY_OF_WEEK,3)//monday

                tvDay.visibility=View.VISIBLE
                txtDays.visibility=View.VISIBLE
            }

        }
        else if(currentDay.equals("Saturday"))

        {
            isFixed=false
            calenderDay.add(Calendar.DAY_OF_WEEK, 3)
            calenderTimer.add(Calendar.DAY_OF_WEEK,2)//monday
            tvDay.visibility=View.VISIBLE
            txtDays.visibility=View.VISIBLE
        }
        else if(currentDay.equals("Sunday"))
        {
            isFixed=false

                calenderDay.add(Calendar.DAY_OF_WEEK, 2)
            calenderTimer.add(Calendar.DAY_OF_WEEK,1)//monday
                tvDay.visibility=View.VISIBLE
                txtDays.visibility=View.VISIBLE


        }
        else{

            if(currentDay.equals("Thursday"))
            {
                if(currentTimeInMillis<=timeToComapredInMillis)
                {
                    calenderDay.add(Calendar.DAY_OF_WEEK, 1)
                    tvDay.visibility=View.VISIBLE
                    txtDays.visibility=View.VISIBLE
                }
                else
                {
                    calenderDay.add(Calendar.DAY_OF_WEEK, 4)
                    calenderTimer.add(Calendar.DAY_OF_WEEK, 1)//friday
                    tvDay.visibility=View.VISIBLE
                    txtDays.visibility=View.VISIBLE
                }
            }
            else
            {
                if(currentTimeInMillis<=timeToComapredInMillis)
                {
                    calenderDay.add(Calendar.DAY_OF_WEEK, 1)
                    calenderTimer
                    tvDay.visibility=View.VISIBLE
                    txtDays.visibility=View.VISIBLE
                }
                else
                {
                    calenderDay.add(Calendar.DAY_OF_WEEK, 2)
                    calenderTimer.add(Calendar.DAY_OF_WEEK,1)//next to next day
                    tvDay.visibility=View.VISIBLE
                    txtDays.visibility=View.VISIBLE
                }
            }

        }

        var endTimeInMillis:Long?=null

        if(isFixed!!)
        {
            println("date of delevery "+formatter.format(calenderTimer.time))
            var timerDate=formatter.format(calenderTimer.time)
            var onlydayTimer=timerDate.split(",")
            var endtime=onlydayTimer[0]+", 15:00"
            endTimeInMillis =getMilliFromDate(endtime)
            println("end time exact "+endtime)
        }
        else
        {
            println("date of delevery "+formatter.format(calenderTimer.time))
            var timerDate=formatter.format(calenderTimer.time)
            var onlydayTimer=timerDate.split(",")
            var endtime=timerDate
            endTimeInMillis =getMilliFromDate(endtime)
            println("end time exact "+endtime)
        }

        println("end time "+endTimeInMillis)




        var df = SimpleDateFormat("d", Locale.ENGLISH)

        var day:String?=null
        day=df.format(calenderDay.time)


        if(day.endsWith("1") && !day!!.endsWith("11")) {

            df = SimpleDateFormat("EEEE d'st' MMM", Locale.ENGLISH);
        }
        else if(day.endsWith("2") && !day.endsWith("12")) {

            df = SimpleDateFormat("EEEE d'nd' MMM", Locale.ENGLISH);
        }
        else if(day.endsWith("3") && !day.endsWith("13")) {

            df = SimpleDateFormat("EEEE d'rd' MMM", Locale.ENGLISH);
        }
        else {

            df = SimpleDateFormat("EEEE d'th' MMM", Locale.ENGLISH);
        }


        println("my day " + df.format(calenderDay.time))
        tvDate.setText(df.format(calenderDay.time))


        val diff=endTimeInMillis-currentTimeInMillis
        countDownTimer = object : CountDownTimer(diff, 1000) {
            override fun onFinish() {
            }

            override fun onTick(millisUntilFinished: Long) {
                val seconds = (millisUntilFinished / 1000) % 60
                val minutes = (millisUntilFinished / (1000 * 60)) % 60
                val hours =
                    (millisUntilFinished / (1000 * 60 * 60)) % 24
                val Days = (millisUntilFinished / (24 * 60 * 60 * 1000))
                println("days " + Days)
                val text =
                    "$hours : $minutes : $seconds"
                tvhour!!.setText(hours.toString())
                tvMins!!.setText(minutes.toString())
                tvSeconds!!.setText(seconds.toString())
                tvDays!!.setText(Days.toString())
            }

        }.start()








    }



    fun getMilliFromDate(dateFormat: String?): Long {
        var date = Date()
        val formatter = SimpleDateFormat("dd.MM.yyyy, HH:mm")
        try {
            date = formatter.parse(dateFormat)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        println("Today is $date")
        return date.time
    }
}