package com.arumdental.home.cart.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arumdental.R
import com.arumdental.home.cart.model.CartResponse
import com.arumdental.home.cart.presenter.CartPresenter
import kotlinx.android.synthetic.main.item_checkout.view.*

class CartAdapter(var context: Context, var list:ArrayList<CartResponse>):RecyclerView.Adapter<CartAdapter.Viewholder>() {
   var onitemClick:updateCart?=null
    var cartpresenter:CartPresenter?=null

    fun setOnClickListener( onitemClick:updateCart)
    {
        this.onitemClick=onitemClick
    }

    inner  class Viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {
        return Viewholder(
            LayoutInflater.from(context).inflate(
                R.layout.item_checkout,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return  list.size
    }

    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        holder.itemView.tvProductName.setText(list.get(position).product_name)
        holder.itemView.tvProductPrice.setText(list.get(position).product_price)
        holder.itemView.tvProductQuantity.setText(""+list.get(position).quantity)
        holder.itemView.ivMinusUpdate.setOnClickListener(View.OnClickListener { v ->
            if(holder.itemView.tvProductQuantity.text.toString().trim().toInt()>0)
            {
                var quant=holder.itemView.tvProductQuantity.text.toString().trim()
                var newquant=quant.toInt()-1
                var cartItemKey=list.get(position).key
            //    holder.itemView.tvProductQuantity.text=""+newquant
                onitemClick!!.updatevalue(newquant,
                  cartItemKey!!,2)
            }



        })
           holder.itemView.ivPlusUpdate.setOnClickListener(View.OnClickListener { v ->
            var quant=holder.itemView.tvProductQuantity.text.toString().trim()
            var newquant=quant.toInt()+1
               var cartItemKey=list.get(position).key
         //   holder.itemView.tvProductQuantity.text=""+newquant
               onitemClick!!.updatevalue(newquant,
          cartItemKey!!,1)

           })
    }


    interface updateCart{
        fun updatevalue(quantity:Int,cart_key:String,i:Int)
    }
}