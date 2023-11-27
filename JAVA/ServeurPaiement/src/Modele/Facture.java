package Modele;

import java.io.Serializable;
import java.util.Date;

public class Facture implements Serializable {
    private int idFacture;
    private Date date;
    private float montant;
    private boolean paye;

    public Facture(int id, Date d, float m, boolean p)
    {
        idFacture = id;
        date = d;
        montant = m;
        paye = p;
    }

    public int getIdFacture() {
        return idFacture;
    }
    public Date getDate() {
        return date;
    }
    public float getMontant() {
        return montant;
    }
    public boolean getPaye() {
        return paye;
    }
}
