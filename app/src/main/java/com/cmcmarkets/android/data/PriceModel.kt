package com.cmcmarkets.android.data

import com.cmcmarkets.api.products.PriceTO
import com.cmcmarkets.api.products.ProductDetailsTO
import com.cmcmarkets.api.products.ProductTO

data class PriceModel(var position : Int = 0,
                      var productId : Long = 0,
                      var priceTO: PriceTO ? = null
)