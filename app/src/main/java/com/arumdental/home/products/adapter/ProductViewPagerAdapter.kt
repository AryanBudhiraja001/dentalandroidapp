package com.arumdental.home.products.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.arumdental.R
import com.arumdental.home.products.model.ProductResponseModel
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.viewpager_item.view.*

class ProductViewPagerAdapter(var context:Context, var list:ArrayList<ProductResponseModel.Images>):RecyclerView.Adapter<ProductViewPagerAdapter.viewholder> (){

    inner  class  viewholder(itemView: View) :RecyclerView.ViewHolder(itemView)
    {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewholder {
        return viewholder(
            LayoutInflater.from(context).inflate(
                R.layout.viewpager_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return  list.size
    }

    override fun onBindViewHolder(holder: viewholder, position: Int) {
        Glide.with(context).load(list.get(position).src).into(holder.itemView.ivViewPager)
    }


}

