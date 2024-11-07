package com.example.locationfinderapp;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity
{
    // Define variables for database to be used, as well as different views
    private DatabaseHelper dbHelper;
    private LinearLayout searchDisplay;
    private SearchView searchBar;
    private Button addLocationButton;

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

        setContentView(R.layout.activity_main);

        // Initialize database helper, and initialize the variables with their associated view
        dbHelper = new DatabaseHelper(this);
        searchDisplay = findViewById(R.id.searchDisplay);
        searchBar = findViewById(R.id.search);
        addLocationButton = findViewById(R.id.addLocationButton);

        // When the 'Add Location' button is clicked, the user is navigated to the Add Location page
        addLocationButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, NewLocation.class);
            startActivity(intent);
        });

        searchBar.setIconifiedByDefault(false); // Make the search bar appear in its full form initially, instead of as just an icon

        // Monitor and address what the user types in the search bar
        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            // The displayed location records are filtered based on the query the user submitted
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                filterLocationRecords(query);
                return true;
            }

            // The displayed location records are filtered based on what the query is currently being changed to
            @Override
            public boolean onQueryTextChange(String newText)
            {
                if (newText.isEmpty())
                {
                    searchDisplay.removeAllViews(); // if there is no query, no location records are being displayed
                }
                else
                {
                    filterLocationRecords(newText);
                }
                return true;
            }
        });
    }

    // Filter the location records from the database that get displayed to the user
    private void filterLocationRecords(String query)
    {
        searchDisplay.removeAllViews();
        Cursor cursor = dbHelper.searchLocationRecords(query); // Cursor for results based on query, that get returned from the database

        if (cursor != null && cursor.moveToFirst())
        {
            do // Store the data of each column for a particular location record in variables, and have it displayed
            {
                String address = cursor.getString(cursor.getColumnIndex("ADDRESS"));
                String latitude = cursor.getString(cursor.getColumnIndex("LATITUDE"));
                String longitude = cursor.getString(cursor.getColumnIndex("LONGITUDE"));
                int id = cursor.getInt(cursor.getColumnIndex("ID"));
                displayRecord(address, latitude, longitude, id); // Display records
            }
            while (cursor.moveToNext());

            cursor.close();
        }
    }

    // Creates the formatting that is used to display each record, and displays the record
    private void displayRecord(String address, String latitude, String longitude, int id)
    {
        // Create the layout for each location record
        LinearLayout locationRecordLayout = new LinearLayout(this);
        locationRecordLayout.setOrientation(LinearLayout.VERTICAL);
        locationRecordLayout.setPadding(16, 16, 16, 16);

        // A box is created that contains the latitude and longitude for display
        TextView latitudeAndLongitudeValue = new TextView(this);
        latitudeAndLongitudeValue.setText(String.format("Latitude: %s\nLongitude: %s", latitude, longitude)); // Displays latitude and longitude
        latitudeAndLongitudeValue.setTypeface(null, Typeface.BOLD);
        latitudeAndLongitudeValue.setTextSize(20);
        latitudeAndLongitudeValue.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)); // Formatting the appearance

        // A box is created that contains the address for display
        TextView addressValue = new TextView(this);
        addressValue.setText("Address: " + address); // Displays address
        addressValue.setTextSize(18);
        addressValue.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)); // Formatting the appearance

        // Create an "Edit" button for each record, that takes the user to the Update/Delete Location Page to either update or delete the location record
        Button editButton = new Button(this);
        editButton.setText("Edit");
        editButton.setOnClickListener(view -> navigateToUpdateAndDeletePage(id)); // When the button is clicked, execute the method for navigating to the Update/Delete Location Page
        editButton.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)); // Formatting the appearance
        editButton.setBackgroundColor(Color.parseColor("#FF8C00"));

        // Add each view component to be included with the location record layout
        locationRecordLayout.addView(latitudeAndLongitudeValue);
        locationRecordLayout.addView(addressValue);
        locationRecordLayout.addView(editButton);

        // Add the location record layout to the search display container
        searchDisplay.addView(locationRecordLayout);
    }

    // Launches intent to navigate to Update/Delete Location Page, when the "Edit" button beside a displayed location record is clicked
    private void navigateToUpdateAndDeletePage(int id)
    {
        Intent intent = new Intent(this, UpdateAndDeleteLocation.class);
        intent.putExtra("id", id); // Send the record that was selected to the Update/Delete Location Page, so that it can be updated or deleted
        startActivity(intent);
    }
}
