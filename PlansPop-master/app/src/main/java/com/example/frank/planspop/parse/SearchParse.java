package com.example.frank.planspop.parse;

import android.util.Log;

import com.example.frank.planspop.AppUtil.AppUtil;
import com.example.frank.planspop.models.Plan;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sbv23 on 11/10/2015.
 */
public class SearchParse {

    Plan p = new Plan();



    public void Search(String searching){

        AppUtil.data = new ArrayList<Plan>();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Plan");
        query.whereStartsWith("nombre", searching);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    Log.d("busqueda", "encontrados " + objects.size());
                    for (int i=0; i<objects.size();i++){
                        Log.d("encontrados", objects.get(i).getString("nombre"));
                        p.setId(objects.get(i).getString("objectId"));
                        p.setNombre(objects.get(i).getString("nombre"));
                        p.setDescripcion(objects.get(i).getString("descripcion"));
                        p.setFecha(objects.get(i).getString("fecha"));
                        p.setLugar(objects.get(i).getParseGeoPoint("lugar"));
                        AppUtil.data.add(p);


                    }

                } else {
                    Log.d("Error", e.getMessage());
                }
            }
        });

    }

}
