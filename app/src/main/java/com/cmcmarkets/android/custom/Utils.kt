package com.cmcmarkets.android.custom

import android.widget.TextView
import com.cmcmarkets.android.custom.Constants.DECIMAL_PLACES_SPREAD
import com.cmcmarkets.android.custom.Constants.SYMBOL_HYPHEN
import com.cmcmarkets.android.custom.Constants.SYMBOL_PERCENTAGE
import com.cmcmarkets.android.exercise.R
import com.cmcmarkets.api.products.ProductDetailsTO

/**
 * This method rounds up a value to the supplied decimal places
 */
fun Double.round(decimals: Int): Double {
    var multiplier = 1.0
    repeat(decimals) { multiplier *= 10 }
    return kotlin.math.round(this * multiplier) / multiplier
}

/**
 * This method appends the country to the currency value, separated by a hyphen
 */
fun TextView.formatCountryCurrency(productDetailsTO: ProductDetailsTO) {
    this.text = StringBuilder(productDetailsTO.country)
            .append(SYMBOL_HYPHEN)
            .append(productDetailsTO.currency).toString()
}

/**
 * This method formats the margin value
 */
fun TextView.formatMargin(productDetailsTO: ProductDetailsTO) {
    this.text = StringBuilder(resources.getString(R.string.label_margin))
            .append(productDetailsTO.margin)
            .append(SYMBOL_PERCENTAGE)
}

/**
 * This method formats the spread value
 */
fun TextView.formatSpread(productDetailsTO: ProductDetailsTO) {
    this.text = StringBuilder(resources.getString(R.string.label_spread))
            .append(productDetailsTO.spread.round(DECIMAL_PLACES_SPREAD))
}
