package com.example.camilo.pruebaeasymart.Objetos;

/**
 * Created by Camilo on 25-06-2016.
 */
public class medioPago {

    private String name;

    private int icon;

    public medioPago(String nombre, int icono)
    {
        super();
        this.name = nombre;
        this.icon = icono;
    }

    public String getNombre()
    {
        return name;
    }

    public void setNombre(String nombre)
    {
        this.name = nombre;
    }

    public int getIcono()
    {
        return icon;
    }

    public void setIcono(int icono)
    {
        this.icon = icono;
    }

}

