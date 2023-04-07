package api

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer

@Serializable
class GetTournamentsRequest()

@Serializable
data class Tournament(val _id: String, val name: String, val status: String)

interface TournamentsAPI {
    suspend fun getTournaments(): List<Tournament>
}

class TournamentsClient(webClient: WebClient) : TournamentsAPI, BaseAPI(webClient) {
    override suspend fun getTournaments(): List<Tournament> {
        return sendPrivateRequest(
            "listTournaments",
            GetTournamentsRequest(),
            ListSerializer(Tournament.serializer())
        )
    }
}
