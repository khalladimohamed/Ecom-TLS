import Serveur.Logger.LoggerConsole;
import Serveur.ThreadServeurPool;
import Serveur.Protocole.VESPAP;
import JDBC.BeanGenerique;
import JDBC.BeanMetier;
import Serveur.ThreadServeurPoolTLS;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;


public class Main {
    public static void main(String[] args) {

        try {
            BeanGenerique beanGenerique = new BeanGenerique(BeanGenerique.MYSQL, "192.168.109.130", "PourStudent", "Student", "PassStudent1_");
        }
        catch (ClassNotFoundException | SQLException e)
        {
            e.printStackTrace();
            System.exit(1);
        }

        LoggerConsole logger = new LoggerConsole();

        BeanMetier beanMetier = new BeanMetier(logger);
        VESPAP vespap = new VESPAP(logger, beanMetier);

        Properties propertiesFile = new Properties();

        try (FileInputStream fis = new FileInputStream("config.properties")) {
            propertiesFile.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        try {
            ThreadServeurPool threadServeurPool = new ThreadServeurPool(Integer.parseInt(propertiesFile.getProperty("PORT_PAIEMENT")), vespap, Integer.parseInt(propertiesFile.getProperty("NB_THREADS_POOL")), logger);
            threadServeurPool.start();
        }
        catch (Exception e)
        {
            System.out.println("Erreur lors du lancement du serveur none secure");
            e.printStackTrace();
            System.exit(1);
        }

        try {
            ThreadServeurPoolTLS threadServeurPoolTls = new ThreadServeurPoolTLS(Integer.parseInt(propertiesFile.getProperty("PORT_PAIEMENT_TLS")), vespap, Integer.parseInt(propertiesFile.getProperty("NB_THREADS_POOL")), logger);
            threadServeurPoolTls.start();
        }
        catch (Exception e)
        {
            System.out.println("Erreur lors du lancement du serveur TLS secure");
            e.printStackTrace();
            System.exit(1);
        }

    }
}