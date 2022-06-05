package com.teamc22ps135.healthlens.data.pref

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.teamc22ps135.healthlens.data.model.FirstInstall
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FirstInstallPreference private constructor(private val dataStore: DataStore<Preferences>) {
    fun getStateFirstInstall(): Flow<FirstInstall> {
        return dataStore.data.map { preferences ->
            FirstInstall(
                preferences[FIRST_INSTALL_KEY] ?: true
            )
        }
    }

    suspend fun setStateFirstInstall(firstInstall: FirstInstall) {
        dataStore.edit { preferences ->
            preferences[FIRST_INSTALL_KEY] = firstInstall.isFirstInstall
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: FirstInstallPreference? = null

        private val FIRST_INSTALL_KEY = booleanPreferencesKey("firstInstall")

        fun getInstance(dataStore: DataStore<Preferences>): FirstInstallPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = FirstInstallPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}