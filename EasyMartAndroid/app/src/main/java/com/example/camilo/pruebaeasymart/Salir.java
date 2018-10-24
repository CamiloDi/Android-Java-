package com.example.camilo.pruebaeasymart;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.logging.Handler;
import java.util.logging.LogRecord;


public class Salir extends AppCompatActivity {

    private Login log = new Login();
    private MenuApp ma = new MenuApp();
    private Comprar com = new Comprar();
    private Pagar pag = new Pagar();
    private Espera esp= new Espera();
    private final int DURACION_SPLASH = 5000;
    //region Preferencias
    public final static String PREFS_NAME = "Sesion_Usuario";
    SharedPreferences settings ;
    //endregion



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salir);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
       Bundle bundle = getIntent().getExtras();
        TextView nombre = (TextView) findViewById(R.id.txtSalir);
        settings  = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        if(settings.getBoolean("deEspera",false)) {
            nombre.setText(settings.getString("nomUsuario",""));
            settings.edit().clear();
            settings.edit().commit();
            log.lo.finish();
            ma.me.finish();
            com.co.finish();
            esp.es.finish();
            pag.pa.finish();
            settings.edit().clear();
            settings.edit().commit();
        }
        else
        {
            nombre.setText(settings.getString("nomUsuario",""));
            settings.edit().clear();
            settings.edit().commit();
            log.lo.finish();
            ma.me.finish();

        }


        toolbar.postDelayed(new Runnable() {
            public void run() {
                System.exit(0);
            }
        }, DURACION_SPLASH);


    }



}
