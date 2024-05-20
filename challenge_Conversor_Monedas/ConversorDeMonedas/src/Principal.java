import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class Principal {
    public static void main(String[] args) {
        Set<String> monedasFiltradas = new HashSet<>();
        // Agrego las monedas que quiero aplicar filtro
        monedasFiltradas.add("ARS");
        monedasFiltradas.add("BOB");
        monedasFiltradas.add("BRL");
        monedasFiltradas.add("CLP");
        monedasFiltradas.add("COP");
        monedasFiltradas.add("USD");

        try {
            ConversorMonedas conversorMonedas = new ConversorMonedas(monedasFiltradas);
            conversorMonedas.mostrarMenu();


        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jMonedas = gson.toJson(monedasFiltradas);
        try (FileWriter fileWriter = new FileWriter("monedas.json")){
            fileWriter.write(jMonedas);
            System.out.println("Archivo Json generado exitosamente!");
        }catch (IOException e){
            System.err.println("Error al escribir el archivo Json." + e.getMessage());
        }

    }

}



