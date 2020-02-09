package com.cmcmarkets.android.injection.modules

import com.cmcmarkets.android.exercise.fragments.WatchListsFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentModule {
    @ContributesAndroidInjector
    internal abstract fun contributeWatchListsFragment(): WatchListsFragment

}