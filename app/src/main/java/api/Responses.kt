package api

import kotlinx.serialization.Serializable

// {"response":{"result":{"token":"eyJhbGc","user":{"_id":"611fb0fc060d643c083338b0","name":"Imperat","avatar":" ","workspaceId":"sokol"}}},"id":"0.89587461179677"}

@Serializable
data class PredviditelError(val name: String, val message: String)

@Serializable
data class WebSocketResponse<T>(val result: T? = null, val error: PredviditelError? = null)

@Serializable
data class WebSocketResponseWrapper<T>(val response: WebSocketResponse<T>, val id: String)

@Serializable
data class User(val _id: String, val name: String, val avatar: String, val workspaceId: String)

@Serializable
data class UserResponse(val token: String, val refreshToken: String, val user: User)

//@Serializable
//data class UserLoginResponse(val token: String, val user: User, val id: String)

@Serializable
data class WebSocketResponseId(val id: String)
