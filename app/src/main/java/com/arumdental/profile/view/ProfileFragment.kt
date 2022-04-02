package com.arumdental.profile.view

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.arumdental.R
import com.arumdental.home.ContactFormActivity
import com.arumdental.home.HomeActivity
import com.arumdental.home.address.AddressFragment
import com.arumdental.home.address.AllAddressFragment
import com.arumdental.home.changePassword.ChangePasswordPresenter
import com.arumdental.home.changePassword.ChangePasswordResponse
import com.arumdental.home.changePassword.changePasswordView
import com.arumdental.home.faq.FAQFragment
import com.arumdental.home.order.view.OrdersFragment
import com.arumdental.home.privacyPolicy.PrivacyPolicyFragment
import com.arumdental.home.terms.ContactFragment
import com.arumdental.home.terms.InstructionForUse
import com.arumdental.home.terms.TermsAndConditions
import com.arumdental.profile.ProfileContract
import com.arumdental.utils.*
import com.arumdental.web.RetrofitClient
import com.bumptech.glide.util.Util
import kotlinx.android.synthetic.main.profile_layout.*
import okhttp3.ResponseBody
import retrofit2.Response
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class ProfileFragment :Fragment(), View.OnClickListener , changePasswordView{

    var showToolbar:ShowToolbar?=null
    var userSharedPrefences:UserSharedPrefences?=null
    var presenter:ChangePasswordPresenter?=null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.profile_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        showToolbar=context as HomeActivity
    }
    private  fun init(){
        ivBackProfile.setOnClickListener(this)
        llMyOrder.setOnClickListener(this)
        llManageAddress.setOnClickListener(this)
        llPrivacyPolicy.setOnClickListener(this)
        llFaq.setOnClickListener(this)
        llHelp.setOnClickListener(this)
        llInstructionToUse.setOnClickListener(this)
        llContact.setOnClickListener(this)
        llMyPassword.setOnClickListener(this)
        userSharedPrefences=UserSharedPrefences.getInstance()
        presenter= ChangePasswordPresenter(this, retrofit = RetrofitClient())
       showToolbar!!.changeToolbar(false, "profile",false, true)
        if(userSharedPrefences?.name!=null)
        {
            tvUserName.setText(userSharedPrefences?.name)
        }
        if(userSharedPrefences?.email!=null)
        {
            tvUserEmail.setText(userSharedPrefences?.email)
        }

    }

    override fun onClick(v: View?) {
        when(v){
            ivBackProfile->
            {
                activity!!.supportFragmentManager.popBackStack()
            }
            llMyOrder->
            {
                (context as HomeActivity).replaceFragmentWithOutBackStack(OrdersFragment(), "Order frag")
            }

            llManageAddress->
            {

                (context as HomeActivity).replaceFragment(AllAddressFragment(),"address frag")
            }
            llPrivacyPolicy->{
                (context as HomeActivity).replaceFragmentWithOutBackStack(PrivacyPolicyFragment(), "Privacy frag")
            }
            llFaq->
            {
                (context as HomeActivity).replaceFragmentWithOutBackStack(FAQFragment(), "faq frag")
            }
            llHelp->
            {
                (context as HomeActivity).replaceFragmentWithOutBackStack(TermsAndConditions(), "Terms frag")
            }
            llInstructionToUse->
            {
                (context as HomeActivity).replaceFragmentWithOutBackStack(InstructionForUse(), "Instruction frag")
            }
            llContact->
            {
                (context as HomeActivity).replaceFragmentWithOutBackStack(ContactFormActivity(), "Contact frag")
            }
            llMyPassword->
            {
                 showChangePasswordDialog()
            }
        }
    }


    private  fun showChangePasswordDialog()
    {
        val dialog = Dialog(context!!);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
        dialog.setContentView(R.layout.dialpg_change_password);


        val ivClose = dialog.findViewById<ImageView>(R.id.ivClose)
        val etCurrentPassword = dialog.findViewById<EditText>(R.id.etCurrentPassword)
        val etNewPassword = dialog.findViewById<EditText>(R.id.etNewPassword)
        val etConfirmPassword = dialog.findViewById<EditText>(R.id.etConfirmPassword)
        val tvDoneForgotPassword=dialog.findViewById<TextView>(R.id.tvDoneForgotPassword)


        ivClose.setOnClickListener {
            dialog.dismiss()
        }

        tvDoneForgotPassword.setOnClickListener(View.OnClickListener { v ->

            if (!TextUtils.isEmpty(etCurrentPassword.text.toString().trim())) {

                if (!TextUtils.isEmpty(etNewPassword.text.toString().trim())) {
                    if (etNewPassword.text!!.length > 5) {

                        if (etNewPassword.text.toString().trim()
                                        .equals(etConfirmPassword.text.toString().trim())
                        ) {
                            presenter?.ChangePassword(
                                   userSharedPrefences!!.id,
                                    etCurrentPassword.text.toString().trim(),
                                    etNewPassword.text.toString().trim()
                            )
                          showProgress(context!!)
                            dialog.dismiss()
                        } else {
                            Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
                        }

                    } else {
                        Toast.makeText(
                                context!!,
                                "Password should contain at least 5 characters",
                                Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(context!!, "Enter new password", Toast.LENGTH_SHORT).show()
                }


            } else {
                Toast.makeText(context!!, "Enter current password", Toast.LENGTH_SHORT).show()
            }
        })
        dialog.show()
    }

    override fun onSucessChangePassword(response: Response<ChangePasswordResponse>) {
        if (isAdded) {
           hideProgress()
            if (response.body() != null && response.isSuccessful) {
                if (response.body()?.code == 200) {
                    Toast.makeText(activity, response.body()!!.msg, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(activity, response.body()!!.msg, Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(
                        activity,
                        "something went wrong !! please try again",
                        Toast.LENGTH_SHORT
                )
                        .show()
            }
        }
    }

    override fun onErrorChangePassword(t:Throwable) {
        if (isAdded) {
           hideProgress()
            if (t is UnknownHostException) {
                Toast.makeText(activity, "No Internet Connection", Toast.LENGTH_SHORT).show()
            } else if (t is SocketTimeoutException) {
                Toast.makeText(
                        activity,
                        "Server is not responding. Please try again",
                        Toast.LENGTH_SHORT
                )
                        .show()
            } else if (t is ConnectException) {
                Toast.makeText(activity, "Failed to connect server", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(
                        activity,
                        "something went wrong !! please try again",
                        Toast.LENGTH_SHORT
                )
                        .show()
            }
        }

    }
}