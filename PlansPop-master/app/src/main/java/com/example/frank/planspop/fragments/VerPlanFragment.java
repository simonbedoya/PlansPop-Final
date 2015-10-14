package com.example.frank.planspop.fragments;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.frank.planspop.AppUtil.AppUtil;
import com.example.frank.planspop.MainActivity;
import com.example.frank.planspop.R;
import com.parse.CountCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class VerPlanFragment extends Fragment implements View.OnClickListener {


    public interface ActionVerPLanFragment{
        void cargarMapa(double latitud, double longitud);
    }

    ActionVerPLanFragment actionVerPLanFragment;

    TextView nombre, descripcion,fecha, hora, plancreador, asistentes, estado;
    Button ver_mapa, ir;
    String id_plan = AppUtil.data.get(ListaFragment.POSITION).getId();

    MainActivity main = new MainActivity();


    ListaFragment plan = new ListaFragment();

    Context context;

    public VerPlanFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        actionVerPLanFragment = (ActionVerPLanFragment) context;
        this.context=context;
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View v = inflater.inflate(R.layout.fragment_ver_plan, container, false);

        nombre = (TextView) v.findViewById(R.id.plan_nombre);
        descripcion = (TextView) v.findViewById(R.id.plan_descripcion);
        fecha = (TextView) v.findViewById(R.id.plan_fecha);
        hora = (TextView) v.findViewById(R.id.plan_Hora);
        plancreador = (TextView) v.findViewById(R.id.plan_creador);
        asistentes = (TextView) v.findViewById(R.id.plan_numero_participantes);
        ver_mapa = (Button) v.findViewById(R.id.btn_ver_lugar);
        ir = (Button) v.findViewById(R.id.btn_asistir);
        estado = (TextView) v.findViewById(R.id.estado);
        Log.d("Posicion", String.valueOf(ListaFragment.POSITION));

        Load_data();
        Count_assitant();
        Ver_asist();
        main.getDialog().hide();
        ir.setOnClickListener(this);
        ver_mapa.setOnClickListener(this);

        return v;
    }

    public void Load_data(){
        if (ListaFragment.POSITION!=-1){
            nombre.setText(AppUtil.data.get(ListaFragment.POSITION).getNombre());
            descripcion.setText(AppUtil.data.get(ListaFragment.POSITION).getDescripcion());
            String[] separar;
            separar = AppUtil.data.get(ListaFragment.POSITION).getFecha().split(" ");
            fecha.setText(separar[0]);
            hora.setText(separar[1]);
            ParseObject user = AppUtil.data.get(ListaFragment.POSITION).getUser();
            try {
                plancreador.setText(user.fetchIfNeeded().getString("name"));

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    public void Count_assitant(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Plan");
        query.getInBackground(id_plan, new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    ParseRelation relation = object.getRelation("Asistentes");
                    ParseQuery query = relation.getQuery();
                    query.countInBackground(new CountCallback() {
                        @Override
                        public void done(int count, ParseException e) {
                            if (e == null) {
                                Log.d("score", "Sean has played " + count + " games");
                                asistentes.setText(Integer.toString(count));

                            } else {

                            }

                        }
                    });

                }
            }
        });
    }

    public void Ver_asist(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Plan");
        query.getInBackground(id_plan, new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    ParseRelation relation = object.getRelation("Asistentes");
                    ParseQuery<ParseObject> query = relation.getQuery();
                    query.whereEqualTo("objectId", ParseUser.getCurrentUser().getObjectId());
                    query.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> objects, ParseException e) {
                            if (objects.size() == 1) {
                                Log.d("Asis", "SI");

                                ir.setVisibility(View.VISIBLE);
                                ir.setText("NO ASISTIR");
                                ir.setBackgroundColor(Color.RED);
                                estado.setText("ASISTIRE");
                            } else {

                                ir.setVisibility(View.VISIBLE);
                                ir.setText("ASISTIR");
                                ir.setBackgroundColor(Color.GREEN);
                                Log.d("Asis", "NO");
                            }
                        }
                    });
                }
            }
        });
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_asistir:

                final ParseUser user = ParseUser.getCurrentUser();
                String id_plan = AppUtil.data.get(ListaFragment.POSITION).getId();
                ParseQuery<ParseObject> query = ParseQuery.getQuery("Plan");
                query.getInBackground(id_plan, new GetCallback<ParseObject>() {
                    public void done(ParseObject object, ParseException e) {
                        if (e == null) {
                            // Now let's update it with some new data. In this case, only cheatMode and score
                            // will get sent to the Parse Cloud. playerName hasn't changed.
                            ParseRelation<ParseObject> relation = object.getRelation("Asistentes");

                            if (AppUtil.aux_asistant == 0) {
                                relation.add(user);
                                ir.setText("NO ASISTIR");
                                estado.setText("ASISTIRE");
                                ir.setBackgroundColor(Color.RED);
                                AppUtil.aux_asistant = 1;

                                Log.d("Asistir", "SI");
                            } else {
                                relation.remove(user);
                                ir.setText("ASISTIR");
                                ir.setBackgroundColor(Color.GREEN);
                                estado.setText("---");
                                Log.d("Asistir", "NO");
                                AppUtil.aux_asistant = 0;
                            }

                            object.saveInBackground();
                            Count_assitant();

                        }
                    }
                });
                break;

            case R.id.btn_ver_lugar:
                if (ListaFragment.POSITION!=-1){

                    actionVerPLanFragment.cargarMapa(AppUtil.data.get(ListaFragment.POSITION).getLugar().getLatitude(),
                            AppUtil.data.get(ListaFragment.POSITION).getLugar().getLongitude());
                }


                break;
        }
    }
}
