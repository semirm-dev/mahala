package semir.mahovkic.mahala

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import semir.mahovkic.mahala.data.VotesApi
import semir.mahovkic.mahala.data.network.MahalaService

@Module
@InstallIn(SingletonComponent::class)
class MainModule {

    @Provides
    fun providesVotesApi(): VotesApi = MahalaService()

    @Provides
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO
}