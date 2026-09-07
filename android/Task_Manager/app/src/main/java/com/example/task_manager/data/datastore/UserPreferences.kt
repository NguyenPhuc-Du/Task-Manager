package com.example.task_manager.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPreferences @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        val TOKEN_KEY = stringPreferencesKey("jwt_token")
        val USER_ID_KEY = stringPreferencesKey("user_id")
        val USER_EMAIL_KEY = stringPreferencesKey("user_email")
        val USER_NAME_KEY = stringPreferencesKey("user_name")
        val LANGUAGE_KEY = stringPreferencesKey("language")
    }

    val token: Flow<String?> = dataStore.data
        .catch { emit(emptyPreferences()) }
        .map { it[TOKEN_KEY] }
    val userId: Flow<String?> = dataStore.data
        .catch { emit(emptyPreferences()) }
        .map { it[USER_ID_KEY] }
    val userEmail: Flow<String?> = dataStore.data
        .catch { emit(emptyPreferences()) }
        .map { it[USER_EMAIL_KEY] }
    val userName: Flow<String?> = dataStore.data
        .catch { emit(emptyPreferences()) }
        .map { it[USER_NAME_KEY] }
    val language: Flow<String> = dataStore.data
        .catch { emit(emptyPreferences()) }
        .map { it[LANGUAGE_KEY] ?: "vi" }

    suspend fun saveUser(token: String, userId: String, email: String, name: String? = null) {
        dataStore.edit { prefs ->
            prefs[TOKEN_KEY] = token
            prefs[USER_ID_KEY] = userId
            prefs[USER_EMAIL_KEY] = email
            if (name != null) {
                prefs[USER_NAME_KEY] = name
            }
        }
    }

    suspend fun setLanguage(lang: String) {
        dataStore.edit { prefs ->
            prefs[LANGUAGE_KEY] = lang
        }
    }

    suspend fun clearUser() {
        dataStore.edit { it.clear() }
    }
}