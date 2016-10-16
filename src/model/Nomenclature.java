
package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Class Nomenclature
 * @author Epulapp
 */
public class Nomenclature
{
    private ArrayList<Lien> listeLiens = new ArrayList();
    private ArrayList<Article> listeArticles = new ArrayList();
    private int nbSemaines;

    public Nomenclature(int nbSemaines)
    {
        this.nbSemaines = nbSemaines;
    }
    
    public void calculNiveauNomenclature()
    {
        
    }
    
    public void parcoursNomenclature()
    {
        
    }
    
    public void ajouterLien()
    {
        
    }
    
    public void ajouterArticle()
    {
        
    }
    
    public List<Article> rechercheParents() // rechercheComposés
    {
        return new ArrayList();
    }
    
    public void afficherBesoinsNets()
    {
        
    }

    public ArrayList<Lien> getListeLiens()
    {
        return listeLiens;
    }

    public void setListeLiens(ArrayList<Lien> listeLiens)
    {
        this.listeLiens = listeLiens;
    }

    public ArrayList<Article> getListeArticles()
    {
        return listeArticles;
    }

    public void setListeArticles(ArrayList<Article> listeArticles)
    {
        this.listeArticles = listeArticles;
    }

    public int getNbSemaines()
    {
        return nbSemaines;
    }

    public void setNbSemaines(int nbSemaines)
    {
        this.nbSemaines = nbSemaines;
    }
}
