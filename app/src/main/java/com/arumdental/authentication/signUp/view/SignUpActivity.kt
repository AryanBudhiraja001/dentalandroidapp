package com.arumdental.authentication.signUp.view

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.arumdental.R
import com.arumdental.authentication.BaseActivity
import com.arumdental.authentication.login.LoginContract
import com.arumdental.authentication.login.model.LoginResponseModel
import com.arumdental.authentication.login.model.ValidateTokenResponse
import com.arumdental.authentication.login.presenter.LoginPresenter
import com.arumdental.authentication.login.view.LoginActivity
import com.arumdental.authentication.signUp.SignUpContract
import com.arumdental.authentication.signUp.model.paramModel.SignUpMetaData
import com.arumdental.authentication.signUp.model.paramModel.SignUpParamModel
import com.arumdental.authentication.signUp.model.response.SignUpResponse
import com.arumdental.authentication.signUp.presenter.SignUpPresenter
import com.arumdental.home.HomeActivity
import com.arumdental.utils.UserSharedPrefences
import com.arumdental.utils.hideProgress
import com.arumdental.utils.showProgress
import com.arumdental.utils.showSnackBar
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity(), View.OnClickListener , SignUpContract.SignUpView, LoginContract.LoginView{

    var signUpPresenter:SignUpPresenter?= null
    var loginPresenter: LoginPresenter?=null
    var signUpParamModel=SignUpParamModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        init()
    }

    private  fun init()
    {
        signUpPresenter= SignUpPresenter(this)
        loginPresenter= LoginPresenter(this)

        tvLogin.setOnClickListener(this)
        tvSignUp.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v)
        {
            tvLogin->
            {
                val intent= Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
            tvSignUp->
            {

               if(signUpPresenter!!.validateCreateAccountData(
                       etFirstName.text.toString().trim(),
                   etLastName.text.toString().trim(),
                       etBusinessName.text.toString().trim(),
                       etEmailAddress.text.toString().trim(),
                       etPhoneNumber.text.toString().trim(),

                       etPassword.text.toString().trim(),
                       etConfirmPassword.text.toString().trim()

                   ))
               {
                   var signUpMetaDatalist:ArrayList<SignUpMetaData>?= ArrayList()
                   signUpMetaDatalist!!.add(SignUpMetaData("phone",etPhoneNumber.text.toString().trim()))
                   signUpMetaDatalist!!.add(SignUpMetaData("vat",etTax.text.toString().trim()))
                   signUpParamModel.email=etEmailAddress.text.toString().trim()
                   signUpParamModel.first_name=etFirstName.text.toString().trim()
                   signUpParamModel.last_name=etLastName.text.toString().trim()
                   signUpParamModel.password=etPassword.text.toString().trim()
                   signUpParamModel.username=etEmailAddress.text.toString().trim()
                   signUpParamModel.meta_data=signUpMetaDatalist
               /*    loginPresenter!!.login("arunDental","lVFl#YpKfJ!P!35^Q*")
                   showProgress(this)*/
                    signUpPresenter!!.signUp(signUpParamModel)
                   showProgress(this)
               }
                else
               {
                   showSnackBar(signUpview, signUpPresenter!!.errorMessage.toString())
               }
            }
        }
    }

    override fun onSucessSignUp(signUpResponse: SignUpResponse) {
        hideProgress()
        showAlertDialog(signUpResponse)
       // showSnackBar(signUpview,"sucesss")

    }

    override fun onErrorSignUp(error: String) {
        hideProgress()
        Log.d("errorrrrr",error)
        showSnackBar(signUpview,error)

    }

    override fun onSucesslogin(loginResponseModel: LoginResponseModel) {

        UserSharedPrefences.getInstance().token=loginResponseModel.data!!.token
        signUpPresenter!!.signUp(signUpParamModel)
    }

    override fun onfailure(error: String) {

    }

    override fun onSucessValidateToken(validateTokenResponse: ValidateTokenResponse) {

    }

    override fun onfailureValidateToken(error: String) {

    }

    private  fun showAlertDialog(signUpResponse: SignUpResponse)
    {
        val builder =
            AlertDialog.Builder(this)
        builder.setMessage("Your account has been created successfully, please login now")
            .setCancelable(false)
            .setPositiveButton("Okay", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    UserSharedPrefences.getInstance().name= signUpResponse.first_name+signUpResponse.last_name
                    UserSharedPrefences.getInstance().email=signUpResponse.email
                  //  UserSharedPrefences.getInstance().islogin="yes"
                    val intent=Intent(this@SignUpActivity, LoginActivity::class.java)
                    startActivity(intent)
                    finish()



                }



            })



        val alert = builder.create()
        alert.show()
    }
}