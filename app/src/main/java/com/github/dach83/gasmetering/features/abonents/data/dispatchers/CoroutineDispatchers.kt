package com.github.dach83.gasmetering.features.abonents.data.dispatchers

import kotlinx.coroutines.CoroutineDispatcher

data class CoroutineDispatchers(
    val main: CoroutineDispatcher,
    val io: CoroutineDispatcher
)
