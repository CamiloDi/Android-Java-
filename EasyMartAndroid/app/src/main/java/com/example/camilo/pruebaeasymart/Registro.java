package com.example.camilo.pruebaeasymart;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.camilo.pruebaeasymart.Objetos.Usuario;
import com.example.camilo.pruebaeasymart.Webservice.WebService;
import com.example.camilo.pruebaeasymart.Webservice.WebServiceNode;

import java.io.UnsupportedEncodingException;

public class Registro extends AppCompatActivity {
    
    private Button Cerrar, Registrar;
    ProgressBar progresBarWS;
    boolean registroStatus;
    public EditText rutText,nombreText,correoText,passwordText,direccionText,fonoText,confirmaText;
    public String  confirmaRegistro;
    static boolean errored = false;
    public Usuario usuario = new Usuario();


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_actualizar);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            setContentView(R.layout.content_registro);
            DisplayMetrics dm = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dm);
            int widthVentana = dm.widthPixels;
            int heightVentana = dm.heightPixels;
            getWindow().setLayout((int) (widthVentana * .9), (int) (heightVentana * .7));

            progresBarWS = (ProgressBar) findViewById(R.id.progressBar7);
            rutText = (EditText) findViewById(R.id.txtRutr);
            nombreText = (EditText) findViewById(R.id.txtNombrer);
            correoText = (EditText) findViewById(R.id.txtCorreor);
            passwordText = (EditText) findViewById(R.id.txtPassr);
            direccionText = (EditText) findViewById(R.id.txtDirecr);
            fonoText = (EditText) findViewById(R.id.txtFonor);
            confirmaText = (EditText) findViewById(R.id.txtConfr);



            Registrar = (Button) findViewById(R.id.btnRegistrar);
            //region Registrar
            Registrar.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (rutText.getText().length() != 0 && rutText.getText().toString() != "") {
                        if (nombreText.getText().length() != 0 && nombreText.getText().toString() != "") {
                            if (correoText.getText().length() != 0 && correoText.getText().toString() != "") {
                                if (passwordText.getText().length() != 0 && passwordText.getText().toString() != "") {
                                    if (direccionText.getText().length() != 0 && direccionText.getText().toString() != "") {
                                        if (fonoText.getText().length() != 0 && fonoText.getText().toString() != "") {
                                            if (confirmaText.getText().length() != 0 && confirmaText.getText().toString() != "" )
                                            {
                                                usuario.setRut(Integer.parseInt(rutText.getText().toString()));
                                                usuario.setNombre(nombreText.getText().toString());
                                                usuario.setCorreo(correoText.getText().toString());
                                                usuario.setPasswrd(passwordText.getText().toString());
                                                usuario.setDireccion(direccionText.getText().toString());
                                                usuario.setTelefono(Integer.parseInt(fonoText.getText().toString()));


                                                confirmaRegistro = confirmaText.getText().toString();
                                              /* if(confirmaRegistro==usuario.getPasswrd())
                                                {*/
                                                    AsyncCallWsRegistro task = new AsyncCallWsRegistro();
                                                    task.execute();
                                                /*}else
                                                    {
                                                    Toast.makeText(getBaseContext(), "Contraseñas son diferentes", Toast.LENGTH_SHORT).show();

                                                }*/

                                           }
                                            else
                                            {
                                                Toast.makeText(getBaseContext(), "Por favor ingrese la confirmacion de su Password", Toast.LENGTH_SHORT).show();

                                            }

                                        } else {
                                            Toast.makeText(getBaseContext(), "Por favor ingrese nuevamente su Telefono", Toast.LENGTH_SHORT).show();

                                        }
                                    } else {
                                        Toast.makeText(getBaseContext(), "Por favor ingrese nuevamente su Dirección", Toast.LENGTH_SHORT).show();

                                    }
                                } else {
                                    Toast.makeText(getBaseContext(), "Por favor ingrese nuevamente su Contraseña", Toast.LENGTH_SHORT).show();

                                }
                            } else {
                                Toast.makeText(getBaseContext(), "Por favor ingrese nuevamente su Nombre", Toast.LENGTH_SHORT).show();

                            }
                        }

                    } else {
                        Toast.makeText(getBaseContext(), "Por favor ingrese nuevamente su Rut", Toast.LENGTH_SHORT).show();

                    }

                }
            });
            //endregion

            Cerrar = (android.widget.Button) findViewById(R.id.btnCerrar);
            Cerrar.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    finish();

                }
            });



        }

    private class AsyncCallWsRegistro extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            //Call Web Method
            //registroStatus = WebService.invokeRegistroWS(rutRegistro,nombreRegistro,correoRegistro,passRegistro,direcRegistro,fonoRegistro, "agregarUsuario");
            try {
                registroStatus = WebServiceNode.crearUserNode(usuario);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        //Once WebService returns response
        protected void onPostExecute(Void result) {
            //Make Progress Bar invisible
            progresBarWS.setVisibility(View.INVISIBLE);


            //Error status is false
            if (!errored) {
                //Based on Boolean value returned from WebService

                if (registroStatus!=false) {
                    //Navigate to Home Screen
                    Toast.makeText(getBaseContext(), "Registro Exitoso!!, por favor ingrese", Toast.LENGTH_SHORT).show();
                    finish();

                } else {
                    //Set Error message
                    Toast.makeText(getBaseContext(), "Fallo el Registro, Intente nuevamente", Toast.LENGTH_SHORT).show();

                }
                //Error status is true
            } else {
                Toast.makeText(getBaseContext(), "Fallo el Registro, Intente nuevamente", Toast.LENGTH_SHORT).show();
            }
            //Re-initialize Error Status to False
            errored = false;
        }

        protected void onPreExecute() {
            progresBarWS.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

}
