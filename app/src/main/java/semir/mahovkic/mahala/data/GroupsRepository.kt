package semir.mahovkic.mahala.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import semir.mahovkic.mahala.data.model.Group
import javax.inject.Inject

class GroupsRepository @Inject constructor(
    private val groupsRemoteDataSource: GroupsRemoteDataSource
) {
    private val _groups = mutableListOf<Group>()

    suspend fun getGroupsStream(): Flow<List<Group>> {
        val groups = groupsRemoteDataSource.getGroupsStream()
        _groups.clear()
        _groups.addAll(groups)
        return MutableStateFlow(_groups)
    }
}