package com.example.camilo.pruebaeasymart.Objetos;

import android.widget.TextView;

/**
 * Created by Desarrollo on 12-09-2017.
 */

public class Elementos{
    TextView nombreProducto;
    TextView cantidad;
    TextView precio;
    TextView total;
    public Elementos() {

    }

    public Elementos(TextView nombreProducto,TextView cantidad, TextView precio, TextView total) {
        this.nombreProducto = nombreProducto;
        this.cantidad = cantidad;
        this.precio = precio;
        this.total = total;
    }
    public TextView getNombreProducto() {
        return nombreProducto;
    }

    public TextView getPrecio() {
        return precio;
    }

    public TextView getTotal() {
        return total;
    }

    public TextView getCantidad() {
        return cantidad;
    }


    public void setNombreProducto(TextView nombreProducto) {
        this.nombreProducto = nombreProducto;
    }
    public void setPrecio(TextView precio) {
        this.precio = precio;
    }

    public void setTotal(TextView total) {
        this.total = total;
    }

    public void setCantidad(TextView cantidad) {
        this.cantidad = cantidad;
    }


}
