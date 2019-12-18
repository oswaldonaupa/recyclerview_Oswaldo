package com.example.login.ui.login

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Patterns
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.viewModelScope
import com.example.login.Interface.LoginApi
import com.example.login.Model.respuesta
import com.example.login.data.LoginRepository
import com.example.login.data.Result

import com.example.login.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

class LoginViewModel(private val loginRepository: LoginRepository,
                     private val application: Application
) : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    @RequiresApi(Build.VERSION_CODES.M)
    fun login(username: String, password: String) {
        // can be launched in a separate asynchronous job
        viewModelScope.launch {
            val result = withContext(Dispatchers.Default){

                loginRepository.login(username,password)
            }
                    if (result is Result.Success) {
                        _loginResult.value =
                            LoginResult(success = LoggedInUserView(displayName = result.data.displayName))

                        val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
                        val keyGenParameterSpec = KeyGenParameterSpec.Builder("MyKeyAlias",
                            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
                            .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                            .build()

                        keyGenerator.init(keyGenParameterSpec)
                        keyGenerator.generateKey()

                        val pair = encryptData(result.data.token)
                        val pair2 = encryptData(result.data.displayName)

                        val decryptedData = decryptData(pair.first, pair.second)

                        val encrypted = pair.second.toString(Charsets.UTF_8)
                        val encrypted2 = pair2.second.toString(Charsets.UTF_8)
                        val decryptedData2 = decryptData(pair2.first, pair2.second)
                        println("Encrypted data: $encrypted")
                        println("Decrypted data: $decryptedData")
                        println("Encrypted data: $encrypted2")
                        println("Decrypted data: $decryptedData2")




                        saveOauthAccessToken(encrypted, encrypted2)
                    } else {
                        _loginResult.value = LoginResult(error = R.string.login_failed)
                    }
                }
    }

    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value = LoginFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
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
        return password.length > 5
    }
    //Custom method to save Token
    private fun saveOauthAccessToken(token:String, user:String){
        val sharedPref: SharedPreferences = application.getSharedPreferences("credentials",
            Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString("token",token)
        editor.putString("user",user)
        editor.apply()
    }

    fun getKey(): SecretKey {
        val keystore = KeyStore.getInstance("AndroidKeyStore")
        keystore.load(null)

        val secretKeyEntry = keystore.getEntry("MyKeyAlias", null) as KeyStore.SecretKeyEntry
        return secretKeyEntry.secretKey
    }

    fun encryptData(data: String): Pair<ByteArray, ByteArray> {
        val cipher = Cipher.getInstance("AES/CBC/NoPadding")

        var temp = data
        while (temp.toByteArray().size % 16 != 0)
            temp += "\u0020"

        cipher.init(Cipher.ENCRYPT_MODE, getKey())

        val ivBytes = cipher.iv
        val encryptedBytes = cipher.doFinal(temp.toByteArray(Charsets.UTF_8))

        return Pair(ivBytes, encryptedBytes)
    }

    fun decryptData(ivBytes: ByteArray, data: ByteArray): String{
        val cipher = Cipher.getInstance("AES/CBC/NoPadding")
        val spec = IvParameterSpec(ivBytes)

        cipher.init(Cipher.DECRYPT_MODE, getKey(), spec)
        return cipher.doFinal(data).toString(Charsets.UTF_8).trim()
    }
}
