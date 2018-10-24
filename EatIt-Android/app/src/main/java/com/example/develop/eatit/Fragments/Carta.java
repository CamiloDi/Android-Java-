package com.example.develop.eatit.Fragments;

import android.app.ProgressDialog;
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
import com.example.develop.eatit.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Carta extends Fragment {

    private String TAG = Carta.class.getSimpleName();

    private ProgressDialog pDialog;
    private ListView lv;


    private static String url = "http://190.96.4.162/nc/ws/carta.php";
    ArrayList<HashMap<String, String>> listaCarta;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_carta, container, false);

        listaCarta = new ArrayList();

        lv = (ListView) rootView.findViewById(R.id.lvCarta);

        new GetProductos().execute();
        return rootView;
    }

    private class GetProductos extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Por favor Espere, Cargando...");
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
                    JSONArray cartas = jsonObj.getJSONArray("carta");

                    // looping through All Contacts
                    for (int i = 0; i < cartas.length(); i++) {
                        JSONObject c = cartas.getJSONObject(i);

                        String nombre = c.getString("Nombre");
                        String detalle = c.getString("Detalle");
                        String valor = c.getString("Valor");
                        String tipoProducto = c.getString("Descripcion");




                        // tmp hash map for single contact
                        HashMap<String, String> carta = new HashMap<>();

                        // adding each child node to HashMap key => value
                        carta.put("Nombre", nombre);
                        carta.put("Detalle", detalle);
                        carta.put("Valor", valor);
                        carta.put("Descripcion", tipoProducto);

                        // adding contact to contact list
                        listaCarta.add(carta);
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
                    getActivity(), listaCarta,
                    R.layout.listview_formato, new String[]{"Nombre", "Detalle",
                    "Valor"}, new int[]{R.id.tvNombreProducto,
                    R.id.tvDescripcionProducto, R.id.tvPrecioProducto});

            lv.setAdapter(adapter);
        }


    }
}