package com.cmcmarkets.android.custom

import android.widget.TextView
import com.cmcmarkets.android.custom.Constants.SYMBOL_HYPHEN
import com.cmcmarkets.android.custom.Constants.SYMBOL_PERCENTAGE
import com.cmcmarkets.android.exercise.R
import com.cmcmarkets.api.products.ProductDetailsTO
import java.lang.StringBuilder

fun Double.round(decimals: Int): Double {
    var multiplier = 1.0
    repeat(decimals) { multiplier *= 10 }
    return kotlin.math.round(this * multiplier) / multiplier
}

fun TextView.formatCountryCurrency(productDetailsTO: ProductDetailsTO) {
    this.text = StringBuilder(productDetailsTO.country)
            .append(SYMBOL_HYPHEN)
            .append(productDetailsTO.currency).toString()
}

fun TextView.formatMargin(productDetailsTO: ProductDetailsTO) {
    this.text = StringBuilder(resources.getString(R.string.label_margin))
            .append(productDetailsTO.margin)
            .append(SYMBOL_PERCENTAGE)
}

fun TextView.formatSpread(productDetailsTO: ProductDetailsTO) {
    this.text = StringBuilder(resources.getString(R.string.label_spread))
            .append(productDetailsTO.spread.round(2))
}
