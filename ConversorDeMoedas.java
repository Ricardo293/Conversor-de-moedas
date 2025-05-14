import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.json.JSONObject;
import java.util.Scanner;

public class ConversorDeMoedas {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Digite a moeda de origem (ex: USD): ");
        String moedaOrigem = scanner.nextLine().toUpperCase();

        System.out.print("Digite a moeda de destino (ex: BRL): ");
        String moedaDestino = scanner.nextLine().toUpperCase();

        System.out.print("Digite o valor a converter: ");
        double valor = scanner.nextDouble();

        double resultado = converterMoeda(moedaOrigem, moedaDestino, valor);

        if (resultado >= 0) {
            System.out.printf("%.2f %s = %.2f %s\n", valor, moedaOrigem, resultado, moedaDestino);
        } else {
            System.out.println("Erro ao converter moeda.");
        }

        scanner.close();
    }

    public static double converterMoeda(String de, String para, double valor) {
        String url = String.format("https://api.exchangerate.host/convert?from=%s&to=%s&amount=%f", de, para, valor);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JSONObject json = new JSONObject(response.body());

            if (json.getBoolean("success")) {
                return json.getDouble("result");
            } else {
                System.out.println("Erro na resposta da API.");
                return -1;
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return -1;
        }
    }
}