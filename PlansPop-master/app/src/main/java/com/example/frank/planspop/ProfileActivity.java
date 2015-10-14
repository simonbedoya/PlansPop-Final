package com.example.frank.planspop;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.ParseUser;

public class ProfileActivity extends AppCompatActivity {

    EditText name,username,sex,birth;
    TextView email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        name = (EditText) findViewById(R.id.profile_name);
        username = (EditText) findViewById(R.id.profile_username);
        sex = (EditText) findViewById(R.id.profile_sex);
        birth = (EditText) findViewById(R.id.profile_birthdate);
        email = (TextView) findViewById(R.id.profile_email);

        ParseUser user = ParseUser.getCurrentUser();
        name.setText(user.getString("name"));
        name.requestFocus();
        getWindow().setSoftInputMode(Window.PROGRESS_VISIBILITY_OFF);
        username.setText(user.getUsername());
        sex.setText(user.getString("sex"));
        birth.setText(user.getString("b_date"));
        email.setText(user.getEmail());




    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
