package Serveur.Protocole;
import Serveur.Exception.FinConnexionException;
import ObjetsVESPAP.*;

public interface Protocole
{
    String getNom();
    Reponse TraiteRequete(Requete requete) throws FinConnexionException;
}
