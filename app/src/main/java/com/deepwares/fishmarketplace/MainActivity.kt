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
import com.amplifyframework.api.graphql.model.ModelMutation
import com.amplifyframework.api.graphql.model.ModelQuery
import com.amplifyframework.auth.AuthUserAttributeKey
import com.amplifyframework.core.Amplify
import com.amplifyframework.core.model.query.predicate.QueryPredicate
import com.amplifyframework.datastore.generated.model.SellerPushToken
import com.deepwares.fishmarketplace.interfaces.SpeciesSelector
import com.deepwares.fishmarketplace.model.Species
import com.deepwares.fishmarketplace.ui.creator.CreateFragmentDirections
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_main.*
import java.text.FieldPosition
import java.util.*

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

        updateToken()
    }

    override fun selectSpecies(species: Species, position: Int) {
        val action =
            CreateFragmentDirections.actionNavigationNewToNavigationAdd(position, species.name)
        navController.navigate(action)
    }

    fun updateToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // Log and toast
            Log.d(TAG, "Got new token" + token)

            token?.let {
                Amplify.API.query(
                    ModelQuery.list(
                        SellerPushToken::class.java,
                        SellerPushToken.USER_ID.eq(Amplify.Auth.currentUser.userId)
                    ), {
                        Log.d(TAG, "Result for push token : $it")
                        if (it.hasData() && !it.hasErrors()) {
                            val items = it.data.items
                            var hasToken = false
                            var id = UUID.randomUUID().toString()

                            items?.forEach {
                                Log.d(TAG, "Token : " + it)
                                hasToken = true
                                id = it.id
                            }
                            val sellerToken = SellerPushToken.Builder()
                                .userId(Amplify.Auth.currentUser.userId)
                                .token(token)
                                .id(id).build()
                            Amplify.API.mutate(
                                if (hasToken) ModelMutation.update(sellerToken) else ModelMutation.create(
                                    sellerToken
                                ), {
                                    Log.d(
                                        TAG,
                                        "Create or Update Token result | hasToken : $hasToken | result : " + it
                                    )
                                }, {
                                    Log.e(TAG, "Create or Update Token error : $it", it)
                                })
                        }
                    }, {
                        Log.e(TAG, "Error fetching push : $it", it)
                    })
            }
        })
    }
}