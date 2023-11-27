package ObjetsVESPAP;

import Modele.Facture;

import java.util.ArrayList;

public class ReponseGetFactures implements Reponse
{
    private String message;
    private ArrayList<Facture> tableauFactures;

    public ReponseGetFactures(ArrayList<Facture> tabFactures, String m)
    {
        tableauFactures = tabFactures;
        message = m;
    }


    public ArrayList<Facture> getTableauFactures() {
        return tableauFactures;
    }
}
