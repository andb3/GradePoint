package com.andb.apps.aspen

import com.andb.apps.aspen.model.DarkMode
import com.netguru.kissme.Kissme
import kotlinx.coroutines.flow.MutableStateFlow
import org.koin.core.KoinComponent
import org.koin.core.inject

class AndroidSettings(val storage: Kissme) : StorageAndroid(storage){
    var darkMode: DarkMode
        get() = DarkMode.valueOf(storage.getString("darkMode", DarkMode.LIGHT.name) ?: DarkMode.LIGHT.name)
        set(value) {
            storage.putString("darkMode", value.name)
            darkModeFlow.value = darkMode
        }

    val darkModeFlow = MutableStateFlow(darkMode)

    var fontSize: Int
        get() = storage.getInt("fontSize", 14)
        set(value) {
            storage.putInt("fontSize", value)
            fontSizeFlow.value = fontSize
        }

    val fontSizeFlow = MutableStateFlow(fontSize)

    var assignmentHeaderColor: Boolean
        get() = storage.getBoolean("assignmentHeaderColor", false)
        set(value) {
            storage.putBoolean("assignmentHeaderColor", value)
            assignmentHeaderColorFlow.value = assignmentHeaderColor
        }

    val assignmentHeaderColorFlow = MutableStateFlow(assignmentHeaderColor)
}

