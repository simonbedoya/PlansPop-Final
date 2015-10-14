package com.example.frank.planspop.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.frank.planspop.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class Register_cypFragment extends Fragment {

    Button btn_next;

    public Register_cypFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_register_cyp, container, false);
        initUI(v);
        return v;
    }
    public void initUI(View v) {
        final Register_IpFragment regip = new Register_IpFragment();
        btn_next = (Button) v.findViewById(R.id.btn_nextcyp);

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.support.v4.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.container, regip);
                ft.commit();
            }
        });
    }
    @Override
    public void onStart() {
        super.onStart();
    }
}
