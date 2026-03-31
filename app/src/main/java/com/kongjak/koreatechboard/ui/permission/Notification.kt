package com.kongjak.koreatechboard.ui.permission

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.kongjak.koreatechboard.R
import com.kongjak.koreatechboard.ui.components.dialog.TextDialog

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun CheckNotificationPermission(
    onPermissionGranted: () -> Unit
) {
    val isPermissionGranted = isNotificationPermissionGranted()

    if (!isPermissionGranted) {
        RequestNotificationPermission()
    } else {
        onPermissionGranted()
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestNotificationPermission() {
    val permissionState =
        rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS)

    if (permissionState.status.shouldShowRationale) {
        RationaleDialog()
    } else {
        PermissionDialog { permissionState.launchPermissionRequest() }
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun isNotificationPermissionGranted(): Boolean {
    return rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS).status.isGranted
}

@Composable
private fun RationaleDialog() {
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(true) }

    if (showDialog) {
        TextDialog(
            title = stringResource(id = R.string.permission_notification_request_title),
            content = stringResource(id = R.string.permission_notification_request_rationale_content),
            onConfirm = {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri: Uri = Uri.fromParts("package", context.packageName, null)
                intent.data = uri
                context.startActivity(intent)
                showDialog = false
            },
            onDismiss = {
                showDialog = false
            }
        )
    }
}

@Composable
private fun PermissionDialog(content: () -> Unit) {
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(true) }
    if (showDialog) {
        TextDialog(
            title = stringResource(id = R.string.permission_notification_request_title),
            content = stringResource(id = R.string.permission_notification_request_content),
            onConfirm = {
                content()
                showDialog = false
            },
            onDismiss = {
                Toast.makeText(
                    context,
                    context.getString(R.string.permission_notification_denied),
                    Toast.LENGTH_SHORT
                ).show()
                showDialog = false
            }
        )
    }
}
