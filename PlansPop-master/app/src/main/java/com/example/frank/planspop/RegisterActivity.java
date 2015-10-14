package com.example.frank.planspop;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.frank.planspop.AppUtil.AppUtil;
import com.example.frank.planspop.fragments.Register_cypFragment;


public class RegisterActivity extends AppCompatActivity implements DialogInterface.OnClickListener {

    Register_cypFragment reg;
    AlertDialog close;
    public static String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        reg = new Register_cypFragment();

        putFragment(R.id.container,reg);

        Confirmation_close();

    }
    public void Confirmation_close(){
        close = new AlertDialog.Builder(this)
                .setTitle("Cancelar cuenta")
                .setMessage("¿Seguro que quieres cancelar la creación de la cuenta? Se descartará toda informacion.")
                .setPositiveButton("Sí", this)
                .setNegativeButton("No", this)
                .create();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
        return true;
    }
    public void r_button(View view){
        int id = view.getId();
        switch (id){
            case R.id.h:
                AppUtil.sex="hombre";
                break;
            case R.id.m:
                AppUtil.sex="mujer";
                break;
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            close.show();
        }
        return super.onOptionsItemSelected(item);
    }
    public void putFragment(int idContainer, Fragment fragment){
        FragmentTransaction fT = getSupportFragmentManager()
                .beginTransaction();
        fT.replace(idContainer, fragment);
        fT.commit();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            close.show();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if(which == DialogInterface.BUTTON_POSITIVE){
            Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
