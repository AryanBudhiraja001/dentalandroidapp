package com.arumdental.home.order.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.arumdental.R
import com.arumdental.home.HomeActivity
import com.arumdental.home.order.OrderContract
import com.arumdental.home.order.adapter.OrderAdapter
import com.arumdental.home.order.model.OrdersModel
import com.arumdental.home.order.presenter.OrderPresenter
import com.arumdental.utils.ShowToolbar
import com.arumdental.utils.UserSharedPrefences
import com.arumdental.utils.hideProgress
import com.arumdental.utils.showProgress
import kotlinx.android.synthetic.main.order_layout.*

class OrdersFragment :Fragment() , OrderContract.OrderView, View.OnClickListener{

    var orderPresenter:OrderPresenter?=null
    var showToolbar:ShowToolbar?=null
    var userSharedPrefences:UserSharedPrefences?=null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.order_layout, container, false)
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
        ivBackOrder.setOnClickListener(this)
        userSharedPrefences=UserSharedPrefences.getInstance()
        orderPresenter= OrderPresenter(this)
        orderPresenter!!.getOrders(userSharedPrefences!!.id)
        showProgress(context!!)
        showToolbar!!.changeToolbar(false,"Order frag", false, true)


    }

    override fun onSucessOrder(ordersModel: ArrayList<OrdersModel>) {
        hideProgress()
        if(ordersModel.size>0)
        {
            tvNoOrders.visibility=View.GONE
            rvOrder.visibility=View.VISIBLE
            for( i in ordersModel.indices)
            {
                if(ordersModel.get(i).line_items==null)
                {
                    ordersModel.removeAt(i)

                }
            }
            var orderAdapter=OrderAdapter(context!!,ordersModel )
            rvOrder.adapter=orderAdapter
        }
        else
        {
            tvNoOrders.visibility=View.VISIBLE
            rvOrder.visibility=View.GONE
        }


    }

    override fun onErrorOrder(error: String) {
        hideProgress()
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
        println("djfhjksdg"+error)

    }

    override fun onClick(v: View?) {
        when(v)
        {
            ivBackOrder->
            {
                activity!!.supportFragmentManager.popBackStack()
            }

        }
    }
}