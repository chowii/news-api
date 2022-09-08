package com.example.news

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.example.news.databinding.ActivityMainBinding
import com.example.news.feature.headlines.ui.HeadlinesFragment
import com.example.news.feature.saved.ui.SavedFragment
import com.example.news.sources.ui.SourcesFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var mainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater).apply {
            setContentView(root)
        }
        mainBinding.bottomNavigationView.configureBottomNavView()
    }

    private fun BottomNavigationView.configureBottomNavView() {
        setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_headlines -> {
                    HeadlinesFragment.newInstance()
                }
                R.id.action_sources -> {
                    SourcesFragment.newInstance()
                }
                R.id.action_saved -> {
                    SavedFragment.newInstance()
                }
                else -> null
            }?.let { fragment ->
                supportFragmentManager.commit {
                    replace(R.id.fragmentContainerView, fragment)
                }
                true
            } ?: false
        }
        selectedItemId = R.id.action_sources
    }
}
