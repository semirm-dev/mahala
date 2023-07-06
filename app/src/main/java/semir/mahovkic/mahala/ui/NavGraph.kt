package semir.mahovkic.mahala.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import semir.mahovkic.mahala.ui.candidate.CandidateDetailsScreen
import semir.mahovkic.mahala.ui.candidate.CandidatesScreen
import semir.mahovkic.mahala.ui.candidate.CandidatesViewModel

@Composable
fun NavGraph(
    candidatesViewModel: CandidatesViewModel = viewModel()
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screens.Candidates.route
    ) {
        composable(Screens.Candidates.route) {
            CandidatesScreen(navController, candidatesViewModel)
        }
        composable(Screens.CandidateDetails.route) {
            CandidateDetailsScreen(navController, candidatesViewModel)
        }
    }
}