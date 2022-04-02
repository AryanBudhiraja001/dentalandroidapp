package com.arumdental.home.filter.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arumdental.R
import com.arumdental.home.brands.model.CategoriesResponse
import com.arumdental.home.filter.model.CustomModel
import kotlinx.android.synthetic.main.item_category.view.*

class CommonAdapter (var context: Context, var list:ArrayList<CustomModel>): RecyclerView.Adapter<CommonAdapter.Viewholder>() {

    inner  class Viewholder(itemView: View) : RecyclerView.ViewHolder(itemView){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {
        return Viewholder(
            LayoutInflater.from(context).inflate(
                R.layout.item_category,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return  list.size
    }

    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        holder.itemView.tvCategory.setText(list.get(position).name)
    }
}