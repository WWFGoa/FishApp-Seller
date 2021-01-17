package com.deepwares.fishmarketplace

import android.os.Bundle
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.amplifyframework.auth.AuthUserAttributeKey
import com.amplifyframework.core.Amplify
import com.deepwares.fishmarketplace.interfaces.SpeciesSelector
import com.deepwares.fishmarketplace.model.Species
import com.deepwares.fishmarketplace.ui.creator.CreateFragmentDirections
import kotlinx.android.synthetic.main.activity_main.*
import java.text.FieldPosition

class MainActivity : AppCompatActivity(), SpeciesSelector {
    lateinit var navController: NavController
    val TAG = MainActivity::class.java.name
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        navController = findNavController(R.id.nav_host_fragment)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_existing, R.id.navigation_new, R.id.navigation_past
            )
        )

        setSupportActionBar(toolbar)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        Amplify.Auth.fetchUserAttributes({ list ->
            val name = list.find { it.key == AuthUserAttributeKey.name() }
            Log.d(TAG, "AuthUserAttributes   : " + list)
            Log.d(TAG, "AuthUserAttributeKey Name   : " + name?.value)
        }, { e -> Log.d(TAG, "Error", e) })
    }

    override fun selectSpecies(species: Species, position: Int) {
        val action =
            CreateFragmentDirections.actionNavigationNewToNavigationAdd(position, species.name)
        navController.navigate(action)
    }
}