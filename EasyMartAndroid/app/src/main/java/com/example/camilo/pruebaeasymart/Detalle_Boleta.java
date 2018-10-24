package com.example.camilo.pruebaeasymart;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.camilo.pruebaeasymart.Objetos.Boleta_pro;
import com.example.camilo.pruebaeasymart.Objetos.Adaptador;
import com.example.camilo.pruebaeasymart.Objetos.Elementos;
import com.example.camilo.pruebaeasymart.Webservice.WebService;
import com.example.camilo.pruebaeasymart.Webservice.WebServiceNode;

import java.util.ArrayList;
import java.util.List;

public class Detalle_Boleta extends AppCompatActivity {

    TextView idBoletaText,localBoletaText,rutUsuarioText,fechaBoletaText,totalBoletaText;
    static boolean errored = false;
    Boleta_pro[] listaProductos;
    ListView muestraLista;
    String idBoleta;

    public Adaptador miadapatador;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle__boleta);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        idBoletaText = (TextView) findViewById(R.id.txtNBoleta);
        localBoletaText = (TextView) findViewById(R.id.txtLocal);
        rutUsuarioText = (TextView) findViewById(R.id.txtRutBol);
        fechaBoletaText = (TextView) findViewById(R.id.txtFechaBol);
        totalBoletaText = (TextView) findViewById(R.id.txtTotalBol);
        muestraLista = (ListView) findViewById(R.id.lstBoleta);

        Bundle bundle = getIntent().getExtras();
        idBoleta= bundle.get("id").toString();
        idBoletaText.setText("Boleta Nº "+idBoleta);
        //localBoletaText.setText("Local "+bundle.get("localBoletaText"));
        rutUsuarioText.setText("Rut "+bundle.get("rutBolDet"));
        totalBoletaText.setText("Total $" + bundle.get("Total"));
        fechaBoletaText.setText("Fecha: " + bundle.getString("fecha"));

        AsyncCallWsListaProductos tarea = new AsyncCallWsListaProductos();
        tarea.execute();
    }
    //Metodo utilizado para obtener la lista de los productos comprados por el id de la boleta
    private class AsyncCallWsListaProductos extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            //Call Web Method
            //listaProductos = WebService.invokeCargaBoletasDetalle(idBoleta, "CamitantesBlancos");
            listaProductos = WebServiceNode.cargaDetalleBoletaNode(idBoleta);
            return  null;
        }
        @Override
        //Once WebService returns response
        protected void onPostExecute(Void result) {

            //Error status is false
            if (!errored) {
                //Based on Boolean value returned from WebService
                if (listaProductos!=null) {
                    //Navigate to Home Screen
                    List<Boleta_pro> datos = new ArrayList<Boleta_pro>();

                    for (int i = 0; i < listaProductos.length; i++)
                        {
                            datos.add(listaProductos[i]);
                        }
                    miadapatador = new Adaptador(Detalle_Boleta.this,R.layout.listadetalleboleta,datos);
                    muestraLista.setAdapter(miadapatador);
                    
                    Toast.makeText(getBaseContext(), "Los datos se cargaron Exitosamente", Toast.LENGTH_SHORT).show();
                } else {
                    //Set Error message
                    Toast.makeText(getBaseContext(), "Problema al cargar datos", Toast.LENGTH_SHORT).show();
                }
                //Error status is true
            } else {
                Toast.makeText(getBaseContext(), "Problema al cargar datos", Toast.LENGTH_SHORT).show();
            }
            //Re-initialize Error Status to False
            errored = false;
        }
    }

}


