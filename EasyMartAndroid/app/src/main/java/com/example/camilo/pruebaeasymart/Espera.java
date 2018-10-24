package com.example.camilo.pruebaeasymart;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.camilo.pruebaeasymart.Webservice.WebService;
import com.example.camilo.pruebaeasymart.Webservice.WebServiceNode;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.UnsupportedEncodingException;

import static com.example.camilo.pruebaeasymart.R.layout.activity_espera;

public class Espera extends AppCompatActivity {
    Button Espera;
    private String nombreUsuarioEspera, numPedidoEscaneado, numBoleta,codigoValidacion,numPedido;
    public static Activity es;
    static boolean errored = false;
    boolean pedidoStatus = false;
    TextView numPedidoText,nombreUsuarioText,numeroBoletaText ;
    //region Preferencias
    public final static String PREFS_NAME = "Sesion_Usuario";
    SharedPreferences settings ;
    SharedPreferences.Editor editor;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_espera);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        nombreUsuarioText = (TextView) findViewById(R.id.txtNombreEspera);
        numeroBoletaText = (TextView) findViewById(R.id.txtNumBoletaEspera);
        numPedidoText = (TextView) findViewById(R.id.txtPedido);


        //region Leer Preferencias.
        settings  = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        nombreUsuarioText.setText(settings.getString("nomUsuario",""));
        numeroBoletaText.setText(settings.getString("NroBoleta",""));
        numPedidoText.setText(settings.getInt("NroPedido",0)+"");
        //endregion

        nombreUsuarioEspera = nombreUsuarioText.getText().toString();
        numBoleta=numeroBoletaText.getText().toString();

        //Nota**
        //Para validar que el codigo QR que nos entregan esta bien, se junta el numero de la boleta
        //mas el numero del pedido y se validan.
        codigoValidacion =settings.getString("NroBoleta","") + (settings.getInt("NroPedido",0));
        numPedido = numPedidoText.getText().toString();

        android.app.AlertDialog.Builder d =new android.app.AlertDialog.Builder(Espera.this);

        d.setMessage("Su pago se realizo de forma exitosa.")
                .setCancelable(false)
                .setTitle("Exito al pagar")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        android.app.AlertDialog alert = d.create();
        alert.show();
        Espera = (Button) findViewById(R.id.btnListo);
        Espera.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Se responde al evento click
                if (v.getId() == R.id.btnListo) {
                    //Se instancia un objeto de la clase IntentIntegrator
                    IntentIntegrator scanIntegrator = new IntentIntegrator(Espera.this);
                    //Se procede con el proceso de scaneo
                    scanIntegrator.initiateScan();
            }}});
        es = this;
    }

    //Metodo utilizado para verificar el codigo qr corresponde a nuestro pedido
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        //Se obtiene el resultado del proceso de scaneo y se parsea
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null) {
            //Quiere decir que se obtuvo resultado por lo tanto:
            //Desplegamos en pantalla el contenido del código de barra scaneado
            numPedidoEscaneado = scanningResult.getContents();
            //Desplegamos en pantalla el nombre del formato del código de barra scaneado
            if(codigoValidacion.equals(numPedidoEscaneado)){

                AsyncCallWSPedidoActualizar task = new AsyncCallWSPedidoActualizar();
                task.execute();

            }
            else{
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Codigo incorrecto, vuelva a intentarlo!", Toast.LENGTH_SHORT);
                toast.show();

            }
        } else {
            //Quiere decir que NO se obtuvo resultado
            Toast toast = Toast.makeText(getApplicationContext(),
                    "No se ha recibido datos del escaneo!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
    //Metodo utilizado para actualizar el estado del pedido
    private class AsyncCallWSPedidoActualizar extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            //Call Web Method
            //pedidoStatus = WebService.invokeActualizarWSPedido(numPedido,numBoleta, "actualizarPedido");
            try {
                pedidoStatus = WebServiceNode.actualizarPedidoNode(numPedido,numBoleta);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        //Once WebService returns response
        protected void onPostExecute(Void result) {
            //Error status is false
            if (!errored) {
                //Based on Boolean value returned from WebService

                if (pedidoStatus !=false) {
                    Intent id = new Intent(Espera.this, Salir.class);
                    //id.putExtra("nombreSalir", nombreUsuarioEspera);

                    editor = settings.edit();
                    editor.putBoolean("deEspera",true);
                    editor.commit();
                    startActivity(id);
                    Toast.makeText(getBaseContext(), "Gracias", Toast.LENGTH_SHORT).show();
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