
package model;

/**
 * Class Lien
 * @author Epulapp
 */
public class Lien
{
    private Article composant;
    private Article compose;
    private int coefficientLien;

    public Lien(Article composant, Article compose, int coefficientLien)
    {
        this.composant = composant;
        this.compose = compose;
        this.coefficientLien = coefficientLien;
    }

    public Article rechercheArticle(Article a)
    {
        return new Article();
    }

    public Article getComposant()
    {
        return composant;
    }

    public void setComposant(Article composant)
    {
        this.composant = composant;
    }

    public Article getCompose() {
        return compose;
    }

    public void setCompose(Article compose)
    {
        this.compose = compose;
    }

    public int getCoefficientLien()
    {
        return coefficientLien;
    }

    public void setCoefficientLien(int coefficientLien)
    {
        this.coefficientLien = coefficientLien;
    }
}
