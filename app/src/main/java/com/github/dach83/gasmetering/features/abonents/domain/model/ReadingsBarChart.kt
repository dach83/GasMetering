package com.github.dach83.gasmetering.features.abonents.domain.model

import kotlin.math.abs

class ReadingsBarChart(private val readings: Readings) {

    /**
     * Builds a list of spent gas volumes for the last [depth] count of meter reading.
     * The volumes are normalized to range [-1, 1].
     */
    fun normalizeVolumes(depth: Int): List<Pair<ReadingsDate, Float>> {
        val volumes = mutableListOf<Pair<ReadingsDate, Double>>()
        for (i in readings.size - 1 downTo readings.size - depth) {
            if (i < 1) break
            val date = readings.keys.elementAt(i)
            val curr = readings.values.elementAt(i)
            val prev = readings.values.elementAt(i - 1)
            volumes.add(date to curr - prev)
        }

        val max = volumes.maxOfOrNull { abs(it.second) } ?: 1.0
        return volumes.map {
            it.first to (it.second / max).toFloat()
        }.reversed()
    }
}
