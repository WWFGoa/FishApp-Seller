package com.deepwares.fishmarketplace

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        // setSupportActionBar(toolbar)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_existing, R.id.navigation_new, R.id.navigation_past
            )
        )

        setSupportActionBar(toolbar)
        setupActionBarWithNavController(navController, appBarConfiguration)

        navView.setupWithNavController(navController)
       // bottomNav.setupWithNavController(navController)


/*
        NavigationUI.setupWithNavController(
            toolbar, navController,
            // appBarConfiguration
            AppBarConfiguration.Builder(
                setOf(
                    R.id.navigation_existing, R.id.navigation_new, R.id.navigation_past
                )
            ).build()
        )

 */
       // NavigationUI.setupWithNavController(nav_view, navController)
        //setupActionBarWithNavController(toolbar, navController, appBarConfiguration)
        // navView.setupWithNavController(navController)
    }
}