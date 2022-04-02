package com.arumdental.home.products.adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.arumdental.R
import com.arumdental.home.HomeActivity
import com.arumdental.home.products.model.ProductResponseModel
import com.arumdental.home.products.view.ProductDetailEye
import com.arumdental.home.products.view.ProductDetailFragment
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_product.view.*
import kotlinx.android.synthetic.main.item_product.view.ivEye
import kotlinx.android.synthetic.main.item_product.view.ivProductImage
import kotlinx.android.synthetic.main.item_product.view.tvProductName
import kotlinx.android.synthetic.main.item_product.view.tvProductPrice
import kotlinx.android.synthetic.main.item_product_grid.view.*

class ProductAdapter( var context:Context,  var list:ArrayList<ProductResponseModel>, var resource:Int):RecyclerView.Adapter<ProductAdapter.Viewholder>(){

    var onClick:onAddToCart?=null

    fun setOnClickListener(onClick:onAddToCart)
    {
        this.onClick=onClick
    }

    inner  class  Viewholder(itemView: View) :RecyclerView.ViewHolder(itemView){
        var text=itemView.findViewById<TextView>(R.id.tvAddToCart)
        var ivFav=itemView.findViewById<ImageView>(R.id.ivFav)




    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {
        return Viewholder(
            LayoutInflater.from(context).inflate(
               resource,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return  list.size

    }

    override fun onBindViewHolder(holder: Viewholder, position: Int) {

        holder.itemView.tvProductName.setText(list.get(position).name)
        holder.itemView.tvProductPrice.setText("€"+list.get(position).price)
        if(list.get(position).images!=null && list.get(position).images!!.size>0)
        {
            Glide.with(context).load(list.get(position).images!!.get(0).src).into(holder.itemView.ivProductImage)
        }
        if(list.get(position).isFav!!)
        {
            holder.ivFav.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.heart_fill))

        }
        else{
            holder.ivFav.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.heart_grey))
        }


        holder.itemView.setOnClickListener(View.OnClickListener { v ->
            var args=Bundle()
            args.putSerializable("product Detail",list.get(position))
            var fragment=ProductDetailEye()
            fragment.arguments=args
            (context as HomeActivity).replaceFragmentWithOutBackStack(fragment!!,"product Desc  Eye frag")
        })

        holder.itemView.ivEye.setOnClickListener { v ->

            var args=Bundle()
            args.putSerializable("product Detail",list.get(position))
            var fragment=ProductDetailEye()
            fragment.arguments=args
            (context as HomeActivity).replaceFragmentWithOutBackStack(fragment!!,"product Desc  Eye frag")
        }

        holder.text.setOnClickListener(View.OnClickListener { v ->
            onClick!!.onAddToCart(list.get(position).id!!,1)


        })

        holder.ivFav.setOnClickListener(View.OnClickListener { v ->

            if(list.get(position).isFav!!)
            {
                onClick!!.onAddFav(list.get(position).id!!, list.get(position).price.toString(),2)


            }
            else{
                onClick!!.onAddFav(list.get(position).id!!, list.get(position).price.toString(),1)

            }

        })




    }

    interface  onAddToCart{
        fun  onAddToCart( product_id:Int,qunatity:Int)
        fun onAddFav( product_id:Int, price:String, i:Int)

    }
}