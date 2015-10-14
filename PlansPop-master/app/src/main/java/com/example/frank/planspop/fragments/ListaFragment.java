package com.example.frank.planspop.fragments;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

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
public class ListaFragment extends TitleFragment implements AdapterView.OnItemClickListener, PlanParse.PlanParseInterface {

    public static int POSITION=-1;
    static final int PAGE=10;
    /*
        public static final int TYPE_RECENTS=0;
        public static final int TYPE_PAGE=1;
        public static final int TYPE_ALL=2;
    */
    int type;


    public interface OnItemSelectedList {
        void onItemSelectedList(int position);
    }

    OnItemSelectedList onItemSelectedList;

    ListView list;
    // PlanAdapter adapter;

    Context context;

    PlanParse planParse= new PlanParse(this);

    //  SwipeRefreshLayout refreshLayout;



    public ListaFragment() {
    }

    @Override
    public void onAttach(Context context) {
        this.context = context;
        onItemSelectedList = (OnItemSelectedList) context;
        super.onAttach(context);
        AppUtil.tab = 2;

        AppUtil.data = new ArrayList<>();
        AppUtil.positionSelected = -1;

        AppUtil.adapter = new PlanAdapter(context, AppUtil.data);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_lista, container, false);
/*
        refreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.refresh);
        refreshLayout.setOnRefreshListener(this);

        int color1 = ContextCompat.getColor(context, R.color.colorPrimary);
        int color2 = ContextCompat.getColor(context,R.color.primary_dark_material_dark);
        int color3 =  ContextCompat.getColor(context, R.color.accent_material_light);

        refreshLayout.setColorSchemeColors(color1,color2,color3);
*/

        list = (ListView) v.findViewById(R.id.list);
        list.setAdapter(AppUtil.adapter);
        list.setOnItemClickListener(this);
        AppUtil.adapter.notifyDataSetChanged();



        planParse.getAllPlans();

        //planParse.getPlanByPages(PAGE, null);
        //       type = PAGE;
        //planParse.getPlanById("YMjI8yzg5B");

        //list.setOnScrollListener(this);

        return v;
    }
    public void  search (){
        Log.d("Prueba", "LLego" + AppUtil.searching);
        //PlanParse planp = new PlanParse(this);
        planParse.getPlanByName(AppUtil.searching);

    }
    public void Reload(){
        planParse.getAllPlans();
    }
    //public void Relation(Plan plan, ParseObject userid){planParse.updatePlan(plan, userid);}


    @Override
    public void onResume() {
        AppUtil.adapter.notifyDataSetChanged();
        super.onResume();
    }

    @Override
    public String getTitle() {
        return "Planes";
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        onItemSelectedList.onItemSelectedList(position);
        POSITION = position;
        Log.d("POsicion", String.valueOf(position));

    }


    //region ParsePlan
    @Override
    public void done(boolean exito) {

    }

    @Override
    public void resultPlan(boolean exito, Plan plan) {
        if (exito == true)
            Log.i("resultPLan exito:", "es verdadero");

        else
            Log.i("resultPLan exito:", "es falso");

    }

    @Override
    public void resultListPlans(boolean exito, List<Plan> planes) {
        if (exito == true) {

            AppUtil.data.clear();
            for (int i = 0; i < planes.size(); i++) {
                AppUtil.data.add(planes.get(i));
            }

            //PlanAdapter adap = new PlanAdapter(AppUtil.cont,AppUtil.search);

        }
        else
            Log.i("resultPLan exito:", "es falso");

        AppUtil.adapter.notifyDataSetChanged();
    }

    //endregion

/*
    //region Scroll final
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if(firstVisibleItem+visibleItemCount == totalItemCount && AppUtil.data.size() > 0){
       //     type = TYPE_PAGE;
            planParse.getPlanByPages(PAGE, AppUtil.data.get(totalItemCount-1).getCreatedAt());
        }
    }
    //endregion

    @Override
    public void onRefresh() {
        type=TYPE_RECENTS;
        planParse.getRecentPLanes(AppUtil.data.get(0).getCreatedAt());
    }*/

}
