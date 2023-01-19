package com.github.dach83.gasmetering.di

import com.github.dach83.gasmetering.features.abonents.domain.usecase.LoadAbonents
import com.github.dach83.gasmetering.features.abonents.domain.usecase.LoadAbonentsImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AbonentsModule {

    @Binds
    abstract fun bindLoadAbonents(impl: LoadAbonentsImpl): LoadAbonents
}
