import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class ConversorMonedas {
    private String apiUrl;
    private HttpClient httpClient;
    private Gson gson;
    private Scanner scanner;
    private Set<String> monedasFiltradas;

    public ConversorMonedas(Set<String> monedasFiltradas) {
        apiUrl = "https://v6.exchangerate-api.com/v6/ce49790328961c240fb679af/latest/";
        httpClient = HttpClient.newHttpClient();
        gson = new Gson();
        scanner = new Scanner(System.in);
        this.monedasFiltradas = monedasFiltradas;
    }


    public void mostrarMenu() throws IOException, InterruptedException {
        while (true) {
            System.out.println("Ingrese el código de la moneda a convertir (ARS, BOB, BRL, CLP, COP, USD), o 'salir' para salir:");
            String monedaBase = scanner.nextLine().toUpperCase(); // Convertir a mayúsculas para asegurar la coincidencia

            if (monedaBase.equals("SALIR")) {
                System.out.println("Saliendo...");
                return;
            }

            obtenerTasasDeCambio(monedaBase);
        }
    }

    private void obtenerTasasDeCambio(String monedaBase) throws IOException, InterruptedException {
        String apiUrl = this.apiUrl + monedaBase;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        JsonObject jsonResponse = gson.fromJson(response.body(), JsonObject.class);

        // Verificar si jsonResponse contiene "conversion_rates"
        if (!jsonResponse.has("conversion_rates")) {
            System.out.println("No se pudo obtener las tasas de cambio para " + monedaBase);
            return;
        }

        JsonObject rates = jsonResponse.getAsJsonObject("conversion_rates");

        System.out.println("Ingrese la cantidad de " + monedaBase + " que desea convertir:");
        double cantidad = scanner.nextDouble();
        scanner.nextLine();

        System.out.println("Tasas de cambio respecto a " + monedaBase + ":");
        for (String currencyCode : monedasFiltradas) {
            if (rates.has(currencyCode)) {
                double rate = rates.get(currencyCode).getAsDouble();
                double resultado = cantidad * rate;
                System.out.println(currencyCode + ": " + rate + " (Equivalente a " + cantidad + " " + monedaBase + " = " + resultado + " " + currencyCode + ")");
            } else {
                System.out.println("No se encontró tasa de cambio para " + currencyCode);
            }
        }
    }



}
