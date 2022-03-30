package co.edu.eci.parcial;

import org.json.JSONObject;
import spark.Request;
import spark.Response;
import static spark.Spark.*;

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