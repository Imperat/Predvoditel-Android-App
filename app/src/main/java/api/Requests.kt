package api

import kotlinx.serialization.Serializable

@Serializable
data class WebSocketRequestBody<T>(val type: String, val method: String, val params: T)

@Serializable
data class WebSocketRequest<T>(var auth: String, val body: WebSocketRequestBody<T>, val id: String)
