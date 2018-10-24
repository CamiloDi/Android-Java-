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
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.camilo.pruebaeasymart.Correo.GMailSender;
import com.example.camilo.pruebaeasymart.Objetos.Usuario;
import com.example.camilo.pruebaeasymart.Webservice.WebService;
import com.example.camilo.pruebaeasymart.Webservice.WebServiceNode;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Login extends AppCompatActivity {

    public static Activity lo;
    static boolean errored = false;
    Button Ingresar,Registrar,Olvidar;
    public EditText rutIngreso, passIngreso;
    String nombre = null, rutEnvioWS, passEnvioWS,rutOlvidar;
    private Usuario usuarioOlvidar;
    ProgressBar webservicePG;
    String loginStatus,mensaje_error;
    private final int duracionMensaje = 10000;

    //Variables usadas para el envio de correo
    String asunto_correo = "";
    String recibe_correo = "",Cuerpo = "";
    String envia_correo = "";
    String password_correo= "";
    GMailSender senderCorreo;

    ProgressDialog dialog;
    ImageView mPasswordVisibilityView;

    //Variables de Sesion
    public static final String PREFS_NAME = "Sesion_Usuario";
    SharedPreferences preferencias ;
    SharedPreferences.Editor editor;

    public int prueba;





    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        webservicePG = (ProgressBar) findViewById(R.id.progressBar1);
        Ingresar = (Button) findViewById(R.id.btnIngresar);
        Registrar = (Button) findViewById(R.id.btnRegistrarse);
        rutIngreso = (EditText) findViewById(R.id.txtRut);
        passIngreso = (EditText) findViewById(R.id.txtPass);
        Olvidar = (Button) findViewById(R.id.btnOlvidar);
        senderCorreo = new GMailSender(envia_correo, password_correo);
        mPasswordVisibilityView = (ImageView) findViewById(R.id.fragment_login_password_visibility);
        asunto_correo = getString(R.string.asunto_correo);
        envia_correo = getString(R.string.usuario_correo);
        password_correo = getString(R.string.pass_correo);



        mensaje_error = getString(R.string.mensaje_login);

        //region Metodo Login desde la clave
        passIngreso.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    Ingresar.performClick();
                    return true;
                }
                return false;
            }
        });
        //endregion

        //region Metodo Olvido Clave
        Olvidar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                final EditText input = new EditText(Login.this);
                final NumberPicker o = new NumberPicker(Login.this);
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                input.setHint("sin puntos ni digito verificador");
                builder.setMessage("Ingrese su rut, y le enviaremos su contraseña" )
                    .setView(input)
                .setTitle("Enviar Contraseña")
                        .setCancelable(false)
                        .setPositiveButton("Enviar",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        if (input.getText().length() != 0 && input.getText().toString() != "") {
                                            rutOlvidar =input.getText().toString();
                                            AsyncCallWSConsultaCorreo t = new AsyncCallWSConsultaCorreo();
                                            t.execute();
                                            dialog.cancel();
                                        } else {

                                            Toast.makeText(getBaseContext(), "Por favor ingrese su Rut", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                        )
                                            .setNegativeButton("Cancelar",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            dialog.cancel();
                                                        }
                                                    });
                AlertDialog alert = builder.create();
                alert.show();

            }
        });
        //endregion

        //region Metodo Ingresar
        Ingresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rutIngreso.getText().length() != 0 && rutIngreso.getText().toString() != "") {
                    if (passIngreso.getText().length() != 0 && passIngreso.getText().toString() != "") {
                        rutEnvioWS = rutIngreso.getText().toString();
                        passEnvioWS = passIngreso.getText().toString();
                        dialog = ProgressDialog.show(Login.this, "",
                                "Cargando, por favor espere...", true);
                        AsyncCallWSIngreso task = new AsyncCallWSIngreso();
                        task.execute();
                    } else {
                        Toast.makeText(getBaseContext(), "Por favor ingrese su Contraseña", Toast.LENGTH_SHORT).show();

                    }
                } else {
                    Toast.makeText(getBaseContext(), "Por favor ingrese su Rut", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //endregion

        //region Metodo Registrar
        Registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Registro.class));
            }
        });
        //endregion


        lo = this;
        passIngreso.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                mPasswordVisibilityView.setVisibility(s.length() > 0 ? View.VISIBLE : View.GONE);
            }
        });
        mPasswordVisibilityView.setOnTouchListener(mPasswordVisibleTouchListener);


    }

    //Este es el metodo para poder ingresar a la aplicacion.
    private class AsyncCallWSIngreso extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            //Call Web Method
            //loginStatus = WebService.invokeLoginWS(rutEnvioWS, passEnvioWS, "loginUser");
            loginStatus = WebServiceNode.loginUserNode(rutEnvioWS,passEnvioWS);

            return null;
        }
        @Override
        //Once WebService returns response
        protected void onPostExecute(Void result) {
            //Make Progress Bar invisible
            webservicePG.setVisibility(View.INVISIBLE);
            Intent intObj = new Intent(Login.this, MenuApp.class);


            //Error status is false
            if (!errored) {
                //Based on Boolean value returned from WebService
                if (loginStatus!="Error"&&loginStatus!="null") {
                    dialog.dismiss();
                    nombre=loginStatus;
                    preferencias = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                    editor = preferencias.edit();
                    editor.putString("rutUsuario",rutEnvioWS);
                    editor.putString("nomUsuario",nombre);
                    editor.commit();
                    //Navigate to Home Screen
                    startActivity(intObj);
                } else {
                    //Set Error message
                    Toast.makeText(getBaseContext(), mensaje_error, Toast.LENGTH_SHORT).show();
                    dialog.dismiss();

                }
                //Error status is true
            } else {
                Toast.makeText(getBaseContext(), mensaje_error, Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
            //Re-initialize Error Status to False
            errored = false;
        }

        protected void onPreExecute() {
            webservicePG.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }
    //Se envia el rut consultando los datos del usuario
    private class AsyncCallWSConsultaCorreo extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            //usuario=WebService.invokeCargaDatos(rutOlvidar, "selectUsuario");
            usuarioOlvidar=WebServiceNode.cargaDatosUsuarioNode(rutOlvidar);
            return null;
        }

        @Override
        //Once WebService returns response
        protected void onPostExecute(Void result) {
            webservicePG.setVisibility(View.INVISIBLE);
            super.onPostExecute(result);
            if(usuarioOlvidar!=null){
                //Aca Busco la plantilla de html para el envio del Correo
                InputStream is = null;
                int size =0;
                byte[] buffer = null;
                try {
                    is = getAssets().open("PlantillaCorreo.html");
                    size = is.available();
                    buffer = new byte[size];
                    is.read(buffer);
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                String str = new String(buffer);
                recibe_correo=usuarioOlvidar.getCorreo();
                Cuerpo = str;
                Cuerpo = Cuerpo.replace("#nuevaClave#", usuarioOlvidar.getPasswrd());
                Cuerpo = Cuerpo.replace("#emailUsuario#", recibe_correo);
                AsyncEnvioCorreo t=new AsyncEnvioCorreo();
                t.execute();

            } else{
                Toast.makeText(getBaseContext(), "Su rut no existe, por favor Registrese", Toast.LENGTH_SHORT).show();
            }
        }
        protected void onPreExecute() {
            webservicePG.setVisibility(View.VISIBLE);
        }
    }
    //Con los datos enviados previamente se envia un correo con su contraseña
    private class AsyncEnvioCorreo extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... mApk) {
            try {senderCorreo.sendMail(asunto_correo, Cuerpo, envia_correo, recibe_correo);

            }catch (Exception e){
                Toast.makeText(getApplicationContext(), "No se envio el correo con su contraseña!", Toast.LENGTH_SHORT).show();
            }
            return null;
        }

        @Override
        //Once WebService returns response
        protected void onPostExecute(Void result) {
            webservicePG.setVisibility(View.INVISIBLE);
            super.onPostExecute(result);
            Toast.makeText(getApplicationContext(), "Se envio el correo con su contraseña!", Toast.LENGTH_SHORT).show();
        }
        protected void onPreExecute() {
            webservicePG.setVisibility(View.VISIBLE);
        }
    }

    //region Metodo Ver Clave
    private View.OnTouchListener mPasswordVisibleTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            final boolean isOutsideView = event.getX() < 0 ||
                    event.getX() > v.getWidth() ||
                    event.getY() < 0 ||
                    event.getY() > v.getHeight();

            // change input type will reset cursor position, so we want to save it
            final int cursor = passIngreso.getSelectionStart();

            if (isOutsideView || MotionEvent.ACTION_UP == event.getAction())
                passIngreso.setInputType( InputType.TYPE_CLASS_TEXT |
                        InputType.TYPE_TEXT_VARIATION_PASSWORD);
            else
                passIngreso.setInputType( InputType.TYPE_CLASS_TEXT |
                        InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);

            passIngreso.setSelection(cursor);
            return true;
        }
    };
    //endregion

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }


}




