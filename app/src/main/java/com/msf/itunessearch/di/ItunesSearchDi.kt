package com.msf.itunessearch.di

import com.msf.itunessearch.BuildConfig
import com.msf.itunessearch.core.CoroutinesContextProvider
import com.msf.itunessearch.core.RequestWrapper
import com.msf.itunessearch.core.RequestWrapperImpl
import com.msf.itunessearch.network.ItunesService
import com.msf.itunessearch.network.LoggingInterceptor
import com.msf.itunessearch.repository.ItunesSearchRepository
import com.msf.itunessearch.repository.impl.ItunesSearchRepositoryImpl
import com.msf.itunessearch.usecase.ItunesSearchUseCase
import com.msf.itunessearch.viewmodel.ItunesSearchViewModel
import okhttp3.OkHttpClient
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ItunesSearchDi {
    val module = module {
        single {
            CoroutinesContextProvider()
        }

        factory<RequestWrapper> { RequestWrapperImpl() }
        factory { LoggingInterceptor() }
        factory { provideOkHttpClient(interceptor = get()) }
        factory { providerServiceApi(retrofit = get()) }
        factory { provideRetrofit(okHttpClient = get()) }
        factory {
            ItunesSearchUseCase(
                repository = get(),
                contextProvider = get(),
                requestWrapper = get()
            )
        }
        factory<ItunesSearchRepository> { ItunesSearchRepositoryImpl(itunesService = get()) }
        viewModel {
            ItunesSearchViewModel(
                useCase = get()
            )
        }
    }
}

fun provideOkHttpClient(interceptor: LoggingInterceptor): OkHttpClient {
    return OkHttpClient().newBuilder().addInterceptor(interceptor).build()
}

fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder().baseUrl(BuildConfig.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}

fun providerServiceApi(retrofit: Retrofit): ItunesService = retrofit.create(ItunesService::class.java)
