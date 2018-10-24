package com.example.camilo.pruebaeasymart;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.example.camilo.pruebaeasymart.Objetos.Producto;
import com.example.camilo.pruebaeasymart.Webservice.WebService;
import com.example.camilo.pruebaeasymart.Webservice.WebServiceNode;

/**
 * Created by Luis B. on 18-06-2016.
 * Modified by Camilo D. on 17-07-2017.
 */
public class AgregarProductos extends AppCompatActivity {
    TextView detalleText,productoText,idText,precioText;
    Button Agregar,Cancelar;
    NumberPicker CantProductoNP;
    public String idProducto,nombreProducto;
    int precioProducto,stockProducto,precioTotal;
    public Producto producto;
    static boolean errored = false;
    ProgressBar ProgressBarWS;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_agregarproducto);

        final Bundle bundle = getIntent().getExtras();
        idProducto = bundle.getString("idProd");
        //Obteniendo la instancia del TextView
        detalleText = (TextView) findViewById(R.id.txtDetalle);
        productoText = (TextView) findViewById(R.id.txtProducto);
        Agregar = (Button) findViewById(R.id.btnAgregar);
        Cancelar = (Button) findViewById(R.id.btnCancelar);
        CantProductoNP = (NumberPicker) findViewById(R.id.nPicker);
        idText = (TextView) findViewById(R.id.txtID);
        precioText = (TextView) findViewById(R.id.txtPrecio);
        ProgressBarWS = (ProgressBar) findViewById(R.id.progressBar2);


        //Ejecucion Llamado a Web Service Automatico
        AsyncCallWsDetalleProducto task = new AsyncCallWsDetalleProducto();
        task.execute();

        //Cuando se ejecute el boton Agregar sucedera esto

        Agregar.setOnClickListener(new View.OnClickListener(){
            public void onClick (View v){

                    Intent data = new Intent(AgregarProductos.this, Comprar.class);
                    data.putExtra("APNombre", nombreProducto);
                    data.putExtra("APNumber", CantProductoNP.getValue());
                    data.putExtra("APPrecio", precioTotal);
                    setResult(RESULT_OK, data);
                    finish();

            }
        });
        Cancelar.setOnClickListener(new View.OnClickListener(){
            public void onClick (View v){
                finish();
                //Creacion Instacia para pasar Valores a Comprar.class
                Intent data = new Intent(AgregarProductos.this , Comprar.class);
                //Ejecutar Actividad Comprar con el objeto creado
                setResult(RESULT_CANCELED, data);
    }
});
        //Metodo utilizado para el cambio de la cantidad de productos
        CantProductoNP.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                precioTotal = precioProducto * newVal;
                precioText.setText(" $ " + precioTotal);

            }
        });


    }
        //Invocar Web Service Productos el cual obtiene los datos del producto
        private class AsyncCallWsDetalleProducto extends AsyncTask<String, Void, Void> {
            @Override
            protected Void doInBackground(String... params) {
                //Call llamado a web Service de idProducto Bundle , "nombre de la instancia en WebService"
                //producto se utilizara en el postExecute para cargar los valores en los textView
                producto = WebServiceNode.cargaDatosProductoNode(idProducto);
                return null;
            }
            protected void onPostExecute(Void result) {
                ProgressBarWS.setVisibility(View.INVISIBLE);
                //Error status is false
                if (!errored) {
                    //Based on Boolean value returned from WebService
                    if (producto!=null&&producto.getDetalle()!=null) {
                        //Asignacion de TextView desde Web Service llamado en producto.
                        idText.setText(idProducto);
                        Toast.makeText(getBaseContext(), "Lectura Exitosa", Toast.LENGTH_SHORT).show();
                        productoText.setText(producto.getNombre_producto().toString());
                        detalleText.setText(producto.getDetalle().toString());
                        precioTotal = producto.getValor()*1;
                        nombreProducto = producto.getNombre_producto();
                        stockProducto = producto.getStock();
                        precioProducto = producto.getValor();
                        CantProductoNP.setValue(1);
                        CantProductoNP.setMaxValue(stockProducto);
                        CantProductoNP.setMinValue(1);
                        CantProductoNP.setWrapSelectorWheel(false);
                        Typeface fuenteTiza = Typeface.createFromAsset(getAssets(), "chalkdust.ttf");
                        precioText.setTypeface(fuenteTiza);
                        precioText.setText(" $ " + precioTotal);
                        productoText.setVisibility(View.VISIBLE);
                        detalleText.setVisibility(View.VISIBLE);

                    } if(producto.getNombre_producto()=="Producto no Existe!, Por favor escanee otro codigo"){
                        Toast.makeText(getBaseContext(), producto.getNombre_producto(), Toast.LENGTH_SHORT).show();
                        finish();
                        //Creacion Instacia para pasar Valores a Comprar.class
                        Intent data = new Intent(AgregarProductos.this , Comprar.class);
                        //Ejecutar Actividad Comprar con el objeto creado
                        setResult(RESULT_CANCELED, data);
                    }
                    /*else {
                        //Set Error message
                        Toast.makeText(getBaseContext(), "Problema al cargar datos 1", Toast.LENGTH_SHORT).show();
                    }*/
                    //Error status is true
                } else {
                    Toast.makeText(getBaseContext(), "Problema al cargar datos 2", Toast.LENGTH_SHORT).show();
                }
                //Re-initialize Error Status to False
                errored = false;
            }
            protected void onPreExecute() {
                ProgressBarWS.setVisibility(View.VISIBLE);
            }
        }
}
