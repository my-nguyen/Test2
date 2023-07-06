/*
 * Copyright 2015, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.nguyen.test2

import android.content.SharedPreferences
import java.util.Calendar

/**
 * Helper class to manage access to [SharedPreferences].
 */
class SharedPreferencesHelper(private val sharedPreferences: SharedPreferences) {
    /**
     * Saves the given [SharedPreferenceEntry] that contains the user's settings to
     * [SharedPreferences].
     *
     * @param sharedPreferenceEntry contains data to save to [SharedPreferences].
     * @return `true` if writing to [SharedPreferences] succeeded. `false`
     * otherwise.
     */
    fun savePersonalInfo(sharedPreferenceEntry: SharedPreferenceEntry): Boolean {
        // Start a SharedPreferences transaction.
        val editor = sharedPreferences.edit()
        editor.putString(KEY_NAME, sharedPreferenceEntry.name)
        editor.putLong(KEY_DOB, sharedPreferenceEntry.dateOfBirth.timeInMillis)
        editor.putString(KEY_EMAIL, sharedPreferenceEntry.email)

        // Commit changes to SharedPreferences.
        return editor.commit()
    }// Get data from the SharedPreferences.

    // Create and fill a SharedPreferenceEntry model object.
    /**
     * Retrieves the [SharedPreferenceEntry] containing the user's personal information from
     * [SharedPreferences].
     *
     * @return the Retrieved [SharedPreferenceEntry].
     */
    fun getPersonalInfo(): SharedPreferenceEntry {
        // Get data from the SharedPreferences.
        val name = sharedPreferences.getString(KEY_NAME, "")
        val dobMillis = sharedPreferences.getLong(KEY_DOB, Calendar.getInstance().timeInMillis)
        val dateOfBirth = Calendar.getInstance()
        dateOfBirth.timeInMillis = dobMillis
        val email = sharedPreferences.getString(KEY_EMAIL, "")

        // Create and fill a SharedPreferenceEntry model object.
        return SharedPreferenceEntry(name, dateOfBirth, email)
    }

    companion object {
        // Keys for saving values in SharedPreferences.
        const val KEY_NAME = "key_name"
        const val KEY_DOB = "key_dob_millis"
        const val KEY_EMAIL = "key_email"
    }
}