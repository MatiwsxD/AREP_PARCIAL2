package co.edu.eci.parcial;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

class Main {

    public static void main(String[] args) {

        String[] url = new String[]{"http://ec2-54-173-27-43.compute-1.amazonaws.com:3456","http://ec2-35-175-146-55.compute-1.amazonaws.com:3456"};
        int cont = 0;
        while (true){
            if(cont == 0){
                cont = 1;
            }
            else if(cont == 1){
                cont = 0;
            }
            System.out.println("Digite la operacion (log si es logatimica o exp si es exponencial");
            Scanner in = new Scanner(System.in);
            String funcion = in.nextLine();
            System.out.println("Digite el valor");
            Scanner on = new Scanner(System.in);
            String numero = in.nextLine();
            String m =url[cont]+"/"+funcion+"?value=" +numero;
            if(funcion == "exit" || numero == "exit"){
                break;
            }
            else{
                PeticionGet(m);
            }

        }
    }
    public static void PeticionGet(String x) {
        String url = x;
        String respuesta = "";
        try {
            respuesta = peticionHttpGet(url);
            System.out.println("La respuesta es:\n" + respuesta);
        } catch (Exception e) {
            // Manejar excepción
            e.printStackTrace();
        }
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
}
