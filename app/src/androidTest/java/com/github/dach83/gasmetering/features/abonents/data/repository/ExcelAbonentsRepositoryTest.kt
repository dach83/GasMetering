package com.github.dach83.gasmetering.features.abonents.data.repository

import android.content.Context
import androidx.core.net.toUri
import androidx.test.core.app.ApplicationProvider
import com.github.dach83.gasmetering.R
import com.github.dach83.gasmetering.features.abonents.data.dispatchers.CoroutineDispatchers
import com.github.dach83.gasmetering.features.abonents.domain.model.Abonent
import com.github.dach83.sharedtest.models.fakeAbonents
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.File
import java.io.InputStream
import java.io.OutputStream

@OptIn(ExperimentalCoroutinesApi::class)
class ExcelAbonentsRepositoryTest {

    private val testDispatcher = StandardTestDispatcher()
    private val context = ApplicationProvider.getApplicationContext<Context>()
    private val dispatchers = CoroutineDispatchers(
        io = testDispatcher
    )

    private val abonentsFile: File = File(context.cacheDir, "abonents.xlsx")

    private var abonents: List<Abonent> = emptyList()

    @Before
    fun createAbonentsExcelFile() {
        var input: InputStream? = null
        var output: OutputStream? = null
        try {
            input = context.resources.openRawResource(R.raw.abonents)
            output = abonentsFile.outputStream()
            output.write(input.readBytes())
        } finally {
            input?.close()
            output?.close()
        }
    }

    @After
    fun deleteAbonentsExcelFile() {
        abonentsFile.delete()
    }

    @Test
    fun can_read_abonents_excel_file() = runTest(testDispatcher) {
        val sut = ExcelAbonentsRepository(context, dispatchers)

        sut.loadAbonents(abonentsFile.toUri(), ::onLoading)

        advanceUntilIdle()
        assertThat(abonents).containsExactlyElementsIn(fakeAbonents)
    }

    private fun onLoading(progress: Int, abonents: List<Abonent>) {
        this.abonents = abonents
    }
}
