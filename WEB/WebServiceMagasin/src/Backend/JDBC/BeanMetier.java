package Backend.JDBC;

import Backend.Logger.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;


public class BeanMetier {

    private Logger logger;

    public BeanMetier(Logger logger)
    {
        this.logger = logger;
    }


    public synchronized boolean updateArticle(int idArticle, float nouveauPrix, int nouveauStock) {
        String query = "UPDATE articles SET prix = " + nouveauPrix + ", stock = " + nouveauStock + " WHERE id = " + idArticle;

        try {
            int rowsAffected = BeanGenerique.executeUpdate(query);
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public synchronized ResultSet getArticleDetails(int articleId) throws SQLException {
        String query = "SELECT * FROM articles WHERE id = " + articleId;
        return BeanGenerique.executeQuery(query);
    }


    public synchronized ResultSet getAllArticles() throws SQLException {
        String query = "SELECT * FROM articles";
        return BeanGenerique.executeQuery(query);
    }

}
