package semir.mahovkic.mahala.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import semir.mahovkic.mahala.data.model.Party
import javax.inject.Inject

class PartiesRepository @Inject constructor(
    private val partiesRemoteDataSource: PartiesRemoteDataSource
) {
    private val _parties = mutableListOf<Party>()

    suspend fun getPartiesStream(): Flow<List<Party>> {
        val parties = partiesRemoteDataSource.getPartiesStream()
        _parties.clear()
        _parties.addAll(parties)
        return MutableStateFlow(_parties)
    }
}

