package com.github.dach83.gasmetering.features.recentdocs.presentation

import android.content.Context
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.core.net.toUri
import androidx.test.core.app.ApplicationProvider
import org.junit.After
import org.junit.Rule
import org.junit.Test
import java.io.File

class RecentDocsScreenUiTest {

    @get:Rule
    val composeRule = createComposeRule()

    private val context: Context = ApplicationProvider.getApplicationContext()

    // folder for temporary files
    private val tempDir = TemporaryDir(context)

    private val recentDocsRepository = FakeRecentDocRepository()

    @After
    fun tearDown() {
        tempDir.remove()
    }

    @Test
    fun saved_recent_docs_is_displayed() {
        // arrange
        val firstDoc = tempDir.createFile("first.xls")
        val secondDoc = tempDir.createFile("second.xlsx")
        initRecentDocsHistory(firstDoc, secondDoc)

        // act
        composeRule.setContent {
            RecentDocsScreen()
        }

        // assert
        composeRule.onNodeWithText("first").assertExists()
        composeRule.onNodeWithText("second").assertExists()
    }

    private fun initRecentDocsHistory(vararg recentDocs: File) {
        recentDocs.forEach { file ->
            recentDocsRepository.saveDoc(file.toUri())
        }
    }
}
