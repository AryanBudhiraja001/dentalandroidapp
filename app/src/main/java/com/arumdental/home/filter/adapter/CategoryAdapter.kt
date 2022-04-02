package com.arumdental.home.filter.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arumdental.R
import com.arumdental.home.brands.model.CategoriesResponse
import com.arumdental.home.products.model.ProductResponseModel
import kotlinx.android.synthetic.main.item_category.view.*

class CategoryAdapter (var context: Context, var list:ArrayList<CategoriesResponse>, var typemain:Int):RecyclerView.Adapter<CategoryAdapter.Viewholder>() {

    var categoryIdlist:ArrayList<Int>?= ArrayList()
    var categoryIdlistMain:ArrayList<Int>?= ArrayList()
    var onSelectItem:onSelect?=null

    fun  setOnClickListener(onSelectItem:onSelect)
    {
        this.onSelectItem=onSelectItem
    }

    inner  class Viewholder(itemView: View) :RecyclerView.ViewHolder(itemView){

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

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }

    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        holder.itemView.tvCategory.setText(list.get(position).name)

        holder.itemView.cbCategory.setOnCheckedChangeListener { buttonView, isChecked ->


                if(isChecked)
                {
                    holder.itemView.cbCategory.isChecked=true
                    categoryIdlist!!.add(list.get(position).id!!)
                    println("ghhh"+categoryIdlist!!.size)
                    onSelectItem!!.onSelect(categoryIdlist,typemain)

                }
                else{
                    holder.itemView.cbCategory.isChecked=false
                    categoryIdlist!!.remove(list.get(position).id!!)
                    println("ghhhremove"+categoryIdlist!!.size)
                    onSelectItem!!.onSelect(categoryIdlist!!, typemain)

                }



        }
    }

    interface onSelect{
        fun onSelect(categoryId:ArrayList<Int>?=ArrayList(),type:Int)
      fun  onSelectCategory(categoryId:ArrayList<Int>?=ArrayList(),type:Int)
    }

    fun updateList(list1:ArrayList<CategoriesResponse>, type: Int) {

        if(type==1)
        {
            typemain=type
            list = list1
            notifyDataSetChanged()

        }

    }
}