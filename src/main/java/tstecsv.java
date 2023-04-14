import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class testecsv {
    public static void main(String[] args) {
        String jsonFilePath = "caminho/para/arquivo.json";
        String csvFilePath = "caminho/para/arquivo.csv";

        try {
            // Leitura do arquivo JSON
            BufferedReader reader = new BufferedReader(new FileReader(jsonFilePath));
            JsonElement jsonElement = JsonParser.parseReader(reader);
            reader.close();

            // Conversão para CSV
            List<String[]> csvData = new ArrayList<>();
            if (jsonElement.isJsonArray()) {
                JsonArray jsonArray = jsonElement.getAsJsonArray();
                for (JsonElement element : jsonArray) {
                    if (element.isJsonObject()) {
                        JsonObject jsonObject = element.getAsJsonObject();
                        String[] row = new String[jsonObject.size()];
                        int i = 0;
                        for (String key : jsonObject.keySet()) {
                            row[i++] = jsonObject.get(key).getAsString();
                        }
                        csvData.add(row);
                    }
                }
            }

            // Escrita do arquivo CSV
            FileWriter writer = new FileWriter(csvFilePath);
            for (String[] row : csvData) {
                writer.append(String.join(",", row));
                writer.append("\n");
            }
            writer.close();

            System.out.println("Arquivo CSV gerado com sucesso!");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}