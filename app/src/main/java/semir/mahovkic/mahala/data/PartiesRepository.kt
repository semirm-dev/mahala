package semir.mahovkic.mahala.data

import kotlinx.coroutines.flow.Flow
import semir.mahovkic.mahala.data.model.Party
import javax.inject.Inject

class PartiesRepository @Inject constructor(
    private val partiesRemoteDataSource: PartiesRemoteDataSource
) {
    suspend fun getPartiesStream(): Flow<List<Party>> =
        partiesRemoteDataSource.getPartiesStream()
}

