package com.cmcmarkets.android.custom

import com.cmcmarkets.android.data.ProductModel

interface ProductItemViewedListener {
    fun onViewed(position : Int, productId: Long)
}