package com.github.dach83.gasmetering.features.recentdocs.presentation

import android.net.Uri

class FakeRecentDocRepository {

    private val uris = mutableListOf<Uri>()

    fun saveDoc(uri: Uri) {
        uris.add(uri)
    }
}
