package com.submission.stoup.data.remote.pref

import android.provider.ContactsContract.CommonDataKinds.Email
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesOf
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferences private constructor(private val dataStore: DataStore<Preferences>){

    companion object{
        @Volatile
        private var INSTANCE: UserPreferences? = null

        private val EMAIL_KEY = stringPreferencesKey("email")
        private val TOKEN_KEY = stringPreferencesKey("token")
        private val LOGIN_KEY = booleanPreferencesKey("login")

        fun getInstance(dataStore: DataStore<Preferences>): UserPreferences{
            return INSTANCE ?: synchronized(this){
                val instance = UserPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }

    suspend fun saveSessions(user: UserModel){
        dataStore.edit { preferences ->
            preferences[EMAIL_KEY] = user.email
            preferences[TOKEN_KEY] = user.token
            preferences[LOGIN_KEY] = true
        }
    }

    suspend fun getSessions(): Flow<UserModel> {
        return dataStore.data.map { preferences ->
            UserModel(
                preferences[EMAIL_KEY] ?: "",
                preferences[TOKEN_KEY] ?: "",
                preferences[LOGIN_KEY] ?: false
            )
        }
    }

    suspend fun logout(){
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

}