package com.arumdental.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.arumdental.R
import kotlinx.android.synthetic.main.order_placed_layout.*

class OrderPlacedFragment:Fragment(), View.OnClickListener {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.order_placed_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }
    private  fun init()
    {
        tvGoToHome.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v)
        {
            tvGoToHome->
            {
                val fm: FragmentManager = activity!!.supportFragmentManager
                for (i in 0 until fm.getBackStackEntryCount()) {
                    fm.popBackStack()
                }
             /*  val intent= Intent(context!!, HomeActivity::class.java)
                startActivity(intent)*/
            }
        }
    }
}