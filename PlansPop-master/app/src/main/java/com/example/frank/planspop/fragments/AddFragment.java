package com.example.frank.planspop.fragments;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.frank.planspop.R;

import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddFragment extends Fragment implements View.OnClickListener {


    public  interface ActionListenerAddFragment{
        void siguiente(String nombre, String descripcion, String fechaHora);

    }

    private int mYear, mMonth, mDay, mHora,mMinuto;


    ActionListenerAddFragment actionListenerAddFragment;

    EditText edit_nombre,edit_descripcion,edit_fecha, edit_hora;
    Button btn_siguiente;

    Context context;

    public AddFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        actionListenerAddFragment = (ActionListenerAddFragment) context;
        this.context = context;
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v;
        v = inflater.inflate(R.layout.fragment_add, container, false);

        edit_nombre = (EditText) v.findViewById(R.id.nombre);
        edit_descripcion = (EditText) v.findViewById(R.id.descripcion);
        edit_fecha = (EditText) v.findViewById(R.id.fecha);
        edit_hora = (EditText) v.findViewById(R.id.hora);

        btn_siguiente = (Button) v.findViewById(R.id.btn_siguienteAddFragment);


        //<editor-fold desc="Botton fecha">

        edit_fecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePicker = new DatePickerDialog(v.getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                edit_fecha.setText(dayOfMonth + "/" + monthOfYear + "/" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePicker.show();
            }
        });
        //</editor-fold>

        //<editor-fold desc="Hora">
        edit_hora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                mHora = calendar.get(calendar.HOUR_OF_DAY);
                mMinuto  = calendar.get(calendar.MINUTE);

                TimePickerDialog timePicker = new TimePickerDialog(v.getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String am_pm;
                        String hora;

                        if(hourOfDay < 12) {
                            am_pm = "AM";
                            if(hourOfDay==0){
                                hourOfDay=12;
                            }
                        } else {
                            if(hourOfDay!=12){
                                hourOfDay=hourOfDay-12;
                            }
                            am_pm = "PM";
                        }
                        if(hourOfDay<10){
                            if(minute<10){
                                hora = "0"+hourOfDay+":"+"0"+minute+" "+am_pm;
                            }
                            else{
                                hora = "0"+hourOfDay+":"+minute+" "+am_pm;
                            }
                        }
                        else{
                            hora= hourOfDay+":"+minute+" "+am_pm;
                        }

                        edit_hora.setText(hora);
                    }
                },mHora,mMinuto,false);
                timePicker.show();
            }
        });
        //</editor-fold>


        btn_siguiente.setOnClickListener(this);

        //    btn_lugar.setOnClickListener(this);
        //   btn_lugar = (Button) v.findViewById(R.id.btn_marcar_lugar);
        return v;
    }

    @Override
    public void onClick(View v) {
        String name = edit_nombre.getText().toString();
        String description = edit_descripcion.getText().toString();
        String Sfecha = edit_fecha.getText().toString();
        String Shora = edit_hora.getText().toString();

        if(!name.equals("") && !description.equals("") && !Sfecha.equals("") && !Shora.equals("")) {
            actionListenerAddFragment.siguiente(name, description, Sfecha + " " + Shora);
        }
        else {
            Toast.makeText(context,"Todos los campos son requeridos", Toast.LENGTH_SHORT).show();
        }
            /*case R.id.btn_add:
                Plan plan = new Plan();
                plan.setNombre(edit_nombre.getText().toString());
                plan.setDescripcion(edit_descripcion.getText().toString());
                plan.setFecha(txt_fecha.getText().toString());
                plan.setImgPath(ImgF.getPath());

                PlanParse planParse = new PlanParse(this);

                try {
                    planParse.insertPlan(plan);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                actionListenerAddFragment.terminarFragment(true);

                break;*/

    }

}
