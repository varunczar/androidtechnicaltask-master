package com.cmcmarkets.android.injection

import com.cmcmarkets.android.CustomApp
import com.cmcmarkets.android.injection.modules.ActivityBuilder
import com.cmcmarkets.android.injection.modules.ApplicationModule
import com.cmcmarkets.android.injection.modules.FragmentModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
        modules = [
            (AndroidSupportInjectionModule::class),
            (ApplicationModule::class),
            (ActivityBuilder::class),
            (FragmentModule::class)]
)

interface AppComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: CustomApp): Builder

        fun build(): AppComponent
    }

    fun inject(application: CustomApp)
}