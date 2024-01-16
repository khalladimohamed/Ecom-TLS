package Backend;

import Backend.JDBC.BeanGenerique;
import Backend.JDBC.BeanMetier;
import Backend.Logger.LoggerConsole;
import Backend.StaticHandlers.HandlerCss;
import Backend.StaticHandlers.HandlerHtml;
import Backend.StaticHandlers.HandlerImages;
import Backend.StaticHandlers.HandlerJavascript;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.sun.net.httpserver.*;
import javax.net.ssl.*;
import java.io.*;
import java.security.*;
import java.security.cert.CertificateException;


public class StockManagementService {

    public static void main(String[] args) throws IOException, CertificateException, NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException, KeyManagementException {

        ///////////////////////////// NON SECURISE ////////////////////////////////////////////////
        HttpServer serveur = null;
        try {
            serveur = HttpServer.create(new InetSocketAddress(8080), 0);

            serveur.createContext("/", new HandlerHtml());
            serveur.createContext("/css", new HandlerCss());
            serveur.createContext("/js", new HandlerJavascript());
            serveur.createContext("/images", new HandlerImages());

            serveur.createContext("/api", new StockHandler());

            System.out.println("Démarrage du serveur HTTP...");
            serveur.start();
        } catch (IOException e) {
            System.out.println("Erreur: " + e.getMessage());
        }


        ///////////////////////////////// SECURISE ////////////////////////////////////////////////
        HttpsServer serveurSecurise = HttpsServer.create(new InetSocketAddress(8443), 0);
        SSLContext sslContext = SSLContext.getInstance("TLS");

        // Chargement du keystore
        char[] password = "ecom2023".toCharArray();
        KeyStore keyStore = KeyStore.getInstance("JKS");
        try (InputStream keyStoreStream = new FileInputStream("keystoreServeurHttps.jks")) {
            keyStore.load(keyStoreStream, password);
        }

        // Initialisation du gestionnaire de clés
        char[] keyPassword = "cleServeur".toCharArray();
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
        keyManagerFactory.init(keyStore, keyPassword);

        // Initialisation du gestionnaire de confiance
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("SunX509");
        trustManagerFactory.init(keyStore);

        sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);

        serveurSecurise.setHttpsConfigurator(new HttpsConfigurator(sslContext) {
            public void configure(HttpsParameters params) {
                try {
                    SSLContext c = SSLContext.getDefault();
                    SSLEngine engine = c.createSSLEngine();
                    params.setNeedClientAuth(false);
                    params.setCipherSuites(engine.getEnabledCipherSuites());
                    params.setProtocols(engine.getEnabledProtocols());

                    // Obtient le contexte SSL de SSLContext et définie-le
                    SSLParameters sslParameters = c.getDefaultSSLParameters();
                    params.setSSLParameters(sslParameters);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });


        // Ajout des gestionnaires statiques
        serveurSecurise.createContext("/", new HandlerHtml());
        serveurSecurise.createContext("/css", new HandlerCss());
        serveurSecurise.createContext("/js", new HandlerJavascript());
        serveurSecurise.createContext("/images", new HandlerImages());

        serveurSecurise.createContext("/api", new StockHandler());

        System.out.println("Démarrage du serveur HTTPS...");
        serveurSecurise.start();
    }


    static class StockHandler implements HttpHandler {

        BeanGenerique beanGenerique;

        {
            try {
                beanGenerique = new BeanGenerique(BeanGenerique.MYSQL, "192.168.109.130", "PourStudent", "Student", "PassStudent1_");
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        LoggerConsole logger = new LoggerConsole();
        BeanMetier beanMetier = new BeanMetier(logger);


        @Override
        public void handle(HttpExchange exchange) throws IOException {

            String method = exchange.getRequestMethod();

            if (method.equals("GET")) {
                System.out.println("--- Requête GET reçue (obtenir les articles) ---");
                handleGetRequest(exchange);
            } else if (method.equals("POST")) {
                System.out.println("--- Requête POST reçue (mise a jour du stock) ---");
                handlePostRequest(exchange);
            }
        }


        private void handleGetRequest(HttpExchange exchange) throws IOException {
            try {
                ResultSet resultSet = beanMetier.getAllArticles();
                String jsonResponse = convertResultSetToJson(resultSet);

                byte[] responseBytes = jsonResponse.getBytes("UTF-8");

                exchange.sendResponseHeaders(200, responseBytes.length);
                OutputStream os = exchange.getResponseBody();
                os.write(jsonResponse.getBytes());
                os.close();
            } catch (SQLException e) {
                e.printStackTrace();
                exchange.sendResponseHeaders(500, -1);
            }
        }


        private void handlePostRequest(HttpExchange exchange) throws IOException {
            try {
                InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), "utf-8");
                BufferedReader br = new BufferedReader(isr);
                String postData = br.readLine();

                boolean updateSuccessful = updateArticle(postData);

                String response;
                if (updateSuccessful) {
                    response = "Oui";
                } else {
                    response = "Non";
                }

                exchange.sendResponseHeaders(200, response.length());
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            } catch (SQLException e) {
                e.printStackTrace();
                exchange.sendResponseHeaders(500, -1);
            }
        }


        private boolean updateArticle(String postData) throws SQLException {
            String[] params = postData.split("&");

            int idArticle = 0;
            float prix = 0;
            int stock = 0;

            for (String param : params) {
                String[] keyValue = param.split("=");
                if (keyValue.length == 2) {
                    String key = keyValue[0];
                    String value = keyValue[1];

                    switch (key) {
                        case "idArticle":
                            idArticle = Integer.parseInt(value);
                            break;
                        case "prix":
                            prix = Float.parseFloat(value);
                            break;
                        case "stock":
                            stock = Integer.parseInt(value);
                            break;
                    }
                }
            }

            return beanMetier.updateArticle(idArticle, prix, stock);
        }


        private String convertResultSetToJson(ResultSet resultSet) throws SQLException {
            StringBuilder json = new StringBuilder("[");

            while (resultSet.next()) {
                json.append("{");
                json.append("\"id\": ").append(resultSet.getInt("id")).append(", ");
                json.append("\"intitule\": \"").append(resultSet.getString("intitule")).append("\", ");
                json.append("\"prix\": ").append(resultSet.getFloat("prix")).append(", ");
                json.append("\"stock\": ").append(resultSet.getInt("stock")).append(", ");
                json.append("\"image\": \"").append(resultSet.getString("image")).append("\"");
                json.append("}");

                if (!resultSet.isLast()) {
                    json.append(",");
                }
            }

            json.append("]");

            return json.toString();
        }

    }
}

