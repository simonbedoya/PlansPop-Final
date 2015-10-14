package com.example.frank.planspop;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {

    TextView txt_register, txt_rpass;
    EditText user,pass;
    Button login;
    String muser, mpass;
    ProgressDialog dialog;

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);

        StartAnimations();

        dialog = new ProgressDialog(this);
        dialog.setMessage("Iniciando sesión.....");

        user = (EditText) findViewById(R.id.edt_user);
        pass = (EditText) findViewById(R.id.edt_pass);
        login = (Button) findViewById(R.id.btn_login);
        txt_register = (TextView) findViewById(R.id.txt_register);
        txt_rpass = (TextView) findViewById(R.id.txt_rpass);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(pass.getWindowToken(), 0);
                dialog.show();
                muser = user.getText().toString();
                mpass = pass.getText().toString();
                if (muser.matches("") || mpass.matches("")){
                    dialog.hide();
                    Toast toast = Toast.makeText(getApplicationContext(), "Informacion: Por favor llene los campos.", Toast.LENGTH_SHORT);
                    toast.show();
                }else{

                    ParseUser.logInInBackground(muser, mpass, new LogInCallback() {

                        @Override
                        public void done(ParseUser u, ParseException e) {


                            if (u != null) {

                                dialog.hide();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                dialog.hide();
                                boolean net = isOnline();
                                if (net == true) {

                                    user.setText("");
                                    pass.setText("");
                                    Toast toast = Toast.makeText(getApplicationContext(), "Error: Usuario o Contraseña incorrectos.", Toast.LENGTH_SHORT);
                                    toast.show();
                                } else {

                                    user.setText("");
                                    pass.setText("");
                                    Toast toast = Toast.makeText(getApplicationContext(), "Error: La conexión se está tardando, verifique su conexion a internet", Toast.LENGTH_SHORT);
                                    toast.show();
                                }
                            }
                        }
                    });
            }}
        });

        txt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
        txt_rpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RemPassActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    private void StartAnimations() {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
        anim.reset();
        TableRow tb = (TableRow) findViewById(R.id.table_img);
        tb.clearAnimation();
        tb.startAnimation(anim);
        anim = AnimationUtils.loadAnimation(this, R.anim.translate);
        anim.reset();
        ImageView  logo = (ImageView) findViewById(R.id.logo);
        logo.clearAnimation();
        logo.startAnimation(anim);
        anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
        anim.reset();
        LinearLayout ln_t = (LinearLayout) findViewById(R.id.linear_text);
        ln_t.clearAnimation();
        ln_t.startAnimation(anim);
        anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
        anim.reset();
        LinearLayout ln_t2 = (LinearLayout) findViewById(R.id.linear_text2);
        ln_t2.clearAnimation();
        ln_t2.startAnimation(anim);


    }
    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }

        return false;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }
}
