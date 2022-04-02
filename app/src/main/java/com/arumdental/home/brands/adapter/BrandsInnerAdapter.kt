package com.arumdental.home.brands.adapter

import android.content.Context
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.arumdental.R
import com.arumdental.home.HomeActivity
import com.arumdental.home.brands.model.CategoriesResponse
import com.arumdental.home.brands.model.ColorModel
import com.arumdental.home.products.view.ProductFragment
import kotlinx.android.synthetic.main.item_inner_brand.view.*
import java.util.*

class BrandsInnerAdapter (var context: Context, var list:List<CategoriesResponse>,var colorlist:ArrayList<ColorModel>):RecyclerView.Adapter<BrandsInnerAdapter.Viewholder>(){

    inner  class  Viewholder(itemView: View) :RecyclerView.ViewHolder(itemView){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {
        return Viewholder(
            LayoutInflater.from(context).inflate(
                R.layout.item_inner_brand,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return  list.size

    }

    override fun onBindViewHolder(holder: Viewholder, position: Int) {

        holder.itemView.tvBrand.setText("ARUM® "+list.get(position).slug)
        holder.itemView.tvUpperText.setText(Html.fromHtml(list.get(position).name))
        val selection=getItem()
        holder.itemView.background=ContextCompat.getDrawable(context, selection!!.drawable)

        holder.itemView.lowerLayout.setBackgroundColor(ContextCompat.getColor(context, selection!!.color))
        holder.itemView.setOnClickListener(View.OnClickListener { v ->
            var args= Bundle()
            args.putString("comingFrom","brand")
            args.putInt("id", list.get(position).id!!)
            var fragment=ProductFragment()
            fragment.arguments=args
            (context as HomeActivity).replaceFragment(fragment, "product frag")
        })

    }

    private fun getItem(): ColorModel? {
        if (colorlist != null) {
            val index = Random().nextInt(colorlist.size)
            return colorlist.get(index)
        }
        return null
    }
}