package com.nguyen.test2

import android.content.SharedPreferences
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import java.util.Calendar

/**
 * Unit tests for the [SharedPreferencesHelper] that mocks [SharedPreferences].
 */
@RunWith(MockitoJUnitRunner::class)
class SharedPreferencesHelperTest {
    private val sharedPreferenceEntry = SharedPreferenceEntry(TEST_NAME, TEST_DATE_OF_BIRTH, TEST_EMAIL)
    private val goodPreferencesHelper = createMockSharedPreference()
    private val brokenPreferencesHelper = createBrokenMockSharedPreference()

    @Mock
    var goodMockPreferences: SharedPreferences? = null
    @Mock
    var brokenMockPreferences: SharedPreferences? = null
    @Mock
    var goodMockEditor: SharedPreferences.Editor? = null
    @Mock
    var brokenMockEditor: SharedPreferences.Editor? = null

    @Test
    fun sharedPreferencesHelper_SaveAndReadPersonalInformation() {
        // Save the personal information to SharedPreferences
        val success = goodPreferencesHelper.savePersonalInfo(sharedPreferenceEntry)
        MatcherAssert.assertThat(
            "Checking that SharedPreferenceEntry.save... returns true",
            success, CoreMatchers.`is`(true)
        )

        // Read personal information from SharedPreferences
        val (name, dateOfBirth, email) = goodPreferencesHelper.getPersonalInfo()

        // Make sure both written and retrieved personal information are equal.
        MatcherAssert.assertThat<String>(
            "Checking that SharedPreferenceEntry.name has been persisted and read correctly",
            sharedPreferenceEntry.name,
            CoreMatchers.`is`(CoreMatchers.equalTo<String>(name))
        )
        MatcherAssert.assertThat(
            "Checking that SharedPreferenceEntry.dateOfBirth has been persisted and read correctly",
            sharedPreferenceEntry.dateOfBirth,
            CoreMatchers.`is`(CoreMatchers.equalTo(dateOfBirth))
        )
        MatcherAssert.assertThat<String>(
            "Checking that SharedPreferenceEntry.email has been persisted and read correctly",
            sharedPreferenceEntry.email,
            CoreMatchers.`is`(CoreMatchers.equalTo<String>(email))
        )
    }

    @Test
    fun sharedPreferencesHelper_SavePersonalInformationFailed_ReturnsFalse() {
        // Read personal information from a broken SharedPreferencesHelper
        val success = brokenPreferencesHelper.savePersonalInfo(sharedPreferenceEntry)
        MatcherAssert.assertThat(
            "Makes sure writing to a broken SharedPreferencesHelper returns false", success,
            CoreMatchers.`is`(false)
        )
    }

    /**
     * Creates a mocked SharedPreferences.
     */
    private fun createMockSharedPreference(): SharedPreferencesHelper {
        // Mocking reading the SharedPreferences as if mMockSharedPreferences was previously written correctly.
        `when`(goodMockPreferences?.getString(eq(SharedPreferencesHelper.KEY_NAME), anyString()))
            .thenReturn(sharedPreferenceEntry.name)
        `when`(goodMockPreferences?.getString(eq(SharedPreferencesHelper.KEY_EMAIL), anyString()))
            .thenReturn(sharedPreferenceEntry.email)
        `when`(goodMockPreferences?.getLong(eq(SharedPreferencesHelper.KEY_DOB), anyLong()))
            .thenReturn(sharedPreferenceEntry.dateOfBirth.timeInMillis)

        // Mocking a successful commit.
        `when`(goodMockEditor?.commit()).thenReturn(true)

        // Return the MockEditor when requesting it.
        `when`(goodMockPreferences?.edit()).thenReturn(goodMockEditor)
        return SharedPreferencesHelper(goodMockPreferences!!)
    }

    /**
     * Creates a mocked SharedPreferences that fails when writing.
     */
    private fun createBrokenMockSharedPreference(): SharedPreferencesHelper {
        // Mocking a commit that fails.
        `when`(brokenMockEditor?.commit()).thenReturn(false)

        // Return the broken MockEditor when requesting it.
        `when`(brokenMockPreferences?.edit()).thenReturn(brokenMockEditor)
        return SharedPreferencesHelper(brokenMockPreferences!!)
    }

    companion object {
        private const val TEST_NAME = "Test name"
        private const val TEST_EMAIL = "test@email.com"
        private val TEST_DATE_OF_BIRTH = Calendar.getInstance()

        init {
            TEST_DATE_OF_BIRTH[1980, 1] = 1
        }
    }
}