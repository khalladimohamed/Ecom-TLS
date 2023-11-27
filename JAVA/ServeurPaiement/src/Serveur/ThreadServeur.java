package Serveur;

import Serveur.Logger.Logger;
import Serveur.Protocole.Protocole;

import java.io.IOException;
import java.net.ServerSocket;
public abstract class ThreadServeur extends Thread
{
    protected int port;
    protected Protocole protocole;
    protected Logger logger;
    protected ServerSocket ssocket;

    public ThreadServeur(int port, Protocole protocole, Logger logger) throws
            IOException
    {
        super("TH Serveur (port=" + port + ",protocole=" + protocole.getNom() + ")");
        this.port = port;
        this.protocole = protocole;
        this.logger = logger;

        ssocket = new ServerSocket(port);
    }
}
