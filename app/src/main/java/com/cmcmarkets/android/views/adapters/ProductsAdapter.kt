package com.cmcmarkets.android.views.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cmcmarkets.android.custom.ProductItemViewedListener
import com.cmcmarkets.android.data.ProductModel
import com.cmcmarkets.android.views.viewholders.ProductViewHolder
import com.cmcmarkets.api.products.PriceTO

/**
 * This adapter helps to display the list of product details along with their prices
 */
class ProductsAdapter : RecyclerView.Adapter<ProductViewHolder>() {

    var mProductModels = ArrayList<ProductModel>()

    var mProductPrices = HashMap<Int, PriceTO>()

    var mProductItemViewedListener : ProductItemViewedListener? = null

    init {
        //Set stable ids to avoid duplication of data across rows on scroll
        setHasStableIds(true)
    }

    /**
     * This method resets the adapter data
     */
    fun clearItems() {
        this.mProductModels.clear()
        this.mProductPrices.clear()
        notifyDataSetChanged()
    }

    /***
     * This method sets the adapter data to display
     */
    fun addItems(items: ArrayList<ProductModel>) {
        this.mProductModels.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(mProductModels[position],mProductPrices[position], position, mProductItemViewedListener)
    }

    override fun getItemCount(): Int {
        return mProductModels.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }


}