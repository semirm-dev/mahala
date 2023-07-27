package semir.mahovkic.mahala.data

import kotlinx.coroutines.flow.Flow
import semir.mahovkic.mahala.data.model.Group
import javax.inject.Inject

class GroupsRepository @Inject constructor(
    private val groupsRemoteDataSource: GroupsRemoteDataSource
) {
    suspend fun getGroupsStream(): Flow<List<Group>> =
        groupsRemoteDataSource.getGroupsStream()
}