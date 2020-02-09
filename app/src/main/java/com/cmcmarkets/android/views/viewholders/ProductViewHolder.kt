package com.cmcmarkets.android.views.viewholders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cmcmarkets.android.custom.*
import com.cmcmarkets.android.custom.Constants.SYMBOL_HYPHEN
import com.cmcmarkets.android.data.ProductModel
import com.cmcmarkets.android.exercise.R
import com.cmcmarkets.api.products.PriceTO
import kotlinx.android.synthetic.main.product_card.view.*
import kotlinx.android.synthetic.main.product_item.view.*

class ProductViewHolder(v: View) : RecyclerView.ViewHolder(v) {
    var mTextViewCurrencyName: TextView
    var mTextViewSellPrice: TextView
    var mTextViewBuyPrice: TextView
    var mTextViewCountryCurrency: TextView
    var mTextViewSpread: TextView
    var mTextViewMargin: TextView

    init {
        mTextViewCurrencyName = v.name as TextView
        mTextViewSellPrice = v.sell_price as TextView
        mTextViewBuyPrice = v.buy_price as TextView
        mTextViewCountryCurrency = v.country_currency as TextView
        mTextViewSpread = v.spread as TextView
        mTextViewMargin = v.margin as TextView
    }

    fun bind(productModel: ProductModel?) {
        productModel?.apply {
            productTO?.apply {
                mTextViewCurrencyName.text = name
            }
            productDetailsTO?.apply {
                mTextViewCountryCurrency.formatCountryCurrency(this)
                mTextViewSpread.formatMargin(this)
                mTextViewMargin.formatSpread(this)
            }


        }
    }

    companion object {
        fun create(parent: ViewGroup): ProductViewHolder {
            return ProductViewHolder(
                    LayoutInflater.from(parent.context).inflate(R.layout.product_card, parent, false))
        }
    }
}