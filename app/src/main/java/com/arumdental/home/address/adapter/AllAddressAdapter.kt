package com.arumdental.home.address.adapter

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.arumdental.R
import com.arumdental.home.brands.model.CustomCategoryModel
import com.arumdental.profile.updateProfile.model.Billing
import com.arumdental.utils.UserSharedPrefences
import com.google.gson.Gson
import kotlinx.android.synthetic.main.address_item_layout.view.*

class AllAddressAdapter(var context: Context, var list: ArrayList<Billing>):RecyclerView.Adapter<AllAddressAdapter.Viewholder>() {
    private var row_index: Int=0
    private var isSelected:Boolean=false

    var onItemClick:onClick?=null
    fun setOnClickListener(onItemClick:onClick)
    {
        this.onItemClick=onItemClick
    }
    inner  class  Viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {
        return Viewholder(
            LayoutInflater.from(context).inflate(
                R.layout.address_item_layout,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: Viewholder, position: Int) {


        var addressString=list.get(position).address_1+" ,"+list.get(position).address_2+" ,\n"+list.get(position).city+", "+list.get(position).state+", \n"+list.get(position).country+",\n"+list.get(position).postcode
        holder.itemView.tvAddrress.setText(addressString)
        holder.itemView.tvPhoneNumber.setText(list.get(position).phone)

        holder.itemView.checkbox.setOnClickListener(View.OnClickListener { v ->
            isSelected = true
            row_index = position
            notifyDataSetChanged()
            onItemClick!!.onItemClicListner(position, list)



        })
        if (isSelected) {
            if (row_index == position) {

                holder.itemView.checkbox.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.radio_selected))

            } else {
                holder.itemView.checkbox.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.radio_unselecred))
            }
        }

        holder.itemView.tvEditAddress.setOnClickListener(View.OnClickListener { v ->
            onItemClick!!.onItemClicListnerEdit(position,list)
        })

        holder.itemView.tvDeleteAddress.setOnClickListener(View.OnClickListener { v ->
            onItemClick!!.onItemClicListnerDelete(position,list)
        })








    }

    override fun getItemCount(): Int {
        return  list.size
    }

    interface  onClick{
        fun onItemClicListner(position: Int, list:ArrayList<Billing>)
        fun onItemClicListnerEdit(position: Int, list:ArrayList<Billing>)
        fun onItemClicListnerDelete(position: Int, list:ArrayList<Billing>)
    }
}