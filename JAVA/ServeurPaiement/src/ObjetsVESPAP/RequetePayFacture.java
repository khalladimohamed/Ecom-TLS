package ObjetsVESPAP;

public class RequetePayFacture implements Requete
{
    private int idFacture;
    private String nom;
    private String numeroCarte;

    public RequetePayFacture(int id, String n, String numCarte)
    {
        idFacture = id;
        nom = n;
        numeroCarte = numCarte;
    }

    public int getIdFacture() {
        return idFacture;
    }

    public String getNom() {
        return nom;
    }

    public String getNumeroCarte() {
        return numeroCarte;
    }
}
