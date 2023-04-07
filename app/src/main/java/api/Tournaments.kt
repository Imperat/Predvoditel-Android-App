package api

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import java.util.Date
import kotlin.properties.Delegates

@Serializable
class GetTournamentsRequest()

@Serializable
data class Tournament(val _id: String, val name: String, val status: String)

@Serializable
data class CreateTournamentParams(val name: String, val teams: List<Team>)

@Serializable
data class Team(val _id: String, val name: String, val players: List<String>, val color: String)

@Serializable
data class TournamentInfo(val _id: String, val name: String)

@Serializable
data class TournamentStats(
    val tableViewQuery: TableViewQuery,
    val goalMakers: GoalMakes,
    val teamsResult: TeamsResult,
)

@Serializable
class TableViewQuery {
    lateinit var _id: String;
    lateinit var firstTeam: TableViewQuery.Team
    lateinit var secondTeam: TableViewQuery.Team

    @Serializable
    class Team {
        lateinit var _id: String;
        lateinit var goals: GoalInfo;

        @Serializable
        class GoalInfo {
            lateinit var _id: String;
            lateinit var playerId: String;
            lateinit var playerName: String;
            lateinit var teamId: String;
            lateinit var teamName: String;
            lateinit var scoredAt: String;
            var isSelfGoal by Delegates.notNull<Boolean>();
        }
    }
}

@Serializable
class GoalMakes(val playerId: String, val playerName: String, val goalsCount: String)

@Serializable
class TeamsResult(val teamId: String, val players: List<PlayerInfo>, val teamColor: String)

@Serializable
class PlayerInfo(val _id: String, val name: String)


interface TournamentsAPI {
    suspend fun getTournaments(): List<Tournament>
    suspend fun createTournament(params: CreateTournamentParams): String
    suspend fun completeTournament(tournamentId: String)
    suspend fun getTournament(tournamentId: String): TournamentInfo
    suspend fun getStats(tournamentId: String): TournamentStats
    suspend fun getLastGameId(tournamentId: String): String
}

class TournamentsClient(webClient: WebClient) : TournamentsAPI, BaseAPI(webClient) {
    override suspend fun createTournament(createTournamentParams: CreateTournamentParams): String {
        return sendPrivateRequest(
            "createTournament",
            createTournamentParams,
            String.serializer()
        )
    }

    override suspend fun getTournaments(): List<Tournament> {
        return sendPrivateRequest(
            "listTournaments",
            GetTournamentsRequest(),
            ListSerializer(Tournament.serializer())
        )
    }

    override suspend fun completeTournament(tournamentId: String) {
        return sendPrivateRequest(
            "completeTournament",
            tournamentId,
            Unit.serializer(),
        )
    }

    override suspend fun getStats(tournamentId: String): TournamentStats {
        TODO("Not yet implemented")
    }

    override suspend fun getLastGameId(tournamentId: String): String {
        return sendPrivateRequest(
            "getLastGameId",
            tournamentId,
            String.serializer(),
        )
    }

    override suspend fun getTournament(tournamentId: String): TournamentInfo {
        TODO("Not yet implemented")
    }
}
