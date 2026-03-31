package com.kongjak.koreatechboard.ui.components.preference

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.kongjak.koreatechboard.ui.components.dialog.TextDialog

@Composable
fun DialogPreference(
    modifier: Modifier = Modifier,
    title: String,
    summary: String,
    dialogTitle: String,
    dialogContent: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    val showDialog = remember { mutableStateOf(false) }

    Preference(modifier = modifier, title = title, summary = summary) {
        showDialog.value = true
    }

    if (showDialog.value) {
        TextDialog(
            title = dialogTitle,
            content = dialogContent,
            onConfirm = {
                showDialog.value = false
                onConfirm()
            },
            onDismiss = {
                showDialog.value = false
                onDismiss()
            }
        )
    }
}
