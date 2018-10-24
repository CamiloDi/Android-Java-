package com.example.develop.eatit.Objetos;

import java.util.Date;

/**
 * Created by Camilo on 10-04-2017.
 */

public class oBoleta {

    private int IdBoleta;
    private int IdUsuario;
    private int IdSucursal;
    private Date fecha;
    private int IdEmpleado;
    private int IdEstado;
    private String Pago;
    private int Total;

    public oBoleta(int idBoleta, int idUsuario, int idSucursal, Date fecha, int idEmpleado, int idEstado, String pago, int total, String direccion) {
        setIdBoleta(idBoleta);
        setIdUsuario(idUsuario);
        setIdSucursal(idSucursal);
        this.setFecha(fecha);
        setIdEmpleado(idEmpleado);
        setIdEstado(idEstado);
        setPago(pago);
        setTotal(total);
        setDireccion(direccion);
    }

    private String Direccion;


    public int getIdBoleta() {
        return IdBoleta;
    }

    public void setIdBoleta(int idBoleta) {
        IdBoleta = idBoleta;
    }

    public int getIdUsuario() {
        return IdUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        IdUsuario = idUsuario;
    }

    public int getIdSucursal() {
        return IdSucursal;
    }

    public void setIdSucursal(int idSucursal) {
        IdSucursal = idSucursal;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public int getIdEmpleado() {
        return IdEmpleado;
    }

    public void setIdEmpleado(int idEmpleado) {
        IdEmpleado = idEmpleado;
    }

    public int getIdEstado() {
        return IdEstado;
    }

    public void setIdEstado(int idEstado) {
        IdEstado = idEstado;
    }

    public String getPago() {
        return Pago;
    }

    public void setPago(String pago) {
        Pago = pago;
    }

    public int getTotal() {
        return Total;
    }

    public void setTotal(int total) {
        Total = total;
    }

    public String getDireccion() {
        return Direccion;
    }

    public void setDireccion(String direccion) {
        Direccion = direccion;
    }
}
