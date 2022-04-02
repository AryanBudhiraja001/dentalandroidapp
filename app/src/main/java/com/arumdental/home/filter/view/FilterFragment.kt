package com.arumdental.home.filter.view

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arumdental.R
import com.arumdental.home.HomeActivity
import com.arumdental.home.brands.BrandsContract
import com.arumdental.home.brands.model.CategoriesResponse
import com.arumdental.home.brands.presenter.BrandsPresenter
import com.arumdental.home.brands.view.BrandsFragment
import com.arumdental.home.filter.FilterContract
import com.arumdental.home.filter.SizeContract
import com.arumdental.home.filter.adapter.CategoryAdapter
import com.arumdental.home.filter.model.FilterResponseModel
import com.arumdental.home.filter.presenter.FilterPresenter
import com.arumdental.home.filter.presenter.SizePresenter
import com.arumdental.home.products.model.ProductResponseModel
import com.arumdental.home.products.view.ProductFragment
import com.arumdental.utils.ShowToolbar
import com.arumdental.utils.hideProgress
import com.arumdental.utils.showProgress
import com.arumdental.utils.showSnackBar
import com.google.gson.Gson
import kotlinx.android.synthetic.main.filter_fragment.*
import kotlinx.android.synthetic.main.filter_fragment.FilterView
import kotlinx.android.synthetic.main.filter_fragment.cbAll
import kotlinx.android.synthetic.main.filter_fragment.cbInStock
import kotlinx.android.synthetic.main.filter_fragment.edSearchCategory
import kotlinx.android.synthetic.main.filter_fragment.rvCategory
import kotlinx.android.synthetic.main.filter_fragment.tvUpdateFilter
import kotlinx.android.synthetic.main.filter_new_fragment.*
import kotlinx.android.synthetic.main.layout_filter.*
import retrofit2.http.Url

class FilterFragment : Fragment(), View.OnClickListener, BrandsContract.BrandsView,
    SizeContract.SizeView, CategoryAdapter.onSelect, FilterContract.FilterView {

    var showToolbar: ShowToolbar? = null
    var brandsPresenter: BrandsPresenter? = null
    var sizePresenter: SizePresenter? = null
    var filterPresenter: FilterPresenter? = null
    var sizelist: String? = null
    var volumelist: String? = null


    var typeList: String? = null
    var categoryList: String? = null
    var layoutmanger: RecyclerView.LayoutManager? = null
    var stockValue: String? = null
    var typeValue: String? = null
    var sizeValue: String? = null
    var volumeValue: String? = null
    var categoryMainList: ArrayList<CategoriesResponse>? = null
    var sizelistMain: ArrayList<CategoriesResponse>? = ArrayList()
    var typelistMain: ArrayList<CategoriesResponse>? = ArrayList()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.filter_new_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        showToolbar = context as HomeActivity

    }

    private fun init() {
        showToolbar!!.changeToolbar(true, "filter", false, false)
        filterPresenter = FilterPresenter(this)
        sizePresenter = SizePresenter(this)

        brandsPresenter = BrandsPresenter(this)
        brandsPresenter!!.getCategories()




        edSearchCategory.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                /*if (s!!.length > 0) {
                    filter().filter(s.toString())

                } else {
                    var categoryAdapter = CategoryAdapter(context!!, categoryMainList!!, 1)
                    categoryAdapter.setOnClickListener(this@FilterFragment)
                    rvCategory.adapter = categoryAdapter
                }*/
            }

        })

        showProgress(context!!)
        llFilter.setOnClickListener(this)
        llSortBrands.setOnClickListener(this)
        tvUpdateFilter.setOnClickListener(this)
        llSortBrands.setBackgroundColor(
            ContextCompat.getColor(
                context!!,
                R.color.colorDarkBackground
            )
        )
        llFilter.setBackgroundColor(ContextCompat.getColor(context!!, R.color.colorBackground))
        cbAll.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                cbInStock.isChecked = false
                stockValue = null
            }

        }
        cbInStock.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                cbAll.isChecked = false
                stockValue = "instock"
            }
        }
       /* cbAllType.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                cbEngaging.isChecked = false
                cbNonEngaging.isChecked = false
                typeValue = "All"
            }

        }
        cbEngaging.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                cbAllType.isChecked = false
                cbNonEngaging.isChecked = false
                typeValue = "Engaging"
            }

        }
        cbNonEngaging.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                cbEngaging.isChecked = false
                cbAllType.isChecked = false
                typeValue = "Non-Engaging"
            }

        }

        cbAllSize.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                cbWP.isChecked = false
                cbRP.isChecked = false
                cbNP.isChecked = false
                sizeValue = "All"
            }

        }
        cbNP.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                cbWP.isChecked = false
                cbRP.isChecked = false
                cbAllSize.isChecked = false
                sizeValue = "NP"
            }

        }
        cbRP.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                cbWP.isChecked = false
                cbNP.isChecked = false
                cbAllSize.isChecked = false
                sizeValue = "RP"
            }

        }
        cbWP.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                cbRP.isChecked = false
                cbNP.isChecked = false
                cbAllSize.isChecked = false
                sizeValue = "WP"
            }

        }
        cbAllVolume.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                cbPack.isChecked = false
                cbSingle.isChecked = false
                volumeValue = "All"
            }

        }
        cbSingle.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                cbPack.isChecked = false
                cbAllVolume.isChecked = false
                volumeValue = "Single"
            }

        }
        cbPack.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                cbSingle.isChecked = false
                cbAllVolume.isChecked = false
                volumeValue = "Single"
            }

        }*/





    }


    override fun onClick(v: View?) {
        when (v) {
            llFilter -> {
                activity!!.supportFragmentManager.popBackStack()
            }

            llSortBrands -> {
                (context as HomeActivity).replaceFragmentWithOutBackStack2(
                    BrandsFragment(),
                    "brands frag"
                )
            }
            tvUpdateFilter ->
            {

                var query:String?="wp-json/wc/v3/products?per_page=100"
                if(categoryList!=null)
                {
                    query=query+'&'+"category="+categoryList

                    if(sizelist!=null)
                    {
                        query=query+'&'+"pa_size="+sizelist

                        if(volumelist!=null)
                        {
                            query=query+'&'+"pa_volume="+volumelist


                            if(typeList!=null)
                            {
                                query=query+'&'+"pa_pro-type="+typeList
                                if(stockValue!=null)
                                {
                                    query=query+'&'+"stock_status"+stockValue
                                }
                            }
                            else
                            {
                                if(stockValue!=null)
                                {
                                    query=query+'&'+"stock_status"+stockValue
                                }

                            }
                        }
                        else
                        {
                            if(typeList!=null)
                            {
                                query=query+'&'+"pa_pro-type="+typeList
                            }
                            else
                            {
                                if(stockValue!=null)
                                {
                                    query=query+'&'+"stock_status"+stockValue
                                }
                            }
                        }

                    }
                    else
                    {
                        if(volumelist!=null)
                        {
                            query=query+'&'+"pa_volume="+volumelist


                            if(typeList!=null)
                            {
                                query=query+'&'+"pa_pro-type="+typeList
                            }
                            else
                            {
                                if(stockValue!=null)
                                { println("stock "+"0")
                                    query=query+'&'+"stock_status"+stockValue
                                }

                            }
                        }
                        else
                        {
                            if(typeList!=null)
                            {
                                query=query+'&'+"pa_pro-type="+typeList
                            }
                            else
                            {
                                if(stockValue!=null)
                                { println("stock "+"1")
                                    query=query+'&'+"stock_status"+stockValue
                                }
                                else
                                {
                                    query=query
                                }

                            }
                        }
                    }

                }
                else
                {
                    if(sizelist!=null)
                    {
                        query=query+'&'+"pa_size="+sizelist

                        if(volumelist!=null)
                        {
                            query=query+'&'+"pa_volume="+volumelist


                            if(typeList!=null)
                            {
                                query=query+'&'+"pa_pro-type="+typeList
                            }
                            else
                            {
                                if(stockValue!=null)
                                { println("stock "+"2")
                                    query=query+'&'+"stock_status"+stockValue
                                }

                            }
                        }

                    }
                    else{
                        if(volumelist!=null)
                        {
                            query=query+'&'+"pa_volume="+volumelist


                            if(typeList!=null)
                            {
                                query=query+'&'+"pa_pro-type="+typeList
                            }
                            else
                            {
                                if(stockValue!=null)
                                { println("stock "+"3")
                                    query=query+'&'+"stock_status"+stockValue
                                }

                            }

                        }
                        else{
                            if(typeList!=null)
                            {
                                query=query+'&'+"pa_pro-type="+typeList
                            }
                            else
                            {
                                if(stockValue!=null)
                                {
                                    println("stock "+"4")
                                    query=query+'&'+"stock_status"+stockValue
                                }

                            }
                        }
                    }

                }

                println("my quert  "+query)


                if(TextUtils.isEmpty(edSearchCategory.text.toString().trim()))
                {
                    filterPresenter!!.getFilters(query!!)
                    showProgress(context!!)
                    stockValue=null
                    sizelist=null
                    volumelist=null
                    typeList=null
                    categoryList=null
                }
                else
                {
                    query=query+'&' + "search=" + edSearchCategory.text.toString().trim().toUpperCase()
                    println("my quert  "+query)
                    filterPresenter!!.getFilters( query!!)
                    showProgress(context!!)
                    edSearchCategory.setText("")
                    stockValue=null
                    sizelist=null
                    volumelist=null
                    typeList=null
                    categoryList=null
                }
            }
                /*var query: String? = ""
                if (categoryList != null) {
                    query = "category=" + categoryList

                    if (sizeValue != null) {
                        query = query + '&' + "size=" + sizeValue

                        if (volumeValue != null) {
                            query = query + '&' + "volume=" + volumeValue


                            if (typeValue != null) {
                                query =
                                    query + '&' + "types=" + typeValue
                                if (stockValue != null) {
                                    query = query + '&' + "stock_status=" + stockValue
                                }
                            } else {
                                if (stockValue != null) {
                                    query = query + '&' + "stock_status=" + stockValue
                                }

                            }
                        } else {
                            if (typeValue != null) {
                                query =
                                    query + '&' + "types=" + typeValue
                            } else {
                                if (stockValue != null) {
                                    query = query + '&' + "stock_status=" + stockValue
                                }
                            }
                        }

                    } else {
                        if (volumeValue != null) {
                            query = query + '&' + "volume=" + volumeValue


                            if (typeValue != null) {
                                query =
                                    query + '&' + "types=" + typeValue
                            } else {
                                if (stockValue != null) {
                                    query = query + '&' + "stock_status=" + stockValue
                                }

                            }
                        } else {
                            if (typeValue != null) {
                                query =
                                    query + '&' + "types=" + typeValue
                            } else {
                                if (stockValue != null) {
                                    query = query + '&' + "stock_status=" + stockValue
                                }

                            }
                        }
                    }

                } else {
                    if (sizeValue != null) {
                        query = query + '&' + "size=" + sizeValue

                        if (volumeValue != null) {
                            query = query + '&' + "volume=" + volumeValue


                            if (typeValue != null) {
                                query =
                                    query + '&' + "types=" + typeValue
                            } else {
                                if (stockValue != null) {
                                    query = query + '&' + "stock_status=" + stockValue
                                }

                            }
                        }

                    } else {
                        if (volumeValue != null) {
                            query = query + '&' + "volume=" + volumeValue


                            if (typeValue != null) {
                                query =
                                    query + '&' + "types=" + typeValue
                            } else {
                                if (stockValue != null) {
                                    query = query + '&' + "stock_status=" + stockValue
                                }

                            }

                        } else {
                            if (typeValue != null) {
                                query =
                                    query + '&' + "types=" + typeValue
                            } else {
                                if (stockValue != null) {
                                    query = query + '&' + "stock_status=" + stockValue
                                }

                            }
                        }
                    }

                }*/






        }
    }

    override fun onSucess(categoriesResponse: ArrayList<CategoriesResponse>) {

        if (isAdded) {

            categoryMainList = categoriesResponse
            var categoryAdapter = CategoryAdapter(context!!, categoriesResponse, 1)
            categoryAdapter.setOnClickListener(this)
            rvCategory.adapter = categoryAdapter
            sizePresenter!!.getSizes()
        }


    }

    override fun onError(error: String) {

        if (isAdded) {
            hideProgress()
            activity!!.supportFragmentManager.popBackStack()
        }


    }

    override fun onSucessSearch(categoriesResponse: ArrayList<CategoriesResponse>) {

    }

    override fun onErrorSearch(error: String) {

    }

    override fun onSucessSize(sizelist: ArrayList<CategoriesResponse>) {
        if (isAdded) {

            categoryMainList = sizelist
            sizelistMain=sizelist
            var categoryAdapter = CategoryAdapter(context!!, sizelist, 2)
            categoryAdapter.setOnClickListener(this)
            rvSized.adapter = categoryAdapter
            sizePresenter!!.getVolume()

        }



    }

    override fun onErrorSize(error: String) {

        if (isAdded) {
            hideProgress()
            activity!!.supportFragmentManager.popBackStack()
        }


    }

    override fun onSucessType(typelist: ArrayList<CategoriesResponse>) {
        if (isAdded) {
            hideProgress()
            categoryMainList = typelist
            var categoryAdapter = CategoryAdapter(context!!, typelist, 4)
            categoryAdapter.setOnClickListener(this)
            rvType.adapter = categoryAdapter

        }
    }

    override fun onErrorType(error: String) {
        if (isAdded) {
            hideProgress()
            activity!!.supportFragmentManager.popBackStack()
        }
    }


    override fun onSucessVolume(volumelist: ArrayList<CategoriesResponse>) {

        if (isAdded) {

            categoryMainList = volumelist
            var categoryAdapter = CategoryAdapter(context!!, volumelist, 3)
            categoryAdapter.setOnClickListener(this)
            rvVolume.adapter = categoryAdapter
            sizePresenter?.getTypes()
        }

    }

    override fun onErrorVolume(error: String) {

        if (isAdded) {
            hideProgress()
            activity!!.supportFragmentManager.popBackStack()
        }

    }

    override fun onSelect(categoryId: ArrayList<Int>?, type: Int) {

        println("selected Ids" + categoryId)
        if (type == 1) {
            categoryList = categoryId.toString().replace("[", "").replace("]", "")
                .replace(", ", ",");

        } else if (type == 2) {

            sizelist = categoryId.toString().replace("[", "").replace("]", "")
                .replace(", ", ",");

        } else if (type == 3) {
            volumelist = categoryId.toString().replace("[", "").replace("]", "")
                .replace(", ", ",");
        } else {
            typeList = categoryId.toString().replace("[", "").replace("]", "")
                .replace(", ", ",");
        }


    }

    override fun onSelectCategory(categoryId: ArrayList<Int>?, type: Int) {
        println("selected Ids" + categoryId)
        if (type == 1) {
            categoryList = categoryId.toString().replace("[", "").replace("]", "")
                .replace(", ", ",");

        }
    }

    override fun onSucessFilter(productList: ArrayList<ProductResponseModel>) {

        if (isAdded) {
            hideProgress()


            var args = Bundle()
            args.putString("comingFrom", "filter")
            args.putSerializable("list", productList!!)
            var fragment = ProductFragment()
            fragment.arguments = args
            (context as HomeActivity).replaceFragmentWithOutBackStack(fragment, "product frag")
        }


    }

    override fun onErrorFilter(error: String) {

        if (isAdded) {
            hideProgress()
          //  activity!!.supportFragmentManager.popBackStack()
            showSnackBar(FilterView, error)
        }

    }

    private fun filter(): Filter {
        val temp: ArrayList<CategoriesResponse> =
            ArrayList()

        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                for (list in categoryMainList!!) {
                    if (!constraint!!.isEmpty()) {
                        if (list.name!!.toUpperCase()
                                .contains(constraint.toString().toUpperCase())
                        ) {
                            temp.add(list)
                        }
                    }

                }
                return null!!
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                var categoryAdapter = CategoryAdapter(context!!, categoryMainList!!, 1)
                categoryAdapter.setOnClickListener(this@FilterFragment)
                categoryAdapter.updateList(temp, 1)

                rvCategory.adapter = categoryAdapter
            }


        }


    }

   

}