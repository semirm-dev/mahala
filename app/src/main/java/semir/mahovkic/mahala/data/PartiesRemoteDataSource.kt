package semir.mahovkic.mahala.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import semir.mahovkic.mahala.data.model.Party
import javax.inject.Inject

class PartiesRemoteDataSource @Inject constructor(
    private val api: VotingApi,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    suspend fun getPartiesStream(): List<Party> =
        withContext(ioDispatcher) {
            api.getParties()
        }
}