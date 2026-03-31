package com.kongjak.koreatechboard.ui.components.dialog

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.kongjak.koreatechboard.R

@Composable
fun TextDialog(
    title: String,
    content: String,
    onConfirmString: String = stringResource(id = R.string.dialog_confirm),
    onDismissString: String = stringResource(id = R.string.dialog_dismiss),
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    BasicDialog(
        title = title,
        content = {
            Text(text = content, style = MaterialTheme.typography.bodyMedium)
        },
        onConfirmString = onConfirmString,
        onDismissString = onDismissString,
        onConfirm = onConfirm,
        onDismiss = onDismiss
    )
}
