package com.arumdental.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.arumdental.R
import com.arumdental.authentication.login.view.LoginActivity
import com.arumdental.authentication.signUp.view.SignUpActivity
import com.arumdental.home.HomeActivity
import com.arumdental.utils.UserSharedPrefences
import kotlinx.android.synthetic.main.activity_base.*

class BaseActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
        init()
    }

    private  fun init()
    {
        btLogin.setOnClickListener(this)
        btSignUp.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v)
        {
            btLogin->{
                val intent= Intent(this,
                    LoginActivity::class.java)
                startActivity(intent)
                finish()

            }
            btSignUp->
            {
                val intent= Intent(this,
                    SignUpActivity::class.java)
                startActivity(intent)
                finish()

            }
        }
    }
}