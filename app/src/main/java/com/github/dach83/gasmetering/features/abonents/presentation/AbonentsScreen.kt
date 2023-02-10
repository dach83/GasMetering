package com.github.dach83.gasmetering.features.abonents.presentation

import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.dach83.gasmetering.core.presentation.navigation.NavigationTransitions
import com.github.dach83.gasmetering.features.abonents.domain.model.*
import com.github.dach83.gasmetering.features.abonents.presentation.components.*
import com.github.dach83.gasmetering.features.destinations.SortOrderBottomSheetDestination
import com.github.dach83.gasmetering.features.destinations.TakeReadingsScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.result.NavResult
import com.ramcosta.composedestinations.result.ResultRecipient
import kotlin.math.roundToInt

@RootNavGraph(start = true)
@Destination(style = NavigationTransitions::class)
@Composable
fun AbonentsScreen(
    viewModel: AbonentsViewModel = hiltViewModel(),
    navigator: DestinationsNavigator,
    resultRecipient: ResultRecipient<SortOrderBottomSheetDestination, AbonentSortOrder>,
) {
    val uiState = viewModel.uiState
    val filter by viewModel.filter.collectAsState()
    val abonents by viewModel.filteredAbonents.collectAsState(initial = emptyList())

    // handle changing sort order from bottom sheet
    resultRecipient.onNavResult { result ->
        if (result is NavResult.Value) {
            viewModel.changeSortOrder(newSortOrder = result.value)
        }
    }

    // open excel document launcher
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = viewModel::loadExcelFile,
    )

    // collapsing toolbar height calculation
    val toolbarHeight = 64.dp
    val toolbarHeightPx = with(LocalDensity.current) { toolbarHeight.roundToPx().toFloat() }
    var toolbarOffsetHeightPx by remember { mutableStateOf(0f) }
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource,
            ): Offset {
                // try to consume before LazyColumn to collapse toolbar if needed, hence pre-scroll
                val delta = consumed.y
                val newOffset = toolbarOffsetHeightPx + delta
                toolbarOffsetHeightPx = if (filter.searchEnabled) {
                    0f // in search mode, the toolbar doesn't collapsing
                } else {
                    newOffset.coerceIn(-toolbarHeightPx, 0f)
                }
                // here's the catch: let's pretend we consumed 0 in any case, since we want
                // LazyColumn to scroll anyway for good UX
                // We're basically watching scroll without taking it
                return super.onPostScroll(consumed, available, source)
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(nestedScrollConnection),
    ) {
        AbonentsScreenBody(
            uiState = uiState,
            abonents = abonents,
            sortOrder = filter.sortOrder,
            toolbarHeight = toolbarHeight,
            searchEnabled = filter.searchEnabled,
            onOpenDocClick = launcher::openExcelDoc,
            onAbonentClick = {
                navigator.navigate(TakeReadingsScreenDestination())
            },
            onSortClick = {
                navigator.navigate(
                    SortOrderBottomSheetDestination(filter.sortOrder),
                )
            },
        )
        SearchToolbar(
            enabled = filter.searchEnabled,
            searchQuery = filter.searchQuery,
            onStartSearch = viewModel::startSearch,
            onCancelSearch = viewModel::cancelSearch,
            onOpenDocClick = launcher::openExcelDoc,
            modifier = Modifier
                .height(toolbarHeight)
                .offset { IntOffset(x = 0, y = toolbarOffsetHeightPx.roundToInt()) },
        )
        LoadingProgressIndicator(
            uiState = uiState,
        )
    }
}

private fun ManagedActivityResultLauncher<Array<String>, Uri?>.openExcelDoc() {
    val excelDocMime = arrayOf(
        "application/vnd.ms-excel",
        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
    )
    launch(excelDocMime)
}
