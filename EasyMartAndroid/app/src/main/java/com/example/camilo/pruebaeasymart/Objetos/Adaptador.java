package com.example.camilo.pruebaeasymart.Objetos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.camilo.pruebaeasymart.R;

import java.util.List;

/**
 * Created by Desarrollo on 12-09-2017.
 */

public class Adaptador extends ArrayAdapter<Boleta_pro> {
    private int layout;
    private List<Boleta_pro> mObjects;
    public Adaptador(Context context, int resource, List<Boleta_pro> objects)
    {
        super(context, resource, objects);
        mObjects = objects;
        layout = resource;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        Elementos elementos = null;
        if(convertView == null)
        {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(layout, parent, false);
            Elementos elemento = new Elementos();

            elemento.nombreProducto = (TextView) convertView.findViewById(R.id.nombre_producto);
            elemento.nombreProducto.setText(mObjects.get(position).getNombreProducto());
            //elemento.nombreProducto.setId(mObjects.get(position).getId_producto());

            elemento.cantidad = (TextView) convertView.findViewById(R.id.idCantidad);
            elemento.cantidad.setText("Cantidad: "+mObjects.get(position).getCantidad()+"");

            elemento.precio = (TextView) convertView.findViewById(R.id.idPrecio);
            elemento.precio.setText("Precio: $"+mObjects.get(position).getValor());

            elemento.total = (TextView) convertView.findViewById(R.id.idTotal);
            elemento.total.setText("Total: $"+mObjects.get(position).getTotal());

            convertView.setTag(elemento);
        }

        return convertView;
    }
}