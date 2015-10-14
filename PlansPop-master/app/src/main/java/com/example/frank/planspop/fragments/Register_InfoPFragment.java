package com.example.frank.planspop.fragments;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.frank.planspop.AppUtil.AppUtil;
import com.example.frank.planspop.LoginActivity;
import com.example.frank.planspop.R;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class Register_InfoPFragment extends Fragment implements DialogInterface.OnClickListener  {

    EditText birthdate,cmp_name, userr, passw;
    private int mYear, mMonth, mDay;
    Button next;
    RadioButton ho,mu;
    AlertDialog close, register_ok;
    ProgressDialog dialog;


    public Register_InfoPFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_register__info, container, false);
        Register_IpFragment reg = new Register_IpFragment();

        next = (Button) v.findViewById(R.id.btn_nextip);
        cmp_name = (EditText) v.findViewById(R.id.edt_name);
        userr = (EditText) v.findViewById(R.id.edt_user);
        passw = (EditText) v.findViewById(R.id.edt_pass);
        birthdate = (EditText) v.findViewById(R.id.edt_fechan);

        Confirmation_close();
        Confirmation_register();

        dialog = new ProgressDialog(getContext());
        dialog.setMessage("Procesando la información.....");

        birthdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dpd = new DatePickerDialog(v.getContext(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // Setear valor en editText
                                birthdate.setText(dayOfMonth + "-"
                                        + (monthOfYear + 1) + "-" + year);
                            }
                        }, mYear, mMonth, mDay);
                dpd.show();
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(passw.getWindowToken(), 0);
                dialog.show();
                AppUtil.c_name = cmp_name.getText().toString();
                AppUtil.b_date = birthdate.getText().toString();
                AppUtil.username = userr.getText().toString();
                AppUtil.pass = passw.getText().toString();
                String ma = AppUtil.email;
                String sexo = AppUtil.sex;

                ParseUser user = new ParseUser();
                user.setUsername(AppUtil.username);
                user.setPassword(AppUtil.pass);
                user.setEmail(AppUtil.email);
                user.put("name", AppUtil.c_name);
                user.put("b_date",AppUtil.b_date);
                user.put("sex",AppUtil.sex);

                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        dialog.hide();
                        if (e == null) {
                            Log.i("Sign", "Correcto");
                            register_ok.show();
                            AppUtil.c_name="";
                            AppUtil.username="";
                            AppUtil.email="";
                            AppUtil.b_date="";
                            AppUtil.pass="";
                            AppUtil.sex="";

                        } else {
                            Log.i("e", e.getMessage());
                            String[] palabras = e.getMessage().split(" ");
                            for (String i : palabras) {
                                if (i.equals("username")) {
                                    Toast toast = Toast.makeText(getContext(), "Error: Username already exist", Toast.LENGTH_SHORT);
                                    toast.show();
                                    userr.requestFocus();
                                    userr.setBackgroundColor(Color.RED);
                                } else {
                                    close.show();
                                    /*Toast toast = Toast.makeText(getContext(), "Error: Email already exist", Toast.LENGTH_SHORT);
                                    toast.show();
                                        Register_IpFragment regip = new Register_IpFragment();
                                        android.support.v4.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
                                        ft.replace(R.id.container, regip);
                                        ft.commit();*/
                                }



                            }
                            Log.i("Sign", "Incorrecto");
                        }
                    }
                });
            }
        });

        return v;
    }


    public void Confirmation_close(){
        close = new AlertDialog.Builder(getContext())
                .setTitle("Problemas")
                .setMessage("El correo electronico " + AppUtil.email + " ya se encuentra registrado en el sistema.")
                .setPositiveButton("Ok", this)
                .create();
    }
    public void Confirmation_register(){
        register_ok = new AlertDialog.Builder(getActivity())
                .setTitle("Felicitaciones!!")
                .setMessage("Bienvenid@!! " + AppUtil.username + " a la comunidad PlanPOP, te invitamos a confirmar tu registro via email y acceder a la app.")
                .setNegativeButton("Ok", this)
                .create();
    }


    public void onClick(DialogInterface dialog, int which) {
        if(which == DialogInterface.BUTTON_POSITIVE){
            Register_IpFragment regip = new Register_IpFragment();
            android.support.v4.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.container, regip);
            ft.commit();
        }
        if(which == DialogInterface.BUTTON_NEGATIVE){
            Intent intent = new Intent(getActivity(),LoginActivity.class);
            startActivity(intent);
            getActivity().finish();
        }
    }


}
