package semir.mahovkic.mahala.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import semir.mahovkic.mahala.ui.candidate.CandidateDetailsScreen
import semir.mahovkic.mahala.ui.candidate.CandidatesScreen
import semir.mahovkic.mahala.ui.candidate.CandidatesViewModel

@Composable
fun NavGraph(
    viewModel: CandidatesViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screens.Candidates.route
    ) {
        composable(Screens.Candidates.route) {
            CandidatesScreen(navController, viewModel)
        }
        composable(Screens.CandidateDetails.route) {
            CandidateDetailsScreen(navController, viewModel)
        }
    }
}