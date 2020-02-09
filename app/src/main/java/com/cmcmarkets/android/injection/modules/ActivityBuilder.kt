package com.cmcmarkets.android.injection.modules

import com.cmcmarkets.android.exercise.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilder {

    @ContributesAndroidInjector()
    abstract fun bindActivityMain(): MainActivity
}