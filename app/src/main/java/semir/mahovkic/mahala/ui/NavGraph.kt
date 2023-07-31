package semir.mahovkic.mahala.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import semir.mahovkic.mahala.ui.candidates.CandidatesScreen
import semir.mahovkic.mahala.ui.candidates.CandidatesViewModel
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
            "${Screens.CandidateDetails.route}/{candidateId}/{groupId}",
            arguments = listOf(
                navArgument("candidateId") { type = NavType.StringType },
                navArgument("groupId") { type = NavType.IntType },
            )
        ) {
            CandidateDetailsScreen(
                navController,
                it.arguments?.getString("candidateId") ?: "",
                it.arguments?.getInt("groupId") ?: 0,
                candidateDetailsViewModel
            )
        }
    }
}