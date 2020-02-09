package com.cmcmarkets.android.exercise

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.cmcmarkets.android.exercise.fragments.WatchListsFragment
import dagger.android.AndroidInjection

/**
 * This is the main entry point of the application. Here is where respective fragments are called,
 * to navigate to different screens of the app.
 */
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        //Inject this activity into Dagger
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setFragment(WatchListsFragment.newInstance())
    }

    /**
     * This method sets the main fragment
     */
    private fun setFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit()
    }
}
