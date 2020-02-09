package com.cmcmarkets.android.data

import com.cmcmarkets.api.products.ProductDetailsTO
import com.cmcmarkets.api.products.ProductTO

data class ProductModel(var productId: Long = 0,
                        var productTO: ProductTO? = null,
                        var productDetailsTO: ProductDetailsTO? = null
)