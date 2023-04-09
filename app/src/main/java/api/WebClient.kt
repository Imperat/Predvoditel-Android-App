package api

import android.util.Log
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import java.security.cert.X509Certificate
import javax.net.ssl.X509TrustManager
import io.ktor.client.plugins.websocket.*
import io.ktor.http.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.*
import kotlinx.serialization.json.Json
import java.util.*
import kotlin.concurrent.thread

@Serializable
data class UserLoginRequest(val username: String, val password: String)

@Serializable
data class LoginByRefreshTokenRequest(val userId: String, val refreshToken: String)

class OldTokensError : Error();

fun newWebClient(): WebClient {
    val channel = Channel<Channel<String>>();
    return WebClient(
        { UUID.randomUUID().toString() },
        mutableMapOf(),
        channel,
    ).also {
        thread {
            runBlocking {
                it.init()
            }
        }
    }
}

class WebClient(
    val idGenerator: () -> String,
    val requests: MutableMap<String, (str: String) -> WebSocketResponseWrapper<Any>>,
    val channel: Channel<Channel<String>>
) {
    lateinit var jwtToken: String;
    lateinit var refreshToken: String;
    lateinit var userId: String;
    var onSetTokensCallback: ((String, String, String) -> Unit)? = null;

    fun setTokens(jwtToken: String, refreshToken: String, userId: String) {
        this.jwtToken = jwtToken;
        this.refreshToken = refreshToken;
        this.userId = userId;
        onSetTokensCallback?.let { it(jwtToken, refreshToken, userId) }
    }

    companion object {
        private var instance: WebClient? = null
        fun getInstance(): WebClient {
            if (instance == null) {
                instance = newWebClient();
            }

            return instance!!;
        }
    }

    fun <T, U> getPublicWsRequest(
        serializer: KSerializer<U>,
        method: String,
        params: T
    ): WebSocketRequest<T> {
        val id = idGenerator()
        requests[id] = {
            Json {
                ignoreUnknownKeys
            }.decodeFromString(serializer, it) as WebSocketResponseWrapper<Any>;
        }

        return WebSocketRequest(
            auth = "",
            body = WebSocketRequestBody(
                type = "api",
                method,
                params,
            ),
            id,
        );
    }

    fun <T, U> getPrivateWsRequest(
        serializer: KSerializer<U>,
        method: String,
        params: T
    ): WebSocketRequest<T> {
        val request = getPublicWsRequest(serializer, method, params);
        request.auth = this.jwtToken;
        return request;
    }

    suspend fun handleWsResponse(result: String): WebSocketResponseWrapper<Any> {
        val responseIdHolder = Json {
            ignoreUnknownKeys = true
        }.decodeFromString(WebSocketResponseId.serializer(), result)

        val parser = requests[responseIdHolder.id]
            ?: throw java.lang.Exception("There is not handler for the message");

        requests.remove(responseIdHolder.id);
        val parsedResult = parser(result);

        if (parsedResult.response.error != null) {
            if (parsedResult.response.error.message != "jwt expired") {
                throw Error("API ERROR")
            }

            val loginByRefreshTokenResult = loginByRefreshToken(userId, refreshToken)
            setTokens(
                loginByRefreshTokenResult.token,
                loginByRefreshTokenResult.refreshToken,
                loginByRefreshTokenResult.user._id
            )

            throw OldTokensError();
        }

        return parsedResult;
    }

    private val client = HttpClient(CIO) {
        install(WebSockets)
        engine {
            https {
                trustManager = object : X509TrustManager {
                    override fun checkClientTrusted(p0: Array<out X509Certificate>?, p1: String?) {}

                    override fun checkServerTrusted(p0: Array<out X509Certificate>?, p1: String?) {}

                    override fun getAcceptedIssuers(): Array<X509Certificate>? = null
                }
            }
        }
    }

    suspend fun login(username: String, password: String): UserResponse {
        val internalChannel = Channel<String>();
        channel.send(internalChannel);
        internalChannel.send(
            Json.encodeToString(
                getPublicWsRequest(
                    WebSocketResponseWrapper.serializer<UserResponse>(
                        UserResponse.serializer()
                    ), "login", UserLoginRequest(username, password)
                )
            )
        );
        val strResponse = internalChannel.receive();
        val response = handleWsResponse(strResponse) as WebSocketResponseWrapper<UserResponse>;
        if (response.response.result == null) {
            throw Error("Not Result")
        }

        this.jwtToken = response.response.result.token;
        this.refreshToken = response.response.result.refreshToken;

        return response.response.result;
    }

    suspend fun loginByRefreshToken(userId: String, refreshToken: String): UserResponse {
        val internalChannel = Channel<String>();
        channel.send(internalChannel);
        internalChannel.send(
            Json.encodeToString(
                getPublicWsRequest(
                    WebSocketResponseWrapper.serializer(
                        UserResponse.serializer()
                    ), "loginByRefreshToken", LoginByRefreshTokenRequest(userId, refreshToken)
                )
            )
        );
        val strResponse = internalChannel.receive();
        val response = handleWsResponse(strResponse) as WebSocketResponseWrapper<UserResponse>;
        if (response.response.result == null) {
            throw Error("Not Result")
        }

        this.jwtToken = response.response.result.token;
        this.refreshToken = response.response.result.refreshToken;

        return response.response.result;
    }

    suspend fun init() {
        client.webSocket(
            method = HttpMethod.Get,
            host = "predvoditel.com",
            port = 80,
            path = "/ws"
        ) {
            while (true) {
                val internalChannel = channel.receive();
                val request = internalChannel.receive();
                Log.i("API ->", request)
                send(request)
                val othersMessage = incoming.receive() as? Frame.Text
                val str = othersMessage?.readText()

                if (str != null) {
                    Log.i("API <- ", str)
                    internalChannel.send((str));
                }
            }
        }
    }
}
