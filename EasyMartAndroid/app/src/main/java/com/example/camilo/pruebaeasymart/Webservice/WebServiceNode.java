package com.example.camilo.pruebaeasymart.Webservice;

/**
 * Created by Camilo D. on 12-07-2017.
 */
import android.util.Log;

import com.example.camilo.pruebaeasymart.Login;
import com.example.camilo.pruebaeasymart.Objetos.Boleta;
import com.example.camilo.pruebaeasymart.Objetos.BoletaPro_Formato;
import com.example.camilo.pruebaeasymart.Objetos.Boleta_pro;
import com.example.camilo.pruebaeasymart.Objetos.Producto;
import com.example.camilo.pruebaeasymart.Objetos.Usuario;
import com.google.gson.Gson;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.mindrot.jbcrypt.BCrypt;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class WebServiceNode {


    private static String URL = "http://54.207.10.181:5000/";


    //region Login User
    public static String loginUserNode(String user, String pass) {
        String loginStatus = "Error";
        //service api url
        String url = URL+"loginUser/?username="+user+"&password="+pass;
        //String url = URL+"loginUser/";

        HttpClient client = new DefaultHttpClient();
        HttpGet httpget = new HttpGet(url);
        httpget.setHeader("Accept", "application/json");


        HttpResponse response;

        try {

            httpget.setHeader("content-type", "application/json");

            response = client.execute(httpget);

            StatusLine statusLine = response.getStatusLine();

            if (statusLine.getStatusCode() == HttpStatus.SC_OK) {

                InputStream in = response.getEntity().getContent();
                BufferedReader buffered = new BufferedReader(new InputStreamReader(in));
                StringBuilder fullLines = new StringBuilder();

                String line;

                while ((line = buffered.readLine()) != null) {
                    fullLines.append(line);
                }
                in.close();

                String result = fullLines.toString();
                JSONObject respJSON = new JSONObject(result);
                if(respJSON.getString("loginSuccess")=="null")
                {
                return loginStatus;
                }
                else{
                    loginStatus = respJSON.getString("loginSuccess");
                }

            }
            else{
                return loginStatus;
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return loginStatus;
    }
   //endregion

    //region Crear Usuario
    public static boolean crearUserNode(Usuario usuario) throws UnsupportedEncodingException {
         boolean Status = false;
        //service api url
      String url =URL+"crearUser/";


        HttpClient client = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);

        HttpResponse response;

        try {

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(6);
            nameValuePairs.add(new BasicNameValuePair("rut",usuario.getRut()+""));
            nameValuePairs.add(new BasicNameValuePair("nombre", usuario.getNombre()));
            nameValuePairs.add(new BasicNameValuePair("correo", usuario.getCorreo()));
            nameValuePairs.add(new BasicNameValuePair("password",usuario.getPasswrd() ));
            nameValuePairs.add(new BasicNameValuePair("direccion", usuario.getDireccion()));
            nameValuePairs.add(new BasicNameValuePair("telefono", usuario.getTelefono()+""));
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(nameValuePairs,"UTF-8");
            httpPost.setEntity(entity);

            BasicHttpParams params = new BasicHttpParams();
            httpPost.setParams(params);

            response = client.execute(httpPost);

            StatusLine statusLine = response.getStatusLine();

            if (statusLine.getStatusCode() == HttpStatus.SC_OK) {

                InputStream in = response.getEntity().getContent();
                BufferedReader buffered = new BufferedReader(new InputStreamReader(in));
                StringBuilder fullLines = new StringBuilder();

                String line;

                while ((line = buffered.readLine()) != null) {
                    fullLines.append(line);
                }
                in.close();

                String result = fullLines.toString();
                JSONObject respJSON = new JSONObject(result);
                Status = respJSON.getBoolean("postCheck");
            }
            else{
                return Status;
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return Status;

    }
//endregion

    //region Consultar Producto
    public static Producto cargaDatosProductoNode(String idProducto ) {
        Producto pr = null;
        //service api url
        String url = URL+"detalleProducto/?id_producto="+idProducto+"";

        HttpClient client = new DefaultHttpClient();
        HttpGet httpget = new HttpGet(url);
        httpget.setHeader("Accept", "application/json");

        HttpResponse response;

        try {

            httpget.setHeader("content-type", "application/json");

            response = client.execute(httpget);

            StatusLine statusLine = response.getStatusLine();

            if (statusLine.getStatusCode() == HttpStatus.SC_OK) {

                InputStream in = response.getEntity().getContent();
                BufferedReader buffered = new BufferedReader(new InputStreamReader(in));
                StringBuilder fullLines = new StringBuilder();

                String line;

                while ((line = buffered.readLine()) != null) {
                    fullLines.append(line);
                }
                in.close();

                String result = fullLines.toString();

                int retorno = result.indexOf("[]");

                    if(retorno!= 0)
                    {
                        JSONObject respJSON = new JSONObject(result);

                            pr = new Producto();
                            pr.setNombre_producto(respJSON.getString("NOMBRE"));
                            pr.setDetalle(respJSON.getString("DETALLE"));
                            pr.setValor(respJSON.getInt("VALOR"));
                            pr.setStock(respJSON.getInt("STOCK"));
                            //pr.setImagen(respJSON.getJSONObject(i).getString("FOTO"));

                    }
                    else{
                        pr = new Producto();
                        pr.setNombre_producto("Producto no Existe!, Por favor escanee otro codigo valido!");
                    }
                }
            else{
                return pr;
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return pr;
    }
    //endregion

    //region Cargar Usuario
    public static Usuario cargaDatosUsuarioNode(String rut) {
        Usuario us = null;
        //service api url
        String url = URL+"selectUser/?rut="+rut+"";
        //String url = URL+"loginUser/";

        HttpClient client = new DefaultHttpClient();
        HttpGet httpget = new HttpGet(url);
        httpget.setHeader("Accept", "application/json");


        HttpResponse response;

        try {

            httpget.setHeader("content-type", "application/json");

            response = client.execute(httpget);

            StatusLine statusLine = response.getStatusLine();

            if (statusLine.getStatusCode() == HttpStatus.SC_OK) {

                InputStream in = response.getEntity().getContent();
                BufferedReader buffered = new BufferedReader(new InputStreamReader(in));
                StringBuilder fullLines = new StringBuilder();

                String line;

                while ((line = buffered.readLine()) != null) {
                    fullLines.append(line);
                }
                in.close();

                String result = fullLines.toString();
                int retorno = result.indexOf("[]");


                if(retorno!= 0)
                {JSONObject respJSON = new JSONObject(result);
                        us = new Usuario();
                        us.setNombre(respJSON.getString("NOMBRE"));
                        us.setCorreo(respJSON.getString("CORREO"));
                        //us.setPasswrd(respJSON.getString("PASSWRD"));
                        us.setDireccion(respJSON.getString("DIRECCION"));
                        us.setTelefono(respJSON.getInt("TELEFONO"));

                }else{
                    return us;
                }


            }

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return us;
    }
    //endregion

    //region Actualizar  Usuario
    public static boolean Actualiza(Usuario usuario, String nvaPass) {
        boolean Status = false;
        //service api url
        String url =URL+"modificarUsuario/";


        HttpClient client = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);

        HttpResponse response;

        try {

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(7);
            nameValuePairs.add(new BasicNameValuePair("nvopsw",nvaPass));
            nameValuePairs.add(new BasicNameValuePair("rut",usuario.getRut()+""));
            nameValuePairs.add(new BasicNameValuePair("nombre", usuario.getNombre()));
            nameValuePairs.add(new BasicNameValuePair("correo", usuario.getCorreo()));
            nameValuePairs.add(new BasicNameValuePair("password",usuario.getPasswrd() ));
            nameValuePairs.add(new BasicNameValuePair("direccion", usuario.getDireccion()));
            nameValuePairs.add(new BasicNameValuePair("telefono", usuario.getTelefono()+""));
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(nameValuePairs,"UTF-8");
            httpPost.setEntity(entity);

            BasicHttpParams params = new BasicHttpParams();
            httpPost.setParams(params);

            response = client.execute(httpPost);

            StatusLine statusLine = response.getStatusLine();

            if (statusLine.getStatusCode() == HttpStatus.SC_OK) {

                InputStream in = response.getEntity().getContent();
                BufferedReader buffered = new BufferedReader(new InputStreamReader(in));
                StringBuilder fullLines = new StringBuilder();

                String line;

                while ((line = buffered.readLine()) != null) {
                    fullLines.append(line);
                }
                in.close();

                String result = fullLines.toString();
                JSONObject respJSON = new JSONObject(result);
                Status = respJSON.getBoolean("postCheck");
            }
            else{
                return Status;
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return Status;

    }
//endregion

    //region Cargar Boletas
    public static Boleta[] cargaBoletasNode(String rut) {
        Boleta[] boletas = null;
        //service api url
        String url = URL+"boletas/?rut="+rut;

        HttpClient client = new DefaultHttpClient();
        HttpGet httpget = new HttpGet(url);
        httpget.setHeader("Accept", "application/json");

        HttpResponse response;

        try {

            httpget.setHeader("content-type", "application/json");

            response = client.execute(httpget);

            StatusLine statusLine = response.getStatusLine();

            if (statusLine.getStatusCode() == HttpStatus.SC_OK) {

                InputStream in = response.getEntity().getContent();
                BufferedReader buffered = new BufferedReader(new InputStreamReader(in));
                StringBuilder fullLines = new StringBuilder();

                String line;

                while ((line = buffered.readLine()) != null) {
                    fullLines.append(line);
                }
                in.close();

                String result = fullLines.toString();
                int esVacio = result.indexOf("[]");
                int esArray =result.indexOf("[{");


                if(esVacio== -1 && esArray==-1)
                {
                    JSONObject respJSON = new JSONObject(result);
                    boletas = new Boleta[1];
                    Boleta boleta = new Boleta();
                    boleta.setId_boleta(respJSON.getInt("ID_BOLETA"));
                    boleta.setFecha(respJSON.getString("FECHA"));
                    boleta.setTotal(respJSON.getInt("TOTAL"));
                    //boleta.setId_empleado(respJSON.getJSONObject(i).getInt("ID_EMPLEADO"));
                    boletas[0] = boleta;
                }
                if(esVacio== -1 && esArray==0)
                {
                    JSONArray respJSON = new JSONArray(result);
                    boletas = new Boleta[respJSON.length()];
                    Boleta boleta;
                    for(int i = 0; i < respJSON.length(); i++)
                    {
                        boleta = new Boleta();
                        boleta.setId_boleta(respJSON.getJSONObject(i).getInt("ID_BOLETA"));
                        boleta.setFecha(respJSON.getJSONObject(i).getString("FECHA"));
                        boleta.setTotal(respJSON.getJSONObject(i).getInt("TOTAL"));
                        //boleta.setId_empleado(respJSON.getJSONObject(i).getInt("ID_EMPLEADO"));
                        boletas[i] = boleta;

                    }

                }
                if(esVacio== 0 && esArray==-1)
                {
                    boletas = new Boleta[1];
                    Boleta boleta = new Boleta();
                    boleta.setId_boleta(0);
                    boletas[0] = boleta;
                }
                else{
                    return boletas;
                }


            }

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return boletas;
    }
    //endregion

    //region Cargar Detalle Boleta
    public static Boleta_pro[] cargaDetalleBoletaNode(String id_boleta) {
        Boleta_pro[] detalleBoleta = null;
        //service api url
        String url = URL+"detalleBoleta/?id_boleta="+id_boleta;

        HttpClient client = new DefaultHttpClient();
        HttpGet httpget = new HttpGet(url);
        httpget.setHeader("Accept", "application/json");


        HttpResponse response;

        try {

            httpget.setHeader("content-type", "application/json");

            response = client.execute(httpget);

            StatusLine statusLine = response.getStatusLine();

            if (statusLine.getStatusCode() == HttpStatus.SC_OK) {

                InputStream in = response.getEntity().getContent();
                BufferedReader buffered = new BufferedReader(new InputStreamReader(in));
                StringBuilder fullLines = new StringBuilder();

                String line;

                while ((line = buffered.readLine()) != null) {
                    fullLines.append(line);
                }
                in.close();

                String result = fullLines.toString();
                int esVacio = result.indexOf("[]");
                int esArray =result.indexOf("[{");


                if(esVacio== -1 && esArray==-1)
                {
                    JSONObject respJSON = new JSONObject(result);
                    detalleBoleta = new Boleta_pro[1];
                    Boleta_pro producto = new Boleta_pro();
                        producto.setId_boleta(Integer.parseInt(id_boleta));
                        producto.setId_producto(respJSON.getInt("ID_PRODUCTO"));
                        producto.setNombreProducto(respJSON.getString("NOMBRE"));
                        producto.setCantidad(respJSON.getInt("CANTIDAD"));
                        producto.setValor(respJSON.getInt("VALOR"));
                        producto.setTotal(respJSON.getInt("TOTAL"));
                        detalleBoleta[0] = producto;
                }
                if(esVacio== -1 && esArray==0)
                {
                    JSONArray respJSON = new JSONArray(result);
                    detalleBoleta = new Boleta_pro[respJSON.length()];
                    Boleta_pro producto;
                    for(int i = 0; i < respJSON.length(); i++)
                    {
                        producto = new Boleta_pro();
                        producto.setId_boleta(Integer.parseInt(id_boleta));
                        producto.setId_producto(respJSON.getJSONObject(i).getInt("ID_PRODUCTO"));
                        producto.setNombreProducto(respJSON.getJSONObject(i).getString("NOMBRE"));
                        producto.setCantidad(respJSON.getJSONObject(i).getInt("CANTIDAD"));
                        producto.setValor(respJSON.getJSONObject(i).getInt("VALOR"));
                        producto.setTotal(respJSON.getJSONObject(i).getInt("TOTAL"));
                        detalleBoleta[i] = producto;

                    }
                }else{
                    return detalleBoleta;
                }


            }

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return detalleBoleta;
    }
    //endregion

    //region agregarBoleta
    public static int agregarBoletaNode(String rut, int total) {
        int nroBoleta = 0;
        //service api url
        String url =URL+"crearBoleta/";


        HttpClient client = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);

        HttpResponse response;

        try {

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("rut",rut));
            nameValuePairs.add(new BasicNameValuePair("total",""+total));

            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(nameValuePairs,"UTF-8");
            httpPost.setEntity(entity);

            BasicHttpParams params = new BasicHttpParams();
            httpPost.setParams(params);

            response = client.execute(httpPost);

            StatusLine statusLine = response.getStatusLine();

            if (statusLine.getStatusCode() == HttpStatus.SC_OK) {

                InputStream in = response.getEntity().getContent();
                BufferedReader buffered = new BufferedReader(new InputStreamReader(in));
                StringBuilder fullLines = new StringBuilder();

                String line;

                while ((line = buffered.readLine()) != null) {
                    fullLines.append(line);
                }
                in.close();

                String result = fullLines.toString();
                JSONObject respJSON = new JSONObject(result);
                nroBoleta = respJSON.getInt("id_boleta");
            }
            else{
                return nroBoleta;
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return nroBoleta;

    }
    //endregion

    //region Agregar Producto Boleta
    public static boolean agregarBoletaProNode(Boleta_pro[] listaProductos, String rut) {
        boolean retorno = false;
        //service api url
        String url =URL+"insertBoletaProducto/";


        HttpClient client = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);

        HttpResponse response;

        try {
            BoletaPro_Formato[] listaFormateada = new BoletaPro_Formato[listaProductos.length];

            for(int i=0;i<listaProductos.length;i++)
            {
                BoletaPro_Formato boleta = new BoletaPro_Formato();


                boleta.setCantidad(listaProductos[i].getCantidad());
                boleta.setId_producto(listaProductos[i].getId_producto());
                boleta.setRut(Integer.parseInt(rut));
                listaFormateada[i] = boleta;

            }
            String alpha = new Gson().toJson(listaFormateada);

            StringEntity entity = new StringEntity(alpha);
            httpPost.setHeader("Content-type", "application/json");

            httpPost.setEntity(entity);

            BasicHttpParams params = new BasicHttpParams();
            httpPost.setParams(params);

            response = client.execute(httpPost);

            StatusLine statusLine = response.getStatusLine();

            if (statusLine.getStatusCode() == HttpStatus.SC_OK) {

                InputStream in = response.getEntity().getContent();
                BufferedReader buffered = new BufferedReader(new InputStreamReader(in));
                StringBuilder fullLines = new StringBuilder();

                String line;

                while ((line = buffered.readLine()) != null) {
                    fullLines.append(line);
                }
                in.close();

                String result = fullLines.toString();
                JSONObject respJSON = new JSONObject(result);
                retorno = respJSON.getBoolean("postCheck");
            }
            else{
                return retorno;
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return retorno;

    }
//endregion

    //region Cargar Ultima Boleta
    public static Boleta cargarUltimaBoletaNode(String rut) {
        Boleta boleta = null;
        //service api url
        String url = URL+"ultimaboleta/?rut="+rut;

        HttpClient client = new DefaultHttpClient();
        HttpGet httpget = new HttpGet(url);
        httpget.setHeader("Accept", "application/json");


        HttpResponse response;

        try {

            httpget.setHeader("content-type", "application/json");

            response = client.execute(httpget);

            StatusLine statusLine = response.getStatusLine();

            if (statusLine.getStatusCode() == HttpStatus.SC_OK) {

                InputStream in = response.getEntity().getContent();
                BufferedReader buffered = new BufferedReader(new InputStreamReader(in));
                StringBuilder fullLines = new StringBuilder();

                String line;

                while ((line = buffered.readLine()) != null) {
                    fullLines.append(line);
                }
                in.close();

                String result = fullLines.toString();
                int retorno = result.indexOf("[]");


                if (retorno != 0) {

                    JSONObject respJSON = new JSONObject(result);
                        boleta = new Boleta();

                        boleta.setId_boleta(respJSON.getInt("ID_BOLETA"));
                        boleta.setFecha(respJSON.getString("FECHA"));
                        boleta.setTotal(respJSON.getInt("TOTAL"));
                        //boleta.setId_empleado(respJSON.getJSONObject(i).getInt("ID_EMPLEADO"));

                }
                }else{
                    return boleta;
                }

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return boleta;
    }
    //endregion

    //region Obtener Pedido
    public static int obtenerPedidoNode(String id_boleta) {
        int num_pedido = 0;
        //service api url
        String url = URL+"obtenerPedido/?id_boleta="+id_boleta;

        HttpClient client = new DefaultHttpClient();
        HttpGet httpget = new HttpGet(url);
        httpget.setHeader("Accept", "application/json");


        HttpResponse response;

        try {

            httpget.setHeader("content-type", "application/json");

            response = client.execute(httpget);

            StatusLine statusLine = response.getStatusLine();

            if (statusLine.getStatusCode() == HttpStatus.SC_OK) {

                InputStream in = response.getEntity().getContent();
                BufferedReader buffered = new BufferedReader(new InputStreamReader(in));
                StringBuilder fullLines = new StringBuilder();

                String line;

                while ((line = buffered.readLine()) != null) {
                    fullLines.append(line);
                }
                in.close();

                String result = fullLines.toString();
                int retorno = result.indexOf("[]");
                if (retorno != 0) {

                    JSONObject respJSON = new JSONObject(result);
                    num_pedido =Integer.parseInt(respJSON.getString("ID_PEDIDO"));
                }
            }else{
                return num_pedido;
            }

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return num_pedido;
    }
    //endregion

    //region Crear Pedido
    public static int crearPedidoNode(String id_boleta, String rut) throws UnsupportedEncodingException {
        int id_pedido = 0;
        //service api url
        String url =URL+"crearPedido/";


        HttpClient client = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);

        HttpResponse response;

        try {

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("rut",rut));
            nameValuePairs.add(new BasicNameValuePair("id_boleta", id_boleta));

            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(nameValuePairs,"UTF-8");
            httpPost.setEntity(entity);

            BasicHttpParams params = new BasicHttpParams();
            httpPost.setParams(params);

            response = client.execute(httpPost);

            StatusLine statusLine = response.getStatusLine();

            if (statusLine.getStatusCode() == HttpStatus.SC_OK) {

                InputStream in = response.getEntity().getContent();
                BufferedReader buffered = new BufferedReader(new InputStreamReader(in));
                StringBuilder fullLines = new StringBuilder();

                String line;

                while ((line = buffered.readLine()) != null) {
                    fullLines.append(line);
                }
                in.close();

                String result = fullLines.toString();
                int retorno = result.indexOf("postCheck");
                if (retorno != 2)
                {
                JSONObject respJSON = new JSONObject(result);
                id_pedido = Integer.parseInt(respJSON.getString("ID_PEDIDO"));

                return id_pedido;
                }
                else {
                    return id_pedido;
                }
            }
            else{
                return id_pedido;
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return id_pedido;

    }
//endregion


    //region Actualizar Pedido
    public static boolean actualizarPedidoNode(String pedido,String boleta) throws UnsupportedEncodingException {
        boolean estadoPedido = false;
        //service api url
        String url =URL+"actualizarPedido/";


        HttpClient client = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);

        HttpResponse response;

        try {

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("pedido",pedido));
            nameValuePairs.add(new BasicNameValuePair("boleta", boleta));

            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(nameValuePairs,"UTF-8");
            httpPost.setEntity(entity);

            BasicHttpParams params = new BasicHttpParams();
            httpPost.setParams(params);

            response = client.execute(httpPost);

            StatusLine statusLine = response.getStatusLine();

            if (statusLine.getStatusCode() == HttpStatus.SC_OK) {

                InputStream in = response.getEntity().getContent();
                BufferedReader buffered = new BufferedReader(new InputStreamReader(in));
                StringBuilder fullLines = new StringBuilder();

                String line;

                while ((line = buffered.readLine()) != null) {
                    fullLines.append(line);
                }
                in.close();

                String result = fullLines.toString();

                    JSONObject respJSON = new JSONObject(result);
                    estadoPedido = respJSON.getBoolean("postSPCheck");

                    return estadoPedido;

            }
            else{
                return estadoPedido;
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return estadoPedido;

    }
//endregion

    //region Consultar dolar
    public static int consultarDolar(String fechaFormateada,int contadorDias) {
        int dolar = 0;
        //service api url



        String url = "http://mindicador.cl/api/dolar/"+fechaFormateada;


        HttpClient client = new DefaultHttpClient();
        HttpGet httpget = new HttpGet(url);
        httpget.setHeader("Accept", "application/json");


        HttpResponse response;

        try {

            httpget.setHeader("content-type", "application/json");

            response = client.execute(httpget);

            StatusLine statusLine = response.getStatusLine();

            if (statusLine.getStatusCode() == HttpStatus.SC_OK) {

                InputStream in = response.getEntity().getContent();
                BufferedReader buffered = new BufferedReader(new InputStreamReader(in));
                StringBuilder fullLines = new StringBuilder();

                String line;

                while ((line = buffered.readLine()) != null) {
                    fullLines.append(line);
                }
                in.close();

                String result = fullLines.toString();


                JSONObject resp = new JSONObject(result);
                JSONArray arreglo = resp.getJSONArray("serie");
                if(arreglo.length()> 0)
                {
                    for(int i = 0; i < arreglo.length(); i++)
                    {
                        String conversion = arreglo.getJSONObject(i).getString("valor");
                        dolar=Math.round(Float.parseFloat(conversion));
                    }
                }else{
                    Date fechaFinal;
                    Calendar ayer  =  Calendar.getInstance();
                    ayer.add(Calendar.DATE,-contadorDias);
                    fechaFinal =ayer.getTime();
                    SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");
                    String nuevaFecha = formato.format(fechaFinal);
                    contadorDias++;
                    dolar = new WebServiceNode().consultarDolar(nuevaFecha,contadorDias);
                }

            }
            else{
                return dolar;
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return dolar;
    }
    //endregion
    }


