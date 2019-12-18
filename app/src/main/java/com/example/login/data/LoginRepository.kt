package com.example.login.data

import android.annotation.TargetApi
import android.app.Application
import android.content.Context
import android.os.Build
import android.security.KeyPairGeneratorSpec
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.login.data.model.LoggedInUser
import java.io.IOException
import java.math.BigInteger
import java.security.GeneralSecurityException
import java.security.Key
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.util.*
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.security.auth.x500.X500Principal

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class LoginRepository(val dataSource: LoginDataSource) {

    // in-memory cache of the loggedInUser object
    var user: LoggedInUser? = null
        private set

    val isLoggedIn: Boolean
        get() = user != null

    init {
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
        user = null
    }

    fun logout(application: Application): Result<String> {
        user = null
        if (dataSource.logout(application)){
            return Result.Success("Logout")
        }
        return Result.Error(IOException())
    }

    suspend fun login(username: String, password: String): Result<LoggedInUser> {
        // handle login
        val response = dataSource.login(username,password)
        if (response.isSuccessful) {
            Log.d("cuack","response: ${response.body()!!}")
            val result = response.body()!!
            val userLogged  = LoggedInUser(java.util.UUID.randomUUID().toString(),
                "UserTest", result.token)
            System.out.println(result.token)

            setLoggedInUser(userLogged)




            return Result.Success(userLogged)
        }

        return Result.Error(IOException("Error logging in"))
    }





    private fun setLoggedInUser(loggedInUser: LoggedInUser) {
        this.user = loggedInUser
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }
}
