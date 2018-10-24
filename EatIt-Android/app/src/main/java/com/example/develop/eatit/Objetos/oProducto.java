package com.example.develop.eatit.Objetos;



public class oProducto {
    private int IdProducto;
    private String Nombre;
    private String Detalle;
    private int Valor;
    private int Tipo_Producto;
    private int Stock;

    public oProducto(int idProducto,String nombre,String detalle,int valor,int tipo_Producto,int stock){
        this.setIdProducto(idProducto);
        this.setNombre(nombre);
        this.setDetalle(detalle);
        this.setValor(valor);
        this.setTipo_Producto(tipo_Producto);
        this.setStock(stock);



    }

    public int getIdProducto() {
        return IdProducto;
    }

    public void setIdProducto(int idProducto) {
        IdProducto = idProducto;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getDetalle() {
        return Detalle;
    }

    public void setDetalle(String detalle) {
        Detalle = detalle;
    }

    public int getValor() {
        return Valor;
    }

    public void setValor(int valor) {
        Valor = valor;
    }

    public int getTipo_Producto() {
        return Tipo_Producto;
    }

    public void setTipo_Producto(int tipo_Producto) {
        Tipo_Producto = tipo_Producto;
    }

    public int getStock() {
        return Stock;
    }

    public void setStock(int stock) {
        Stock = stock;
    }
}
