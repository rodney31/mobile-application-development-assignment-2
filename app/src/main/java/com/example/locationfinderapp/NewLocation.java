package com.example.locationfinderapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class NewLocation extends AppCompatActivity
{
    // Define variables for database to be used, as well as different views
    private DatabaseHelper dbHelper;
    private EditText addressEditText, latitudeEditText, longitudeEditText;
    Button addLocationButton, cancelButton;

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

        setContentView(R.layout.activity_new_location);

        // Initialize database helper, and initialize the variables with their associated view
        dbHelper = new DatabaseHelper(this);
        addressEditText = findViewById(R.id.addressEditText);
        latitudeEditText = findViewById(R.id.latitudeEditText);
        longitudeEditText = findViewById(R.id.longitudeEditText);
        addLocationButton = findViewById(R.id.addLocationButton2);
        cancelButton = findViewById(R.id.cancelButton);

        // When the 'Add Location' button is clicked, the location is added to the database, and the user is returned to the home page
        addLocationButton.setOnClickListener(v -> {
            addLocation();
            navigateToHomePage();
        });

        cancelButton.setOnClickListener(v -> navigateToHomePage()); // When the 'Cancel" button is clicked, the user returns to the home page
    }

    // Adds provided location to the database
    private void addLocation()
    {
        // Store the data the user provided for the location
        String address = addressEditText.getText().toString();
        String latitude = latitudeEditText.getText().toString();
        String longitude = longitudeEditText.getText().toString();

        dbHelper.insertLocation(address, latitude, longitude); // Create a location record with the provided data

        Toast.makeText(this, "Location has been added!", Toast.LENGTH_LONG).show(); // Send a Toast message indicating the location has been added
    }

    // Launches intent to navigate to Home Page
    private void navigateToHomePage()
    {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
