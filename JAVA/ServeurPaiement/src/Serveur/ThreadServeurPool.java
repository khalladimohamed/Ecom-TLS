package Serveur;

import Serveur.Logger.Logger;
import Serveur.FileAttente;
import Serveur.Protocole.Protocole;
import Serveur.ThreadClientPool;
import Serveur.ThreadServeur;

import java.io.IOException;
import java.net.*;


public class ThreadServeurPool extends ThreadServeur
{
    private FileAttente connexionsEnAttente;
    private ThreadGroup pool;
    private int taillePool;


    public ThreadServeurPool(int port, Protocole protocole, int taillePool, Logger logger) throws IOException
    {
        super(port, protocole, logger);

        connexionsEnAttente = new FileAttente();
        pool = new ThreadGroup("POOL");
        this.taillePool = taillePool;
    }

    @Override
    public void run()
    {
        logger.Trace("Démarrage du TH Serveur (Pool)...");
        // Création du pool de threads
        try
        {
            for (int i=0 ; i<taillePool ; i++)
                new ThreadClientPool(protocole,connexionsEnAttente,pool,logger).start();
        }
        catch (IOException ex)
        {
            logger.Trace("Erreur I/O lors de la création du pool de threads");
            return;
        }

        // Attente des connexions
        while(!this.isInterrupted())
        {
            Socket csocket;
            try
            {
                ssocket.setSoTimeout(2000);
                csocket = ssocket.accept();
                logger.Trace("Connexion acceptée, mise en file d'attente.");
                connexionsEnAttente.addConnexion(csocket);
            }
            catch (SocketTimeoutException ex)
            {
                // Pour vérifier si le thread a été interrompu
            }
            catch (IOException ex)
            {
                logger.Trace("Erreur I/O");
            }
        }
        logger.Trace("TH Serveur (Pool) interrompu.");
        pool.interrupt();
    }
}

