package JDBC;

import Serveur.Logger.Logger;
import Modele.Article;
import Modele.Facture;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;


public class BeanMetier {

    private Logger logger;

    public BeanMetier(Logger logger)
    {
        this.logger = logger;
    }



    public synchronized boolean login(String username, String password)
    {
        String query = "SELECT * FROM employes WHERE login = '" + username + "'";

        try {

            ResultSet resultSet = BeanGenerique.executeQuery(query);

            if (resultSet.next()) {
                String passwordDB = resultSet.getString("password");

                if (passwordDB.equals(password)) {
                    return true;
                }
            }

            return false;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            System.exit(1);
            return false;
        }
    }



    public synchronized boolean verifyClientName(String username)
    {
        String query = "SELECT * FROM clients WHERE login = '" + username + "'";

        try {
            ResultSet resultSet = BeanGenerique.executeQuery(query);

            if(!resultSet.next())
            {
                return false;
            }

            return true;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            System.exit(1);
            return false;
        }

    }



    public synchronized boolean addEmploye(String username, String password)
    {
        String query = "INSERT INTO employes (login, password) VALUES ('" + username + "', '" + password + "')";

        try
        {
            BeanGenerique.executeUpdate(query);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return false;
        }
        return true;
    }



    public synchronized ArrayList<Facture> getFactures(int idClient) throws SQLException
    {
        ArrayList<Facture> factures = new ArrayList<>();

        String query = "SELECT * FROM factures WHERE idclient = " + idClient;

        ResultSet resultSet = BeanGenerique.executeQuery(query);

        while(resultSet.next())
        {
            logger.Trace("Facture trouvée");
            int idFacture = resultSet.getInt("id");
            int idClientDB = resultSet.getInt("idclient");
            Date date = resultSet.getDate("date");
            boolean etat = resultSet.getBoolean("paye");
            float montant = resultSet.getFloat("montant");

            Facture facture = new Facture(idFacture, date, montant, etat);

            factures.add(facture);
        }

        return factures;
    }



    public synchronized String payFacture(int idFacture) throws SQLException
    {
        String query = "UPDATE factures SET paye = 1 WHERE id = " + idFacture;

        BeanGenerique.executeUpdate(query);

        return "OK";
    }



    public synchronized ArrayList<Article> getArticles(int idFacture)
    {
        String query = "SELECT * FROM ventes WHERE idFacture = " + idFacture;

        ArrayList<Article> articles = new ArrayList<>();

        try {
            ResultSet resultSet = BeanGenerique.executeQuery(query);

            while(resultSet.next())
            {
                int idArticle = resultSet.getInt("idArticle");
                String query2 = "SELECT * FROM articles WHERE id = " + idArticle;
                int quantite = resultSet.getInt("quantite");

                ResultSet resultSet2 = BeanGenerique.executeQuery(query2);

                resultSet2.next();
                Article article = new Article(resultSet2.getString("intitule"), quantite, resultSet2.getFloat("prix"));

                articles.add(article);

            }

            return articles;

        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }


}
