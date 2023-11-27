package ObjetsVESPAP;

public class RequeteLogin implements Requete
{
    private String login;
    private String password;
    private boolean newEmploye;
    public RequeteLogin(String l,String p,boolean n) {
        login = l;
        password = p;
        newEmploye = n;
    }

    public String getLogin() {
        return login;
    }
    public String getPassword() {
        return password;
    }
    public boolean isNewEmploye(){
        return newEmploye;
    }
}
