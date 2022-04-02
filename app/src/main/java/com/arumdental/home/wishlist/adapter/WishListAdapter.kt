package com.arumdental.home.wishlist.adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.arumdental.R
import com.arumdental.home.HomeActivity
import com.arumdental.home.products.model.ProductResponseModel
import com.arumdental.home.products.view.ProductDetailEye
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_product.view.*
import kotlinx.android.synthetic.main.wishlist_item.view.*

class WishListAdapter(var context: Context, var list:ArrayList<ProductResponseModel>):RecyclerView.Adapter<WishListAdapter.Viewholder>() {

    var onItemClick:onClick?=null

    fun setOnClickListener(onClick: onClick)
    {
        this.onItemClick=onClick
    }


    inner  class  Viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {
        return Viewholder(
            LayoutInflater.from(context).inflate(
                R.layout.wishlist_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return  list.size
    }

    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        holder.itemView.tvProductNameW.setText(list.get(position).name)
        holder.itemView.tvProductPriceW.setText("£"+list.get(position).price)
        if(list.get(position).images!=null && list.get(position).images!!.size>0)
        {
            Glide.with(context).load(list.get(position).images!!.get(0).src).into(holder.itemView.ivProductImageW)
        }
        else
        {

        }
        holder.itemView.ivFavW.setOnClickListener(View.OnClickListener { v ->
            onItemClick?.onFavClickListener(list.get(position).id!!)
        })

        holder.itemView.setOnClickListener(View.OnClickListener { v ->
            var args= Bundle()
            args.putSerializable("product Detail",list.get(position))
            var fragment= ProductDetailEye()
            fragment.arguments=args
            (context as HomeActivity).replaceFragmentWithOutBackStack(fragment!!,"product Desc  Eye frag")

        })
        holder.itemView.tvAddToCartW.setOnClickListener(View.OnClickListener { v ->
            onItemClick?.onAddToCart(list.get(position).id!!,1)
        })
    }

    interface  onClick{
        fun onFavClickListener(productID:Int)
        fun onAddToCart(product_id:Int,qunatity:Int)
    }
}