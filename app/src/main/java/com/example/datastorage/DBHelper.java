// app/src/main/java/com/example/datastorageapp/DBHelper.java
package com.example.datastorage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

    // Database Name
    private static final String DATABASE_NAME = "UserDB.db";
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Table Name
    public static final String TABLE_USERS = "users";
    // Column Names
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_EMAIL = "email";

    // SQL statement to create the users table
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_USERS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_NAME + " TEXT NOT NULL," +
                    COLUMN_EMAIL + " TEXT NOT NULL UNIQUE);"; // UNIQUE ensures email is unique

    // SQL statement to drop the users table if it exists
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_USERS;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // This method is called when the database is created for the first time.
        // Execute the SQL statement to create the users table.
        db.execSQL(SQL_CREATE_ENTRIES);
        Log.d("DBHelper", "Database created with table: " + TABLE_USERS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This method is called when the database needs to be upgraded.
        // For simplicity, we're just dropping the old table and recreating it.
        // In a real application, you would handle migrations more carefully
        // to preserve existing user data.
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
        Log.d("DBHelper", "Database upgraded from version " + oldVersion + " to " + newVersion);
    }
}
