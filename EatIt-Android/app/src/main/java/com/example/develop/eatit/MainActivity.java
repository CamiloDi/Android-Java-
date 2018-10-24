package com.example.develop.eatit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.develop.eatit.Fragments.Carta;
import com.example.develop.eatit.Fragments.Datos;
import com.example.develop.eatit.Fragments.Help;
import com.example.develop.eatit.Fragments.Historial;
import com.example.develop.eatit.Fragments.Home;

import static com.example.develop.eatit.Login.USER_NAME;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private String username;
    //aca inicilizo
    private SharedPreferences.Editor editor ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
       ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        Intent intent = getIntent();
        username = intent.getStringExtra("user1");
        //aca asigno variable de preferencias
        editor=getSharedPreferences(username, MODE_PRIVATE).edit();



        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.contenedor_Fragment, new Home()).commit();


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();
        FragmentManager fn= getSupportFragmentManager();

        if (id == R.id.navDatos) {
            //aca envio a al fragment la variable
            editor.putString("user", USER_NAME);
            editor.commit();


            fn.beginTransaction().replace(R.id.contenedor_Fragment,new Datos()).commit();




        } else if (id == R.id.navCarta) {

            fn.beginTransaction().replace(R.id.contenedor_Fragment,new Carta()).commit();

        } else if (id == R.id.navHistorial) {
            fn.beginTransaction().replace(R.id.contenedor_Fragment,new Historial()).commit();

        } else if (id == R.id.navHelp) {
            fn.beginTransaction().replace(R.id.contenedor_Fragment,new Help()).commit();

        } else if (id == R.id.navSalir) {
            Intent intent = new Intent(this,Login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }








}
