package com.arumdental.home.products.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.arumdental.R
import com.arumdental.home.products.model.ProductResponseModel
import com.bumptech.glide.Glide

class ProductImageSliderAdpater(var context: Context, var list:ArrayList<ProductResponseModel.Images>) :PagerAdapter() {
    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return  view==`object`
    }

    override fun getCount(): Int {
        return  list.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = inflater.inflate(R.layout.viewpager_item, null)
        view.tag = "view$position"
        val imageview =
            view.findViewById<View>(R.id.ivViewPager) as ImageView

        Glide.with(context).load(list.get(position).src).into(imageview)

        val viewPager = container as ViewPager
        viewPager.addView(view, 0)

        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val viewPager = container as ViewPager
        val view = `object` as View
        viewPager.removeView(view)
    }
}


