package com.example.develop.eatit;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Login extends AppCompatActivity {

    Button btnIngresar;
    private EditText etCorreo,etPassword;
    TextView tvRegistro;
    private StringBuilder resul = null;
    String result ="";
    private Dialog loadingDialog;
    public static final String USER_NAME = "USUARIO";


    String usuario;
    String clave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        btnIngresar =(Button) findViewById(R.id.btnIngresar);
        etCorreo =(EditText) findViewById(R.id.etCorreo);
        etPassword =(EditText) findViewById(R.id.etPassword);
        tvRegistro =(TextView) findViewById(R.id.tvRegistrateAqui);


        etPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    btnIngresar.performClick();
                    return true;
                }
                return false;
            }
        });
        btnIngresar.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                loadingDialog = ProgressDialog.show(Login.this, "Por favor Espere", "Cargando...");
                usuario = etCorreo.getText().toString();
                clave = etPassword.getText().toString();

                AsyncCallWS task = new AsyncCallWS();
                task.execute();

            }
        });


        tvRegistro.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Intent intent = new Intent(Login.this,Registro.class);
                startActivity(intent);

            }
        });
    }

    private class AsyncCallWS extends AsyncTask<Object, Object, String> {



           /*  @Override
           protected void onPreExecute() {
                super.onPreExecute();
                loadingDialog = ProgressDialog.show(Login.this, "Por favor Espere", "Cargando...");
            }*/

            @Override
            protected String doInBackground(Object... params) {
                URL url = null;
                String linea = "";
                int respuesta = 0;

                try {

                    url = new URL("http://190.96.4.162/nc/ws/loginUsuario.php?usuario=" + usuario + "&clave=" + clave);

                    HttpURLConnection conection = (HttpURLConnection) url.openConnection();
                    respuesta = conection.getResponseCode();
                    resul = new StringBuilder();
                    if (respuesta == HttpURLConnection.HTTP_OK) {
                        InputStream in = new BufferedInputStream(conection.getInputStream());
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                        while ((linea = reader.readLine()) != null) {
                            resul.append(linea);

                        }
                        result = resul.toString();


                    }
                }catch(IOException e){ e.printStackTrace();}
                return result;
            }

            @Override
            protected void onPostExecute(String result){
                super.onPostExecute(result);
                loadingDialog.dismiss();
                if(result.equals("success")){
                Toast.makeText(getApplicationContext(), "Bienvenido", Toast.LENGTH_LONG).show();
                SharedPreferences prefe = getSharedPreferences("pref", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefe.edit();
                    Intent intent = new Intent(Login.this, MainActivity.class);
                    intent.putExtra("userL", usuario);
                    editor.putString("UserLogin",usuario);
                    editor.commit();
                    finish();
                    startActivity(intent);
                }else {
                    Toast.makeText(getApplicationContext(), "Correo o Clave Invalida", Toast.LENGTH_LONG).show();
                }
            }
        }

    }
