import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import api.TournamentStats
import com.example.predvoditel.R
import com.example.predvoditel.ui.tournament_view.BombardiersRecyclerViewAdapter

private const val TOURNAMENT_STATS = "TOURNAMENT_STATS";


class GoalMakersFragment : Fragment() {
    private lateinit var tournamentStats: TournamentStats
    private var bombardiersRecyclerView: RecyclerView? = null;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            tournamentStats = it.getParcelable(TOURNAMENT_STATS)!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =
            inflater.inflate(R.layout.fragment_completed_tournament_goal_makers, container, false)
        bombardiersRecyclerView = view.findViewById(R.id.bombardiersStats);

        if (bombardiersRecyclerView != null) {
            bombardiersRecyclerView!!.layoutManager = LinearLayoutManager(context)
            bombardiersRecyclerView?.adapter =
                BombardiersRecyclerViewAdapter(tournamentStats.goalMakers)
        }

        return view;
    }

    companion object {
        @JvmStatic
        fun newInstance(tournamentStats: TournamentStats) =
            GoalMakersFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(TOURNAMENT_STATS, tournamentStats)
                }
            }
    }
}