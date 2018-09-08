package de.reiss.edizioni.architecture.di

import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import de.reiss.edizioni.architecture.EdizioniCEMDateJsonAdapter
import de.reiss.edizioni.downloader.BASE_URL
import de.reiss.edizioni.downloader.list.EdizioniJsonService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.*

@Module(
        includes = [
            OkHttpModule::class
        ]
)
open class RetrofitModule {

    @Provides
    @ApplicationScope
    open fun moshi(): Moshi = Moshi.Builder()
            .add(Date::class.java, EdizioniCEMDateJsonAdapter())
            .build()

    @Provides
    @ApplicationScope
    open fun retrofit(okHttpClient: OkHttpClient, moshi: Moshi): Retrofit = Retrofit.Builder().baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

    @Provides
    @ApplicationScope
    open fun twdService(okHttpClient: OkHttpClient, retrofit: Retrofit): EdizioniJsonService =
            retrofit.create(EdizioniJsonService::class.java)

}