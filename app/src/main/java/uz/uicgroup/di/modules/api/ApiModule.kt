package uz.uicgroup.di.modules.api

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import uz.uicgroup.data.remote.api.DictionaryApi
import uz.uicgroup.data.remote.api.SpellingApi
import uz.uicgroup.data.remote.api.TranslationApi
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @[Provides Singleton]
    fun provideTransApi(retrofit: Retrofit): TranslationApi =
        retrofit.create(TranslationApi::class.java)

    @[Provides Singleton]
    fun provideSpellingApi(retrofit: Retrofit): SpellingApi =
        retrofit.create(SpellingApi::class.java)

    @[Provides Singleton]
    fun provideDictionaryApi(retrofit: Retrofit): DictionaryApi =
        retrofit.create(DictionaryApi::class.java)
}