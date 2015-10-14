package com.example.frank.planspop;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;

import com.example.frank.planspop.AppUtil.AppUtil;
import com.example.frank.planspop.fragments.AddPlanMapsFragment;
import com.example.frank.planspop.fragments.EditarMisPlanesFragment;
import com.example.frank.planspop.fragments.VerMisPlanesFragment;
import com.example.frank.planspop.models.Plan;
import com.example.frank.planspop.parse.PlanParse;
import com.parse.DeleteCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;


public class MisPlanesActivity extends AppCompatActivity implements EditarMisPlanesFragment.ActionEditarMisPlanesFragment, AddPlanMapsFragment.OnLugarSelected, PlanParse.PlanParseInterface {

    ProgressDialog progressDialog;
    PlanParse planParse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_planes);

        EditarMisPlanesFragment editarMisPlanesFragment = new EditarMisPlanesFragment();
        putFragment(R.id.mis_planes_contairner, editarMisPlanesFragment);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    public void putFragment(int idContainer, Fragment fragment){
        FragmentTransaction fT = getSupportFragmentManager()
                .beginTransaction();
        fT.replace(idContainer, fragment);
        fT.commit();
    }




    @Override
    public void onLugarSelected(double latitud, double longitud, String direccion, String nombreLugar) {
        Plan p = AppUtil.data_misPlanes.get(AppUtil.positionSelectedMisPlanes);
        ParseGeoPoint posicion = new ParseGeoPoint(latitud,longitud);
        p.setLugar(posicion);
        p.setDireccion(nombreLugar);
        planParse = new PlanParse(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Cargando Lugar...");
        planParse.updatePlan(p);


        EditarMisPlanesFragment editarMisPlanes= new EditarMisPlanesFragment();
        putFragment(R.id.mis_planes_contairner, editarMisPlanes);

    }

    @Override
    public void done(boolean exito) {
        progressDialog.cancel();
        progressDialog.hide();
    }

    @Override
    public void resultPlan(boolean exito, Plan plan) {

    }

    @Override
    public void resultListPlans(boolean exito, List<Plan> planes) {

    }

    @Override
    public void EliminarOLoadMap(Boolean loadMaps, Boolean eliminar) {

        if(loadMaps) {
            AddPlanMapsFragment addPlanMapsFragment = new AddPlanMapsFragment();
            putFragment(R.id.mis_planes_contairner, addPlanMapsFragment);
        }
        else if(eliminar){
            Plan plan = AppUtil.data_misPlanes.get(AppUtil.positionSelectedMisPlanes);
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Eliminando...");
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Plan");
            query.getInBackground(plan.getId(), new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject object, ParseException e) {
                    deletePlan(object);
                }
            });
        }
    }

    public void deletePlan (ParseObject object){
        object.deleteInBackground(new DeleteCallback() {
            @Override
            public void done(ParseException e) {
                progressDialog.cancel();
                progressDialog.hide();
                if( e== null) {
                    terminarActivity();
                }
                else{

                    Log.i("Error", "Eliminando");
                }
            }
        });
    }

    public void terminarActivity (){

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
