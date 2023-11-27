package Serveur;

import Serveur.Logger.Logger;
import Serveur.Protocole.Protocole;

import javax.net.ssl.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;

public abstract class ThreadServeurTLS extends Thread
{
    protected int port;
    protected Protocole protocole;
    protected Logger logger;

    protected SSLServerSocket sslServerSocket;

    public ThreadServeurTLS(int port, Protocole protocole, Logger logger) throws
            IOException, KeyStoreException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException, KeyManagementException {
        super("TH Serveur (port=" + port + ",protocole=" + protocole.getNom() + ")");
        this.port = port;
        this.protocole = protocole;
        this.logger = logger;


        KeyStore serverKeyStore = KeyStore.getInstance("JKS");
        char[] serverKeyPass = "ecom2023".toCharArray();
        FileInputStream serverKeyInput = new FileInputStream("KeyStoreServeur.jks");
        serverKeyStore.load(serverKeyInput, serverKeyPass);

        char[] keyPass = "cleServeur".toCharArray();
        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(serverKeyStore, keyPass);

        TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
        tmf.init(serverKeyStore);

        SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
        sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

        SSLServerSocketFactory sslServerSocketFactory = sslContext.getServerSocketFactory();

        sslServerSocket = (SSLServerSocket) sslServerSocketFactory.createServerSocket(53000);

    }
}
