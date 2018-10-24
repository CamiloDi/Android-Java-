package com.example.develop.eatit.Fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.develop.eatit.Http.HttpHandler;
import com.example.develop.eatit.Login;
import com.example.develop.eatit.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;

public class Historial extends Fragment {

    private String TAG = Historial.class.getSimpleName();

    private ProgressDialog pDialog;
    private ListView lv;
    private String correo;


    private static String url = "";
    ArrayList<HashMap<String, String>> listaHistorial;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Intent intent = getActivity().getIntent();
        String username = intent.getStringExtra(Login.USER_NAME);
        View rootView = inflater.inflate(R.layout.fragment_historial,container,false);

        listaHistorial = new ArrayList();

        lv = (ListView) rootView.findViewById(R.id.lvHistorial);
        SharedPreferences prefs = getActivity().getSharedPreferences("pref", MODE_PRIVATE);
        correo = prefs.getString("UserLogin",null);
        url="http://192.168.1.47/ws/selectPedidos.php?correo="+correo;

        Toast.makeText(getContext(),correo, Toast.LENGTH_SHORT).show();

        new Historial.GetPedidos().execute();

        return rootView;
    }


    private class GetPedidos extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray pedidos = jsonObj.getJSONArray("pedido");

                    // looping through All Contacts
                    for (int i = 0; i < pedidos.length(); i++) {
                        JSONObject c = pedidos.getJSONObject(i);

                        String fecha = c.getString("Fecha");
                        String hora = c.getString("Hora");
                        String producto = c.getString("Producto");
                        String total = c.getString("Total");




                        // tmp hash map for single contact
                        HashMap<String, String> pedido = new HashMap<>();

                        // adding each child node to HashMap key => value
                        pedido.put("Fecha", fecha);
                        pedido.put("Hora", hora);
                        pedido.put("Producto", producto);
                        pedido.put("Total", total);

                        // adding contact to contact list
                        listaHistorial.add(pedido);
                    }
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

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */
            ListAdapter adapter = new SimpleAdapter(
                    getActivity(), listaHistorial,
                    R.layout.listview_formato_historial, new String[]{"Nombre", "Detalle",
                    "Valor"}, new int[]{R.id.tvNombreProducto,
                    R.id.tvDescripcionProducto, R.id.tvPrecioProducto});

            lv.setAdapter(adapter);
        }


    }

}
