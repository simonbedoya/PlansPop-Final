package com.example.frank.planspop.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.frank.planspop.AppUtil.AppUtil;
import com.example.frank.planspop.LoginActivity;
import com.example.frank.planspop.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class Register_IpFragment extends Fragment {

    EditText edt_email;
    Button btn_next;
    public static String email;


    public Register_IpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_register__ip, container, false);
        initUI(v);

        return v;
    }
    public void initUI(View v) {

    }

    @Override
    public void onStart() {
        super.onStart();
        final Register_InfoPFragment reginfo = new Register_InfoPFragment();
        btn_next = (Button) getActivity().findViewById(R.id.btn_nextip);
        edt_email = (EditText) getActivity().findViewById(R.id.edt_email);

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edt_email.getWindowToken(), 0);
                LoginActivity log = new LoginActivity();

                AppUtil.email = edt_email.getText().toString();
                if (AppUtil.email.matches("")){
                        Toast toast = Toast.makeText(getContext(), "Por favor ingrese correo electronico",Toast.LENGTH_SHORT);
                        toast.show();
                }else {
                    android.support.v4.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.container, reginfo);
                    ft.commit();
                }
            }
        });

    }
}
