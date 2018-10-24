package com.example.camilo.pruebaeasymart.Webservice;

import com.example.camilo.pruebaeasymart.Login;
import com.example.camilo.pruebaeasymart.Objetos.Boleta;
import com.example.camilo.pruebaeasymart.Objetos.Boleta_pro;
import com.example.camilo.pruebaeasymart.Objetos.Producto;
import com.example.camilo.pruebaeasymart.Objetos.Usuario;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class WebService {
    //Namespace of the Webservice - can be found in WSDL
    private static String NAMESPACE = "http://dao.easy.com";
    //Webservice URL - WSDL File location
    private static String URL = "http://190.100.216.62:8080/EasyMart/services/UsuarioWebService?wsdl";//Make sure you changed IP address
    private static String URL2 = "http://190.100.216.62:8080/EasyMart/services/ProductoWebService?wsdl";
    //SOAP Action URI again Namespace + Web method name
    private static String SOAP_ACTION1 = "http://dao.easy.com/loginUser";
    private static String SOAP_ACTION2 = "http://dao.easy.com/agregarUsuario";
    private static String SOAP_ACTION3 = "http://dao.easy.com/selectUsuario";
    private static String SOAP_ACTION4 = "http://dao.easy.com/modificarUsuario";
    private static String SOAP_ACTION5 = "http://dao.easy.com/detalleProducto";
    private static String SOAP_ACTION6 = "http://dao.easy.com/boletas";
    private static String SOAP_ACTION7 = "http://dao.easy.com/agregarBoleta";
    private static String SOAP_ACTION8 = "http://dao.easy.com/insertBoletaProducto";
    private static String SOAP_ACTION9 = "http://dao.easy.com/selectBoleta";
    private static String SOAP_ACTION10 = "http://dao.easy.com/CamitantesBlancos";
    private static String SOAP_ACTION12 = "http://dao.easy.com/actualizarPedido";
    private static String SOAP_ACTION11 = "http://dao.easy.com/selectPedido";
    private static String SOAP_ACTION13 = "http://dao.easy.com/crearPedido";


    public static String invokeLoginWS(String userName, String passWord, String webMethName) {
        String loginStatus = "Error";
        // Create request
        SoapObject request = new SoapObject(NAMESPACE, webMethName);
        // Property which holds input parameters
        PropertyInfo unamePI = new PropertyInfo();
        PropertyInfo passPI = new PropertyInfo();
        // Set Username
        unamePI.setName("username");
        // Set Value
        unamePI.setValue(userName);
        // Set dataType
        unamePI.setType(String.class);
        // Add the property to request object
        request.addProperty(unamePI);
        //Set Password
        passPI.setName("password");
        //Set dataType
        passPI.setValue(passWord);
        //Set dataType
        passPI.setType(String.class);
        //Add the property to request object
        request.addProperty(passPI);
        // Create envelope
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        // Set output SOAP object
        envelope.setOutputSoapObject(request);
        // Create HTTP call object
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        try {
            // Invoke web service
            androidHttpTransport.call(SOAP_ACTION1 + webMethName, envelope);
            // Get the response
            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            // Assign it to  boolean variable variable
            loginStatus = response.toString();

        } catch (Exception e) {
            //Assign Error Status true in static variable 'errored'
            //Login.errored = true;
            e.printStackTrace();
        }
        //Return booleam to calling object
        return loginStatus;
    }

    public static int invokeRegistroWS(String rut, String nombre, String correo, String pass, String direc, String fono, String webMethName) {
        int loginStatus = 1;

        // Create request
        SoapObject request = new SoapObject(NAMESPACE, webMethName);
        // Property which holds input parameters
        PropertyInfo rutPI = new PropertyInfo();
        PropertyInfo nomPI = new PropertyInfo();
        PropertyInfo corrPI = new PropertyInfo();
        PropertyInfo passPI = new PropertyInfo();
        PropertyInfo dirPI = new PropertyInfo();
        PropertyInfo fonPI = new PropertyInfo();

        rutPI.setName("rut");
        rutPI.setValue(rut);
        rutPI.setType(String.class);
        request.addProperty(rutPI);

        nomPI.setName("nombre");
        nomPI.setValue(nombre);
        nomPI.setType(String.class);
        request.addProperty(nomPI);

        corrPI.setName("correo");
        corrPI.setValue(correo);
        corrPI.setType(String.class);
        request.addProperty(corrPI);

        passPI.setName("password");
        passPI.setValue(pass);
        passPI.setType(String.class);
        request.addProperty(passPI);

        dirPI.setName("direccion");
        dirPI.setValue(direc);
        dirPI.setType(String.class);
        request.addProperty(dirPI);

        fonPI.setName("telefono");
        fonPI.setValue(fono);
        fonPI.setType(String.class);
        request.addProperty(fonPI);


        // Create envelope
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        // Set output SOAP object
        envelope.setOutputSoapObject(request);
        // Create HTTP call object
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        try {
            // Invoke web service
            androidHttpTransport.call(SOAP_ACTION2 + webMethName, envelope);
            // Get the response
            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            // Assign it to  boolean variable variable
            loginStatus = Integer.parseInt(response.toString());

        } catch (Exception e) {
            //Assign Error Status true in static variable 'errored'
           // Login.errored = true;
            e.printStackTrace();
        }
        //Return booleam to calling object
        return loginStatus;
    }

    public static Usuario invokeCargaDatos(String rut, String webMethName) {
        Usuario us = null;


        // Create request
        SoapObject request = new SoapObject(NAMESPACE, webMethName);
        // Property which holds input parameters
        PropertyInfo rutPI = new PropertyInfo();

        rutPI.setName("rut");
        rutPI.setValue(rut);
        rutPI.setType(String.class);
        request.addProperty(rutPI);


        // Create envelope
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        // Set output SOAP object
        envelope.setOutputSoapObject(request);
        // Create HTTP call object
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        try {
            // Invoke web service
            androidHttpTransport.call(SOAP_ACTION3 + webMethName, envelope);
            // Get the response
            SoapObject respuesta = (SoapObject) envelope.getResponse();

            if (respuesta != null){
                us = new Usuario();
            us.setCorreo(respuesta.getProperty("correo").toString());
            us.setDireccion(respuesta.getProperty("direccion").toString());
            us.setNombre(respuesta.getProperty("nombre").toString());
            us.setPasswrd(respuesta.getProperty("password").toString());
            //us.setRut(Integer.parseInt(respuesta.getProperty("rut").toString()));
            us.setTelefono(Integer.parseInt(respuesta.getProperty("telefono").toString()));}

        } catch (XmlPullParserException e1) {
            e1.printStackTrace();
        } catch (SoapFault soapFault) {
            soapFault.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return us;


    }

    public static String invokeActualizarWS(String rut, String nombre, String correo, String pass, String direc, String fono, String webMethName) {
        String loginStatus = "Error";

        // Create request
        SoapObject request = new SoapObject(NAMESPACE, webMethName);
        // Property which holds input parameters
        PropertyInfo rutPI = new PropertyInfo();
        PropertyInfo nomPI = new PropertyInfo();
        PropertyInfo corrPI = new PropertyInfo();
        PropertyInfo passPI = new PropertyInfo();
        PropertyInfo dirPI = new PropertyInfo();
        PropertyInfo fonPI = new PropertyInfo();

        rutPI.setName("rut");
        rutPI.setValue(rut);
        rutPI.setType(String.class);
        request.addProperty(rutPI);

        nomPI.setName("nombre");
        nomPI.setValue(nombre);
        nomPI.setType(String.class);
        request.addProperty(nomPI);

        corrPI.setName("correo");
        corrPI.setValue(correo);
        corrPI.setType(String.class);
        request.addProperty(corrPI);

        passPI.setName("password");
        passPI.setValue(pass);
        passPI.setType(String.class);
        request.addProperty(passPI);

        dirPI.setName("direccion");
        dirPI.setValue(direc);
        dirPI.setType(String.class);
        request.addProperty(dirPI);

        fonPI.setName("telefono");
        fonPI.setValue(fono);
        fonPI.setType(String.class);
        request.addProperty(fonPI);


        // Create envelope
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        // Set output SOAP object
        envelope.setOutputSoapObject(request);
        // Create HTTP call object
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        try {
            // Invoke web service
            androidHttpTransport.call(SOAP_ACTION4 + webMethName, envelope);
            // Get the response
            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            // Assign it to  boolean variable variable
            loginStatus = response.toString();

        } catch (Exception e) {
            //Assign Error Status true in static variable 'errored'
           // Login.errored = true;
            e.printStackTrace();
        }
        //Return booleam to calling object
        return loginStatus;
    }

    public static Producto invokeCargaDatosProducto(String id_prod, String webMethName) {
        Producto pr = null;


        // Create request
        SoapObject request = new SoapObject(NAMESPACE, webMethName);
        // Property which holds input parameters
        PropertyInfo IdPI = new PropertyInfo();

        IdPI.setName("id_producto");
        IdPI.setValue(id_prod);
        IdPI.setType(String.class);
        request.addProperty(IdPI);


        // Create envelope
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        // Set output SOAP object
        envelope.setOutputSoapObject(request);
        // Create HTTP call object
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL2);

        try {
            // Invoke web service
            androidHttpTransport.call(SOAP_ACTION5 + webMethName, envelope);
            // Get the response
            SoapObject respuesta = (SoapObject) envelope.getResponse();
            if (respuesta != null) {


                pr = new Producto();
                pr.setNombre_producto(respuesta.getProperty("nombre").toString());
                pr.setDetalle(respuesta.getProperty("detalle").toString());
                pr.setValor(Integer.parseInt(respuesta.getProperty("valor").toString()));
                pr.setStock(Integer.parseInt(respuesta.getProperty("stock").toString()));
                // pr.setImagen(respuesta.getProperty("foto").toString());
            } else {
                pr = new Producto();
                pr.setNombre_producto("Producto no Existe!, Por favor escanee otro codigo");
            }

        } catch (XmlPullParserException e1) {
            e1.printStackTrace();
        } catch (SoapFault soapFault) {
            soapFault.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return pr;


    }

    public static Boleta[] invokeCargaBoletas(String rut, String webMethName) {
        Boleta[] us = null;


        // Create request
        SoapObject request = new SoapObject(NAMESPACE, webMethName);
        // Property which holds input parameters
        PropertyInfo rutPI = new PropertyInfo();

        rutPI.setName("rut");
        rutPI.setValue(rut);
        rutPI.setType(String.class);
        request.addProperty(rutPI);


        // Create envelope
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        // Set output SOAP object
        envelope.setOutputSoapObject(request);

        HttpTransportSE transporte = new HttpTransportSE(URL2);
        try {
            transporte.call(SOAP_ACTION6 + webMethName, envelope);

            SoapObject resSoap = (SoapObject) envelope.bodyIn;
            us = new Boleta[resSoap.getPropertyCount()];


            for (int i = 0; i < resSoap.getPropertyCount(); i++) {
                SoapObject object = (SoapObject) resSoap.getProperty(i);


                Boleta cli = new Boleta();
                cli.setFecha(object.getProperty("fecha").toString());
                cli.setId_boleta(Integer.parseInt(object.getProperty("idBoleta").toString()));
                cli.setId_empleado(Integer.parseInt(object.getProperty("idEmpleado").toString()));
                //cli.setId_sucursal(Integer.parseInt(object.getProperty("idSucursal").toString()));
                cli.setRut_user(Integer.parseInt(object.getProperty("rut").toString()));
                cli.setTotal(Integer.parseInt(object.getProperty("total").toString()));
                us[i] = cli;

            }
        } catch (XmlPullParserException e1) {
            e1.printStackTrace();
        } catch (SoapFault soapFault) {
            soapFault.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return us;


    }

    public static int invokeAgregarBoleta(String rut, int total, String webMethName) {
        int us = 0;


        // Create request
        SoapObject request = new SoapObject(NAMESPACE, webMethName);
        // Property which holds input parameters
        PropertyInfo rutPI = new PropertyInfo();
        PropertyInfo totPI = new PropertyInfo();

        rutPI.setName("rut");
        rutPI.setValue(rut);
        rutPI.setType(String.class);
        request.addProperty(rutPI);

        totPI.setName("total");
        totPI.setValue(total);
        totPI.setType(String.class);
        request.addProperty(totPI);


        // Create envelope
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        // Set output SOAP object
        envelope.setOutputSoapObject(request);

        HttpTransportSE transporte = new HttpTransportSE(URL2);
        try {
            transporte.call(SOAP_ACTION7 + webMethName, envelope);

            SoapPrimitive resSoap = (SoapPrimitive) envelope.getResponse();
            us = Integer.parseInt(resSoap.toString()); /*new Boleta();

            us.setFecha(resSoap.getProperty("fecha").toString());
            us.setId_boleta(Integer.parseInt(resSoap.getProperty("idBoleta").toString()));
            us.setId_empleado(Integer.parseInt(resSoap.getProperty("idEmpleado").toString()));
            us.setId_sucursal(Integer.parseInt(resSoap.getProperty("idSucursal").toString()));
            us.setRut_user(Integer.parseInt(resSoap.getProperty("rut").toString()));
            us.setTotal(Integer.parseInt(resSoap.getProperty("total").toString()));*/

        } catch (XmlPullParserException e1) {
            e1.printStackTrace();
        } catch (SoapFault soapFault) {
            soapFault.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return us;


    }

    public static int invokeInsertarBoletaProducto(Boleta_pro[] x, String rut, String webMethName) {
        int us = 0;
        Boleta_pro b;


        // Create request
        SoapObject request = new SoapObject(NAMESPACE, webMethName);
        // Property which holds input parameters
        PropertyInfo rutPI = new PropertyInfo();
        PropertyInfo idPPI = new PropertyInfo();
        PropertyInfo canPI = new PropertyInfo();

        for (int i = 0; i < x.length; i++) {
            b = new Boleta_pro();
            b.setId_producto(x[i].getId_producto());
            b.setCantidad(x[i].getCantidad());

            rutPI.setName("tommy");
            rutPI.setValue(rut);
            rutPI.setType(String.class);
            request.addProperty(rutPI);

            idPPI.setName("asu");
            idPPI.setValue(b.getId_producto());
            idPPI.setType(String.class);
            request.addProperty(idPPI);

            canPI.setName("bianca");
            canPI.setValue(b.getCantidad());
            canPI.setType(String.class);
            request.addProperty(canPI);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
            // Set output SOAP object
            envelope.setOutputSoapObject(request);

            HttpTransportSE transporte = new HttpTransportSE(URL2);

            try {
                transporte.call(SOAP_ACTION8 + webMethName, envelope);

                SoapPrimitive resSoap = (SoapPrimitive) envelope.getResponse();
                us = us + Integer.parseInt(resSoap.toString());

            } catch (XmlPullParserException e1) {
                e1.printStackTrace();
            } catch (SoapFault soapFault) {
                soapFault.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

        return us;


    }

    public static Boleta invokeCargaBoletaPago(String rut, String webMethName) {
        Boleta us = null;


        // Create request
        SoapObject request = new SoapObject(NAMESPACE, webMethName);
        // Property which holds input parameters
        PropertyInfo rutPI = new PropertyInfo();

        rutPI.setName("rut");
        rutPI.setValue(rut);
        rutPI.setType(String.class);
        request.addProperty(rutPI);


        // Create envelope
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        // Set output SOAP object
        envelope.setOutputSoapObject(request);

        HttpTransportSE transporte = new HttpTransportSE(URL2);
        try {
            transporte.call(SOAP_ACTION9 + webMethName, envelope);

            SoapObject resSoap = (SoapObject) envelope.getResponse();
            us = new Boleta();


            us.setFecha(resSoap.getProperty("fecha").toString());
            us.setId_boleta(Integer.parseInt(resSoap.getProperty("idBoleta").toString()));
            us.setId_empleado(Integer.parseInt(resSoap.getProperty("idEmpleado").toString()));
            //cli.setId_sucursal(Integer.parseInt(resSoap.getProperty("idSucursal").toString()));
            us.setRut_user(Integer.parseInt(resSoap.getProperty("rut").toString()));
            us.setTotal(Integer.parseInt(resSoap.getProperty("total").toString()));


        } catch (XmlPullParserException e1) {
            e1.printStackTrace();
        } catch (SoapFault soapFault) {
            soapFault.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return us;


    }

    public static String[] invokeCargaBoletasDetalle(String id_boleta, String webMethName) {
        String[] us = null;


        // Create request
        SoapObject request = new SoapObject(NAMESPACE, webMethName);
        // Property which holds input parameters
        PropertyInfo rutPI = new PropertyInfo();

        rutPI.setName("id_boleta");
        rutPI.setValue(id_boleta);
        rutPI.setType(String.class);
        request.addProperty(rutPI);


        // Create envelope
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        // Set output SOAP object
        envelope.setOutputSoapObject(request);

        HttpTransportSE transporte = new HttpTransportSE(URL2);
        try {
            transporte.call(SOAP_ACTION10 + webMethName, envelope);

            SoapObject resSoap = (SoapObject) envelope.bodyIn;
            us = new String[resSoap.getPropertyCount()];

            for (int i = 0; i < resSoap.getPropertyCount(); i++) {
                SoapObject object = (SoapObject) resSoap.getProperty(i);
                String cli = object.getProperty("x").toString();
                us[i] = cli;

            }

        } catch (XmlPullParserException e1) {
            e1.printStackTrace();
        } catch (SoapFault soapFault) {
            soapFault.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return us;


    }

    public static int invokeSelectpedido(String id_boleta, String webMethName) {
        int loginStatus = 0;
        // Create request
        SoapObject request = new SoapObject(NAMESPACE, webMethName);
        // Property which holds input parameters
        PropertyInfo unamePI = new PropertyInfo();
        PropertyInfo passPI = new PropertyInfo();
        // Set Username
        unamePI.setName("id_boleta");
        // Set Value
        unamePI.setValue(id_boleta);
        // Set dataType
        unamePI.setType(String.class);
        // Add the property to request object
        request.addProperty(unamePI);
        // Create envelope
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        // Set output SOAP object
        envelope.setOutputSoapObject(request);
        // Create HTTP call object
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL2);

        try {
            // Invoke web service
            androidHttpTransport.call(SOAP_ACTION11 + webMethName, envelope);
            // Get the response
            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            // Assign it to  boolean variable variable
            loginStatus = Integer.parseInt(response.toString());

        } catch (Exception e) {
            //Assign Error Status true in static variable 'errored'
            //Login.errored = true;
            e.printStackTrace();
        }
        //Return booleam to calling object
        return loginStatus;
    }

    public static int invokeActualizarWSPedido(String pedido,String boleta, String webMethName) {
        int loginStatus = 0;

        // Create request
        SoapObject request = new SoapObject(NAMESPACE, webMethName);
        // Property which holds input parameters
        PropertyInfo rutPI = new PropertyInfo();
        PropertyInfo idbol = new PropertyInfo();


        rutPI.setName("id_pedido");
        rutPI.setValue(pedido);
        rutPI.setType(String.class);
        request.addProperty(rutPI);

        idbol.setName("id_boleta");
        idbol.setValue(boleta);
        idbol.setType(String.class);
        request.addProperty(idbol);


        // Create envelope
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        // Set output SOAP object
        envelope.setOutputSoapObject(request);
        // Create HTTP call object
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL2);

        try {
            // Invoke web service
            androidHttpTransport.call(SOAP_ACTION12 + webMethName, envelope);
            // Get the response
            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            // Assign it to  boolean variable variable
            loginStatus = Integer.parseInt(response.toString());

        } catch (Exception e) {
            //Assign Error Status true in static variable 'errored'
           // Login.errored = true;
            e.printStackTrace();
        }
        //Return booleam to calling object
        return loginStatus;
    }

    public static int invokeCrearpedido(String id_boleta, String rut, String webMethName) {
        int loginStatus = 0;
        // Create request
        SoapObject request = new SoapObject(NAMESPACE, webMethName);
        // Property which holds input parameters
        PropertyInfo unamePI = new PropertyInfo();
        PropertyInfo passPI = new PropertyInfo();
        // Set Username
        unamePI.setName("id_boleta");
        // Set Value
        unamePI.setValue(id_boleta);
        // Set dataType
        unamePI.setType(String.class);
        // Add the property to request object
        request.addProperty(unamePI);
        passPI.setName("rut");
        // Set Value
        passPI.setValue(rut);
        // Set dataType
        passPI.setType(String.class);
        // Add the property to request object
        request.addProperty(passPI);
        // Create envelope
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        // Set output SOAP object
        envelope.setOutputSoapObject(request);
        // Create HTTP call object
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL2);

        try {
            // Invoke web service
            androidHttpTransport.call(SOAP_ACTION13 + webMethName, envelope);
            // Get the response
            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            // Assign it to  boolean variable variable
            loginStatus = Integer.parseInt(response.toString());

        } catch (Exception e) {
            //Assign Error Status true in static variable 'errored'
            //Login.errored = true;
            e.printStackTrace();
        }
        //Return booleam to calling object
        return loginStatus;
    }


}

