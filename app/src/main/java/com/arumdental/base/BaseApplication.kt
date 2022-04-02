package com.arumdental.base

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import androidx.multidex.MultiDex
import com.stripe.android.Stripe

class BaseApplication:Application() {

    companion object{
        var  instance: BaseApplication? = null
        var stripe:Stripe?=null
    }


    fun getInstance(): BaseApplication? {
        if (instance == null) {
            instance = this
        }
        return instance
    }

    override fun onCreate() {
        super.onCreate()
        if (instance == null) {
            instance = this
        }
    }

    fun BaseApplication() {}

    fun hasNetwork(): Boolean {
        return instance!!.checkIfHasNetwork()
    }

    private fun checkIfHasNetwork(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    fun getContext(): Context? {
        return BaseApplication.instance
    }

      fun getStripe(context: Context):Stripe{
        if(stripe==null)
        {
            //stripe= Stripe(context, "pk_test_DyE8LNvYX1ZHzr0TNkk1srRy")
            //stripe= Stripe(context, "sk_test_pnFWgJankoiMeM9XhnbF2HiE")
           stripe= Stripe(context, "pk_live_2j9E8hbbWTMCFGGUZN3rqT9X")
        }
        return stripe as Stripe
    }

}