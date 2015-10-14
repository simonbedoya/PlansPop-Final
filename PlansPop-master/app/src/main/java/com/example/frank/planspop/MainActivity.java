package com.example.frank.planspop;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.frank.planspop.AppUtil.AppUtil;
import com.example.frank.planspop.adapters.PagerAdapter;
import com.example.frank.planspop.fragments.ListaFragment;
import com.example.frank.planspop.fragments.MapsFragment;
import com.example.frank.planspop.fragments.MisPlanesFragment;
import com.example.frank.planspop.fragments.TitleFragment;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DialogInterface.OnClickListener,SearchView.OnQueryTextListener, MenuItemCompat.OnActionExpandListener,
        MapsFragment.OnLugarSelected, ListaFragment.OnItemSelectedList, MisPlanesFragment.ActionMisPlanesFragment{

    List<TitleFragment> data;
    PagerAdapter adapter;
    ViewPager mViewPager;

    public static ProgressDialog dialog;
    ProgressDialog cerrar_sesion;
    AlertDialog close;
    public static ProgressDialog getDialog() {
        return dialog;
    }

    ListaFragment list = new ListaFragment();
    MisPlanesFragment list_my = new MisPlanesFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Cargando informacion...");
        cerrar_sesion = new ProgressDialog(this);
        cerrar_sesion.setMessage("Cerrando sesión...");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        data = new ArrayList<>();

        data.add((TitleFragment) TitleFragment.instantiate(this, ListaFragment.class.getName()));


        MapsFragment mapsFragment = new MapsFragment();
        mapsFragment.init(MapsFragment.LATITUD_POPOYAN, MapsFragment.LONGITUD_POPOYAN, MapsFragment.ZOOM);
        data.add(mapsFragment);
        data.add((TitleFragment) TitleFragment.instantiate(this, MisPlanesFragment.class.getName()));


        adapter = new PagerAdapter(getSupportFragmentManager(),data);

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        Confirmation_close();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);

        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        // LISTENER PARA EL EDIT TEXT
        searchView.setOnQueryTextListener(this);

        // LISTENER PARA LA APERTURA Y CIERRE DEL WIDGET
        MenuItemCompat.setOnActionExpandListener(searchItem, this);

        return super.onCreateOptionsMenu(menu);
    }
    public void Confirmation_close(){
        close = new AlertDialog.Builder(this)
                .setTitle("Cerrar sesión")
                .setMessage("¿Seguro que deseas cerrar sesión.")
                .setPositiveButton("Sí", this)
                .setNegativeButton("No", this)
                .create();
    }
    @Override
    public void onClick(DialogInterface dialog, int which) {
        if(which == DialogInterface.BUTTON_POSITIVE){
            cerrar_sesion.show();
            ParseUser.logOut();
            ParseUser currentUser = ParseUser.getCurrentUser();
            cerrar_sesion.hide();
            Intent intent = new Intent(MainActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.action_add:

                Intent intent = new Intent(this, AddActivity.class);
                startActivity(intent);

                break;

            case R.id.action_search:

                break;
            case R.id.action_logout:
                close.show();

                break;
            case R.id.home:
                Log.d("Tab", String.valueOf(AppUtil.tab));
                switch (AppUtil.tab){
                    case 1:
                        list_my.Reload();
                        break;
                    case 2:
                        list.Reload();
                        break;
                    case 3:
                        list.Reload();
                        break;
                }
                break;
            case R.id.action_profile:
                Intent intent3 = new Intent(this,ProfileActivity.class);
                startActivity(intent3);
                break;
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
    public void onItemSelectedList(int position) {
        dialog.show();
        AppUtil.positionSelected = position;
        Intent intent = new Intent(this,PlanActivity.class);
        startActivity(intent);

    }

    // metodo todavia no implementado.
    @Override
    public void onLugarSelected(double latitud, double longitud) {
        Toast.makeText(this, latitud + " " + longitud, Toast.LENGTH_LONG);
    }


    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public boolean onQueryTextSubmit(String query) {
        View search = findViewById(R.id.action_search);
        InputMethodManager imm = (InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(search.getWindowToken(), 0);
        AppUtil.search_btn=true;
        Log.d("Tab", String.valueOf(AppUtil.tab));
        AppUtil.searching = query.toString();
        switch (AppUtil.tab){
            case 1:
                list_my.search();
                break;
            case 2:
                list.search();
                break;
            case 3:
                list.search();
                break;
        }

        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public boolean onMenuItemActionExpand(MenuItem item) {


        return true;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem item) {
        switch (AppUtil.tab){
            case 1:
                list_my.Reload();
                break;
            case 2:
                list.Reload();
                break;
            case 3:
                list.Reload();
                break;
        }

        return true;
    }

    @Override
    public void ItemSeleccionado(int position) {
        AppUtil.positionSelectedMisPlanes = position;
        Intent intent = new Intent(this,MisPlanesActivity.class);
        startActivity(intent);

    }

}
