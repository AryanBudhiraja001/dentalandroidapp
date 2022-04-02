package com.arumdental.home.privacyPolicy

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.arumdental.R
import com.arumdental.home.HomeActivity
import com.arumdental.utils.ShowToolbar
import com.arumdental.utils.hideProgress
import com.arumdental.utils.showProgress
import kotlinx.android.synthetic.main.privacy_policy.*

class PrivacyPolicyFragment:Fragment(), View.OnClickListener {
    var showToolbar:ShowToolbar?=null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.privacy_policy, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        showToolbar=context as HomeActivity
    }

    private  fun init()
    {
        ivBackPrivacy.setOnClickListener(this)
        showToolbar!!.changeToolbar(false, "Privacy Policy",false, true)
        showProgress(context!!)
        webView.loadUrl("https://www.arumdentalshop.com/privacy-policy-cookie-restriction-mode/")
        webView.settings.javaScriptEnabled = true
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(
                view: WebView,
                url: String
            ) { // do your stuff here
                //utils.dismissdialog();
                hideProgress()
                return
            }
        }
    }

    override fun onClick(v: View?) {
        when(v)
        {
            ivBackPrivacy->
            {
                activity!!.supportFragmentManager.popBackStack()
            }
        }
    }
}