// app/src/main/java/com/example/datastorageapp/UserRegistrationActivity.java
package com.example.datastorage;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class UserRegistrationActivity extends AppCompatActivity {

    private EditText nameEditText;
    private EditText emailEditText;
    private Button registerUserButton;
    private Button viewUsersButton;
    private TextView usersDisplayTextView;

    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);

        nameEditText = findViewById(R.id.name_edit_text);
        emailEditText = findViewById(R.id.email_edit_text);
        registerUserButton = findViewById(R.id.register_user_button);
        viewUsersButton = findViewById(R.id.view_users_button);
        usersDisplayTextView = findViewById(R.id.users_display_text_view);

        // Initialize the DBHelper
        dbHelper = new DBHelper(this);

        registerUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        viewUsersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewUsers();
            }
        });
    }

    private void registerUser() {
        String name = nameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Please enter both name and email.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get a writable database instance
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_NAME, name);
        values.put(DBHelper.COLUMN_EMAIL, email);

        long newRowId = -1;
        try {
            // Insert the new row, returning the primary key value of the new row
            newRowId = db.insert(DBHelper.TABLE_USERS, null, values);
            if (newRowId != -1) {
                Toast.makeText(this, "User registered successfully! ID: " + newRowId, Toast.LENGTH_SHORT).show();
                // Clear the input fields
                nameEditText.setText("");
                emailEditText.setText("");
                // Refresh the list of users
                viewUsers();
            } else {
                Toast.makeText(this, "Error registering user. Email might already exist.", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Log.e("UserRegistration", "Error inserting data: " + e.getMessage());
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            // Always close the database connection
            db.close();
        }
    }

    private void viewUsers() {
        // Get a readable database instance
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Define a projection (columns you want to retrieve)
        String[] projection = {
                DBHelper.COLUMN_ID,
                DBHelper.COLUMN_NAME,
                DBHelper.COLUMN_EMAIL
        };

        // Perform a query to get all users
        Cursor cursor = null;
        StringBuilder usersData = new StringBuilder();
        usersData.append("Registered Users:\n");

        try {
            cursor = db.query(
                    DBHelper.TABLE_USERS,   // The table to query
                    projection,             // The columns to return
                    null,                   // The columns for the WHERE clause
                    null,                   // The values for the WHERE clause
                    null,                   // Don't group the rows
                    null,                   // Don't filter by row groups
                    null                    // The sort order
            );

            if (cursor.getCount() == 0) {
                usersData.append("No users registered yet.");
                Toast.makeText(this, "No users to display.", Toast.LENGTH_SHORT).show();
            } else {
                // Loop through the Cursor to read each user's data
                while (cursor.moveToNext()) {
                    long itemId = cursor.getLong(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_ID));
                    String name = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_NAME));
                    String email = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_EMAIL));

                    // Append user data to the StringBuilder
                    usersData.append("ID: ").append(itemId)
                            .append(", Name: ").append(name)
                            .append(", Email: ").append(email)
                            .append("\n");

                    // Also log to Logcat for debugging
                    Log.d("User Data", "ID: " + itemId + ", Name: " + name + ", Email: " + email);
                }
            }
        } catch (Exception e) {
            Log.e("UserRegistration", "Error querying data: " + e.getMessage());
            usersData.append("Error loading users: ").append(e.getMessage());
            Toast.makeText(this, "Error viewing users: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            // Always close the cursor and database connection
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        // Display the data in the TextView
        usersDisplayTextView.setText(usersData.toString());
    }

    @Override
    protected void onDestroy() {
        // Close the database helper when the activity is destroyed
        if (dbHelper != null) {
            dbHelper.close();
        }
        super.onDestroy();
    }
}