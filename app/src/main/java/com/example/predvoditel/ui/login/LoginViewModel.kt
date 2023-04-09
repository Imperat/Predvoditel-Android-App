package com.example.predvoditel.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Patterns
import com.example.predvoditel.data.LoginRepository
import com.example.predvoditel.data.Result

import com.example.predvoditel.R
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.util.logging.Logger
import kotlin.concurrent.thread

data class LoginFormState(
    val usernameError: Int? = null,
    val passwordError: Int? = null,
    val isDataValid: Boolean = false
)

data class LoggedInUserView(
    val displayName: String,
    val jwtToken: String,
    val refreshToken: String,
    val userId: String,
    //... other data fields that may be accessible to the UI
)

data class LoginResult(
    val success: LoggedInUserView? = null,
    val error: Int? = null
)

class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    fun login(username: String, password: String) {
        MainScope().launch {
            // can be launched in a separate asynchronous job
            val result = loginRepository.login(username, password)

            if (result is Result.Success) {
                _loginResult.value =
                    LoginResult(
                        success = LoggedInUserView(
                            displayName = result.data.displayName,
                            jwtToken = result.data.token,
                            refreshToken = result.data.refreshToken,
                            userId = result.data.userId,
                        )
                    )
            } else {
                _loginResult.value = LoginResult(error = R.string.login_failed)
            }
        }
    }

    fun loginDataChanged(username: String, password: String) {
        var usernameError: Int? = null
        var passwordError: Int? = null

        if (username.length <= 1) {
            usernameError = R.string.invalid_username;
        }

        if (password.length <= 1) {
            passwordError = R.string.invalid_password;
        }

        _loginForm.value = LoginFormState(
            isDataValid = usernameError == null && passwordError == null,
            usernameError = usernameError,
            passwordError = passwordError
        )
    }
}
