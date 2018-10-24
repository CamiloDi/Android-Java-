package com.example.camilo.pruebaeasymart;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.camilo.pruebaeasymart.Objetos.Boleta;
import com.example.camilo.pruebaeasymart.Webservice.WebService;
import com.example.camilo.pruebaeasymart.Webservice.WebServiceNode;

public class Boletas extends AppCompatActivity {
    
    public String rutUsuario;
    public ListView listaBoletas;
    static boolean errored = false;
    public Boleta[] boletas;

    //region Preferencias
    public final static String PREFS_NAME = "Sesion_Usuario";
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boletas);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listaBoletas= (ListView) findViewById(R.id.lstBoletas);

        //region Leer Preferencias.
        SharedPreferences settings  = getSharedPreferences(PREFS_NAME, 0);
        rutUsuario = settings.getString("rutUsuario","");
        //endregion

        AsyncCallWsCargaBoletas task = new AsyncCallWsCargaBoletas();
        task.execute();

        listaBoletas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final int pos = position;

                Intent Ingresar = new Intent(Boletas.this, Detalle_Boleta.class);
                Ingresar.putExtra("id",boletas[pos].getId_boleta());
                Ingresar.putExtra("rutBolDet",rutUsuario);
                Ingresar.putExtra("fecha",boletas[pos].getFecha());
                Ingresar.putExtra("Total",boletas[pos].getTotal());
                startActivity(Ingresar);
            }
        });

    }
    //Metodo que se utiliza para cargar el historial de boletas del usuario
    private class AsyncCallWsCargaBoletas extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            //Call Web Method
            //boletas = WebService.invokeCargaBoletas(rutUsuario, "boletas");
            boletas = WebServiceNode.cargaBoletasNode(rutUsuario);
            return  null;
        }
        @Override
        //Once WebService returns response
        protected void onPostExecute(Void result) {

            //Error status is false
            if (!errored) {
                //Based on Boolean value returned from WebService

                if (boletas!=null) {
                    //Navigate to Home Screen
                    final String[] listaCargada = new String[boletas.length];
                        String boleta;
                    for (int i = 0; i < boletas.length; i++) {
                        if(boletas[0].getId_boleta()==0){
                            Toast.makeText(getBaseContext(), "Ud aun no tiene ninguna boleta!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                            boleta ="Boleta Nº " + boletas[i].getId_boleta();
                            listaCargada[i] = boleta;
                    }
                    ArrayAdapter<String> adaptador =
                            new ArrayAdapter<String>(Boletas.this,
                                    android.R.layout.simple_list_item_1, listaCargada);

                    listaBoletas.setAdapter(adaptador);
                    Toast.makeText(getBaseContext(), "Escoja una boleta para ver su detalle", Toast.LENGTH_SHORT).show();


                } else {
                    //Set Error message
                    Toast.makeText(getBaseContext(), "Problema al cargar sus boletas", Toast.LENGTH_SHORT).show();

                }
                //Error status is true
            } else {
                Toast.makeText(getBaseContext(), "Problema al cargar sus boletas", Toast.LENGTH_SHORT).show();
            }
            //Re-initialize Error Status to False
            errored = false;
        }




    }

}

