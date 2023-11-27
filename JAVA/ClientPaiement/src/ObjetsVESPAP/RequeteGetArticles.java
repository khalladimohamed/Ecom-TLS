package ObjetsVESPAP;

public class RequeteGetArticles implements Requete {

    private int idFacture;

    public RequeteGetArticles(int idFacture)
    {
        this.idFacture = idFacture;
    }

    public int getIdFacture()
    {
        return idFacture;
    }
}
