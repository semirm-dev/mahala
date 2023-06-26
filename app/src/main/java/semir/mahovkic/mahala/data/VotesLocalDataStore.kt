package semir.mahovkic.mahala.data

import semir.mahovkic.mahala.data.local.dao.CandidatesDao
import javax.inject.Inject

class VotesLocalDataStore @Inject constructor(
    private val candidatesDao: CandidatesDao,
)