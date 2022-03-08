package app.crypto.di

import android.app.Application
import androidx.room.Room
import app.crypto.BuildConfig
import app.crypto.data.*
import app.crypto.model.AssetsListUseCase
import app.crypto.model.AssetsListUseCaseImpl
import app.crypto.network.NetworkInterceptor
import app.crypto.utils.CoroutineDispatchers
import app.crypto.utils.DefaultDispatchers
import com.squareup.moshi.FromJson
import com.squareup.moshi.Moshi
import com.squareup.moshi.ToJson
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.math.BigDecimal
import javax.inject.Singleton

@Module(includes = [AppSingletonBindModule::class])
@InstallIn(SingletonComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun provideDispatchers() : CoroutineDispatchers {
        return DefaultDispatchers
    }

    @Singleton
    @Provides
    fun provideApi(retrofit: Retrofit): CryptoAPI {
        return retrofit.create(CryptoAPI::class.java)
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient
            .Builder()
            .addInterceptor(NetworkInterceptor())
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        moshiConverterFactory: MoshiConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BuildConfig.API_URL)
            .addConverterFactory(moshiConverterFactory)
            .build()
    }

    @Singleton
    @Provides
    fun provideMoshiConverterFactory(moshi: Moshi): MoshiConverterFactory {
        return MoshiConverterFactory.create(moshi)
    }

    @Singleton
    @Provides
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .add(BigDecimalAdapter())
            .build()
    }

    @Provides
    @Singleton
    fun provideDatabase(app: Application): CryptoDatabase {
        return Room.databaseBuilder(
            app,
            CryptoDatabase::class.java,
            CryptoDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    fun provideDao(db: CryptoDatabase): CryptoDao {
        return db.cryptoDao
    }

    class BigDecimalAdapter {
        @FromJson
        fun fromJson(string: String) = BigDecimal(string)

        @ToJson
        fun toJson(value: BigDecimal) = value.toString()
    }
}

@ExperimentalCoroutinesApi
@Module
@InstallIn(SingletonComponent::class)
abstract class AppSingletonBindModule {

    @Binds
    abstract fun provideAssetsListUseCase(
        useCase: AssetsListUseCaseImpl
    ) : AssetsListUseCase

    @Binds
    abstract fun provideRepository(
        repository: RepositoryImpl
    ) : Repository

}
