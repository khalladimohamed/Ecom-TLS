package ObjetsVESPAP;

import Modele.Article;

import java.util.ArrayList;

public class ReponseGetArticles implements Reponse{
    private ArrayList<Article> articles;

    public ReponseGetArticles(ArrayList<Article> articles)
    {
        this.articles = articles;
    }

    public ArrayList<Article> getArticles()
    {
        return articles;
    }
}
