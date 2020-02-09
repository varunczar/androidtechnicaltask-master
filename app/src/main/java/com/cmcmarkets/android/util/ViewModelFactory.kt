package com.cmcmarkets.android.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cmcmarkets.android.repository.Repository
import com.cmcmarkets.android.viewmodels.WatchListProductsViewModel
import javax.inject.Inject


class ViewModelFactory
@Inject constructor(private val mRepository: Repository)
    : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(WatchListProductsViewModel::class.java) -> WatchListProductsViewModel(mRepository) as T
            else -> throw IllegalArgumentException("Unknown ViewModel Class ${modelClass.name}")
        }
    }
}

