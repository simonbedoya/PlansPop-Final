package com.example.frank.planspop.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.frank.planspop.R;
import com.example.frank.planspop.models.Plan;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Frank on 13/10/2015.
 */
public class PlanAdapter extends BaseAdapter {

    List<Plan> data;
    Context context;

    public PlanAdapter(Context context, List<Plan> data) {
        this.data = data;
        this.context = context;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v;

        if(convertView == null)
            v = convertView.inflate(context, R.layout.template_plans, null);
        else
            v = convertView;

        Plan p = (Plan) getItem(position);

        TextView nombre = (TextView) v.findViewById(R.id.txt_nombre);
        String name = p.getNombre();
        String primeraMayuscula = name.charAt(0)+"";
        primeraMayuscula.toUpperCase();
        name.replaceFirst(name.charAt(0)+"",primeraMayuscula);
        String name_m = name.toUpperCase();
        nombre.setText(name_m);

        TextView descripcion = (TextView) v.findViewById(R.id.txt_descripcion);
        descripcion.setText(p.getDescripcion());

        TextView lugar = (TextView) v.findViewById(R.id.txt_lugar);
        lugar.setText(p.getDireccion());

        String[] formatDate = p.getFecha().split(" ");
        String date = formatDate[0];
        String hour = formatDate[1];
        String am_pm = formatDate[2];

        TextView fecha = (TextView) v.findViewById(R.id.txt_fecha);
        fecha.setText(date);

        TextView hora = (TextView) v.findViewById(R.id.txt_hora);
        hora.setText(hour+" "+am_pm);

        ImageView img = (ImageView) v.findViewById(R.id.img_fondo);
        Picasso.with(context)
                .load(Uri.parse(p.getImagen()))
                .into(img);

        return v;
    }
}
