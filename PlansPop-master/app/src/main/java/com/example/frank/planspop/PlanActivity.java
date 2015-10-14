package com.example.frank.planspop;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.frank.planspop.fragments.ListaFragment;
import com.example.frank.planspop.fragments.MapsFragment;
import com.example.frank.planspop.fragments.VerPlanFragment;


public class PlanActivity extends AppCompatActivity implements View.OnClickListener, VerPlanFragment.ActionVerPLanFragment, MapsFragment.OnLugarSelected{



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);


        VerPlanFragment verPlanFragment = new VerPlanFragment();
        putFragment(R.id.container_ver_plan,verPlanFragment);


    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_plan, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item){
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if (id == android.R.id.home) {
            ListaFragment list = new ListaFragment();
            list.Reload();

        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onKeyDown ( int keyCode, KeyEvent event){
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            ListaFragment list = new ListaFragment();
            list.Reload();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick (View v){


    }
    public void putFragment(int idContainer, Fragment fragment){
        FragmentTransaction fT = getSupportFragmentManager()
                .beginTransaction();
        fT.replace(idContainer, fragment);
        fT.commit();
    }

    @Override
    public void cargarMapa(double latitud, double longitud) {
        MapsFragment mapsFragment = new MapsFragment();
        mapsFragment.init(latitud, longitud, 18.0f);
        putFragment(R.id.container_ver_plan, mapsFragment);
    }

    @Override
    public void onLugarSelected(double latitud, double longitud) {

    }
}
