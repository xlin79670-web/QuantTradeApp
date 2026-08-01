package com.quanttrade.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.quanttrade.app.ui.dashboard.DashboardFragment
import com.quanttrade.app.ui.accounts.AccountsFragment
import com.quanttrade.app.ui.strategy.StrategyFragment
import com.quanttrade.app.ui.settings.SettingsFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        // 设置底部导航
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_dashboard -> {
                    loadFragment(DashboardFragment())
                    true
                }
                R.id.navigation_accounts -> {
                    loadFragment(AccountsFragment())
                    true
                }
                R.id.navigation_strategy -> {
                    loadFragment(StrategyFragment())
                    true
                }
                R.id.navigation_settings -> {
                    loadFragment(SettingsFragment())
                    true
                }
                else -> false
            }
        }
        
        // 默认加载仪表盘
        if (savedInstanceState == null) {
            loadFragment(DashboardFragment())
        }
    }
    
    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.nav_host_fragment, fragment)
            .commit()
    }
}