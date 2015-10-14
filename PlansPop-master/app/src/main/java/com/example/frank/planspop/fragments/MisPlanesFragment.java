package com.example.frank.planspop.fragments;


import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.frank.planspop.AddActivity;
import com.example.frank.planspop.AppUtil.AppUtil;
import com.example.frank.planspop.R;
import com.example.frank.planspop.adapters.PlanAdapter;
import com.example.frank.planspop.models.Plan;
import com.example.frank.planspop.parse.PlanParse;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MisPlanesFragment extends TitleFragment implements PlanParse.PlanParseInterface, AdapterView.OnItemClickListener, View.OnClickListener {

    public interface ActionMisPlanesFragment{
        void ItemSeleccionado(int position);
    }

    ActionMisPlanesFragment actionMisPlanesFragment;
    ImageView imageView;
    ListView list_misPlanes;
    LinearLayout linearLayoutSin, linearLayoutCon;

    Context context;


    PlanParse planParse = new PlanParse(this);

    public MisPlanesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        actionMisPlanesFragment = (ActionMisPlanesFragment) context;
        this.context = context;
        AppUtil.positionSelectedMisPlanes = -1;
        AppUtil.data_misPlanes = new ArrayList<>();
        AppUtil.tab = 1;

        AppUtil.adapter_list = new PlanAdapter(context, AppUtil.data_misPlanes);
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_mis_planes, container, false);

        linearLayoutSin = (LinearLayout) v.findViewById(R.id.linear_sinPLanes);
        linearLayoutCon = (LinearLayout) v.findViewById(R.id.linear_conPlanes);

        list_misPlanes = (ListView) v.findViewById(R.id.mis_planes_list);
        list_misPlanes.setAdapter(AppUtil.adapter_list);
        list_misPlanes.setOnItemClickListener(this);
        AppUtil.adapter_list.notifyDataSetChanged();

        imageView = (ImageView) v.findViewById(R.id.img_agregarPlan);

        imageView.setOnClickListener(this);

        planParse.getPlanUser();


        return v;
    }

    @Override
    public void onPause() {
        Log.d("Pause", "aqui entra");
        super.onPause();
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getActivity(), AddActivity.class);
        startActivity(intent);
        getActivity().finish();

    }

    @Override
    public void onResume() {
        Log.d("Resume","aqui entra");
        AppUtil.adapter_list.notifyDataSetChanged();
        super.onResume();
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        actionMisPlanesFragment.ItemSeleccionado(position);
    }

    @Override
    public void done(boolean exito) {

    }

    @Override
    public void resultPlan(boolean exito, Plan plan) {

    }
    public void  search (){
        Log.d("Prueba", "LLego" + AppUtil.searching);
        //PlanParse planp = new PlanParse(this);
        planParse.getPlanByName(AppUtil.searching);

    }
    public void Reload(){
        planParse.getPlanUser();
    }

    @Override
    public void resultListPlans(boolean exito, List<Plan> planes) {
        if(exito){
            if(planes.size()>0) {
                if (!AppUtil.search_btn){
                    verlista();
                }

                AppUtil.data_misPlanes.clear();
                for (int i = 0; i < planes.size(); i++) {
                    AppUtil.data_misPlanes.add(planes.get(i));
                }
                AppUtil.adapter_list.notifyDataSetChanged();
            }else{
                if (!AppUtil.search_btn) {
                    veragregar();
                }
                AppUtil.data_misPlanes.clear();
                for (int i = 0; i < planes.size(); i++) {
                    AppUtil.data_misPlanes.add(planes.get(i));
                }
                AppUtil.adapter_list.notifyDataSetChanged();
            }
        }
        else
            Log.i("resultPLan exito:", "es falso");
    }

    public void verlista(){

        linearLayoutCon.setVisibility(View.VISIBLE);
        linearLayoutSin.setVisibility(View.GONE);
    }
    public void veragregar(){

        linearLayoutCon.setVisibility(View.GONE);
        linearLayoutSin.setVisibility(View.VISIBLE);
    }

    @Override
    public String getTitle() {
        return "Mis Planes";
    }
}
