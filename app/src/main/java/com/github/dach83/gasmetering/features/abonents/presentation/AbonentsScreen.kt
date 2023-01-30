package com.github.dach83.gasmetering.features.abonents.presentation

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.dach83.gasmetering.R
import com.github.dach83.gasmetering.core.presentation.navigation.NavigationTransitions
import com.github.dach83.gasmetering.core.presentation.ui.theme.DarkSwamp
import com.github.dach83.gasmetering.features.abonents.domain.model.Abonent
import com.github.dach83.gasmetering.features.abonents.domain.model.Readings
import com.github.dach83.gasmetering.features.abonents.domain.model.ReadingsBarChart
import com.github.dach83.gasmetering.features.abonents.domain.model.ReadingsDate
import com.github.dach83.gasmetering.features.abonents.presentation.state.AbonentsUiState
import com.github.dach83.gasmetering.features.destinations.TakeReadingsScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs
import kotlin.math.roundToInt

@RootNavGraph(start = true)
@Destination(style = NavigationTransitions::class)
@Composable
fun AbonentsScreen(
    viewModel: AbonentsViewModel = hiltViewModel(),
    navigator: DestinationsNavigator
) {
    val uiState = viewModel.uiState
    val filter by viewModel.filter.collectAsState()
    val abonents by viewModel.filteredAbonents.collectAsState(initial = emptyList())

    // open excel document launcher
    val openDocLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = viewModel::loadExcelFile
    )

    // collapsing toolbar calculation
    val toolbarHeight = 64.dp
    val toolbarHeightPx = with(LocalDensity.current) { toolbarHeight.roundToPx().toFloat() }
    var toolbarOffsetHeightPx by remember { mutableStateOf(0f) }
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource
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
            .nestedScroll(nestedScrollConnection)
    ) {
        when (uiState) {
            AbonentsUiState.NoExcelUri ->
                IconAndMessage(
                    iconId = R.drawable.openfolder,
                    textId = R.string.open_exel_doc,
                    onClick = openDocLauncher::openExcelDoc
                )

            is AbonentsUiState.Error ->
                IconAndMessage(
                    iconId = R.drawable.warning,
                    textId = uiState.message,
                    onClick = openDocLauncher::openExcelDoc
                )

            else ->
                AbonentList(
                    abonents = abonents,
                    onAbonentClick = {
                        navigator.navigate(TakeReadingsScreenDestination())
                    },
                    toolbarHeight = toolbarHeight
                )
        }

        SearchToolbar(
            enabled = filter.searchEnabled,
            searchQuery = filter.searchQuery,
            onStartSearch = viewModel::startSearch,
            onCancelSearch = viewModel::cancelSearch,
            onFolderClick = { openDocLauncher.openExcelDoc() },
            modifier = Modifier
                .height(toolbarHeight)
                .offset { IntOffset(x = 0, y = toolbarOffsetHeightPx.roundToInt()) }
        )
        if (uiState is AbonentsUiState.Loading) {
            LinearProgressIndicator(progress = uiState.progress)
        }
    }
}

private fun ManagedActivityResultLauncher<Array<String>, Uri?>.openExcelDoc() {
    val excelDocMime = arrayOf(
        "application/vnd.ms-excel",
        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
    )
    launch(excelDocMime)
}

@Composable
fun IconAndMessage(
    @DrawableRes iconId: Int,
    @StringRes textId: Int,
    onClick: () -> Unit = {}
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .clip(CircleShape)
                .clickable { onClick() }
                .padding(56.dp)
        ) {
            Image(
                painter = painterResource(id = iconId),
                contentDescription = "",
                modifier = Modifier.size(100.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = stringResource(id = textId),
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Composable
fun AbonentList(
    abonents: List<Abonent>,
    onAbonentClick: (Abonent) -> Unit,
    toolbarHeight: Dp
) {
    LazyColumn(
        contentPadding = PaddingValues(top = toolbarHeight),
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
    ) {
        items(abonents) {
            AbonentItem(it, onAbonentClick)
        }
    }
}

@Composable
fun AbonentItem(
    abonent: Abonent,
    onAbonentClick: (Abonent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onAbonentClick(abonent) }
    ) {
        Spacer(modifier = Modifier.height(6.dp))
        AbonentAddress(abonent)
        Spacer(modifier = Modifier.height(4.dp))
        LastReadingAndChart(abonent.readings)
        Spacer(modifier = Modifier.height(7.dp))
        Divider(color = DarkSwamp.copy(.1f))
    }
}

@Composable
private fun AbonentAddress(abonent: Abonent) {
    Text(
        text = "${abonent.id}, ${abonent.address}",
        style = MaterialTheme.typography.labelLarge
    )
}

@Composable
fun LastReadingAndChart(readings: Readings) {
    Row(
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        readings.lastEntry()?.let { entry ->
            Reading(entry.key, entry.value)
        }
        Spacer(modifier = Modifier.width(4.dp))
        VolumesBarChart(readings)
    }
}

@Composable
private fun Reading(
    date: ReadingsDate,
    value: Double
) {
    Column {
        ReadingValue(value)
        Spacer(modifier = Modifier.height(2.dp))
        ReadingDate(date)
    }
}

@Composable
private fun ReadingDate(date: ReadingsDate) {
    Text(
        text = SimpleDateFormat("MMMM, yyyy", Locale.getDefault()).format(date.value),
        style = MaterialTheme.typography.labelSmall
    )
}

@Composable
private fun ReadingValue(value: Double) {
    val (intPart, decPart) = "%09.3f".format(value).split(".", ",")
    Row {
        Surface(shadowElevation = 2.dp) {
            Text(
                text = intPart,
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier
                    .border(
                        width = 1.dp,
                        color = DarkSwamp,
                        shape = RoundedCornerShape(2.dp)
                    )
                    .padding(horizontal = 4.dp, vertical = 2.dp)
            )
        }
        Spacer(modifier = Modifier.width(2.dp))
        Surface(shadowElevation = 2.dp) {
            Text(
                text = decPart,
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier
                    .border(
                        width = 1.dp,
                        color = Color.Red,
                        shape = RoundedCornerShape(2.dp)
                    )
                    .padding(horizontal = 4.dp, vertical = 2.dp)
            )
        }
    }
}

@Composable
fun VolumesBarChart(readings: Readings) {
    val barsCount = 12
    val volumes = ReadingsBarChart(readings).normalizeVolumes(barsCount)
    val scrollState = rememberScrollState(Int.MAX_VALUE)
    Row(
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.Bottom,
        modifier = Modifier
            .height(40.dp)
            .horizontalScroll(scrollState)
    ) {
        volumes.forEach { volume ->
            val barColor = if (volume.second < 0) {
                Color.Red.copy(alpha = .3f)
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = .3f)
            }
            val month = SimpleDateFormat("MMM", Locale.getDefault())
                .format(volume.first.value)
            Column(
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = month,
                    style = MaterialTheme.typography.labelSmall,
                    softWrap = false,
                    modifier = Modifier.scale(0.8f)
                )
                Spacer(
                    modifier = Modifier
                        .width(16.dp)
                        .fillMaxHeight(abs(volume.second))
                        .background(color = barColor)
                )
            }
        }
    }
}

@Composable
fun SearchToolbar(
    enabled: Boolean,
    searchQuery: String,
    onStartSearch: (String) -> Unit,
    onCancelSearch: () -> Unit,
    onFolderClick: () -> Unit,
    modifier: Modifier = Modifier,
    surface: Color = MaterialTheme.colorScheme.surface,
    surfaceVariant: Color = MaterialTheme.colorScheme.surfaceVariant,
    onSurfaceVariant: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge.copy(color = onSurfaceVariant),
    shape: Shape = MaterialTheme.shapes.extraLarge
) {
    val backgroundBrush = if (enabled) {
        SolidColor(surfaceVariant)
    } else {
        Brush.verticalGradient(listOf(surface, surface, Color.Transparent, Color.Transparent))
    }
    val leadingIcon = if (enabled) Icons.Default.ArrowBack else Icons.Default.Search
    val interactionSource = remember { MutableInteractionSource() }
    val focusRequester by remember { mutableStateOf(FocusRequester()) }
    var textField by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue())
    }
    textField = textField.copy(text = searchQuery)

    // When the "Back" button is pressed, the search mode ends first.
    BackHandler(enabled = enabled) {
        onCancelSearch()
    }

    // When the search mode is started, the edit field gets focus.
    LaunchedEffect(key1 = enabled) {
        focusRequester.requestFocus()
    }

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .background(brush = backgroundBrush)
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clip(shape = shape)
            .background(color = surfaceVariant)
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                onStartSearch("")
            }
    ) {
        Icon( // leading icon
            imageVector = leadingIcon,
            contentDescription = "",
            tint = onSurfaceVariant,
            modifier = Modifier
                .padding(start = 12.dp, end = 12.dp)
                .clip(CircleShape)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    enabled = enabled,
                    onClick = onCancelSearch
                )
                .padding(8.dp)
                .size(24.dp)

        )
        BasicTextField( // search query editor
            value = textField,
            onValueChange = {
                if (enabled) {
                    onStartSearch(it.text)
                    textField = it
                }
            },
            enabled = enabled,
            textStyle = textStyle,
            singleLine = true,
            modifier = Modifier
                .weight(10f)
                .focusRequester(focusRequester),
            decorationBox = { innerTextField ->
                if (enabled) {
                    innerTextField()
                } else {
                    Text( // placeholder
                        text = stringResource(id = R.string.search_placeholder),
                        style = textStyle
                    )
                }
            }
        )
        if (enabled.not()) {
            Icon( // trailing icon
                imageVector = Icons.Default.FolderOpen,
                contentDescription = "Open document",
                tint = onSurfaceVariant,
                modifier = Modifier
                    .padding(start = 12.dp, end = 12.dp)
                    .clip(CircleShape)
                    .clickable(onClick = onFolderClick)
                    .padding(8.dp)
                    .size(24.dp)

            )
        }
    }
}
