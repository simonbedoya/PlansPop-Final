package com.example.frank.planspop;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.frank.planspop.fragments.AddFragment;
import com.example.frank.planspop.fragments.AddPlanMapsFragment;
import com.example.frank.planspop.fragments.FotoPlanFragment;
import com.example.frank.planspop.models.Lugar;
import com.example.frank.planspop.models.Plan;
import com.example.frank.planspop.parse.LugarParse;
import com.example.frank.planspop.parse.PlanParse;
import com.parse.ParseGeoPoint;

import java.io.File;
import java.io.IOException;
import java.util.List;


public class AddActivity extends AppCompatActivity implements AddPlanMapsFragment.OnLugarSelected,AddFragment.ActionListenerAddFragment, FotoPlanFragment.ActionListenerFotoPlanFragment, PlanParse.PlanParseInterface, DialogInterface.OnClickListener, LugarParse.LugarParseInterface {


    AddFragment addFragment;
    AddPlanMapsFragment mapsFragment;
    FotoPlanFragment fotoPlanFragment;

    public static String name;

    private String description, dateHour;
    private File image;


    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        addFragment = new AddFragment();
        mapsFragment = new AddPlanMapsFragment();
        fotoPlanFragment = new FotoPlanFragment();

        putFragment(R.id.add_container1, addFragment);


    }


    @Override
    public void onClick(DialogInterface dialog, int which) {
        if(which == DialogInterface.BUTTON_POSITIVE){
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
            finish();
        }
        if(which == DialogInterface.BUTTON_NEGATIVE){
            dialog.cancel();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home){

        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Cancelar la creacion del plan"); //Set Alert dialog title here
            alert.setMessage("¿Seguro que quieres cancelar el proceso? Se descartará toda informacion.");
            alert.setPositiveButton("OK", this);
            alert.setNegativeButton("Cancelar", this);
            AlertDialog alertDialog = alert.create();
            alertDialog.show();

        }
        return super.onKeyDown(keyCode, event);
    }


    public void putFragment(int idContainer, Fragment fragment){
        android.support.v4.app.FragmentTransaction fT = getSupportFragmentManager()
                .beginTransaction();
        fT.replace(idContainer, fragment);
        fT.commit();
    }

    @Override
    public void siguiente(String nombre, String descripcion, String fechaHora) {

        name = nombre;
        description = descripcion;
        dateHour = fechaHora;
        putFragment(R.id.add_container1, fotoPlanFragment);

    }

    @Override
    public void siguienteFotoPlanFragment(File imagen) {
        image = imagen;
        putFragment(R.id.add_container1, mapsFragment);

    }

    @Override
    public void onLugarSelected(double latitud, double longitud, String direccion, String nombreLugar) {

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Cargando datos...");

        Plan plan = new Plan();

        plan.setNombre(name.toLowerCase());
        plan.setDescripcion(description);
        plan.setFecha(dateHour);
        plan.setDireccion(nombreLugar);
        plan.setImgPath(image.getPath());

        ParseGeoPoint posicion = new ParseGeoPoint(latitud,longitud);
        plan.setLugar(posicion);

        Lugar lugar = new Lugar();
        lugar.setNombre(nombreLugar);
        lugar.setDireccion(direccion);
        lugar.setUbicacion(posicion);

        LugarParse lugarParse = new LugarParse(this);
        PlanParse planParse = new PlanParse(this);
        progressDialog.show();
        try {

            planParse.insertPlan(plan);
        } catch (IOException e) {
            e.printStackTrace();
        }

        lugarParse.insertLugar(lugar);

    }

    @Override
    public void done(boolean exito) {
        progressDialog.cancel();
        progressDialog.hide();
        if(exito == true) {

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        else{
            Toast.makeText(this, "ERROR AL CARGAR LOS DATOS", Toast.LENGTH_LONG);
            Log.i("ERROR AL CARGAR", "ERROR");
        }
    }

    @Override
    public void resultPlan(boolean exito, Plan plan) {

    }

    @Override
    public void resultListPlans(boolean exito, List<Plan> planes) {

    }

    @Override
    public void resultListLugares(Boolean exito, List<Lugar> lugares) {

    }
}
