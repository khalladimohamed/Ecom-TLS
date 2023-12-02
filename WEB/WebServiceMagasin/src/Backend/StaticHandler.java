package Backend;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

public class StaticHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type");
        exchange.getResponseHeaders().set("Access-Control-Allow-Credentials", "true");


        // Récupérer le chemin de l'URL
        String path = exchange.getRequestURI().getPath().substring(1); // Enlever le premier caractère ("/")

        // Lire le fichier depuis le répertoire "src/main/webapp"
        File file = new File("src/Frontend", path);

        if (file.exists()) {
            // Envoyer le fichier en tant que réponse
            exchange.sendResponseHeaders(200, file.length());
            String contentType = Files.probeContentType(file.toPath());
            exchange.getResponseHeaders().set("Content-Type", contentType);
            OutputStream os = exchange.getResponseBody();
            Files.copy(file.toPath(), os);
            os.close();
        } else {
            Erreur404(exchange);
        }
    }

    private void Erreur404(HttpExchange exchange) throws IOException {
        String reponse = "Fichier introuvable !!!";
        exchange.sendResponseHeaders(404, reponse.length());
        exchange.getResponseHeaders().set("Content-Type", "text/plain");
        OutputStream os = exchange.getResponseBody();
        os.write(reponse.getBytes());
        os.close();
    }
}

