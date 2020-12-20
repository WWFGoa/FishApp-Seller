package com.deepwares.fishmarketplace.ui.login

import com.deepwares.fishmarketplace.model.LoginError
import com.deepwares.fishmarketplace.ui.login.LoggedInUserView

/**
 * Authentication result : success (user details) or error message.
 */
data class LoginResult(
    val success: LoggedInUserView? = null,
    val error: Int? = null,
    val errorType: LoginError? = null
)
