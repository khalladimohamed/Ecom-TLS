package ObjetsVESPAP;

public class RequeteLogout implements Requete
{
    private String login;

    public RequeteLogout(String l) {
        login = l;
    }
    public String getLogin() {
        return login;
    }
}

