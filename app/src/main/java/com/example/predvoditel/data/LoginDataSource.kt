package com.example.predvoditel.data

import api.WebClient
import com.example.predvoditel.data.model.LoggedInUser
import kotlinx.coroutines.runBlocking
import java.io.IOException

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {
    private val webClient = WebClient.getInstance();

    fun login(username: String, password: String): Result<LoggedInUser> {
        try {
            // TODO: handle loggedInUser authentication
            val fakeUser: LoggedInUser;
            runBlocking {
                val responseUser = webClient.login(username, password);
                fakeUser = LoggedInUser(
                    responseUser.user._id,
                    responseUser.user.name,
                    responseUser.token,
                    responseUser.refreshToken,
                );
            }
            return Result.Success(fakeUser)
        } catch (e: Throwable) {
            return Result.Error(IOException("Error logging in", e))
        }
    }

    fun logout() {
        // TODO: revoke authentication
    }
}