package com.arumdental.authentication.login.view

import android.app.Dialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.arumdental.R
import com.arumdental.authentication.forgotPassword.fpContract
import com.arumdental.authentication.forgotPassword.presenter.fpPresenter
import com.arumdental.authentication.login.LoginContract
import com.arumdental.authentication.login.model.LoginResponseModel
import com.arumdental.authentication.login.model.ValidateTokenResponse
import com.arumdental.authentication.login.presenter.LoginPresenter
import com.arumdental.authentication.signUp.view.SignUpActivity
import com.arumdental.home.HomeActivity
import com.arumdental.utils.UserSharedPrefences
import com.arumdental.utils.hideProgress
import com.arumdental.utils.showProgress
import com.arumdental.utils.showSnackBar
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.change_password_layout.*

class LoginActivity : AppCompatActivity(), View.OnClickListener, LoginContract.LoginView,fpContract.fpView {

    var loginPresenter: LoginPresenter?=null
    var fpPresenter:fpPresenter?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        init()
    }

    private  fun init()
    {
        loginPresenter= LoginPresenter(this)
        fpPresenter= fpPresenter(this)
        tvSignUpLogin.setOnClickListener(this)
        tvLoginLogin.setOnClickListener(this)
        tvForgotPasswordLogin.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
      when(v)
      {
          tvSignUpLogin->
          {
              val intent= Intent(this, SignUpActivity::class.java)
              startActivity(intent)
              finish()
          }
          tvLoginLogin->
          {
              if(loginPresenter!!.validateLoginData(etEmailAddressLogin.text.toString().trim(),
                  etPasswordLogin.text.toString().trim()))
              {
                  loginPresenter!!.login(etEmailAddressLogin.text.toString().trim(), etPasswordLogin.text.toString().trim())
                  showProgress(this)
              }
              else
              {
                  showSnackBar(Loginview, loginPresenter!!.errorMessage.toString())

              }

          }
          tvForgotPasswordLogin->
          {
              forgotPassword(this)
          }
      }
    }

    private  fun forgotPassword(  context: Context)
    {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setContentView(R.layout.change_password_layout)
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
        val cancel: ImageView = dialog.findViewById(R.id.iv_cancelFpDialog)
        val submit: TextView = dialog.findViewById(R.id.tv_submitFp)
        val etEmail : EditText =dialog.findViewById(R.id.etEmailFp)

        cancel.setOnClickListener(View.OnClickListener { v ->

            dialog.dismiss()
        })

        submit.setOnClickListener(View.OnClickListener { v ->


            if(!TextUtils.isEmpty(etEmail.text.toString().trim()))
            {
               fpPresenter!!.fp(etEmail.text.toString().trim())
                dialog.dismiss()
                showProgress(this)

            }
            else
            {
                Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show()
            }
        })

        dialog.show()



    }

    override fun onSucesslogin(loginResponseModel: LoginResponseModel) {
        hideProgress()
        UserSharedPrefences.getInstance().name= loginResponseModel.data!!.displayName
        UserSharedPrefences.getInstance().email=loginResponseModel.data!!.email
        UserSharedPrefences.getInstance().token=loginResponseModel.data!!.token
       UserSharedPrefences.getInstance().id= loginResponseModel.data!!.id!!

        loginPresenter!!.ontokenValidate()
        showProgress(this)
    }

    override fun onfailure(error: String) {
        hideProgress()
        showSnackBar(Loginview, error)



    }

    override fun onSucessValidateToken(validateTokenResponse: ValidateTokenResponse) {
        hideProgress()
        showSnackBar(Loginview, "Login Sucessfully")
        UserSharedPrefences.getInstance().islogin="yes"
        val intent=Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()




    }

    override fun onfailureValidateToken(error: String) {
        hideProgress()
        showSnackBar(Loginview, error)

    }

    override fun onSucess(string: String) {
        hideProgress()
        showSnackBar(Loginview,string)
    }

    override fun onError(error: String) {
        hideProgress()
        showSnackBar(Loginview, error)
    }


}