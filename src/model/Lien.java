package model;

/**
 * Class Lien
 * @author Mélanie DUBREUIL 
 * @author Ophélie EOUZAN
 */

public class Lien {
    Article composant, composé;
    double coefficientLien;
    
    /*
    *
    * Getters et Setters
    *
    */
    
    public Article getComposant() {
        return composant;
    }

    public Article getComposé() {
        return composé;
    }

    public double getCoefficientLien() {
        return coefficientLien;
    }
    
    public void setComposant(Article composant) {
        this.composant = composant;
    }

    public void setComposé(Article composé) {
        this.composé = composé;
    }

    public void setCoefficientLien(double coefficientLien) {
        this.coefficientLien = coefficientLien;
    }
}
