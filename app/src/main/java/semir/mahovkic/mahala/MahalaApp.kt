package semir.mahovkic.mahala

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import semir.mahovkic.mahala.ui.candidate.CandidatesScreen

@Composable
fun MahalaApp() {
    CandidatesScreen(viewModel = viewModel())
}