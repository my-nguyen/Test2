package com.nguyen.test2

import android.app.Activity
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.nguyen.test2.databinding.ActivityMainBinding
import java.util.Calendar

private const val TAG = "MainActivity"

/**
 * An [Activity] that represents an input form page where the user can provide his name, date
 * of birth and email address. The personal information can be saved to [SharedPreferences]
 * by clicking a button.
 */
class MainActivity : AppCompatActivity() {
    private lateinit var preferencesHelper: SharedPreferencesHelper
    private var emailValidator: EmailValidator? = null
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup field validators.
        emailValidator = EmailValidator()
        binding.emailInput.addTextChangedListener(emailValidator)

        // Instantiate a SharedPreferencesHelper.
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        preferencesHelper = SharedPreferencesHelper(sharedPreferences)

        // Fill input fields from data retrieved from the SharedPreferences.
        populateUi()

        binding.saveButton.setOnClickListener {
            // Don't save if the fields do not validate.
            if (!emailValidator!!.isValid) {
                binding.emailInput.error = "Invalid email"
                Log.w(TAG, "Not saving personal information: Invalid email")
                return@setOnClickListener
            }

            // Get the text from the input fields.
            val name = binding.userNameInput.text.toString()
            val dateOfBirth = Calendar.getInstance()
            dateOfBirth[binding.dateOfBirthInput.year, binding.dateOfBirthInput.month] = binding.dateOfBirthInput.dayOfMonth
            val email = binding.emailInput.text.toString()

            // Create a Setting model class to persist.
            val sharedPreferenceEntry = SharedPreferenceEntry(name, dateOfBirth, email)

            // Persist the personal information.
            val isSuccess = preferencesHelper.savePersonalInfo(sharedPreferenceEntry)
            if (isSuccess) {
                Toast.makeText(this, "Personal information saved", Toast.LENGTH_LONG).show()
                Log.i(TAG, "Personal information saved")
            } else {
                Log.e(TAG, "Failed to write personal information to SharedPreferences")
            }
        }

        binding.revertButton.setOnClickListener {
            populateUi()
            Toast.makeText(this, "Personal information reverted", Toast.LENGTH_LONG).show()
            Log.i(TAG, "Personal information reverted")
        }
    }

    /**
     * Initialize all fields from the personal info saved in the SharedPreferences.
     */
    private fun populateUi() {
        val sharedPreferenceEntry = preferencesHelper.getPersonalInfo()
        binding.userNameInput.setText(sharedPreferenceEntry.name)
        val dateOfBirth = sharedPreferenceEntry.dateOfBirth
        binding.dateOfBirthInput.init(
            dateOfBirth!![Calendar.YEAR], dateOfBirth[Calendar.MONTH],
            dateOfBirth[Calendar.DAY_OF_MONTH], null
        )
        binding.emailInput.setText(sharedPreferenceEntry.email)
    }
}