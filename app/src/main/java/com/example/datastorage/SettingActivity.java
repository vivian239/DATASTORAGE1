// app/src/main/java/com/example/datastorageapp/SettingsActivity.java
package com.example.datastorage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

class SettingsActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText emailEditText;
    private Button saveSettingsButton;

    // Define a name for your SharedPreferences file
    private static final String PREF_NAME = "UserSettings";
    // Define keys for the data you want to save
    private static final String KEY_USERNAME = "username_key";
    private static final String KEY_EMAIL = "email_key";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        usernameEditText = findViewById(R.id.username_edit_text);
        emailEditText = findViewById(R.id.email_edit_text);
        saveSettingsButton = findViewById(R.id.save_settings_button);

        // 1. Read saved settings when the activity is created
        loadSettings();

        saveSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 2. Save settings when the button is clicked
                saveSettings();
            }
        });
    }

    private void loadSettings() {
        // Get an instance of SharedPreferences
        SharedPreferences prefs = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        // Retrieve the saved values using their keys
        String savedUsername = prefs.getString(KEY_USERNAME, ""); // "" is the default value if key not found
        String savedEmail = prefs.getString(KEY_EMAIL, "");

        // Set the retrieved values to the EditText fields
        usernameEditText.setText(savedUsername);
        emailEditText.setText(savedEmail);

        Toast.makeText(this, "Settings loaded!", Toast.LENGTH_SHORT).show();
    }

    private void saveSettings() {
        // Get the text from the EditText fields
        String username = usernameEditText.getText().toString();
        String email = emailEditText.getText().toString();

        // Get an instance of SharedPreferences
        SharedPreferences prefs = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        // Get an editor to put values into SharedPreferences
        SharedPreferences.Editor editor = prefs.edit();

        // Put the key-value pairs
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_EMAIL, email);

        // Apply the changes (asynchronously saves the data)
        editor.apply();

        Toast.makeText(this, "Settings saved!", Toast.LENGTH_SHORT).show();
    }
}
