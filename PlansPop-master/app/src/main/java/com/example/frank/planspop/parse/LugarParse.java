package com.example.frank.planspop.parse;

import com.example.frank.planspop.models.Lugar;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Frank on 12/10/2015.
 */
public class LugarParse implements SaveCallback, FindCallback<ParseObject> {



    public interface LugarParseInterface{
        void resultListLugares(Boolean exito, List<Lugar> lugares);
    }

    public static final String LUGARES = "Lugares";
    public static final String L_NOMBRE = "nombre";
    public static final String L_DIRECCION = "direccion";
    public static final String L_UBICACION = "ubicacion";

    LugarParseInterface lugarParseInterface;

    public LugarParse(LugarParseInterface lugarParseInterface) {
        this.lugarParseInterface = lugarParseInterface;
    }

    public void insertLugar(Lugar lugar){
        ParseObject parseObject= new ParseObject(LUGARES);
        llenarLugar(parseObject, lugar);
        parseObject.saveInBackground(this);
    }

    public void updateLugar(Lugar lugar){

    }

    public void deleteLugar(Lugar lugar){

    }

    public void getAllLugares(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery(LUGARES);

        query.findInBackground(this);
    }

    private void llenarLugar(ParseObject parseObject, Lugar lugar){
        parseObject.put(L_NOMBRE,lugar.getNombre());
        parseObject.put(L_DIRECCION,lugar.getDireccion());
        parseObject.put(L_UBICACION, lugar.getUbicacion());
    }

    @Override
    public void done(ParseException e) {

    }

    @Override
    public void done(List<ParseObject> objects, ParseException e) {
        if(e==null) {
            List<Lugar> lugares = new ArrayList<>();
            for(int i=0; i < objects.size() ; i++){
                lugares.add(obtenerLugar(objects.get(i)));
            }
            lugarParseInterface.resultListLugares(true, lugares);
        }
        else{
            lugarParseInterface.resultListLugares(false,null);
        }
    }

    private Lugar obtenerLugar(ParseObject parseObject){
        Lugar lugar = new Lugar();
        lugar.setNombre(parseObject.getString(L_NOMBRE));
        lugar.setDireccion(parseObject.getString(L_DIRECCION));
        lugar.setUbicacion(parseObject.getParseGeoPoint(L_UBICACION));
        return lugar;

    }
}
