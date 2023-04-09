package api

import android.util.Log
import kotlinx.coroutines.channels.Channel
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

open class BaseAPI(val webClient: WebClient) {
    suspend inline fun <T, reified U> sendPrivateRequest(methodName: String, request: U, responseSerializer: KSerializer<T>): T {
        val internalChannel = Channel<String>();
        webClient.channel.send(internalChannel);
        val webSocketRequest = webClient.getPrivateWsRequest(
            WebSocketResponseWrapper.serializer(responseSerializer),
            methodName,
            request,
        )

        internalChannel.send(Json.encodeToString(webSocketRequest))

        val strResponse = internalChannel.receive()
        try {
            val response = webClient.handleWsResponse(strResponse) as WebSocketResponseWrapper<T>

            if (response.response.result != null) {
                return response.response.result;
            }

            throw Error("API ERROR")
        } catch (e: OldTokensError) {
            val webSocketRequest = webClient.getPrivateWsRequest(
                WebSocketResponseWrapper.serializer(responseSerializer),
                methodName,
                request,
            )

            internalChannel.send(Json.encodeToString(webSocketRequest))

            val strResponse = internalChannel.receive()
            val response = webClient.handleWsResponse(strResponse) as WebSocketResponseWrapper<T>

            if (response.response.result != null) {
                return response.response.result;
            }

            throw Error("API ERROR")
        }
    }

    suspend fun <T, U> sendPublicRequest(methodName: String, request: U, responseSerializer: KSerializer<T>): T {
        val internalChannel = Channel<String>();
        webClient.channel.send(internalChannel);
        internalChannel.send(Json.encodeToString(webClient.getPublicWsRequest(
            WebSocketResponseWrapper.serializer(responseSerializer),
            methodName,
            request,
        )))

        val strResponse = internalChannel.receive()
        val response = webClient.handleWsResponse(strResponse) as WebSocketResponseWrapper<T>

        if (response.response.result != null) {
            return response.response.result;
        }

        throw Error("API ERROR")
    }
}
