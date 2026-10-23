package com.example.smartpantrymanager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    private Switch switchExpiryAlerts;
    private Spinner spinnerUnits;
    private Button btnSaveSettings;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Connect the Java code to the screen elements.
        switchExpiryAlerts = findViewById(R.id.switchExpiryAlerts);
        spinnerUnits = findViewById(R.id.spinnerUnits);
        btnSaveSettings = findViewById(R.id.btnSaveSettings);

        // Create storage for the user's settings.
        sharedPreferences = getSharedPreferences(
                "SmartPantrySettings",
                MODE_PRIVATE
        );

        // Load the previously saved settings.
        loadSettings();

        // Save settings when the button is clicked.
        btnSaveSettings.setOnClickListener(v -> saveSettings());
    }

    private void loadSettings() {

        // Load the expiry alert preference.
        boolean expiryAlertsEnabled = sharedPreferences.getBoolean(
                "expiry_alerts_enabled",
                true
        );

        switchExpiryAlerts.setChecked(expiryAlertsEnabled);

        // Load the preferred measurement unit.
        String savedUnit = sharedPreferences.getString(
                "preferred_unit",
                "Pieces"
        );

        // Select the saved unit in the Spinner.
        for (int i = 0; i < spinnerUnits.getCount(); i++) {

            if (spinnerUnits.getItemAtPosition(i)
                    .toString()
                    .equals(savedUnit)) {

                spinnerUnits.setSelection(i);
                break;
            }
        }
    }

    private void saveSettings() {

        // Get the selected settings.
        boolean expiryAlertsEnabled =
                switchExpiryAlerts.isChecked();

        String preferredUnit =
                spinnerUnits.getSelectedItem().toString();

        // Save the settings on the device.
        SharedPreferences.Editor editor =
                sharedPreferences.edit();

        editor.putBoolean(
                "expiry_alerts_enabled",
                expiryAlertsEnabled
        );

        editor.putString(
                "preferred_unit",
                preferredUnit
        );

        editor.apply();

        // Inform the user that the settings were saved.
        Toast.makeText(
                SettingsActivity.this,
                "Settings saved successfully!",
                Toast.LENGTH_SHORT
        ).show();
    }
}