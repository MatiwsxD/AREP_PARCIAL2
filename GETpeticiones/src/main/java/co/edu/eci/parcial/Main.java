package co.edu.eci.parcial;

import org.json.JSONObject;
import spark.Request;
import spark.Response;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import static spark.Spark.get;
import static spark.Spark.port;

class Main {
    private static  int cont = 0;

    public static void main(String[] args) {
        port(getPort());
        get("/exp", (req, res) -> roudrobin(req, res, "exp"));
        get("/log", (req, res) -> roudrobin(req, res, "log"));
    }

    public static String roudrobin(Request req, Response res, String operation) {
        double number = Double.parseDouble(req.queryParams("value"));
        String[] url = new String[]{"http://ec2-54-173-27-43.compute-1.amazonaws.com:3456", "http://ec2-35-175-146-55.compute-1.amazonaws.com:3456"};



        if (cont == 0) {
            cont = 1;
        } else if (cont == 1) {
            cont = 0;
        }
        String m = url[cont] + "/" + operation + "?value=" + number;
        return PeticionGet(m);


    }
    public static String PeticionGet(String x) {
        String url = x;
        String respuesta = "";
        try {
            respuesta = peticionHttpGet(url);
            System.out.println("ServidorLa respuesta es:\n" + respuesta);
            System.out.println(cont);
        } catch (Exception e) {
            // Manejar excepción
            e.printStackTrace();
        }
        return respuesta;
    }

    public static String peticionHttpGet(String urlParaVisitar) throws Exception {
        // Esto es lo que vamos a devolver
        StringBuilder resultado = new StringBuilder();
        // Crear un objeto de tipo URL
        URL url = new URL(urlParaVisitar);

        // Abrir la conexión e indicar que será de tipo GET
        HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
        conexion.setRequestMethod("GET");
        // Búferes para leer
        BufferedReader rd = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
        String linea;
        // Mientras el BufferedReader se pueda leer, agregar contenido a resultado
        while ((linea = rd.readLine()) != null) {
            resultado.append(linea);
        }
        // Cerrar el BufferedReader
        rd.close();
        // Regresar resultado, pero como cadena, no como StringBuilder
        return resultado.toString();
    }
    static int getPort() {
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 4567;
    }
}
