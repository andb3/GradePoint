package com.andb.apps.aspen.ui.settings

import android.os.Build
import androidx.compose.foundation.Icon
import androidx.compose.foundation.Text
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Switch
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.Info
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.VectorAsset
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.andb.apps.aspen.android.BuildConfig
import com.andb.apps.aspen.android.R
import com.andb.apps.aspen.model.DarkMode
import com.andb.apps.aspen.models.Screen
import com.andb.apps.aspen.state.UserAction
import com.andb.apps.aspen.ui.common.Chip
import com.andb.apps.aspen.ui.common.RadioItem
import com.andb.apps.aspen.ui.home.HomeHeader
import com.andb.apps.aspen.util.ActionHandler
import com.andb.apps.aspen.util.*

@Composable
fun SettingsScreen(actionHandler: ActionHandler) {
    val settings = SettingsAmbient.current
    Column {
        HomeHeader(title = "Settings")
        Spacer(modifier = Modifier.height(8.dp))
        SettingsHeaderItem(title = "Theme", topPadding = false)
        val darkModeDialogShown = remember { mutableStateOf(false) }
        SettingsItem(
            title = "Dark Mode",
            icon = vectorResource(id = R.drawable.ic_weather_night),
            modifier = Modifier.clickable(onClick = { darkModeDialogShown.value = true })
        )
        DarkModeDialog(
            showing = darkModeDialogShown.value,
            currentMode = settings.darkMode,
            onClose = {
                settings.darkMode = it
                darkModeDialogShown.value = false
            }
        )

        SettingsHeaderItem(title = "General")
        val showHidden = settings.showHiddenFlow.collectAsState()
        SettingsItem(title = "Hidden Classes Dropdown", icon = Icons.Default.VisibilityOff) {
            Switch(checked = showHidden.value, onCheckedChange = { settings.showHidden = it} )
        }

        SettingsHeaderItem(title = "Experiments")
        SettingsItem(
            title = "Font Size",
            icon = Icons.Default.FormatSize
        ) {
            Chip(
                text = "14sp (default)",
                selected = settings.fontSize == 14,
                onClick = { settings.fontSize = 14 },
                modifier = Modifier.padding(end = 4.dp)
            )
            Chip(
                text = "16sp",
                selected = settings.fontSize == 16,
                onClick = { settings.fontSize = 16 })
        }

        val color = settings.assignmentHeaderColorFlow.collectAsState()
        SettingsItem(
            title = "Assignment Page Header Color",
            icon = Icons.Default.Palette
        ) {
            Chip(
                text = "Background",
                selected = !color.value,
                onClick = { settings.assignmentHeaderColor = false },
                modifier = Modifier.padding(end = 4.dp)
            )
            Chip(
                text = "Green",
                selected = color.value,
                onClick = { settings.assignmentHeaderColor = true })
        }

        if (BuildConfig.DEBUG) {
            SettingsItem(
                title = "Test",
                summary = "Open test screen",
                icon = Icons.Default.Settings,
                modifier = Modifier.clickable(onClick = {
                    actionHandler.handle(UserAction.OpenScreen(Screen.Test))
                })
            )
        }

        SettingsHeaderItem(title = "About")
        SettingsItem(
            title = "Version",
            icon = Icons.Outlined.Info,
            summary = "v" + BuildConfig.VERSION_NAME
        )
    }
}

@Composable
fun SettingsItem(
    title: String,
    summary: String? = null,
    icon: VectorAsset,
    modifier: Modifier = Modifier,
    widget: @Composable() RowScope.() -> Unit = {}
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalGravity = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        Row(verticalGravity = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
            Icon(asset = icon)
            Column(Modifier.padding(start = 16.dp)) {
                Text(text = title, style = MaterialTheme.typography.subtitle1)
                if (summary != null) Text(text = summary)
            }
        }
        Row(children = widget)
    }
}

@Composable
fun SettingsHeaderItem(title: String, topPadding: Boolean = true) {
    Text(
        text = title,
        color = MaterialTheme.colors.primary,
        style = MaterialTheme.typography.subtitle1,
        modifier = Modifier.padding(start = 64.dp, top = if (topPadding) 24.dp else 0.dp, bottom = 8.dp)
    )
}

@Composable
fun DarkModeDialog(showing: Boolean, currentMode: DarkMode, onClose: (selectedMode: DarkMode) -> Unit) {
    val selectedMode = remember { mutableStateOf(currentMode) }
    if (showing) {
        AlertDialog(
            onDismissRequest = { onClose.invoke(currentMode) },
            title = { Text(text = "Dark Mode") },
            text = {
                Column {
                    RadioItem(
                        selected = selectedMode.value == DarkMode.LIGHT,
                        onSelect = { selectedMode.value = DarkMode.LIGHT },
                        radioColor = MaterialTheme.colors.primary,
                        text = "Light"
                    )
                    RadioItem(
                        selected = selectedMode.value == DarkMode.DARK,
                        onSelect = { selectedMode.value = DarkMode.DARK },
                        radioColor = MaterialTheme.colors.primary,
                        text = "Dark"
                    )
                    RadioItem(
                        selected = selectedMode.value == DarkMode.SYSTEM,
                        onSelect = { selectedMode.value = DarkMode.SYSTEM },
                        radioColor = MaterialTheme.colors.primary,
                        text = if (Build.VERSION.SDK_INT >= 29) "Follow System" else "Follow Battery Saver"
                    )
                }
            },
            dismissButton = {
                Button(onClick = { onClose.invoke(currentMode) }, backgroundColor = Color.Unset, elevation = 0.dp) {
                    Text(text = "Cancel".toUpperCase())
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onClose.invoke(selectedMode.value)
                    },
                    backgroundColor = Color.Unset,
                    elevation = 0.dp
                ) {
                    Text(text = "Done".toUpperCase())
                }
            }
        )
    }
}

