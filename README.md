# Autor: Juan Mateo Mejia Zuluaga
# Codigo: 2154840
## _AREP- ARQUITECTURA EMPRESARIAL_

# Parcial segundo tercio #
En este parcial vamos a crear 3 intancias EC2 en amazon, en dos de ellas vamos a estar calculando los logaritmos o exponenciles, dependiendo de la peticion, en el tercer tenemos un algoritmo de roundrobin que va enviando peticiones a estos servidores
## Pre-requisitos

- Maven
- JDK 8 o superior
- Un IDE

# Cálculos necesarios

- Calcular el logaritmo base 10 de un numero:
```     
        public double log(double number){
        return Math.log10(number);
    }
```

- Calcular euler a una potencia:
```     
        public double exp(double number){
        return Math.exp(number);
    }
```
- La clase sparkweb:

```
public class App {
    private static Calculadora calcular = new Calculadora();

    public static void main(String[] args) {
        port(getPort());
        get("/exp", (req, res) -> inputDataPage(req, res,"exp"));
        get("/log", (req, res) -> inputDataPage(req, res,"log"));
    }

    private static JSONObject inputDataPage(Request req, Response res,String operation) {
        double number = Double.parseDouble(req.queryParams("value"));
        JSONObject obj = new JSONObject();
        obj.put("operation",operation);
        obj.put("input",number);
        obj.put("output",(operation.equals("log"))? calcular.log(number) : calcular.exp(number));
        return obj;
    }

    static int getPort() {
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 3456;
    }
}
```
-Clase de proxy:
```
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

```



# Implementacion

Tenemos corriendo 3 servidores sparkweb, uno recibe la peticion, calcula a que servidor se la va a mandar y le envia una peticion get, este responde con un JSON y el proxi responde con un string que contiene el JSON
# Video

https://pruebacorreoescuelaingeduco.sharepoint.com/:v:/s/videospti/EaEocI0ppTtLjEIYq7RwxOABw4lOrVdPRSurtxm_6PgFjQ?e=gZyFky

