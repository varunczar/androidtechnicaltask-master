package com.cmcmarkets.android

import android.app.Activity
import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import androidx.fragment.app.Fragment
import com.cmcmarkets.android.injection.DaggerAppComponent
import com.cmcmarkets.api.internal.implementations.ConnectionMonitor
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

class CustomApp : Application(), HasActivityInjector, HasSupportFragmentInjector {

    @Inject
    lateinit var mDispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    @Inject
    lateinit var mFragmentInjector: DispatchingAndroidInjector<Fragment>

    override fun onCreate() {
        super.onCreate()
        (getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).apply {
            registerInternetChangesListener {
                ConnectionMonitor.isConnected = it
            }
        }

        //Injecting the application
        DaggerAppComponent
                .builder()
                .application(this)
                .build()
                .inject(this)
    }

    override fun activityInjector(): AndroidInjector<Activity> = mDispatchingAndroidInjector
    override fun supportFragmentInjector(): AndroidInjector<Fragment> = mFragmentInjector

}
