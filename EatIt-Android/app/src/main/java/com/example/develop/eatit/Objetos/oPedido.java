package com.example.develop.eatit.Objetos;

/**
 * Created by Camilo on 10-04-2017.
 */

public class oPedido {
//Esto es una prueba para github
    private int idPedido;
    private int idBoleta;
    private int idProducto;

    public oPedido(int idPedido, int idBoleta, int idProducto, int cantidad) {
        this.idPedido = idPedido;
        this.idBoleta = idBoleta;
        this.idProducto = idProducto;
        this.cantidad = cantidad;

    }

    private int cantidad;

    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public int getIdBoleta() {
        return idBoleta;
    }

    public void setIdBoleta(int idBoleta) {
        this.idBoleta = idBoleta;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}
