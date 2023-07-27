package semir.mahovkic.mahala.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext
import semir.mahovkic.mahala.data.model.Party
import javax.inject.Inject

class PartiesRemoteDataSource @Inject constructor(
    private val api: VotingApi,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    private val _parties = mutableListOf<Party>()

    suspend fun getPartiesStream(): Flow<List<Party>> =
        withContext(ioDispatcher) {
            val resp = api.getParties()
            _parties.clear()
            _parties.addAll(resp)
            MutableStateFlow(_parties)
        }
}