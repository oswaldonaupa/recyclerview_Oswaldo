package com.example.login.data

import android.app.Application
import android.content.Context
import com.example.login.Interface.LoginApi
import com.example.login.Interface.LoginService
import com.example.login.Model.respuesta
import com.example.login.Model.user
import com.example.login.data.model.LoggedInUser
import retrofit2.Response
import java.io.IOException

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {

    suspend fun login(username: String, password: String): Response<respuesta> {
        return LoginService.makeLoginService().loginWithCredentials(user(username,password))
    }

    fun logout(application: Application):Boolean {
        // TODO: revoke authentication
        val sharedPref = application.getSharedPreferences("credentials", Context.MODE_PRIVATE)
        val token = sharedPref.getString("token", null)
        if (token != null) {
            val editor = sharedPref.edit()
            editor.remove("token")
            editor.remove("user")
            editor.apply()
            return true
        }
        return false
    }
}

