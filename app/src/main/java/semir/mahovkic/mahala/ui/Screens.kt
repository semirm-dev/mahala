package semir.mahovkic.mahala.ui

sealed class Screens(val route: String) {
    object Candidates : Screens("candidates")
    object CandidateDetails : Screens("candidate_details")
}