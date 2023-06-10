package semir.mahovkic.mahala

import androidx.compose.runtime.Composable
import semir.mahovkic.mahala.data.CandidatesRepository
import semir.mahovkic.mahala.data.room.CandidatesDao
import semir.mahovkic.mahala.ui.candidate.CandidatesScreen
import semir.mahovkic.mahala.ui.candidate.CandidatesViewModel

@Composable
fun MahalaApp() {
    CandidatesScreen(viewModel = CandidatesViewModel(CandidatesRepository(CandidatesDao())))
}