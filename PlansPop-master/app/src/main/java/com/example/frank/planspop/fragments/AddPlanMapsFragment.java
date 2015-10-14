package com.example.frank.planspop.fragments;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.frank.planspop.R;
import com.example.frank.planspop.models.Lugar;
import com.example.frank.planspop.parse.LugarParse;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddPlanMapsFragment extends Fragment implements DialogInterface.OnClickListener,View.OnClickListener, GoogleMap.OnMapLongClickListener, GoogleMap.OnMarkerClickListener, LugarParse.LugarParseInterface {

    public interface OnLugarSelected{
        void onLugarSelected(double latitud, double longitud, String direccion, String nombreLugar);
    }

    public AddPlanMapsFragment() {
        // Required empty public constructor
    }

    OnLugarSelected onLugarSelected;

    GoogleMap mMap;

    Context context;
    EditText edt_buscar;
    Button btn_busca;

    CharSequence[] options = {"Elegir lugar", "Cancelar"};

    private Marker marker = null;
    private MarkerOptions markerOptions = null;


    @Override
    public void onAttach(Context context) {
        onLugarSelected = (OnLugarSelected) context;
        this.context = context;
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v;
        v = inflater.inflate(R.layout.fragment_add_plan_maps, container, false);

        edt_buscar = (EditText) v.findViewById(R.id.edit_buscarMaps_add);
        btn_busca = (Button) v.findViewById(R.id.btn_buscar_mapaMaps_add);

        btn_busca.setOnClickListener(this);
        setUpMapIfNeeded();
        mMap.setOnMapLongClickListener(this);
        mMap.setOnMarkerClickListener(this);

        LugarParse lugarParse = new LugarParse(this);
        lugarParse.getAllLugares();

        // Inflate the layout for this fragment
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    @Override
    public void resultListLugares(Boolean exito, List<Lugar> lugares) {
        if(exito == true) {

            MarkerOptions markerLugaresOption = new MarkerOptions();
            LatLng latitudLongitud;

            String nameLugar, direccionLugar;
            double latitud, longitud;


            for (int i = 0; i < lugares.size(); i++) {

                nameLugar = lugares.get(i).getNombre();
                direccionLugar = lugares.get(i).getDireccion();
                latitud = lugares.get(i).getUbicacion().getLatitude();
                longitud = lugares.get(i).getUbicacion().getLongitude();

                latitudLongitud = new LatLng(latitud, longitud);
                markerLugaresOption.position(latitudLongitud);
                markerLugaresOption.title(nameLugar + " " + direccionLugar);

                mMap.addMarker(markerLugaresOption);

            }
        }
        else {
            Toast.makeText(context,"ERROR CARGANDO LUGARES",Toast.LENGTH_SHORT).show();
        }
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapaAdd)).getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(2.4448143, -76.6147395), 13.0f));
    }

    private void setUpMap() {
        //mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
        mMap.setMyLocationEnabled(true);

    }

    @Override
    public void onClick(View v) {
        String localizacion = edt_buscar.getText().toString();
        List<Address> addressList = null;

        if(!localizacion.equals("")){
            Geocoder geocoder = new Geocoder(context);
            try {
                addressList = geocoder.getFromLocationName(localizacion, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Address address = addressList.get(0);
            LatLng bus_latLng = new LatLng(address.getLatitude(),address.getLongitude());
            mMap.addMarker(new MarkerOptions().position(bus_latLng).title(address.getAddressLine(0)));



            mMap.animateCamera(CameraUpdateFactory.newLatLng(bus_latLng));
        }
        else{
            Toast.makeText(context, "No se ingreso ninguna palabra clave", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onMapLongClick(final LatLng latLng) { // marcar un lugar

        if(marker == null) {
            final AlertDialog.Builder alert = new AlertDialog.Builder(context);
            alert.setTitle("Marcar Lugar"); //Set Alert dialog title here
            alert.setMessage("Nombre Lugar:"); //Message here
            final EditText input = new EditText(context);
            alert.setView(input);
            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    String nombreLugar = input.getEditableText().toString();
                    String direccion = ObtenerDireccion(latLng.latitude,latLng.longitude);

                    markerOptions = new MarkerOptions().position(latLng).title(nombreLugar+" - "+direccion);
                    marker = mMap.addMarker(markerOptions);


                }
            });
            alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog alertDialog = alert.create();
            alertDialog.show();
        }
        else {
            marker.remove();
            marker = null;
        }


    }



    @Override
    public boolean onMarkerClick(final Marker marker) { // elegir el lugar
        this.marker = marker;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Elige una Opción");
        builder.setItems(options,this);
        builder.show();

        return false;
    }


    @Override
    public void onClick(DialogInterface dialog, int which) {

        if (options[which] == "Elegir lugar") {
            String[] title = marker.getTitle().split("-");
            String nombre = title[0];

            String direccion = ObtenerDireccion(marker.getPosition().latitude,marker.getPosition().longitude);
            onLugarSelected.onLugarSelected(marker.getPosition().latitude,marker.getPosition().longitude, direccion, nombre);

        } else if (options[which] == "Cancelar") {
            dialog.dismiss();
        }
    }



    private String ObtenerDireccion(double lati, double longi){
        Geocoder geocoderDireccion = new Geocoder(context);
        List<Address> addresses = null;
        try {
            addresses = geocoderDireccion.getFromLocation(lati,longi,1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Address address = addresses.get(0);
        String direccion = address.getAddressLine(0);
        return  direccion;
    }

}
