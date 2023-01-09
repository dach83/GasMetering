package com.github.dach83.gasmetering.features.recentdocs.presentation

import android.content.Context
import java.io.File

class TemporaryDir(context: Context) {

    private val root = File(context.dataDir, "test").also {
        it.mkdir()
    }

    fun createFile(fileName: String): File {
        val file = File(root, fileName)
        file.createNewFile()
        return file
    }

    fun remove() {
        root.delete()
    }
}
