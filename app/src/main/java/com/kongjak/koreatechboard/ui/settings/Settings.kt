package com.kongjak.koreatechboard.ui.settings

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.kongjak.koreatechboard.R
import com.kongjak.koreatechboard.domain.DARK_THEME_DARK_THEME
import com.kongjak.koreatechboard.domain.DARK_THEME_LIGHT_THEME
import com.kongjak.koreatechboard.domain.DARK_THEME_SYSTEM_DEFAULT
import com.kongjak.koreatechboard.ui.components.preference.DialogPreference
import com.kongjak.koreatechboard.ui.components.preference.ListPreference
import com.kongjak.koreatechboard.ui.components.preference.Preference
import com.kongjak.koreatechboard.ui.components.preference.PreferenceColumn
import com.kongjak.koreatechboard.ui.components.preference.SwitchPreference
import com.kongjak.koreatechboard.ui.permission.RequestNotificationPermission
import com.kongjak.koreatechboard.ui.permission.isNotificationPermissionGranted
import com.kongjak.koreatechboard.util.routes.Department
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

val deptList = listOf(
    Department.Cse,
    Department.Mechanical,
    Department.Mechatronics,
    Department.Ite,
    Department.Ide,
    Department.Arch,
    Department.Mse,
    Department.Ace,
    Department.Sim
)
val deptListString = deptList.map { it.stringResource }
val deptListName = deptList.map { it.name }

val darkTheme = listOf(DARK_THEME_SYSTEM_DEFAULT, DARK_THEME_LIGHT_THEME, DARK_THEME_DARK_THEME)
val darkThemeString = listOf(
    R.string.setting_dark_theme_system_default,
    R.string.setting_dark_theme_light,
    R.string.setting_dark_theme_dark
)

@Composable
fun SettingsScreen(settingsViewModel: SettingsViewModel = hiltViewModel(), openLicenses: () -> Unit) {
    val context = LocalContext.current
    val uiState = settingsViewModel.collectAsState().value

    settingsViewModel.collectSideEffect { settingsViewModel.handleSideEffect(it) }
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        val userDepartment = uiState.userDepartment

        PreferenceColumn {
            ListPreference(
                title = stringResource(id = R.string.setting_user_department_title),
                summary = stringResource(id = deptListString[userDepartment]),
                itemStringResource = deptListString,
                itemValue = deptListName,
                selectedIndex = userDepartment
            ) { item ->
                val subscribe = uiState.subscribeDepartment
                if (subscribe) {
                    settingsViewModel.sendSideEffect(
                        SettingsSideEffect.UpdateDepartmentSubscribe(
                            false
                        )
                    )
                }
                settingsViewModel.sendSideEffect(SettingsSideEffect.SetUserDepartment(item))
                if (subscribe) {
                    settingsViewModel.sendSideEffect(
                        SettingsSideEffect.UpdateDepartmentSubscribe(
                            true
                        )
                    )
                }
            }

            val initDepartment = uiState.initDepartment

            val initDeptList = listOf(
                Department.School,
                Department.Dorm,
                deptList[userDepartment]
            )

            ListPreference(
                title = stringResource(id = R.string.setting_default_board_title),
                summary = stringResource(id = initDeptList.map { it.stringResource }[initDepartment]),
                itemStringResource = initDeptList.map { it.stringResource },
                itemValue = initDeptList.map { it.name },
                selectedIndex = initDepartment
            ) { item ->
                settingsViewModel.sendSideEffect(SettingsSideEffect.SetInitDepartment(item))
            }
        }

        PreferenceColumn(title = stringResource(id = R.string.setting_header_notification)) {
            val isNotificationPermissionGranted =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    isNotificationPermissionGranted()
                } else {
                    true
                }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && !isNotificationPermissionGranted) {
                RequestNotificationPermission()
            }
            SwitchPreference(
                title = stringResource(id = R.string.setting_subscribe_new_notice_school),
                summary = stringResource(id = R.string.setting_subscribe_new_notice_summary_school),
                checked = uiState.subscribeSchool,
                enabled = isNotificationPermissionGranted,
                onCheckedChange = {
                    settingsViewModel.sendSideEffect(SettingsSideEffect.UpdateSchoolSubscribe(it))
                }
            )

            SwitchPreference(
                title = stringResource(id = R.string.setting_subscribe_new_notice_dorm),
                summary = stringResource(id = R.string.setting_subscribe_new_notice_summary_dorm),
                checked = uiState.subscribeDormitory,
                enabled = isNotificationPermissionGranted,
                onCheckedChange = {
                    settingsViewModel.sendSideEffect(SettingsSideEffect.UpdateDormSubscribe(it))
                }
            )

            SwitchPreference(
                title = stringResource(id = R.string.setting_subscribe_new_notice_department),
                summary = stringResource(id = R.string.setting_subscribe_new_notice_summary_department),
                checked = uiState.subscribeDepartment,
                enabled = isNotificationPermissionGranted,
                onCheckedChange = {
                    settingsViewModel.sendSideEffect(SettingsSideEffect.UpdateDepartmentSubscribe(it))
                }
            )

            DialogPreference(
                title = stringResource(id = R.string.setting_delete_all_new_notices_title),
                summary = stringResource(id = R.string.setting_delete_all_new_notices_summary),
                dialogTitle = stringResource(id = R.string.setting_delete_all_new_notices_title),
                dialogContent = stringResource(id = R.string.setting_delete_all_new_notices_confirm),
                onConfirm = {
                    settingsViewModel.sendSideEffect(SettingsSideEffect.DeleteAllNewArticle)
                },
                onDismiss = {
                    // Do nothing
                }
            )
        }

        PreferenceColumn(title = stringResource(id = R.string.setting_header_info)) {
            Preference(
                title = stringResource(id = R.string.setting_license_title)
            ) {
                openLicenses()
            }

            Preference(title = stringResource(id = R.string.setting_source_code_title)) {
                val builder = CustomTabsIntent.Builder()
                val customTabsIntent = builder.build()
                customTabsIntent.launchUrl(
                    context,
                    Uri.parse(context.getString(R.string.setting_source_code_url))
                )
            }

            Preference(title = stringResource(id = R.string.setting_enquiry_mail_title)) {
                val mailIntent = Intent(Intent.ACTION_SENDTO)
                mailIntent.data = Uri.parse("mailto:")
                mailIntent.putExtra(
                    Intent.EXTRA_EMAIL,
                    arrayOf(context.getString(R.string.setting_mail_address))
                )
                mailIntent.putExtra(
                    Intent.EXTRA_SUBJECT,
                    context.getString(R.string.setting_mail_subject)
                )
                mailIntent.putExtra(
                    Intent.EXTRA_TEXT,
                    context.getString(
                        R.string.setting_mail_text,
                        Build.MODEL,
                        Build.VERSION.SDK_INT
                    )
                )
                try {
                    context.startActivity(mailIntent)
                } catch (e: ActivityNotFoundException) {
                    Toast.makeText(
                        context,
                        context.getString(R.string.setting_mail_app_not_found),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        PreferenceColumn(title = stringResource(id = R.string.setting_header_theme)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val isChecked = uiState.isDynamicTheme

                SwitchPreference(
                    title = stringResource(id = R.string.setting_dynamic_theme_title),
                    summary = stringResource(id = R.string.setting_dynamic_theme_summary),
                    checked = isChecked,
                    onCheckedChange = {
                        settingsViewModel.sendSideEffect(
                            SettingsSideEffect.SetDynamicTheme(
                                it
                            )
                        )
                    }
                )
            }

            val isDarkTheme = uiState.isDarkTheme

            ListPreference(
                title = stringResource(id = R.string.setting_dark_theme_title),
                summary = stringResource(
                    id = darkThemeString[isDarkTheme]
                ),
                itemStringResource = darkThemeString,
                itemValue = darkTheme,
                selectedIndex = isDarkTheme
            ) { theme ->
                settingsViewModel.sendSideEffect(SettingsSideEffect.SetDarkTheme(theme))
            }
        }
    }
}

@Preview
@Composable
fun SettingsPreview() {
    SettingsScreen(
        openLicenses = {}
    )
}
