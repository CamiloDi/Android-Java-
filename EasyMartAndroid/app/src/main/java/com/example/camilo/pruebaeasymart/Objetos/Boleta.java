package com.example.camilo.pruebaeasymart.Objetos;

/**
 * Created by Camilo on 09-06-2016.
 */
public class Boleta {
    private int id_boleta;
    private int rut_user;
    private int id_sucursal;
    private int total;
    private String fecha;
    private int id_empleado;
    //Prueba EasyMart

    public int getId_boleta() {
        return id_boleta;
    }

    public void setId_boleta(int id_boleta) {
        this.id_boleta = id_boleta;
    }

    public int getRut_user() {
        return rut_user;
    }

    public void setRut_user(int rut_user) {
        this.rut_user = rut_user;
    }

    public int getId_sucursal() {
        return id_sucursal;
    }

    public void setId_sucursal(int id_sucursal) {
        this.id_sucursal = id_sucursal;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getId_empleado() {
        return id_empleado;
    }

    public void setId_empleado(int id_empleado) {
        this.id_empleado = id_empleado;
    }
}
