package com.example.develop.eatit;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class Registro extends AppCompatActivity{


    private EditText etNombre, etCorreo, etPassword, etNumero, etDireccion;
    private Button btnRegistrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        etNombre = (EditText) findViewById(R.id.etNombre);
        etCorreo = (EditText) findViewById(R.id.etCorreo);
        etPassword = (EditText) findViewById(R.id.etContraseña);
        etNumero = (EditText) findViewById(R.id.etNumero);
        etDireccion = (EditText) findViewById(R.id.etDireccion);
        btnRegistrar = (Button) findViewById(R.id.btnRegistrar);
        btnRegistrar.setOnClickListener (new View.OnClickListener(){

            public void onClick(View v){
                if(!etNombre.getText().toString().trim().equalsIgnoreCase("") ||
                        !etCorreo.getText().toString().trim().equalsIgnoreCase("") ||
                        !etPassword.getText().toString().trim().equalsIgnoreCase("") ||
                        !etDireccion.getText().toString().trim().equalsIgnoreCase("") ||
                        !etNumero.getText().toString().trim().equalsIgnoreCase(""))
                    new insertaUsuario(Registro.this).execute();
                else
                    Toast.makeText(Registro.this, "Error en onclick", Toast.LENGTH_LONG).show();

            }
        });
    }


    private boolean insertar() {
        HttpClient httpclient;
        List<NameValuePair> nameValuePairs;
        HttpPost httppost;
        httpclient = new DefaultHttpClient();
        httppost = new HttpPost("http://190.96.4.162/nc/ws/registraUsuario.php");
        nameValuePairs = new ArrayList<NameValuePair>(5);


        nameValuePairs.add(new BasicNameValuePair("nombre",etNombre.getText().toString().trim()));
        nameValuePairs.add(new BasicNameValuePair("direccion",etDireccion.getText().toString().trim()));
        nameValuePairs.add(new BasicNameValuePair("clave",etPassword.getText().toString().trim()));
        nameValuePairs.add(new BasicNameValuePair("correo",etCorreo.getText().toString().trim()));
        nameValuePairs.add(new BasicNameValuePair("telefono",etNumero.getText().toString().trim()));


        try {
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            httpclient.execute(httppost);
            return true;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    class insertaUsuario extends AsyncTask<String, String, String>{
        private Activity context;

        insertaUsuario(Activity context) {
                this.context = context;
        }

        @Override
        protected String doInBackground(String... params) {
            if (insertar()) {
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "Usuario agregado exitosamente", Toast.LENGTH_LONG).show();
                        etNombre.setText("");
                        etCorreo.setText("");
                        etPassword.setText("");
                        etDireccion.setText("");
                        etNumero.setText("");
                    }
                });

            } else {
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "Error runon", Toast.LENGTH_LONG).show();
                    }
                });

            }
            return null;
        }
    }


}
