package Modele;

import java.io.Serializable;

public class Article implements Serializable {
    private String intitule;
    private int quantite;
    private float prixUnitaire;

    public Article(String intitule, int quantite, float prixUnitaire)
    {
        this.intitule = intitule;
        this.quantite = quantite;
        this.prixUnitaire = prixUnitaire;
    }

    public String getIntitule()
    {
        return intitule;
    }

    public int getQuantite()
    {
        return quantite;
    }

    public float getPrixUnitaire()
    {
        return prixUnitaire;
    }
}
