import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class API {

    private static final String[] FRASES = {
            "Acredite em você e tudo será possível",
            "Cada dia é uma nova chance de recomeçar",
            "Você é mais forte do que imagina",
            "Pequenos passos levam a grandes conquistas",
            "A persistência vence a resistência"
    };

    public static void main(String[] args) throws IOException {

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        // Rota raiz
        server.createContext("/", new RootHandler());

        // Rota /frase
        server.createContext("/frase", new FraseHandler());

        server.setExecutor(null);
        server.start();

        System.out.println("Servidor rodando em http://localhost:8080");
    }

    // Handler da rota "/"
    static class RootHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String response = """
                    <html>
                        <head><title>FluX API</title></head>
                        <body>
                            <h1>Bem-vindo à API de Frases Motivacionais</h1>
                            <p>Acesse <b>/frase</b> para receber uma frase aleatória.</p>
                        </body>
                    </html>
                    """;

            sendHtml(exchange, response);
        }
    }

    // Handler da rota "/frase"
    static class FraseHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            Random random = new Random();
            String frase = FRASES[random.nextInt(FRASES.length)];

            String response = """
                    <html>
                        <head><title>Frase</title></head>
                        <body>
                            <h1>%s</h1>
                        </body>
                    </html>
                    """.formatted(frase);

            sendHtml(exchange, response);
        }
    }

    // Método utilitário para enviar HTML
    private static void sendHtml(HttpExchange exchange, String response) throws IOException {
        byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
        exchange.sendResponseHeaders(200, bytes.length);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }
}
