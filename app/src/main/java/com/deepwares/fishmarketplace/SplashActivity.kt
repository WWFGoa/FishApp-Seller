package com.deepwares.fishmarketplace

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobile.client.Callback
import com.amazonaws.mobile.client.UserState
import com.amazonaws.mobile.client.UserStateDetails
import com.amplifyframework.core.Amplify
import com.deepwares.fishmarketplace.ui.login.LoginActivity
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {
    val TAG = SplashActivity::class.java.name
    val handler = Handler(Looper.getMainLooper())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        logo.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        checkLogin()


    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacksAndMessages(null)
    }


    fun checkLogin() {


        AWSMobileClient.getInstance()
            .initialize(applicationContext, object : Callback<UserStateDetails> {
                override fun onResult(userStateDetails: UserStateDetails) {
                    if (!AWSMobileClient.getInstance().isSignedIn) {
                        AWSMobileClient.getInstance().signOut()
                        showLogin()
                        return
                    }
                    when (userStateDetails.userState) {
                        UserState.SIGNED_IN -> runOnUiThread {
                            Log.i(TAG, "ApiQuickstart | All set and ready to go!")
                            //initReports()
                            goToMain()
                            return@runOnUiThread
                        }
                        UserState.SIGNED_OUT -> runOnUiThread {
                            showLogin()
                            return@runOnUiThread
                        }
                        else -> {
                            AWSMobileClient.getInstance().signOut()
                            showLogin()
                            return
                        }
                    }
                }

                override fun onError(e: Exception) {
                    Log.e(TAG, e.toString())
                }
            })

    }

    private fun goToMain() {

        Log.d(TAG, "User is already logged in. Going to MainNavActivity")
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun showLogin() {
        Log.d(TAG, "User is already logged in. Going to MainNavActivity")
        handler.postDelayed({
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000)


    }
}