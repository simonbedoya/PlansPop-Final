package com.example.frank.planspop.parse;

import com.example.frank.planspop.models.Plan;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Frank on 6/10/2015.
 */
public class PlanParse implements SaveCallback, DeleteCallback, FindCallback<ParseObject> {




    public interface PlanParseInterface{
         void done(boolean exito);
         void resultPlan(boolean exito, Plan plan);
         void resultListPlans(boolean exito, List<Plan> planes);

    }


    PlanParseInterface planParseInterface;

    public static final String PLAN="Plan";
    public static final String C_ID="objectId";
    public static final String C_NOMBRE="nombre";
    public static final String C_DESCRIPCION="descripcion";
    public static final String C_IMAGEN="imagen";
    public static final String C_FECHA="fecha";
    public static final String C_LUGAR="lugar";
    public static final String C_ID_USER="id_user";
    public static final String C_CREATEAT="createdAt";
    public static final String C_DIRECCION="direccion";


    ParseObject parseObject;
    Plan plan;
    ParseFile parseFile;

    public PlanParse(PlanParseInterface planParseInterface) {
        this.planParseInterface = planParseInterface;
    }

    public void insertPlan(Plan plan) throws IOException {
      //  parseObject.put(C_ID_USER, ParseUser.getCurrentUser()); Se obtiene el que tiene la sesion activa
        this.plan = plan;

        if(plan.getImgPath()!=null){
            FileInputStream stream = new FileInputStream(plan.getImgPath());
            byte[] bytes = readFully(stream);
            parseFile = new ParseFile(plan.getNombre()+".jpg",bytes,"image/jpeg");
            parseFile.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if(e==null)
                        saveObject();
                }
            });
        }
        else{
            saveObject();
        }

    }


    private void saveObject(){
        ParseObject parseObject = new ParseObject(PLAN);

        ParseUser currentUser = ParseUser.getCurrentUser();
        parseObject.put(C_ID_USER, currentUser); //Se obtiene el que tiene la sesion activa
        parsePlan(parseObject, plan);
        if(plan.getImgPath()!=null){
            parseObject.put(C_IMAGEN, parseFile);
        }
        parseObject.saveInBackground(this);

    }

    public static byte [] readFully(InputStream is) throws  IOException{

        int len;
        int size = 1024;
        byte [] buf;

        if (is instanceof ByteArrayInputStream){
            size = is.available();
            buf = new byte[size];
            len = is.read(buf,0,size);

        }
        else{
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            buf = new byte[size];
            while ((len = is.read(buf, 0, size))!= -1)
                bos.write(buf, 0, len);
            buf = bos.toByteArray();
        }
        return buf;

    }


    public void updatePlan(final Plan plan){

        ParseQuery<ParseObject> query = ParseQuery.getQuery(PLAN);
        query.getInBackground(plan.getId(), new GetCallback<ParseObject>() {
            @Override
            public void done(final ParseObject object, ParseException e) {

                if(plan.getImgPath()!=null){
                    FileInputStream stream = null;
                    byte[] bytes= null;
                    try {
                        stream = new FileInputStream(plan.getImgPath());
                        bytes = readFully(stream);
                    } catch (FileNotFoundException e1) {
                        e1.printStackTrace();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }

                    parseFile = new ParseFile(plan.getNombre()+".jpg",bytes,"image/jpeg");
                    parseFile.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if(e==null)
                                upObject(object, plan);
                        }
                    });
                }
                else{
                    upObject(object,plan);
                }

            }
        });

    }
    private void upObject(ParseObject parseObject, Plan plan){
        parsePlan(parseObject, plan);
        if(plan.getImgPath()!=null){
            parseObject.put(C_IMAGEN, parseFile);
        }
        parseObject.saveInBackground(this);

    }

    public void deletePlan(final Plan plan){
        ParseQuery<ParseObject> query = ParseQuery.getQuery(PLAN);
        query.getInBackground(plan.getId(), new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                delete(object, plan);
            }
        });
    }

    private void delete(ParseObject parseObject, Plan plan){
        parsePlan(parseObject, plan);
        parseObject.deleteInBackground(this);
    }

    public void getPlanById(String Id){
        ParseQuery<ParseObject> query =  ParseQuery.getQuery(PLAN);

        query.getInBackground(Id, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    planParseInterface.resultPlan(true, parsePlanProceso(object));
                } else {
                    planParseInterface.resultPlan(false, null);
                }
            }
        });

    }

    public void getAllPlans(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery(PLAN);
        query.findInBackground(this);
    }

    public void getRecentPLanes(Date last){
        ParseQuery<ParseObject> query = ParseQuery.getQuery(PLAN);
        if(last!=null)
            query.whereGreaterThan(C_CREATEAT, last);
        query.orderByDescending(C_CREATEAT);
        query.findInBackground(this);

    }

    public void getPlanByPages(int limit, Date last){
        ParseQuery<ParseObject> query = ParseQuery.getQuery(PLAN);
        if (last!=null)
            query.whereLessThan(C_CREATEAT, last);
        query.orderByDescending(C_CREATEAT);
        query.setLimit(limit);
        query.findInBackground(this);
    }
    public void getPlanByName(String searching){
        ParseQuery<ParseObject> query = ParseQuery.getQuery(PLAN);
        query.whereStartsWith("nombre", searching);
        query.findInBackground(this);
    }
    public void getPlanUser(){
        ParseUser parseUser = ParseUser.getCurrentUser();
        ParseQuery<ParseObject> query = ParseQuery.getQuery(PLAN);
        query.whereEqualTo(C_ID_USER, parseUser);
        query.findInBackground(this);
    }

    @Override
    public void done(ParseException e) {
        if (e == null)
            planParseInterface.done(true);
        else
            planParseInterface.done(false);
    }


    @Override
    public void done(List<ParseObject> objects, ParseException e) {
        if(e == null){
            List<Plan> planes = new ArrayList<>();
            for (int i = 0; i<objects.size(); i++){
                planes.add(parsePlanProceso(objects.get(i)));
            }
            planParseInterface.resultListPlans(true, planes);
        }
        else{
            planParseInterface.resultListPlans(false, null);
        }
    }

    private void parsePlan (ParseObject parseObject, Plan plan){
        parseObject.put(C_NOMBRE, plan.getNombre());
        parseObject.put(C_DESCRIPCION, plan.getDescripcion());
        parseObject.put(C_FECHA, plan.getFecha());
        parseObject.put(C_LUGAR, plan.getLugar());
        parseObject.put(C_DIRECCION,plan.getDireccion());
       // ParseRelation<ParseObject> relation = parseObject.getRelation(C_ID_LUGAR); // se añade el lugar
       // relation.add(lugar);

    }

    // Retorna un plan obtenido de una busqueda
    private Plan parsePlanProceso(ParseObject parseObject) {
        Plan plan = new Plan();
        plan.setId(parseObject.getObjectId());
        plan.setNombre(parseObject.getString(C_NOMBRE));
        plan.setDescripcion(parseObject.getString(C_DESCRIPCION));
        plan.setFecha(parseObject.getString(C_FECHA));
        plan.setDireccion(parseObject.getString(C_DIRECCION));

        // Para obtener el lugar del plan
        ParseGeoPoint parseGeoPoint = parseObject.getParseGeoPoint(C_LUGAR);
        plan.setLugar(parseGeoPoint);
        // Para obtener el id del usuario que creo el plan.
        ParseObject user = parseObject.getParseObject(C_ID_USER);
        plan.setUser(user);

        plan.setCreatedAt(parseObject.getCreatedAt());
        ParseFile file = parseObject.getParseFile(C_IMAGEN);
        plan.setImagen(file.getUrl());

        return plan;
    }

}
