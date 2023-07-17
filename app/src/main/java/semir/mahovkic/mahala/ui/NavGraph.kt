package semir.mahovkic.mahala.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import semir.mahovkic.mahala.ui.candidate.CandidatesScreen
import semir.mahovkic.mahala.ui.candidate.CandidatesViewModel
import semir.mahovkic.mahala.ui.candidateDetails.CandidateDetailsScreen
import semir.mahovkic.mahala.ui.candidateDetails.CandidateDetailsViewModel

@Composable
fun NavGraph(
    candidatesViewModel: CandidatesViewModel = viewModel(),
    candidateDetailsViewModel: CandidateDetailsViewModel = viewModel()
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screens.Candidates.route
    ) {
        composable(Screens.Candidates.route) {
            CandidatesScreen(navController, candidatesViewModel)
        }
        composable(
            "${Screens.CandidateDetails.route}/{candidateId}",
            arguments = listOf(navArgument("candidateId") { type = NavType.StringType })
        ) {
            CandidateDetailsScreen(navController, it.arguments?.getString("candidateId") ?: "", candidateDetailsViewModel)
        }
    }
}