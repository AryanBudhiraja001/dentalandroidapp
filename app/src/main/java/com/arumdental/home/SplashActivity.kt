package com.arumdental.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.arumdental.R
import com.arumdental.authentication.BaseActivity
import com.arumdental.utils.UserSharedPrefences

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Handler().postDelayed({

            if(UserSharedPrefences.getInstance().islogin.equals("yes"))
            {
                val intent=Intent(this,HomeActivity::class.java)
                startActivity(intent)
                finish()
            }

            else
            {
                val intent = Intent(this, BaseActivity::class.java)
                startActivity(intent)
                finish()
            }

        }, 3000)

    }
}