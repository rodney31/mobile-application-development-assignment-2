package com.example.locationfinderapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class UpdateAndDeleteLocation extends AppCompatActivity
{
    // Define variables for database to be used, as well as different views
    private DatabaseHelper dbHelper;
    private int id;
    private EditText addressEditText, latitudeEditText, longitudeEditText;
    Button updateButton, deleteButton, cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        // Hide action bar from the screen
        if (getSupportActionBar() != null)
        {
            getSupportActionBar().hide();
        }

        setContentView(R.layout.activity_update_and_delete_location);

        // Initialize database helper, and initialize the variables with their associated view. Get the ID of the selected location record as well
        dbHelper = new DatabaseHelper(this);
        addressEditText = findViewById(R.id.addressEditText2);
        latitudeEditText = findViewById(R.id.latitudeEditText2);
        longitudeEditText = findViewById(R.id.longitudeEditText2);
        updateButton = findViewById(R.id.updateLocationButton);
        deleteButton = findViewById(R.id.deleteLocationButton);
        cancelButton = findViewById(R.id.cancelButton2);
        id = getIntent().getIntExtra("id", -1);

        displayCurrentRecord(); // Display the data of the location record the user selected to edit

        // When the 'Update' or 'Delete' button is clicked, the location record is updated or deleted respectively, and the user is returned to the home page
        updateButton.setOnClickListener(v -> {
            updateLocationRecord();
            navigateToHomePage();
        });
        deleteButton.setOnClickListener(v -> {
            deleteLocationRecord();
            navigateToHomePage();
        });

        cancelButton.setOnClickListener(v -> navigateToHomePage()); // When the 'Cancel" button is clicked, the user returns to the home page
    }

    // Displays the current location record that the user selected to edit
    private void displayCurrentRecord()
    {
        Cursor cursor = dbHelper.getLocationRecord(id); // Cursor for results that get returned from the database

        if (cursor != null && cursor.moveToFirst()) // Set the text of each EditText view to be the data of the selected location record
        {
            addressEditText.setText(cursor.getString(cursor.getColumnIndex("ADDRESS")));
            latitudeEditText.setText(cursor.getString(cursor.getColumnIndex("LATITUDE")));
            longitudeEditText.setText(cursor.getString(cursor.getColumnIndex("LONGITUDE")));
            cursor.close();
        }
    }

    // Sends updated changes made by the user on the loaded location record to the database
    private void updateLocationRecord()
    {
        String address = addressEditText.getText().toString();
        String latitude = latitudeEditText.getText().toString();
        String longitude = longitudeEditText.getText().toString();

        dbHelper.updateLocation(id, address, latitude, longitude); // Update the location record

        Toast.makeText(this, "Location record has been updated!", Toast.LENGTH_LONG).show();
        finish();
    }

    // Deletes the current loaded location from the database
    private void deleteLocationRecord()
    {
        dbHelper.deleteLocation(id); // Delete the location record

        Toast.makeText(this, "Location record has been deleted!", Toast.LENGTH_LONG).show(); // Send a Toast message indicating the location has been updated
        finish();
    }

    // Launches intent to navigate to Home Page
    private void navigateToHomePage()
    {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); // Send a Toast message indicating the location has been deleted
        startActivity(intent);
        finish();
    }
}
