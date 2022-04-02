package com.arumdental.home.brands.view

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Filter
import android.widget.ScrollView
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arumdental.R
import com.arumdental.home.HomeActivity
import com.arumdental.home.brands.BrandsContract
import com.arumdental.home.brands.adapter.BrandsAdapter
import com.arumdental.home.brands.model.CategoriesResponse
import com.arumdental.home.brands.model.ColorModel
import com.arumdental.home.brands.model.CustomCategoryModel
import com.arumdental.home.brands.presenter.BrandsPresenter
import com.arumdental.home.filter.adapter.CategoryAdapter
import com.arumdental.home.filter.model.CustomModel
import com.arumdental.home.filter.view.FilterFragment
import com.arumdental.utils.ShowToolbar
import com.arumdental.utils.hideProgress
import com.arumdental.utils.showProgress
import com.google.gson.Gson
import kotlinx.android.synthetic.main.brands_fragment.*
import kotlinx.android.synthetic.main.common_toolbar.*
import kotlinx.android.synthetic.main.filter_fragment.*
import kotlinx.android.synthetic.main.layout_filter.*

class BrandsFragment : Fragment(), BrandsContract.BrandsView, View.OnClickListener {

    var brandsPresenter: BrandsPresenter? = null
    var colorlist: ArrayList<ColorModel>? = null
    var showToolbar: ShowToolbar? = null
    var mView: View? = null
    var map: Map<Char, List<CategoriesResponse>>? = null
    var brandsAdapter: BrandsAdapter? = null
    var offset: Int = 1
    private var isScrolling = true
    private var isEndOfItems = false
    private lateinit var mlayoutManager: LinearLayoutManager
    private lateinit var mAdapter: BrandsAdapter
    var customlist: ArrayList<CustomCategoryModel>? = ArrayList()
    var isfirst: Boolean = true
    var categoriesResponseMain: ArrayList<CategoriesResponse>? = null
    var categoriesResponseMain2: ArrayList<CategoriesResponse>? = null
    var url =
        "https://app.arumdentalshop.com/wp-json/wc/v3/products/attributes/12/terms?per_page=100";


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.brands_fragment, container, false)


    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        init()
    }

    private fun init() {

        /* rvhome.isNestedScrollingEnabled = true
         ScrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v: NestedScrollView, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
             if (v.getChildAt(v.childCount - 1) != null) {
                 if (scrollY >= v.getChildAt(v.childCount - 1)
                         .measuredHeight - v.measuredHeight && scrollY > oldScrollY
                 ) {
                     //code to fetch more data for endless scrolling

                     isScrolling = false
                     offset += 1;
                     showProgress(context!!)
                     brandsPresenter?.getBrands(offset, CheckPagination.Pagination)

                 }
             }
         } as NestedScrollView.OnScrollChangeListener)*/
        url =
            "https://app.arumdentalshop.com/wp-json/wc/v3/products/attributes/12/terms?per_page=100"
        brandsPresenter = BrandsPresenter(this)
        brandsPresenter!!.getBrands(url)
        showProgress(context!!)
        categoriesResponseMain = ArrayList()
        categoriesResponseMain2 = ArrayList()
        customlist = ArrayList()


        colorlist = ArrayList()
        colorlist!!.add(ColorModel(R.color.colorPrimary, R.drawable.blue_stroke_bg))
        colorlist!!.add(ColorModel(R.color.colorSkyBlue, R.drawable.skyblue_stroke_bg))
        colorlist!!.add(ColorModel(R.color.colorSeaGreen, R.drawable.seagreen_stroke_bg))
        colorlist!!.add(ColorModel(R.color.colorGreen, R.drawable.green_stroke_bg))
        colorlist!!.add(ColorModel(R.color.colorPink, R.drawable.pink_stroke_bg))
        colorlist!!.add(ColorModel(R.color.colorPurple, R.drawable.purple_stroke_bg))

        llFilter.setOnClickListener(this)
        llSortBrands.setOnClickListener(this)
        showToolbar!!.changeToolbar(true, "Brands", true, false)
//        categoriesResponseMain!!.clear()
        println("initial list size " + categoriesResponseMain?.size)


        /* mlayoutManager= LinearLayoutManager(context)
         rvhome.layoutManager=mlayoutManager*/

    }

    override fun onSucess(categoriesResponse: ArrayList<CategoriesResponse>) {


        if (isAdded) {


            if (isfirst) {
                println("main list size" + categoriesResponseMain!!.size)
                categoriesResponseMain!!.addAll(categoriesResponse)
                println("caasdads" + categoriesResponse.size)
                if (categoriesResponseMain!!.size > 0) {
                    tvNoCategory.visibility = View.GONE
                    rvhome.visibility = View.VISIBLE
                    println("sorted list " + Gson().toJson(categoriesResponseMain!!.groupBy {
                        it.name?.get(
                            0
                        )!!.toUpperCase()
                    }))
                    var sortedArrayList =
                        categoriesResponseMain!!.groupBy { it.name?.get(0)!!.toUpperCase() }
                    map = sortedArrayList
                    var list: List<CategoriesResponse>? = ArrayList()
                    var listOfkeys = sortedArrayList.keys.toMutableList()
                    for (i in listOfkeys.indices) {
                        list = sortedArrayList.get(listOfkeys.get(i))
                        customlist!!.add(CustomCategoryModel(listOfkeys.get(i).toString(), list!!))
                    }

                    println("addd lisy " + Gson().toJson(customlist!!))
                    println("fnmdfndsm  non pagintion " + customlist!!.get(0).data.get(0).id)

                    println("aksjakljsslajalsfja " + customlist!!.size)

                    brandsAdapter = BrandsAdapter(context!!, customlist!!, colorlist!!)
                    Log.e("Innnnnnnnnnnnnnn", "innnnnnnnnn")

                    rvhome.adapter = brandsAdapter

                    hideProgress()

                } else {
                    hideProgress()
                    tvNoCategory.visibility = View.VISIBLE
                    rvhome.visibility = View.GONE

                }

                url = url + '&' + "page=" + 2
                brandsPresenter?.getBrands(url)
                isfirst = false
              //  showProgress(context!!)


            }
            else {
                hideProgress()
                println("main list " + Gson().toJson(categoriesResponseMain))

                categoriesResponseMain2!!.addAll(categoriesResponse)
                customlist=ArrayList()
                println("size main " + categoriesResponseMain2!!.size)
                isfirst = true
                if (categoriesResponseMain2!!.size > 0) {
                    tvNoCategory.visibility = View.GONE
                    rvhome.visibility = View.VISIBLE
                    println("sorted list " + Gson().toJson(categoriesResponseMain2!!.groupBy {
                        it.name?.get(
                            0
                        )!!.toUpperCase()
                    }))
                    var sortedArrayList =
                        categoriesResponseMain2!!.groupBy { it.name?.get(0)!!.toUpperCase() }
                    map = sortedArrayList
                    var list: List<CategoriesResponse>? = ArrayList()
                    var listOfkeys = sortedArrayList.keys.toMutableList()
                    for (i in listOfkeys.indices) {
                        list = sortedArrayList.get(listOfkeys.get(i))
                        customlist!!.add(CustomCategoryModel(listOfkeys.get(i).toString(), list!!))
                    }

                    println("addd lisy " + Gson().toJson(customlist!!))
                    println("fnmdfndsm  non pagintion " + customlist!!.get(0).data.get(0).id)

                    println("aksjakljsslajalsfja " + customlist!!.size)

                   brandsAdapter!!.setItems(customlist!!)

                    hideProgress()

                } else {
                    hideProgress()
                    tvNoCategory.visibility = View.VISIBLE
                    rvhome.visibility = View.GONE

                }


            }


        }


    }

    override fun onResume() {
        super.onResume()
        // categoriesResponseMain?.clear()
        println("size gdfgd " + categoriesResponseMain!!.size)
    }

    override fun onError(error: String) {

        if (isAdded) {
            hideProgress()
        }

    }

    override fun onSucessSearch(categoriesResponse: ArrayList<CategoriesResponse>) {
        if (isAdded) {

            if (categoriesResponse.size > 0) {
                tvNoCategory.visibility = View.GONE
                rvhome.visibility = View.VISIBLE
                println("sorted list " + Gson().toJson(categoriesResponse.groupBy {
                    it.name?.get(0)!!.toUpperCase()
                }))
                var sortedArrayList = categoriesResponse.groupBy { it.name?.get(0)!!.toUpperCase() }
                map = sortedArrayList
                var list: List<CategoriesResponse>? = ArrayList()
                var listOfkeys = sortedArrayList.keys.toMutableList()
                var customlistSearch: ArrayList<CustomCategoryModel>? = ArrayList()

                for (i in listOfkeys.indices) {
                    list = sortedArrayList.get(listOfkeys.get(i))
                    customlistSearch!!.add(
                        CustomCategoryModel(
                            listOfkeys.get(i).toString(),
                            list!!
                        )
                    )
                }

                println("addd lisy " + Gson().toJson(customlistSearch!!))




                brandsAdapter = BrandsAdapter(context!!, customlistSearch!!, colorlist!!)
                rvhome.adapter = brandsAdapter
                //rvhome.addOnScrollListener(mScrollListener)
                hideProgress()


            } else {
                hideProgress()
                tvNoCategory.visibility = View.VISIBLE
                rvhome.visibility = View.GONE

            }


        }
    }

    override fun onErrorSearch(error: String) {
        hideProgress()

    }

    @SuppressLint("WrongConstant")
    override fun onClick(v: View?) {
        when (v) {
            llFilter -> {
                customlist!!.clear()

                (context as HomeActivity).replaceFragmentWithOutBackStack2(
                    FilterFragment(),
                    "filter frag"
                )

            }

            llSortBrands -> {

            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        showToolbar = context as HomeActivity
    }

    fun onSearch(string: CharSequence, i: Int) {
        if (i == 1) {
            brandsPresenter!!.searchBrands(string.toString())
            showProgress(context!!)
        } else {
            brandsAdapter = BrandsAdapter(context!!, customlist!!, colorlist!!)
            rvhome.adapter = brandsAdapter
            rvhome.visibility = View.VISIBLE
            tvNoCategory.visibility = View.GONE

        }

    }

    override fun onDetach() {
        super.onDetach()
        categoriesResponseMain?.clear()
    }

    override fun onDestroy() {
        super.onDestroy()
        categoriesResponseMain?.clear()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        categoriesResponseMain?.clear()
    }


}