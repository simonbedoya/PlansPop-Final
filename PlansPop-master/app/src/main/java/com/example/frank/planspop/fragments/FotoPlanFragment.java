package com.example.frank.planspop.fragments;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.frank.planspop.AddActivity;
import com.example.frank.planspop.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 */
public class FotoPlanFragment extends Fragment implements View.OnClickListener {

    public interface ActionListenerFotoPlanFragment{
        void siguienteFotoPlanFragment(File imagen);
    }

    public static final int CAMERA=101;
    public static final int SELECT_IMAGE=102;
    public static final int RESULT_OK = -1;

    ActionListenerFotoPlanFragment actionListenerFotoPlanFragment;

    private File ImgF;

    ImageView img;
    Button btn_tomarFoto, btn_elegirFoto, btn_siguiente;

    Context context;


    public FotoPlanFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        actionListenerFotoPlanFragment = (ActionListenerFotoPlanFragment) context;
        this.context = context;
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v;
        v = inflater.inflate(R.layout.fragment_foto_plan, container, false);

        img = (ImageView) v.findViewById(R.id.img_fotoPlan);
        btn_elegirFoto = (Button) v.findViewById(R.id.btn_elegirFoto);
        btn_tomarFoto = (Button) v.findViewById(R.id.btn_tomarFoto);
        btn_siguiente = (Button) v.findViewById(R.id.btn_siguienteFotoFragment);

        btn_tomarFoto.setOnClickListener(this);
        btn_elegirFoto.setOnClickListener(this);
        btn_siguiente.setOnClickListener(this);

        return v;
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btn_tomarFoto:
                tomarFoto();

                break;
            case R.id.btn_elegirFoto:
                elegirFoto();
                break;
            case R.id.btn_siguienteFotoFragment:

                if(ImgF.getPath()!= null) {
                    actionListenerFotoPlanFragment.siguienteFotoPlanFragment(ImgF);
                }
                else{
                    Toast.makeText(context,"Foto es requerida",Toast.LENGTH_LONG);
                }
                break;
        }
    }

    private void tomarFoto(){


        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File dir = new File(Environment.getExternalStorageDirectory().getPath()+"/PLanes");

        if(!dir.isDirectory()){
            dir.mkdir();
        }


        ImgF = new File(dir, AddActivity.name+ "P" + ".jpg");

        Uri uri = Uri.fromFile(ImgF);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, CAMERA);


    }

    private void elegirFoto(){
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent.createChooser(intent, "Selecionar Imagen"),SELECT_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case CAMERA:
                if(resultCode == RESULT_OK){
                    img.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    try {

                        scaleImage(800);
                        Uri uri = Uri.fromFile(ImgF);
                        img.setImageURI(uri);
                        Log.i("el path es:", ImgF.getPath());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                break;
            case SELECT_IMAGE:
                if(resultCode == RESULT_OK){

                    Uri uri = data.getData();
                    //ImgF = Uri.;
                    String[] filePathColumn = { MediaStore.Images.Media.DATA };
                    Cursor cursor = context.getContentResolver().query(uri,filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();
                    ImgF = new File(picturePath);
                    img.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                    Log.i("el path es:", picturePath);

                }
                break;
        }
    }



    private void scaleImage(int maxAxis) throws IOException {
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(ImgF.getPath(), o);

        int w = o.outWidth;
        int h = o.outHeight;

        int a = w>h?w:h;
        int sampleSize=1;

        while (a>maxAxis){
            sampleSize = sampleSize*2;
            a=a/2;
        }
        o.inJustDecodeBounds=false;
        o.inSampleSize = sampleSize;

        Bitmap b = BitmapFactory.decodeFile(ImgF.getPath(), o);

        ImgF.delete();

        FileOutputStream out = new FileOutputStream(ImgF);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.JPEG, 100, stream);

        out.write(stream.toByteArray());

        b.recycle();
        b=null;

        out.close();
        stream.close();

        //Picasso.with(context).load(ImgF).into(img);
    }





}

