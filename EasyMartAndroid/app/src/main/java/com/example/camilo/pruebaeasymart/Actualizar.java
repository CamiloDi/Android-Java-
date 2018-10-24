package com.example.camilo.pruebaeasymart;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.camilo.pruebaeasymart.Objetos.Usuario;
import com.example.camilo.pruebaeasymart.Webservice.WebService;
import com.example.camilo.pruebaeasymart.Webservice.WebServiceNode;

import org.kobjects.crypt.Crypt;

import javax.crypto.EncryptedPrivateKeyInfo;

public class Actualizar extends AppCompatActivity {

    private Button Guardar;
    public EditText correoEdit,passEdit,direcEdit,fonoEdit,confirmaPassEdit,nuevaPassEdit;
    public String rutActualizar,nombreActualizar,correoActualizar,direcActualizar,passActualizar;
    public int fonoActualizar;
    //static boolean errored = false;
    boolean Status;
    Usuario usuarioCarga = new Usuario(),usuarioActualiza = new Usuario();
    public TextView nombreText;

    ProgressDialog dialog;

    //region Preferencias
    public final static String PREFS_NAME = "Sesion_Usuario";
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        nombreText = (TextView) findViewById(R.id.txtNombre);
        TextView rutText = (TextView) findViewById(R.id.txtRut);
        //region Leer Preferencias.
        SharedPreferences settings  = getSharedPreferences(PREFS_NAME, 0);
        rutActualizar = settings.getString("rutUsuario","");
        nombreActualizar = settings.getString("nomUsuario","");
        //endregion

        rutText.setText(rutActualizar);
        nombreText.setText(nombreActualizar);


        correoEdit = (EditText) findViewById(R.id.txtCorreoac);
        passEdit = (EditText) findViewById(R.id.txtPassac);
        direcEdit = (EditText) findViewById(R.id.txtDirecac);
        fonoEdit = (EditText) findViewById(R.id.txtFonoac);
        confirmaPassEdit = (EditText) findViewById(R.id.txtConfirmaac);
        nuevaPassEdit = (EditText) findViewById(R.id.txtNuevaac);

        dialog = ProgressDialog.show(Actualizar.this, "",
                "Cargando, por favor espere...", true);

        AsyncCallWsCargaDatos task = new AsyncCallWsCargaDatos();
        task.execute();






        Guardar = (Button) findViewById(R.id.btnGuardar);
        Guardar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                nombreActualizar = nombreText.toString();
                correoActualizar = correoEdit.getText().toString();
                direcActualizar = direcEdit.getText().toString();
                fonoActualizar = Integer.parseInt(fonoEdit.getText().toString());
                if(passEdit.getText().length() != 0 && passEdit.getText().toString() != "")
                {
                    if (nuevaPassEdit.getText().length() != 0 && nuevaPassEdit.getText().toString() != "")
                    {
                        if (nuevaPassEdit.getText().length() <= 8)
                        {
                            if ((confirmaPassEdit.getText().length() != 0 && confirmaPassEdit.getText().toString() != "") )//&& confirmaPassEdit.getText().toString() == nuevaPassEdit.getText().toString())
                            {
                                passActualizar = nuevaPassEdit.getText().toString();
                            } else
                                {
                                Toast.makeText(getBaseContext(), "Contraseñas son diferentes", Toast.LENGTH_SHORT).show();
                            }
                        } else
                            {
                            Toast.makeText(getBaseContext(), "Tamaño maximo excedido!", Toast.LENGTH_SHORT).show();
                        }
                    } else
                        {
                        passActualizar = "";
                        }
                }
                else{
                    Toast.makeText(getBaseContext(), "Debe ingresar su Actual Password!", Toast.LENGTH_SHORT).show();
                }
                usuarioActualiza.setRut(Integer.parseInt(rutActualizar));
                usuarioActualiza.setNombre(nombreActualizar);
                usuarioActualiza.setCorreo(correoActualizar);
                usuarioActualiza.setDireccion(direcActualizar);
                usuarioActualiza.setTelefono(fonoActualizar);
                usuarioActualiza.setPasswrd(passEdit.getText().toString());


                AsyncCallWsActualizar task = new AsyncCallWsActualizar();
                task.execute();

            }
        });


        }

    //Con este metodo se cargan los datos al iniciar la pantalla        
    private class AsyncCallWsCargaDatos extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            //Call Web Method
            //usuario = WebService.invokeCargaDatos(rutActualizar, "selectUsuario");
            usuarioCarga = WebServiceNode.cargaDatosUsuarioNode(rutActualizar);
            return  null;
        }
        @Override
        //Once WebService returns response
        protected void onPostExecute(Void result) {

                //Based on Boolean value returned from WebService

                if (usuarioCarga!=null) {
                    dialog.dismiss();
                    //Navigate to Home Screen
                    Toast.makeText(getBaseContext(), "Edite sus datos", Toast.LENGTH_SHORT).show();
                    correoEdit.setText(usuarioCarga.getCorreo().toString());
                    direcEdit.setText(usuarioCarga.getDireccion().toString());
                    fonoEdit.setText(usuarioCarga.getTelefono()+"");
                   // passEdit.setText(usuario.getPasswrd().toString());

                    Toast.makeText(getBaseContext(), "Para actualizar sus datos debe ingresar su Password", Toast.LENGTH_SHORT).show();
                } else {
                    //Set Error message
                    Toast.makeText(getBaseContext(), "Problema al cargar datos", Toast.LENGTH_SHORT).show();

                }
        }




    }
    
    //Con este metodo se actualizan los datos del usuario
    private class AsyncCallWsActualizar extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            //Call Web Method
            //Status = WebService.invokeActualizarWS(rutActualizar,nombreActualizar,correoActualizar,passActualizar,direcActualizar,fonoActualizar, "modificarUsuario");
            Status = WebServiceNode.Actualiza(usuarioActualiza,passActualizar);

            return  null;
        }
        @Override
        //Once WebService returns response
        protected void onPostExecute(Void result) {


                if (Status!=false) {
                    //Navigate to Home Screen
                    Toast.makeText(getBaseContext(), "Se Actualizo Correctamente!!", Toast.LENGTH_SHORT).show();



                } else {
                    //Set Error message
                    Toast.makeText(getBaseContext(), "Problema al Actualizar datos", Toast.LENGTH_SHORT).show();

                }
                //Error status is true

        }




    }





    }

