package ObjetsVESPAP;

public class ReponsePayFacture implements Reponse
{
    private boolean valide;
    private String message;

    public ReponsePayFacture(boolean v, String m)
    {
        valide = v;
        message = m;
    }

    public boolean isValide() {
        return valide;
    }

    public String getMessage() {
        return message;
    }
}
