package semir.mahovkic.mahala.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import semir.mahovkic.mahala.data.model.Group
import javax.inject.Inject

class GroupsRemoteDataSource @Inject constructor(
    private val api: VotingApi,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    suspend fun getGroupsStream(): List<Group> =
        withContext(ioDispatcher) {
            api.getGroups()
        }
}