package com.arumdental.home.brands.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arumdental.R
import com.arumdental.home.brands.model.CategoriesResponse
import com.arumdental.home.brands.model.ColorModel
import com.arumdental.home.brands.model.CustomCategoryModel
import com.google.gson.Gson
import kotlinx.android.synthetic.main.item_brands.view.*

class BrandsAdapter(var context: Context, var list: ArrayList<CustomCategoryModel>, var colorlist:ArrayList<ColorModel>):RecyclerView.Adapter<BrandsAdapter.Viewholder>() {



    inner  class Viewholder(itemView: View) :RecyclerView.ViewHolder(itemView){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {
        return Viewholder(
            LayoutInflater.from(context).inflate(
                R.layout.item_brands,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return  list.size

    }

    override fun onBindViewHolder(holder: Viewholder, position: Int) {

        holder.itemView.tvAlphabetName.setText(list.get(position).alphabet)
        println("inner list "+Gson().toJson(list.get(position).data))
        var brandsInnerAdapter=BrandsInnerAdapter(context, list.get(position).data, colorlist!!)
        var layoutManager=GridLayoutManager(context,2)
        holder.itemView.rvInnerBrands.layoutManager=layoutManager
        holder.itemView.rvInnerBrands.adapter=brandsInnerAdapter


    }


    fun setItems(dataList : ArrayList<CustomCategoryModel>) {
        this.list.addAll(dataList)

        notifyItemRangeInserted(this.list
            .size,dataList.size)
    }
}