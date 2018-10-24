package com.example.camilo.pruebaeasymart;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.camilo.pruebaeasymart.Objetos.Producto;
import com.example.camilo.pruebaeasymart.Webservice.WebService;
import com.example.camilo.pruebaeasymart.Webservice.WebServiceNode;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class DetalleProducto extends AppCompatActivity implements View.OnClickListener {

    private ImageButton escanerBoton;
    public Producto producto;
    static boolean errored = false;
    String idProducto, id,mensaje_escaner;
    EditText idText;
    TextView nomProdText,detalleText, precioText,stockText;
    public Button Buscar;
    ImageView imagen;
    ProgressBar ProgressBarWS;
    //region Preferencias
    public final static String PREFS_NAME = "Sesion_Usuario";
    //endregion


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_producto);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ProgressBarWS = (ProgressBar) findViewById(R.id.progressBar3);
        nomProdText = (TextView) findViewById(R.id.txtProducto);
        detalleText = (TextView) findViewById(R.id.txtDetalle);
        precioText = (TextView) findViewById(R.id.txtPrecio);
        stockText = (TextView) findViewById(R.id.txtStock);
        idText = (EditText) findViewById(R.id.txtId);
        escanerBoton = (ImageButton)findViewById(R.id.btnScan);
        imagen = (ImageView)findViewById(R.id.imgProducto);
        //Bundle bundle = getIntent().getExtras();
        mensaje_escaner = getString(R.string.mensaje_escaner);


        escanerBoton.setOnClickListener((View.OnClickListener) this);
        Buscar = (Button) findViewById(R.id.btnBuscar);

        Buscar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (idText.getText().length() != 0 && idText.getText().toString() != "") {
                    idProducto = idText.getText().toString();
                    AsyncCallWsConsultaProducto task = new AsyncCallWsConsultaProducto();
                    task.execute();
                } else {
                    Toast.makeText(getBaseContext(), mensaje_escaner, Toast.LENGTH_SHORT).show();
                }
            }
        });
        idText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    Buscar.performClick();
                    return true;
                }
                return false;
            }
        });



    }
    public void onClick(View v) {
        //Se responde al evento click
        if (v.getId() == R.id.btnScan) {
            //Se instancia un objeto de la clase IntentIntegrator
            IntentIntegrator scanIntegrator = new IntentIntegrator(this);
            //Se procede con el proceso de scaneo
            scanIntegrator.initiateScan();
        }
    }

    //Metodo utilizado para leer el codigo de barras
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        //Se obtiene el resultado del proceso de scaneo y se parsea
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null) {

            //Quiere decir que se obtuvo resultado por lo tanto:
            //Desplegamos en pantalla el contenido del código de barra scaneado
            String scanContent = scanningResult.getContents();
            idText.setText(scanContent);
            //Desplegamos en pantalla el nombre del formato del código de barra scaneado
            idProducto = idText.getText().toString();
            AsyncCallWsConsultaProducto task = new AsyncCallWsConsultaProducto();
            task.execute();
        } else {
            //Quiere decir que NO se obtuvo resultado
            Toast toast = Toast.makeText(getApplicationContext(),
                    "No se ha recibido datos del escaneo!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
    //Metodo utilizado para consultar los datos del producto por medio de su Id
    private class AsyncCallWsConsultaProducto extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            //Call Web Method
            producto = WebServiceNode.cargaDatosProductoNode(idProducto);

            return  null;
        }
        @Override
        //Once WebService returns response
        protected void onPostExecute(Void result) {
            ProgressBarWS.setVisibility(View.INVISIBLE);
            //Error status is false
            if (!errored) {
                //Based on Boolean value returned from WebService

                if (producto!=null&&producto.getDetalle()!=null) {
                    //Navigate to Home Screen
                    Toast.makeText(getBaseContext(), "Lectura Exitosa", Toast.LENGTH_SHORT).show();
                   /* url = producto.getImagen().toString();
                    imagen.setImageBitmap(BitmapFactory.decodeFile(url));
                    imagen.setVisibility(View.VISIBLE);*/
                    nomProdText.setText(producto.getNombre_producto().toString());
                    detalleText.setText(producto.getDetalle().toString());
                    Typeface tiza = Typeface.createFromAsset(getAssets(), "chalkdust.ttf");
                    precioText.setTypeface(tiza);
                    precioText.setText(" $ " + producto.getValor());
                    stockText.setTypeface(tiza);
                    stockText.setText("   "+producto.getStock());
                    stockText.setTypeface(tiza);

                }
                if(producto.getNombre_producto()=="Producto no Existe!, Por favor escanee otro codigo"){
                    Toast.makeText(getBaseContext(), producto.getNombre_producto(), Toast.LENGTH_SHORT).show();
                }
                else {
                    //Set Error message
                    Toast.makeText(getBaseContext(), "Problema al cargar datos, intente nuevamente", Toast.LENGTH_SHORT).show();

                }
                //Error status is true
            } else {
                Toast.makeText(getBaseContext(), "Problema al cargar datos, intente nuevamente", Toast.LENGTH_SHORT).show();
            }
            //Re-initialize Error Status to False
            errored = false;
        }
        protected void onPreExecute() {
            ProgressBarWS.setVisibility(View.VISIBLE);
        }




    }


    }




