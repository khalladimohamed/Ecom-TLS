package ObjetsVESPAP;

public class ReponseLogin implements Reponse
{
    private boolean valide;
    private String message;

    public void setValide(boolean valide) {
        this.valide = valide;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ReponseLogin()
    {
        valide = false;
        message = "";
    }

    public ReponseLogin(boolean v, String m) {
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

