package com.example.camilo.pruebaeasymart;

import android.app.ActionBar;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.TextureView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Map;


public class MenuApp extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static Activity me;
    public String nombreUsuario, rutUsuario;
    public TextView nombreBarra, nomBienvenida;
    static boolean errored = false;
    Button compra;

    //region Preferencias
    public final static String PREFS_NAME = "Sesion_Usuario";
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_app);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        Bundle bundle = getIntent().getExtras();
        nomBienvenida = (TextView) findViewById(R.id.txtBienvenido);
        //rutUsuario = bundle.getString("Rut");

        //region Leer Preferencias.
        SharedPreferences settings  = getSharedPreferences(PREFS_NAME, 0);
        rutUsuario = settings.getString("rutUsuario","");
        nombreUsuario = settings.getString("nomUsuario","");
        //endregion

        nombreBarra = (TextView) findViewById(R.id.lblNombre);

        nomBienvenida.setText(nombreUsuario);


        compra=(Button)findViewById(R.id.btnComenComprar);
        compra.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent id = new Intent(MenuApp.this, Comprar.class);
                startActivity(id);
            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        me = this;

    }



    @Override
    public void onBackPressed() {
       // nombreBarra.setText(nomBienvenida.toString());
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {

            drawer.closeDrawer(GravityCompat.START);

        } else {
            super.onBackPressed();


        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_app, menu);
        return true;
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Handle navigation view item clicks here.
        int itemId = item.getItemId();

        if (itemId == R.id.nav_Usuario) {
            Intent id = new Intent(MenuApp.this, Actualizar.class);
            startActivity(id);
        } else if (itemId == R.id.nav_Boletas) {
            Intent id = new Intent(MenuApp.this, Boletas.class);
            startActivity(id);

        } else if (itemId == R.id.nav_Preguntas) {
            Intent id = new Intent(MenuApp.this, Preguntas.class);
            startActivity(id);

        } else if (itemId == R.id.nav_Comprar) {
            Intent id = new Intent(MenuApp.this, Comprar.class);
            startActivity(id);

        } else if (itemId == R.id.nav_Producto) {
            Intent id = new Intent(MenuApp.this, DetalleProducto.class);
            startActivity(id);

        } else if (itemId == R.id.nav_Salir) {

            Intent id = new Intent(MenuApp.this, Salir.class);
            startActivity(id);


        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
