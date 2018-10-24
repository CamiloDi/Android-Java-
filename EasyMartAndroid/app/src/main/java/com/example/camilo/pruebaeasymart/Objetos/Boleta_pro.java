package com.example.camilo.pruebaeasymart.Objetos;

/**
 * Created by Camilo on 14-06-2016.
 */
public class Boleta_pro {
    private int id_boleta;
    private int id_producto;
    private int cantidad;
    private String nombreProducto;
    private int valor;
    private int total;

    public Boleta_pro() {

    }

    public Boleta_pro(int id_boleta, int id_producto, int cantidad, String nombreProducto, int valor, int total) {
        this.id_boleta = id_boleta;
        this.id_producto = id_producto;
        this.cantidad = cantidad;
        this.nombreProducto = nombreProducto;
        this.valor = valor;
        this.total = total;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public int getId_boleta() {
        return id_boleta;
    }

    public void setId_boleta(int id_boleta) {
        this.id_boleta = id_boleta;
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

    public int getValor() {
        return valor;
    }

    public void setValor(int valor) {
        this.valor = valor;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
