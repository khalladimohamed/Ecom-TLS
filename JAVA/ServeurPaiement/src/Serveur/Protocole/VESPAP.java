package Serveur.Protocole;

import Serveur.Exception.FinConnexionException;
import Serveur.Logger.Logger;
import JDBC.BeanMetier;
import Modele.Facture;
import ObjetsVESPAP.*;

import java.sql.SQLException;
import java.util.ArrayList;


public class VESPAP implements Protocole {

    private Logger logger;
    private BeanMetier beanMetier;

    public VESPAP(Logger log, BeanMetier beanMetier)
    {
        logger = log;
        this.beanMetier = beanMetier;
    }


    @Override
    public String getNom() {
        return "VESPAP";
    }


    @Override
    public synchronized Reponse TraiteRequete(Requete requete) throws FinConnexionException {

        logger.Trace("Traitement requete");

        if(requete instanceof RequeteLogin)
            return TraiteRequeteLogin((RequeteLogin)requete);
        else if(requete instanceof RequeteGetFactures)
            return TraiteRequeteGetFactures((RequeteGetFactures)requete);
       else if(requete instanceof RequetePayFacture)
            return TraiteRequetePayFacture((RequetePayFacture)requete);
       else if(requete instanceof RequeteLogout)
            TraiteRequeteLogout((RequeteLogout)requete);
       else if(requete instanceof RequeteGetArticles)
            return TraiteRequeteGetArticle((RequeteGetArticles)requete);
        return null;
    }



    private synchronized Reponse TraiteRequeteLogin(RequeteLogin requeteLogin) throws FinConnexionException
    {
        logger.Trace("Requete LOGIN reçue de " + requeteLogin.getLogin());

        ReponseLogin reponseLogin = new ReponseLogin();

        boolean isNewClient = requeteLogin.isNewEmploye();
        String username = requeteLogin.getLogin();
        String password = requeteLogin.getPassword();

        if(!isNewClient)
        {
            if(beanMetier.login(username, password))
            {
                reponseLogin.setMessage("Login OK");
                reponseLogin.setValide(true);
                return reponseLogin;
            }
            else
            {
                reponseLogin.setValide(false);
                reponseLogin.setMessage("Mauvais login / mot de passe");
                throw new FinConnexionException(reponseLogin);
            }
        }
        else
        {
            beanMetier.addEmploye(username, password);
            reponseLogin.setMessage("Employe créé");
            reponseLogin.setValide(true);
            return reponseLogin;
        }

    }



    private synchronized Reponse TraiteRequeteGetFactures(RequeteGetFactures requete)
    {
        logger.Trace("Requete GETFACTURES reçue de " + requete.getIdClient());
        try {
            ArrayList<Facture> factures = beanMetier.getFactures(requete.getIdClient());
            ReponseGetFactures reponse = new ReponseGetFactures(factures, "OK");
            return reponse;
        }
        catch (Exception e)
        {
            return new ReponseGetFactures(new ArrayList<>(), "Probleme BD");
        }
    }



    private synchronized Reponse TraiteRequetePayFacture(RequetePayFacture requete)
    {
        logger.Trace("Requete PAYFACTURE reçue de " + requete.getNom());

        if(!beanMetier.verifyClientName(requete.getNom()))
            return new ReponsePayFacture(false, "Le client n'existe pas !");

        if(!LuhnAlgorithm.isValidLuhnNumber(requete.getNumeroCarte()))
            return new ReponsePayFacture(false, "Carte bancaire invalide");

        String message = null;
        try {
            message = beanMetier.payFacture(requete.getIdFacture());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if(message.equals("OK"))
            return new ReponsePayFacture(true, "Paiement reussi");
        else
            return new ReponsePayFacture(false, "Erreur paiement");
    }



    private synchronized void TraiteRequeteLogout(RequeteLogout requete) throws FinConnexionException
    {
        logger.Trace("Requete LOGOUT reçue de " + requete.getLogin());
        throw new FinConnexionException(null);
    }



    private synchronized Reponse TraiteRequeteGetArticle(RequeteGetArticles requete)
    {
        logger.Trace("Requete GETARTICLES reçue de " + requete.getIdFacture());

        ReponseGetArticles reponse = new ReponseGetArticles(beanMetier.getArticles(requete.getIdFacture()));

        return reponse;
    }
}
