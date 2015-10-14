package com.example.frank.planspop.fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.frank.planspop.AppUtil.AppUtil;
import com.example.frank.planspop.R;
import com.example.frank.planspop.models.Plan;
import com.example.frank.planspop.parse.PlanParse;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class VerMisPlanesFragment extends Fragment implements View.OnClickListener, PlanParse.PlanParseInterface {


    public interface ActionVerMisPlanes{
        void editarInformacion(Boolean informacion, Boolean maps);
        void eliminarPlan(Boolean exito);
    }

    ActionVerMisPlanes actionVerMisPlanes;

    Context context;
    TextView nombre;
    Button btn_actualizarInformacion, btn_editarLugar, btn_eliminar;

    ProgressDialog progressDialog;

    public VerMisPlanesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        actionVerMisPlanes = (ActionVerMisPlanes) context;
        this.context= context;
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v;
        v=inflater.inflate(R.layout.fragment_ver_mis_planes, container, false);

        nombre = (TextView) v.findViewById(R.id.txt_Nombre_verMisPlanes);
        btn_actualizarInformacion = (Button) v.findViewById(R.id.btn_ActualizarInformacion);
        btn_editarLugar = (Button) v.findViewById(R.id.btn_editarLugar);
        btn_eliminar = (Button) v.findViewById(R.id.btn_Eliminar);

        btn_actualizarInformacion.setOnClickListener(this);
        btn_editarLugar.setOnClickListener(this);
        btn_eliminar.setOnClickListener(this);

        loadData();

        return v;
    }

    private void loadData() {

        if(AppUtil.positionSelectedMisPlanes!=-1) {

            nombre.setText(AppUtil.data_misPlanes.get(AppUtil.positionSelectedMisPlanes).getNombre());
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_ActualizarInformacion:
                actionVerMisPlanes.editarInformacion(true,false);
                break;

            case R.id.btn_editarLugar:
                actionVerMisPlanes.editarInformacion(false,true);
                break;

            case R.id.btn_Eliminar:

                Plan p = AppUtil.data_misPlanes.get(AppUtil.positionSelectedMisPlanes);
                PlanParse planParse = new PlanParse(this);
                progressDialog = new ProgressDialog(context);
                progressDialog.setMessage("Eliminando...");
                progressDialog.show();
                planParse.deletePlan(p);

                break;

        }

    }

    @Override
    public void done(boolean exito) {
        progressDialog.cancel();
        progressDialog.hide();
        if(exito) {
            actionVerMisPlanes.eliminarPlan(true);
        }
        else{
            actionVerMisPlanes.eliminarPlan(false);
        }
    }

    @Override
    public void resultPlan(boolean exito, Plan plan) {

    }

    @Override
    public void resultListPlans(boolean exito, List<Plan> planes) {

    }
}
