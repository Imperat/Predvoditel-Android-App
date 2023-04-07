package api

import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer

@Serializable
class GetPlayersRequest()

@Serializable
data class Player(
    val _id: String,
    val name: String,
    val workspaceId: String,
    val createdBy: String,
    val createdAt: String
)

interface PlayersAPI {
    suspend fun getPlayers(): List<Player>
}

class PlayersClient(webClient: WebClient) : PlayersAPI, BaseAPI(webClient) {
    override suspend fun getPlayers(): List<Player> {
        return sendPrivateRequest(
            "getPlayers",
            GetPlayersRequest(),
            ListSerializer(Player.serializer()),
        )
    }
}
