package com.example.develop.eatit.Objetos;

import android.util.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class  oUsuario {
    private String nombre;
    private String correo;
    private String contraseña;
    private String direccion;
    private String telefono;
    public oUsuario(){

    }

    public oUsuario(String nombre, String correo, String contraseña, String direccion, String telefono) {
        this.nombre = nombre;
        this.correo = correo;
        this.contraseña = contraseña;
        this.direccion = direccion;
        this.telefono = telefono;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public oUsuario leerUsuario(JsonReader reader) throws IOException {
         String nombre = null;
         String correo= null;
         String contraseña= null;
         String direccion= null;
         String telefono= null;

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            switch (name) {
                case "nombre":
                    nombre = reader.nextString();
                    break;
                case "correo":
                    correo = reader.nextString();
                    break;
                case "contraseña":
                    contraseña = reader.nextString();
                    break;
                case "direccion":
                    direccion = reader.nextString();
                    break;
                case "telefono":
                    telefono = reader.nextString();
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }
        reader.endObject();
        return new oUsuario(nombre, correo, contraseña, direccion, telefono);
    }

    public List leerArrayUsuarios(JsonReader reader) throws IOException {

        ArrayList usuarios= new ArrayList();

        reader.beginArray();
        while (reader.hasNext()) {
            // Leer objeto
            usuarios.add(leerUsuario(reader));
        }
        reader.endArray();
        return usuarios;
    }

    public List<oUsuario> readJsonStream(InputStream in) throws IOException {
        // Nueva instancia JsonReader
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        try {
            // Leer Array
            return leerArrayUsuarios(reader);
        } finally {
            reader.close();
        }


    }
}
