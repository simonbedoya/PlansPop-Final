package com.example.frank.planspop;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;

public class RemPassActivity extends AppCompatActivity implements DialogInterface.OnClickListener{

    Button send;
    EditText emailr;
    AlertDialog send_ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rem_pass);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        send = (Button) findViewById(R.id.btn_rem);
        emailr = (EditText) findViewById(R.id.emailr);
        Confirmation_send();
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager)getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(emailr.getWindowToken(), 0);
                if(!emailr.getText().toString().matches("")){

                    ParseUser.requestPasswordResetInBackground(emailr.getText().toString(), new RequestPasswordResetCallback() {
                        @Override
                        public void done(com.parse.ParseException e) {
                            if (e == null) {
                                send_ok.show();

                            } else {
                                Log.i("e", e.getMessage());
                                String[] palabras = e.getMessage().split(" ");
                                for (String i : palabras) {
                                    if (i.equals("invalid")) {
                                        Toast toast = Toast.makeText(getApplicationContext(), "Error: Coloca una direccion de correo valida", Toast.LENGTH_SHORT);
                                        toast.show();
                                    }
                                    if (i.equals("no")) {
                                        Toast toast = Toast.makeText(getApplicationContext(), "Error: No esta registrado este correo electronico", Toast.LENGTH_SHORT);
                                        toast.show();
                                    }
                                }


                            }
                        }
                    });
                }else{
                    Toast toast = Toast.makeText(getApplicationContext(), "Error: Coloque una direccion de correo", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });


    }
    public void Confirmation_send(){
        send_ok = new AlertDialog.Builder(this)
                .setTitle("Informacion!!")
                .setMessage("Se le ha enviado un link de recuperacion de contraseña el correo " + emailr.getText().toString() + " , cambie la contraseña e ingrese de nuevo al app")
                .setPositiveButton("Ok", this)
                .create();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_rem_pass, menu);
        return true;
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            Intent intent = new Intent(RemPassActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {

            Intent intent = new Intent(RemPassActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
    public void onClick(DialogInterface dialog, int which) {
        if(which == DialogInterface.BUTTON_POSITIVE){
            Intent intent = new Intent(RemPassActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();
        }
}}
