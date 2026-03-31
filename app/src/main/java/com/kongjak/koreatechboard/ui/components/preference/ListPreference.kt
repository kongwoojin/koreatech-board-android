package com.kongjak.koreatechboard.ui.components.preference

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kongjak.koreatechboard.ui.components.dialog.BasicDialog

@Composable
fun ListPreference(
    modifier: Modifier = Modifier,
    title: String,
    itemStringResource: List<Int>,
    itemValue: List<Any>,
    selectedIndex: Int,
    onClick: (Int) -> Unit
) {
    val zippedList = itemStringResource.zip(itemValue)
    val showDialog = remember { mutableStateOf(false) }

    Preference(modifier = modifier, title = title) {
        showDialog.value = true
    }

    if (showDialog.value) {
        ListPreferenceDialog(
            zippedList = zippedList,
            showDialog = showDialog,
            selectedIndex = selectedIndex,
            onClick = onClick
        )
    }
}

@Composable
fun ListPreference(
    modifier: Modifier = Modifier,
    title: String,
    summary: String,
    itemStringResource: List<Int>,
    itemValue: List<Any>,
    selectedIndex: Int,
    onClick: (Int) -> Unit
) {
    val zippedList = itemStringResource.zip(itemValue)
    val showDialog = remember { mutableStateOf(false) }

    Preference(modifier = modifier, title = title, summary = summary) {
        showDialog.value = true
    }

    if (showDialog.value) {
        ListPreferenceDialog(
            zippedList = zippedList,
            showDialog = showDialog,
            selectedIndex = selectedIndex,
            onClick = onClick
        )
    }
}

@Composable
fun ListPreference(
    modifier: Modifier = Modifier,
    title: String,
    icon: ImageVector,
    itemStringResource: List<Int>,
    itemValue: List<Any>,
    selectedIndex: Int,
    onClick: (Int) -> Unit
) {
    val zippedList = itemStringResource.zip(itemValue)
    val showDialog = remember { mutableStateOf(false) }

    Preference(modifier = modifier, title = title, icon = icon) {
        showDialog.value = true
    }

    if (showDialog.value) {
        ListPreferenceDialog(
            zippedList = zippedList,
            showDialog = showDialog,
            selectedIndex = selectedIndex,
            onClick = onClick
        )
    }
}

@Composable
fun ListPreference(
    modifier: Modifier = Modifier,
    title: String,
    summary: String,
    icon: ImageVector,
    itemStringResource: List<Int>,
    itemValue: List<Any>,
    selectedIndex: Int,
    onClick: (Int) -> Unit
) {
    val zippedList = itemStringResource.zip(itemValue)
    val showDialog = remember { mutableStateOf(false) }

    Preference(modifier = modifier, title = title, summary = summary, icon = icon) {
        showDialog.value = true
    }

    if (showDialog.value) {
        ListPreferenceDialog(
            zippedList = zippedList,
            showDialog = showDialog,
            selectedIndex = selectedIndex,
            onClick = onClick
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListPreferenceDialog(
    zippedList: List<Pair<Int, Any>>,
    showDialog: MutableState<Boolean>,
    selectedIndex: Int,
    onClick: (Int) -> Unit
) {
    val scrollState = rememberScrollState()
    BasicDialog(onDismissRequest = { showDialog.value = false }) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState)
        ) {
            val (selectedValue, onValueSelected) = remember { mutableIntStateOf(selectedIndex) }

            zippedList.forEachIndexed { index, item ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.selectable(
                        selected = (selectedValue == index),
                        onClick = {
                            onClick(index)
                            onValueSelected(index)
                            showDialog.value = false
                        }
                    )
                ) {
                    CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
                        RadioButton(selected = selectedValue == index, onClick = {
                            onClick(index)
                            onValueSelected(index)
                            showDialog.value = false
                        })
                    }
                    Text(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                        text = stringResource(id = item.first)
                    )
                }
                if (zippedList.lastIndex != index) {
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
}

@Preview
@Composable
fun ListPreferenceDialogPreview() {
    val showDialog = remember { mutableStateOf(true) }
    ListPreferenceDialog(
        zippedList = listOf(
            1 to "light",
            2 to "dark"
        ),
        showDialog = showDialog,
        selectedIndex = 0,
        onClick = {}
    )
}
