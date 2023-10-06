package uz.gita.foodmn.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uz.gita.foodmn.domain.AppRepastory
import uz.gita.foodmn.domain.AppRepastoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepastoryModule {

    @[Binds Singleton]
    fun bindRepastory(impl: AppRepastoryImpl): AppRepastory
}