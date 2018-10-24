package com.example.develop.eatit.Fragments;


import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.develop.eatit.Http.HttpHandler;
import com.example.develop.eatit.Objetos.oUsuario;
import com.example.develop.eatit.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;


public class Datos extends Fragment {

    private String TAG = Datos.class.getSimpleName();
    private ListView lv;
    private ProgressDialog pDialog;
    private String username;
    static boolean errored = false;
    private StringBuilder resul = null;
    oUsuario us = new oUsuario();
    oUsuario usmd = new oUsuario();
    EditText etNombre, etContraseña, etDireccion, etTelefono;
    public Button btnModificar;
    private String correo;
    private int resp;


    ArrayList<HashMap<String, String>> listaUsuario;

   /* */



    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_datos, container, false);

        etNombre = (EditText) rootView.findViewById(R.id.etNombre);
        etContraseña = (EditText) rootView.findViewById(R.id.etContraseña);
        etTelefono = (EditText) rootView.findViewById(R.id.etTelefono);
        etDireccion = (EditText) rootView.findViewById(R.id.etDireccion);
        //aca leo las preferencias
        SharedPreferences prefs = getActivity().getSharedPreferences("pref", MODE_PRIVATE);
        correo = prefs.getString("UserLogin",null);
        //aqui termina

        //Toast.makeText(getContext(),USER_NAME, Toast.LENGTH_SHORT).show();
        //Toast.makeText(getContext(),correo, Toast.LENGTH_SHORT).show();


        btnModificar = (Button) rootView.findViewById(R.id.btnModificar);
        btnModificar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                usmd.setNombre(etNombre.getText().toString());
                usmd.setContraseña(etContraseña.getText().toString());
                usmd.setTelefono(etTelefono.getText().toString());
                usmd.setDireccion(etDireccion.getText().toString());

                AsyncCallWSModificar task = new AsyncCallWSModificar();
                task.execute();



            }
                                   });


        cargar();


        return rootView;
    }
    private  void cargar(){

        AsyncCallWS task = new AsyncCallWS();
        task.execute();
    }


    private class AsyncCallWSModificar extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            //Call Web Method
            HttpHandler sh = new HttpHandler();
            URL url = null;
            String nomx =usmd.getNombre();
            String pass =usmd.getContraseña();
            String direcci =usmd.getDireccion();
            String fon =usmd.getTelefono();
            String linea = "";
            int respuesta = 0;
            try {
                url = new URL("http://190.96.4.162/nc/ws/updateUsuario.php?nombre="+nomx+"&correo="+correo+"&clave="+pass+"&direccion="+direcci+"&telefono="+fon);
                HttpURLConnection conection = (HttpURLConnection) url.openConnection();
                respuesta = conection.getResponseCode();
                resul = new StringBuilder();
                if (respuesta == HttpURLConnection.HTTP_OK) {
                    InputStream in = new BufferedInputStream(conection.getInputStream());
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    while ((linea = reader.readLine()) != null) {
                        resul.append(linea);

                    }
                }

                String jsonStr =resul.toString();
                if (jsonStr != null) {
                    try {
                        JSONObject jsonObj = new JSONObject(jsonStr);
                        //JSONArray user = jsonObj.getJSONArray("code");
                        //JSONObject c = user.getJSONObject(0);
                        resp = Integer.parseInt(jsonObj.getString("code"));





                    } catch (final JSONException e) {
                        Log.e(TAG, "Json parsing error: " + e.getMessage());
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getActivity(),
                                        "Json parsing error: " + e.getMessage(),
                                        Toast.LENGTH_LONG)
                                        .show();
                            }
                        });

                    }
                } else {
                    Log.e(TAG, "No podemos traer Json desde el servidor.");
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(),
                                    "No podemos traer JSON desde el servidor. Verifica el log para posibles errores!",
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }

            } catch (IOException e) {
                e.printStackTrace();

            }

            return null;
        }

        @Override
        //Once WebService returns response
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            //Error status is false
            if (!errored) {
                if(resp==1){
                    Toast.makeText(getContext(), "Datos Actualizados", Toast.LENGTH_SHORT).show();
                } else {
                //Set Error message
                Toast.makeText(getContext(), "Problema al cargar datos", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "Fallo conexion", Toast.LENGTH_SHORT).show();
            }
            //Re-initialize Error Status to False
            errored = false;
        }


    }
    private class AsyncCallWS extends AsyncTask<oUsuario, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = ProgressDialog.show(getActivity(),"Por favor Espere", "Cargando...",true);
        }

        @Override
        protected Void doInBackground(oUsuario... params) {

            //Call Web Method
            URL url = null;
            String linea = "";
            int respuesta = 0;
            try {
                url = new URL("http://190.96.4.162/nc/ws/selectUsuario.php?correo=" + correo);
                HttpURLConnection conection = (HttpURLConnection) url.openConnection();
                respuesta = conection.getResponseCode();
                resul = new StringBuilder();
                if (respuesta == HttpURLConnection.HTTP_OK) {
                    InputStream in = new BufferedInputStream(conection.getInputStream());
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    while ((linea = reader.readLine()) != null) {
                        resul.append(linea);

                    }
                }

                String jsonStr =resul.toString();
                if (jsonStr != null) {
                    try {
                        JSONObject jsonObj = new JSONObject(jsonStr);
                        JSONArray user = jsonObj.getJSONArray("usuario");
                        JSONObject c = user.getJSONObject(0);
                        us.setNombre(c.getString("Nombre"));
                        us.setContraseña(c.getString("Clave"));
                        us.setDireccion(c.getString("Direccion"));
                        us.setTelefono(c.getString("Telefono"));




                    } catch (final JSONException e) {
                        Log.e(TAG, "Json parsing error: " + e.getMessage());
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getActivity(),
                                        "Json parsing error: " + e.getMessage(),
                                        Toast.LENGTH_LONG)
                                        .show();
                            }
                        });

                    }
                } else {
                    Log.e(TAG, "No podemos traer Json desde el servidor.");
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(),
                                    "No podemos traer JSON desde el servidor. Verifica el log para posibles errores!",
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }

            } catch (IOException e) {
                e.printStackTrace();

            }

            return null;
        }

        @Override
        //Once WebService returns response
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            Toast.makeText(getActivity(), "Edite sus datos", Toast.LENGTH_SHORT).show();
            //Error status is false
            if (!errored) {
                if(us!=null){
                    pDialog.dismiss();
                    etNombre.setText(us.getNombre());
                    etContraseña.setText(us.getContraseña());
                    etDireccion.setText(us.getDireccion());
                    //int fono= Integer.parseInt(us.getTelefono().toString());

                    etTelefono.setText(us.getTelefono());
                } else {
                    //Set Error message
                    Toast.makeText(getContext(), "Problema al cargar datos", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "Fallo conexion", Toast.LENGTH_SHORT).show();
            }
            //Re-initialize Error Status to False
            errored = false;
        }


    }
}
