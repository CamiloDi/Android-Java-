package com.example.camilo.pruebaeasymart;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.camilo.pruebaeasymart.Objetos.Boleta_pro;
import com.example.camilo.pruebaeasymart.Webservice.WebService;
import com.example.camilo.pruebaeasymart.Webservice.WebServiceNode;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Comprar extends AppCompatActivity implements View.OnClickListener {

    public static Activity co;
    Button Buscar, Pagar;
    EditText idProductoEdit;
    String  idProducto, nombreUsuario, rutUsuario;
    static boolean errored = false;
    public boolean productosAgregadosWS = false;
    TextView totalText;
    ImageButton botonEscaner;
    public int  tot =0, totalBoleta = 0, indiceListaBoletaProducto = 0, sumaTotal = 0, numeroBoleta,  indiceListaEliminar = 0;
    ListView listaProductosListView;
    ArrayList<String> listaProductos = new ArrayList<String>();
    ArrayAdapter<String> adaptadorListaProductos;
    Boleta_pro boletaProducto = new Boleta_pro();
    Boleta_pro[] boletaProductoPasoWS;
    List<Boleta_pro> listaBoletaProducto;
    List<Integer> listaEliminar;
    Integer precioListaEliminar;
    public final static int request_code = 1;
    List<String>nombresProductos;
    ProgressDialog dialog;
    //region Preferencias
    public final static String PREFS_NAME = "Sesion_Usuario";
    SharedPreferences settings ;
    SharedPreferences.Editor editor;
    //endregion


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comprar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        idProductoEdit = (EditText) findViewById(R.id.txtIdCompra);
        totalText = (TextView) findViewById(R.id.txtTotalComp);
        listaProductosListView = (ListView) findViewById(R.id.lstProductos);
        listaBoletaProducto = new ArrayList<Boleta_pro>();
        listaEliminar = new ArrayList<Integer>();

        //region Leer Preferencias.
        settings  = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        rutUsuario = settings.getString("rutUsuario","");
        nombreUsuario = settings.getString("nomUsuario","");
        //endregion

        nombresProductos = new ArrayList<String>();

        Intent intObj = new Intent(Comprar.this, AgregarProductos.class);
        intObj.putExtra("idProd", idProducto);

        //region Metodo Pagar
        //Metodo utilizado para pagar los productos de la lista
        Pagar = (Button) findViewById(R.id.btnPagar);
        Pagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog = ProgressDialog.show(Comprar.this, "",
                        "Cargando, por favor espere...", true);
                boletaProductoPasoWS = new Boleta_pro[listaBoletaProducto.size()];
                for (int i = 0, o = 0; i < listaBoletaProducto.size(); i++, o++) {
                    boletaProductoPasoWS[i] = listaBoletaProducto.get(o);
                }
                if(totalText.getText()!=""&&sumaTotal!=0)
                {

                    AsyncCallWsAgregarBoleta task = new AsyncCallWsAgregarBoleta();
                    task.execute();
                    AsyncCallWsAgregarBoletaProducto task2 = new AsyncCallWsAgregarBoletaProducto();
                    task2.execute();
                    Intent Ingresar = new Intent(Comprar.this, Pagar.class);
                    Set<String> listaConvertidaProductos = new HashSet<String>();
                    listaConvertidaProductos.addAll(listaProductos);
                    editor = settings.edit();
                    editor.putInt("TotalApagar",sumaTotal);
                    editor.putStringSet("ListaProductos",listaConvertidaProductos);
                    editor.commit();
                    startActivity(Ingresar);
                }else
                {
                    Toast.makeText(getBaseContext(), "Debe Agregar un producto a la lista!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //endregion

        //region Metodo Escanear
        //Metodo utilizado para ejecutar la aplicacion de escaner de codigos de barra
        botonEscaner = (ImageButton) findViewById(R.id.imgbtnEscanear);
        botonEscaner.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Se responde al evento click
                if (v.getId() == R.id.imgbtnEscanear) {
                    //Se instancia un objeto de la clase IntentIntegrator
                    IntentIntegrator scanIntegrator = new IntentIntegrator(Comprar.this);
                    //Se procede con el proceso de scaneo
                    scanIntegrator.initiateScan();
                }
            }
            });
        //endregion

        //region Metodo Buscar
        //Metodo para buscar el codigo del producto
        Buscar = (Button) findViewById(R.id.btnBuscar2);
        idProductoEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    Buscar.performClick();
                    return true;
                }
                return false;
            }
        });
        Buscar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (idProductoEdit.getText().length() != 0 && idProductoEdit.getText().toString() != "") {

                    idProducto = idProductoEdit.getText().toString();
                    Intent intObj = new Intent(Comprar.this, AgregarProductos.class);
                    intObj.putExtra("idProd", idProducto);
                    startActivityForResult(intObj, request_code);

                } else {
                    Toast.makeText(getBaseContext(), "Ingrese un Codigo o Escaneelo", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //endregion

        co = this;

        adaptadorListaProductos = new ArrayAdapter<String>(Comprar.this, android.R.layout.simple_list_item_1, listaProductos);
        listaProductosListView.setAdapter(adaptadorListaProductos);

        //region Metodo Eliminar Producto
        //Metodo utilizado para eliminar de la lista un producto
        listaProductosListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final int posicion = position;

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(Comprar.this);
                alertDialog.setMessage("Desea listaEliminar el producto: " + nombresProductos.get(position).toString())
                            .setTitle("CONFIRMAR ELIMINAR")
                            .setCancelable(false)
                            .setPositiveButton("Aceptar",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        listaProductos.remove(posicion);
                                        listaBoletaProducto.remove(posicion);
                                        sumaTotal = sumaTotal - listaEliminar.get(posicion);
                                        //se elimina de las respectivas lista y se resta del total
                                        totalText.setText("$" + sumaTotal);
                                        //se muestra el nuevo total
                                        totalBoleta = sumaTotal;
                                        //se actualiza el nuevo total
                                        listaEliminar.remove(posicion);
                                        //se elimina de la actual lista
                                        indiceListaEliminar--;
                                        indiceListaBoletaProducto--;
                                        //se elimina de los respectivos indices
                                        adaptadorListaProductos.notifyDataSetChanged();
                                        //tot--;

                                        Toast.makeText(getApplicationContext(), "Se elimino el Producto de la Lista", Toast.LENGTH_SHORT).show();
                                        dialog.cancel();
                                    }
                                })
                        .setNegativeButton("Cancelar",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                        Toast.makeText(getApplicationContext(), "No se Elimino el producto", Toast.LENGTH_SHORT).show();
                                    }
                                });
                AlertDialog alert = alertDialog.create();
                alert.show();
            }
        });
        //endregion
    }

    //region Agregar Lista
    //Metodo utilizado para cargar los productos a la lista
    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if ((requestCode == request_code) && (resultCode == RESULT_OK)){

            //tot++;

            //aca se obtienen los datos del producto agregado
            String nombreProducto = data.getStringExtra("APNombre");
            int cantidadProducto = data.getIntExtra("APNumber",1);
            int precioProducto = data.getIntExtra("APPrecio",0);


            precioListaEliminar = precioProducto;
            listaEliminar.add(indiceListaEliminar,precioListaEliminar);
            indiceListaEliminar++;
            //se agrego a la lista en caso de tener que eliminarlo


            listaProductos.add(nombreProducto + "   Cant:" + cantidadProducto + " $  " + precioProducto);
            nombresProductos.add(nombreProducto);
            adaptadorListaProductos.notifyDataSetChanged();
            sumaTotal = totalBoleta+precioProducto;
            totalText.setText("$ " + sumaTotal);
            totalBoleta = sumaTotal;
            //se agrego a la lista para que se vea su nombre, cantidad y precio del producto, ademas se sumo al total
            
            Toast.makeText(getBaseContext(), "Se Agrego Correctamente", Toast.LENGTH_SHORT).show();

            boletaProducto.setId_producto(Integer.parseInt(idProducto));
            boletaProducto.setCantidad(cantidadProducto);

            listaBoletaProducto.add(indiceListaBoletaProducto, boletaProducto);
            indiceListaBoletaProducto++;
            boletaProducto = new Boleta_pro();
            //se agrego a a lista que despues se enviara al WS.
        }
        if(scanningResult != null){
            String scanContent = scanningResult.getContents();
            idProductoEdit.setText(scanContent);
            idProducto = idProductoEdit.getText().toString();
            if (idProductoEdit.getText().length() != 0 && idProductoEdit.getText().toString() != "") {
                Intent intObj = new Intent(Comprar.this, AgregarProductos.class);
                intObj.putExtra("idProd", idProducto);
                startActivityForResult(intObj, request_code);
            } else {
                Toast.makeText(getBaseContext(), "Ingrese un Codigo o Escaneelo", Toast.LENGTH_SHORT).show();
            }
        }
    }
    //endregion

    @Override
    public void onClick(View v) {

    }

    //Metodo utilizado para agregar una boleta
    private class AsyncCallWsAgregarBoleta extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            //Call Web Method
            //numeroBoleta = WebService.invokeAgregarBoleta(rutUsuario, sumaTotal, "agregarBoleta");
            numeroBoleta = WebServiceNode.agregarBoletaNode(rutUsuario, sumaTotal);
            return null;
        }

        @Override
        //Once WebService returns response
        protected void onPostExecute(Void result) {
            if (!errored) {
                if (numeroBoleta != 0) {
                     //Toast.makeText(getBaseContext(), "Exito al Ingresar Boleta", Toast.LENGTH_SHORT).show();
                    editor = settings.edit();
                    editor.putInt("numBoleta",numeroBoleta);
                    editor.commit();
                } else {
                    Toast.makeText(getBaseContext(), "Error al Ingresar Nueva Boleta", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();

                }
            } else {
                Toast.makeText(getBaseContext(), "Error al Ingresar Nueva Boleta", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
            errored = false;
        }
    }

    //Metodo utilizado para agregar los productos y su cantidad de una respectiva boleta
    private class AsyncCallWsAgregarBoletaProducto extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            //Call Web Method
            //productosAgregadosWS = WebService.invokeInsertarBoletaProducto(boletaProductoPasoWS, rutUsuario, "insertBoletaProducto");
            productosAgregadosWS = WebServiceNode.agregarBoletaProNode(boletaProductoPasoWS,rutUsuario);

            return null;
        }

        @Override
        //Once WebService returns response
        protected void onPostExecute(Void result) {
            //Error status is false
            if (!errored) {
                //se valida que todos los productos fueron ingresados en la base.
                if (productosAgregadosWS) {
                    //Navigate to Home Screen
                    dialog.dismiss();
                    Toast.makeText(getBaseContext(), "Exito al Ingresar Boleta", Toast.LENGTH_SHORT).show();

                } else {
                    //Set Error message
                    Toast.makeText(getBaseContext(), "Error al Ingresar Nueva Boleta", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();

                }
                //Error status is true
            } else {
                Toast.makeText(getBaseContext(), "Error al Ingresar Nueva Boleta", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
            //Re-initialize Error Status to False
            errored = false;
        }
    }

}