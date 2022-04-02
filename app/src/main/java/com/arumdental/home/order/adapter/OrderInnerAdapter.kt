package com.arumdental.home.order.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arumdental.R
import com.arumdental.home.order.model.OrderInnerModel
import com.arumdental.home.order.model.OrdersModel
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_order.view.*
import kotlinx.android.synthetic.main.item_order_inner.view.*
import kotlinx.android.synthetic.main.item_ordermain.view.*
import kotlinx.android.synthetic.main.item_product.view.*
import kotlinx.android.synthetic.main.item_product.view.llLowerlayout
import kotlinx.android.synthetic.main.item_product.view.tvProductName
import kotlinx.android.synthetic.main.item_product.view.tvProductQunatity

class OrderInnerAdapter (var context: Context, var list:ArrayList<OrderInnerModel>): RecyclerView.Adapter<OrderInnerAdapter.Viewholder>(){

    inner  class  Viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {
        return Viewholder(
            LayoutInflater.from(context).inflate(
                R.layout.item_order_inner,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        holder.itemView.tvProductQunatity.visibility=View.VISIBLE
        holder.itemView.llLowerlayout.visibility=View.GONE
        holder.itemView.tvProductQunatity.setText("Qunatity :"+list.get(position).quantity)
        holder.itemView.tvProductName.setText(list.get(position).name)
        holder.itemView.tvProductPriceOrder.setText("€"+list.get(position).price)


    }
}