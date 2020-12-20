package com.deepwares.fishmarketplace.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Patterns
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobile.client.Callback
import com.amazonaws.mobile.client.results.SignInResult
import com.amazonaws.mobile.client.results.SignUpResult
import com.amazonaws.services.cognitoidentityprovider.model.InvalidPasswordException
import com.amazonaws.services.cognitoidentityprovider.model.UserNotFoundException
import com.amplifyframework.auth.AuthException
import com.amplifyframework.auth.AuthUserAttribute
import com.amplifyframework.auth.AuthUserAttributeKey
import com.amplifyframework.auth.options.AuthSignUpOptions
import com.amplifyframework.auth.result.AuthSignUpResult
import com.amplifyframework.core.Amplify
import com.deepwares.fishmarketplace.App
import com.deepwares.fishmarketplace.R

import com.deepwares.fishmarketplace.model.LoginError
import com.deepwares.fishmarketplace.repository.LoginRepository
import com.deepwares.fishmarketplaceconsumer.repository.Preferences
import java.lang.Exception

class LoginViewModel() : ViewModel() {

    val TAG = LoginViewModel::class.java.simpleName
    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm
    val loginForm = MutableLiveData<Boolean>(true)

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    fun login(username: String, password: String) {
        // can be launched in a separate asynchronous job

        //val result = loginRepository.login(username, password)
        /*
        if (result is Result.Success) {
            _loginResult.value =
                LoginResult(success = LoggedInUserView(displayName = result.data.displayName))
        } else {
            _loginResult.value = LoginResult(error = R.string.login_failed)
        }

         */
        //AWSMobileClient.getInstance().
        AWSMobileClient.getInstance()
            .signIn(username, password, HashMap(), object : Callback<SignInResult> {
                override fun onResult(result: SignInResult?) {
                    Log.d(TAG, "LOGIN - onResult :  $result | Fetching user attributes")
                    val map = AWSMobileClient.getInstance().userAttributes
                    val id = map["sub"]
                    Preferences.setUserId(App.INSTANCE, id)
                    Log.d(
                        TAG,
                        "user attributes | name :   ${map["name"]} | username :   ${map["username"]} | email :   ${map["email"]}"
                    )
                    //result?.
                    _loginResult.postValue(LoginResult(success = LoggedInUserView(displayName = map["name"]!!)))

                }

                override fun onError(e: Exception?) {

                    Log.e(TAG, "LOGIN - onError | Could not login successfully", e)
                    var loginError = LoginError.Unknown
                    e?.let {
                        if (e is UserNotFoundException) {
                            loginError = LoginError.UserNotFound
                            loginForm.postValue(false)
                        } else if (e is InvalidPasswordException) {
                            loginError = LoginError.PasswordMismatch
                            _loginForm.value =
                                LoginFormState(passwordError = R.string.invalid_password)
                        }
                    }
                    _loginResult.postValue(
                        LoginResult(
                            error = R.string.login_failed,
                            errorType = loginError
                        )
                    )
                }
            })

    }


    fun loginDataChanged(
        phone: String,
        password: String,
        confirmPassword: String,
        name: String

    ) {

        if (loginForm.value!!) {
            if (phone.isNullOrEmpty() || phone.length != 10) {
                _loginForm.value = LoginFormState(usernameError = R.string.invalid_username)
            } else if (!isPasswordValid(password)) {
                _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
            } else {
                _loginForm.value = LoginFormState(isDataValid = true)
            }
        } else {
            if (phone.isNullOrEmpty() || phone.length != 10) {
                _loginForm.value =
                    LoginFormState(phoneError = R.string.invalid_phone)
            } else if (!isPasswordValid(password)) {
                _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
            } else if (!isPasswordValidSignup(password, confirmPassword)) {
                _loginForm.value =
                    LoginFormState(confirmPasswordError = R.string.confirm_mismatch_password)
            } else {
                _loginForm.value = LoginFormState(isDataValid = true)
            }
        }
    }


    fun register(phone: String, password: String, name: String) {
        val username = "+91" + phone
        val map = HashMap<String, String>()
        map.put("name", name)
        map.put("phone_number", username)
        map.put("email", "deepakindia86@gmail.com")
        val attributes: ArrayList<AuthUserAttribute> = ArrayList()
        attributes.add(AuthUserAttribute(AuthUserAttributeKey.name(), name))
        attributes.add(
            AuthUserAttribute(AuthUserAttributeKey.phoneNumber(), username)
        )


        AWSMobileClient.getInstance()
            .signUp(username, password, map, null, object : Callback<SignUpResult> {
                override fun onResult(result: SignUpResult?) {
                    Log.d(TAG, "REGISTER - onResult :  $result | Fetching user attributes")

                    result?.userSub?.let {
                        login(username, password)
                        Preferences.setUserId(App.INSTANCE, it)
                    }
                    /*
                    val map = AWSMobileClient.getInstance().userAttributes


                    Log.d(
                        TAG,
                        "user attributes | name :   ${map["name"]} | username :   ${map["username"]} | email :   ${map["email"]}"
                    )
                    //result?.
                    _loginResult.postValue(LoginResult(success = LoggedInUserView(displayName = map["name"]!!)))
                    */
                }

                override fun onError(e: Exception?) {

                    Log.e(TAG, "REGISTER - onError | Could not login successfully", e)
                    var loginError = LoginError.Unknown
                    var errorToast = R.string.register_failed
                    e?.let {
                        if (e is UserNotFoundException) {
                            loginError = LoginError.UserNotFound
                            errorToast = R.string.user_does_not_exist
                            loginForm.postValue(false)
                        } else if (e is InvalidPasswordException) {
                            loginError = LoginError.PasswordMismatch
                            _loginForm.value =
                                LoginFormState(passwordError = R.string.invalid_password)
                        }
                    }
                    _loginResult.postValue(
                        LoginResult(
                            error = errorToast,
                            errorType = loginError
                        )
                    )
                }
            })

    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 7
    }

    private fun isPasswordValidSignup(password: String, confirmPassword: String): Boolean {
        return isPasswordValid(password) && password.equals(confirmPassword)
    }
}
