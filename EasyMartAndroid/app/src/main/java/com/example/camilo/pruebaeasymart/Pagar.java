package com.example.camilo.pruebaeasymart;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Console;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import com.example.camilo.pruebaeasymart.Objetos.Boleta;
import com.example.camilo.pruebaeasymart.Objetos.medioPago;
import com.example.camilo.pruebaeasymart.Webservice.WebService;
import com.example.camilo.pruebaeasymart.Webservice.WebServiceNode;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;


import org.json.JSONException;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class Pagar extends AppCompatActivity {

    public static Activity pa;    
    private Button Pagar;
    private String rutUsuario,nombreUsuario,numBoleta;
    public Spinner medioPagoSP;
    protected ArrayAdapter<CharSequence> adaptadorMedioPago;
    public int totalApagar,numPedido ,opcionPago,segundos=20,dolar=0;
    public Boleta boleta;
    static boolean errored = false;
    public TextView numBoletaText,localText,fechaText,rutText,totalText;
    ArrayList<String> articulosAPagar=new ArrayList<String>();
    ListView articulosPagarListView;
    ArrayAdapter<String> adaptadorListView;
    ProgressBar progressBarWS;
    ProgressDialog pantallaCargarPago;

    //region Preferencias
    public final static String PREFS_NAME = "Sesion_Usuario";
    SharedPreferences settings ;
    SharedPreferences.Editor editor;
    //endregion

    
    //region Paypal
    private static final String TAG = "paymentExample";
    /**
     * - Set to PayPalConfiguration.ENVIRONMENT_PRODUCTION to move real money.
     *
     * - Set to PayPalConfiguration.ENVIRONMENT_SANDBOX to use your test credentials
     * from https://developer.paypal.com
     *
     * - Set to PayPalConfiguration.ENVIRONMENT_NO_NETWORK to kick the tires
     * without communicating to PayPal's servers.
     */
    private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_NO_NETWORK;
    // note that these credentials will differ between live & sandbox environments.
    private static final String CONFIG_CLIENT_ID = "credentials from developer.paypal.com";
    
    private static final int REQUEST_CODE_PAYMENT = 1;
    //private static final int REQUEST_CODE_FUTURE_PAYMENT = 2;
    //private static final int REQUEST_CODE_PROFILE_SHARING = 3;

    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(CONFIG_ENVIRONMENT)
            .clientId(CONFIG_CLIENT_ID)
                    // The following are only used in PayPalFuturePaymentActivity.
            .merchantName("Example Merchant")
            .merchantPrivacyPolicyUri(Uri.parse("https://www.example.com/privacy"))
            .merchantUserAgreementUri(Uri.parse("https://www.example.com/legal"));
    PayPalPayment articulosAPagarPayPal;
    //endregion



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        articulosPagarListView=(ListView)findViewById(R.id.lstBoletapago);
        progressBarWS = (ProgressBar) findViewById(R.id.progressBar4);

        //region Leer Preferencias.
        settings  = getSharedPreferences(PREFS_NAME, 0);
        rutUsuario = settings.getString("rutUsuario","");
        nombreUsuario = settings.getString("nomUsuario","");
        totalApagar= settings.getInt("TotalApagar",0);
        Set<String> listaPaso = settings.getStringSet("ListaProductos",null);
        articulosAPagar.addAll(listaPaso);
        //endregion

        numBoletaText= (TextView) findViewById(R.id.txtBolPag);
        localText= (TextView) findViewById(R.id.txtLocalP);
        fechaText= (TextView) findViewById(R.id.txtFePag);
        rutText= (TextView) findViewById(R.id.txtRutP);
        totalText= (TextView) findViewById(R.id.txtTotalP);
        pantallaCargarPago = new ProgressDialog(Pagar.this);

        pantallaCargarPago.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pantallaCargarPago.setMessage("Cargando, por favor espere...");
        pantallaCargarPago.setCancelable(true);
        pantallaCargarPago.setMax(100);
        pantallaCargarPago.show();

        AsyncCallWsCargaBoleta task = new AsyncCallWsCargaBoleta();
        task.execute();

        Intent pay = new Intent(Pagar.this, PayPalService.class);
        pay.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(pay);

        List<medioPago> items = new ArrayList<medioPago>(2);
        items.add(new medioPago(getString(R.string.efectivo), R.drawable.efectivo));
        items.add(new medioPago(getString(R.string.paypal), R.drawable.pp258));

        medioPagoSP = (Spinner) findViewById(R.id.spnMedioPago);
        adaptadorMedioPago = ArrayAdapter.createFromResource(this, R.array.MedioPago, android.R.layout.simple_spinner_item);
        adaptadorMedioPago.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        medioPagoSP.setAdapter(adaptadorMedioPago);
        medioPagoSP.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent,
                                               android.view.View v, int position, long id) {
                            opcionPago=position;
                    }
                    public void onNothingSelected(AdapterView<?> parent) {
                        Toast.makeText(getBaseContext(), "Error al seleccionar medio de pago!", Toast.LENGTH_SHORT).show();
                    }
                });

        Pagar =(Button) findViewById(R.id.btnPago);
        Pagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(opcionPago==0)
                {
                    pantallaCargarPago = new ProgressDialog(Pagar.this);
                    pantallaCargarPago.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    pantallaCargarPago.setMessage("Procesando su pago...");
                    pantallaCargarPago.setCancelable(true);
                    pantallaCargarPago.setMax(100);

                    AsyncCallWsGeneraPedidoEfectivo task = new AsyncCallWsGeneraPedidoEfectivo();
                    task.execute();
                    pantallaCargarPago.show();
                }if(opcionPago==1)
                {
                    articulosAPagarPayPal = new PayPalPayment(new BigDecimal((totalApagar/dolar)), "USD",
                            "Boleta Nº "+numBoletaText.getText(), PayPalPayment.PAYMENT_INTENT_SALE);
                    Intent x = new Intent(Pagar.this,
                            PaymentActivity.class);
                    x.putExtra(PaymentActivity.EXTRA_PAYMENT, articulosAPagarPayPal);
                    x.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
                    startActivityForResult(x,REQUEST_CODE_PAYMENT);


                }
                else{
                    Toast.makeText(getBaseContext(), "Error!,debe seleccionar medio de pago!", Toast.LENGTH_SHORT).show();
                }
               }
        });

        pa=this;

    }
    //Metodo que se utiliza para cargar los datos de la boleta creada
    private class AsyncCallWsCargaBoleta extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            //Call Web Method
            //boleta = WebService.invokeCargaBoletaPago(rutUsuario, "selectBoleta");
            Date fechaHoy= Calendar.getInstance().getTime();
            SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");
            String nuevaFecha = formato.format(fechaHoy);
            boleta = WebServiceNode.cargarUltimaBoletaNode(rutUsuario);
            dolar = WebServiceNode.consultarDolar(nuevaFecha,1);
            return  null;
        }
        @Override
        //Once WebService returns response
        protected void onPostExecute(Void result) {
            //Error status is false
            if (!errored) 
            {
                if (boleta!=null) {
                   numBoletaText.setText(boleta.getId_boleta()+"");
                    //numBoletaText.setText(settings.getInt("numBoleta",0));
                    numBoleta = numBoletaText.getText().toString();
                    SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");
                    String nuevaFecha;
                    /*try {

                        Date  startDate = formato.parse(boleta.getFecha().toString());
                        Object newDate = formato.parseObject(boleta.getFecha().toString());
                        nuevaFecha = formato.format(startDate);
                    } catch (ParseException e) {
                        nuevaFecha = boleta.getFecha().toString();
                    }*/
                    nuevaFecha = boleta.getFecha().toString();
                    fechaText.setText(nuevaFecha);
                    totalText.setText("$"+totalApagar);
                    rutText.setText(rutUsuario);

                    adaptadorListView = new ArrayAdapter<String>(Pagar.this,android.R.layout.simple_list_item_1, articulosAPagar);
                    adaptadorListView.setDropDownViewResource(android.R.layout.simple_list_item_1);
                    articulosPagarListView.setAdapter(adaptadorListView);
                    pantallaCargarPago.setMessage("Elija su medio de pago");
                    pantallaCargarPago.dismiss();
                } 
                else 
                    {
                        //Set Error message
                        pantallaCargarPago.dismiss();
                        Toast.makeText(getBaseContext(), "Problema al cargar datos", Toast.LENGTH_SHORT).show();
                    }
                //Error status is true
            } else {
                pantallaCargarPago.dismiss();
                Toast.makeText(getBaseContext(), "Problema al cargar datos", Toast.LENGTH_SHORT).show();
            }
            //Re-initialize Error Status to False
            errored = false;
        }

    }
    //Metodo que se utiliza para generar un pedido pagando en efectivo
    private class AsyncCallWsGeneraPedidoEfectivo extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            //Call Web Method
            //numPedido = WebService.invokeSelectpedido(numBoleta, "selectPedido");
            numPedido = WebServiceNode.obtenerPedidoNode(numBoleta);
            return  null;
        }
        @Override
        //Once WebService returns response
        protected void onPostExecute(Void result) {
            //Error status is false
            if (!errored) 
            {
                //Based on Boolean value returned from WebService
                if (numPedido!= 0) 
                {
                    Intent Ingresar = new Intent(Pagar.this, Espera.class);
                    editor = settings.edit();
                    editor.putInt("NroPedido",numPedido);
                    editor.putString("NroBoleta",numBoleta);
                    editor.commit();
                    pantallaCargarPago.dismiss();
                    startActivity(Ingresar);
                    Toast.makeText(getBaseContext(), "Espere su Pedido", Toast.LENGTH_SHORT).show();
                } 
                else 
                    {
                        new CountDownTimer(segundos*1000,1000){
                            public  void  onTick(long millisUntilFinished){ }
                            public void onFinish(){
                                AsyncCallWsGeneraPedidoEfectivo task = new AsyncCallWsGeneraPedidoEfectivo();
                                task.execute();
                            }
                        }.start();
                    }
                //Error status is true
            }
            //Re-initialize Error Status to False
            errored = false;
        }




    }
    //Metodo que se utiliza para generar un pedido pagando con PayPal
    private class AsyncCallWsGeneraPedidoPayPal extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            //Call Web Method
            //numPedido = WebService.invokeCrearpedido(numBoleta, rutUsuario,"crearPedido");
            try {
                numPedido = WebServiceNode.crearPedidoNode(numBoleta, rutUsuario);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return  null;
        }
        @Override
        //Once WebService returns response
        protected void onPostExecute(Void result) {

            //Error status is false
            if (!errored) {
                //Based on Boolean value returned from WebService

                if (numPedido!= 0)
                {
                    pantallaCargarPago.dismiss();
                    Intent Ingresar = new Intent(Pagar.this, Espera.class);
                    editor = settings.edit();
                    editor.putInt("NroPedido",numPedido);
                    editor.putString("NroBoleta",numBoleta);
                    editor.commit();

                    startActivity(Ingresar);
                    Toast.makeText(getBaseContext(), "Espere su Pedido", Toast.LENGTH_SHORT).show();
                } else
                    {
                    pantallaCargarPago.dismiss();
                    Toast.makeText(getBaseContext(), "Problemas al pagar!!", Toast.LENGTH_SHORT).show();

                }
                //Error status is true
            } else
                {
                pantallaCargarPago.dismiss();
                Toast.makeText(getBaseContext(), "Problemas al pagar!!", Toast.LENGTH_SHORT).show();

            }
            //Re-initialize Error Status to False
            errored = false;
        }




    }
    //Metodo para Abrir la Pantalla de PayPal
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                PaymentConfirmation confirm = data
                        .getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirm != null) {
                    try {
                        Log.i("pago de ejemplo", confirm.toJSONObject().toString(4));
                        System.out.println(confirm.getPayment().toJSONObject()
                                .toString(4));
                        pantallaCargarPago = new ProgressDialog(Pagar.this);
                        pantallaCargarPago.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        pantallaCargarPago.setMessage("Procesando su pago...");
                        pantallaCargarPago.setCancelable(true);
                        pantallaCargarPago.setMax(100);
                        pantallaCargarPago.show();
                        AsyncCallWsGeneraPedidoPayPal task = new AsyncCallWsGeneraPedidoPayPal();
                        task.execute();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                System.out.println("Se cancelo el pago.");
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                System.out
                        .println("An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
            }
        }
    }
    //Metodo que termina con el servicio de PayPal cuando acaba
    @Override
    public void onDestroy() {
        // Stop service when done
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }





}
