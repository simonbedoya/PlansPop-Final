package com.example.frank.planspop.AppUtil;

import android.widget.LinearLayout;

import com.example.frank.planspop.adapters.PlanAdapter;
import com.example.frank.planspop.models.Lugar;
import com.example.frank.planspop.models.Plan;

import java.util.List;

/**
 * Created by Frank on 13/10/2015.
 */
    public class AppUtil {
        public static String email;
        public static String c_name;
        public static String b_date;
        public static String sex = "Hombre";
        public static String username;
        public static String pass;
        public static List<Plan> data;
        public static List<Plan> search;
        public static int positionSelected;
        public static String searching;
        public static int flag=0;
        public static PlanAdapter adapter;
        public static int aux_asistant=0;
        public static List<Plan> data_misPlanes;
        public static List<Lugar> lugares;
        public static int positionSelectedMisPlanes;
        public static int tab=0;
        public static PlanAdapter adapter_list;
        public static Boolean search_btn=false;
}


