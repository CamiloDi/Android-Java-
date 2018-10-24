package com.example.camilo.pruebaeasymart.Objetos;

/**
 * Created by Desarrollo on 24-09-2017.
 */

public class BoletaPro_Formato {
    private int cantidad;
    private int id_producto;
    private int rut;


    public BoletaPro_Formato() {

    }

    public BoletaPro_Formato(int rut, int id_producto, int cantidad) {

        this.cantidad = cantidad;
        this.id_producto = id_producto;
        this.rut = rut;
    }

    public int getRut() {
        return rut;
    }

    public void setRut(int rut) {
        this.rut = rut;
    }

    public int getId_producto() {
        return id_producto;
    }

    public void setId_producto(int id_producto) {
        this.id_producto = id_producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }


}
