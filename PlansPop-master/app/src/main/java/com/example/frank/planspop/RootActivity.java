package com.example.frank.planspop;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.parse.Parse;
import com.parse.ParseUser;

public class RootActivity extends Activity  {


    // Set the duration of the splash screen
    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_root);
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "8ladccmIM9sV7Fj0LMWBj2nIwIbz0ZtPJffGxO8M", "7X6yfM9fkrif2EQ1JytvKJhrreB3MAUkFTGPCnLX");
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            intent = new Intent(RootActivity.this, MainActivity.class);

        } else {
            intent = new Intent(RootActivity.this, LoginActivity.class);
        }
        startActivity(intent);
        finish();


    }


}
