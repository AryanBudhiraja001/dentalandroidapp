package com.arumdental.home.order.adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.arumdental.R
import com.arumdental.home.HomeActivity
import com.arumdental.home.order.model.OrdersModel
import com.arumdental.home.payment.view.PaymentFragment
import com.arumdental.utils.DateHelper
import com.arumdental.utils.getDateFromCreatedAT
import kotlinx.android.synthetic.main.item_order.view.*
import kotlinx.android.synthetic.main.item_ordermain.view.*

class OrderAdapter (var context: Context, var list:ArrayList<OrdersModel>):RecyclerView.Adapter<OrderAdapter.Viewholder>(){

    inner  class  Viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {
        return Viewholder(
            LayoutInflater.from(context).inflate(
                R.layout.item_ordermain,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
      return list.size
    }

    override fun onBindViewHolder(holder: Viewholder, position: Int) {

            holder.itemView.tvOrerNo.setText(""+list.get(position).id!!)


       /* var doubleShippingPrice:Double=list.get(position).shipping_lines!!.get(0).total!!.toDouble()
        var result1=String.format("%.2f", doubleShippingPrice);

        var doubleShippingPrice:Double=list.get(position).shipping_lines!!.get(0).total!!.toDouble()
        var result1=String.format("%.2f", doubleShippingPrice);

        val total=list.get(position).total!!.toInt()+result1.toDouble()*/


            holder.itemView.tvOrderPrice.setText("€"+list.get(position).total)

            var orderInnerAdapter=OrderInnerAdapter(context, list.get(position).line_items!!)
            holder.itemView.rvOrderInner.adapter=orderInnerAdapter

        holder.itemView.tvStatus.setText(list.get(position).status)
        holder.itemView.tvDateOrder.setText(getDateFromCreatedAT(list.get(position).date_created_gmt!!))
        holder.itemView.tvShippingPrice.setText("€"+list.get(position).shipping_total)
        if(list.get(position).status.equals("pending"))
        {
            holder.itemView.tvStatus.setTextColor(ContextCompat.getColor(context!!, R.color.colorRed))
        }
        else
        {
            holder.itemView.tvStatus.setTextColor(ContextCompat.getColor(context!!, R.color.colorGreen))
        }

        holder.itemView.setOnClickListener(View.OnClickListener { v ->

            if(list.get(position).status.equals("pending"))
            {
                var args= Bundle()
                args.putSerializable("orderId",list.get(position).id)
                args.putSerializable("total",list.get(position).total!!.toDouble())
                var fragment= PaymentFragment()
                fragment.arguments=args
                (context as HomeActivity).replaceFragmentWithOutBackStack(fragment!!,"payment frag")
            }
        })


    }
}