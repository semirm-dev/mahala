package semir.mahovkic.mahala

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import semir.mahovkic.mahala.data.VotingApi
import semir.mahovkic.mahala.data.network.MahalaApi
import semir.mahovkic.mahala.data.network.MahalaService
import semir.mahovkic.mahala.data.network.getClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class MainModule {

    private val _api = getClient().create(MahalaApi::class.java)

    @Provides
    @Singleton
    fun providesVotesApi(): VotingApi = MahalaService(_api)

    @Provides
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO
}