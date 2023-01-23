package com.github.dach83.gasmetering.di

import android.content.Context
import com.github.dach83.gasmetering.features.abonents.data.repository.ExcelAbonentsRepository
import com.github.dach83.gasmetering.features.abonents.domain.repository.AbonentsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AbonentsModule {

    @Singleton
    @Provides
    fun provideAbonentsRepository(@ApplicationContext context: Context): AbonentsRepository {
        return ExcelAbonentsRepository(context)
    }
}
