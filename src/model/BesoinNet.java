package model;

/**
 * Class BesoinNet
 * @author Mélanie DUBREUIL
 * @author Ophélie EOUZAN
 */

public class BesoinNet {
    double besoinBrut,livraison, lancement, besoinNet, suggestion, stock, semaine;

    public BesoinNet(double besoinBrut, double livraison, double lancement, double besoinNet, double suggestion, double stock, int semaine) {
        this.besoinBrut = besoinBrut;
        this.livraison = livraison;
        this.lancement = lancement;
        this.besoinNet = besoinNet;
        this.suggestion = suggestion;
        this.stock = stock;
        this.semaine = semaine;
    }

    public void setBesoinBrut(double besoinBrut) {
        this.besoinBrut = besoinBrut;
    }

    public void setLivraison(double livraison) {
        this.livraison = livraison;
    }

    public void setLancement(double lancement) {
        this.lancement = lancement;
    }

    public void setBesoinNet(double besoinNet) {
        this.besoinNet = besoinNet;
    }

    public void setSuggestion(double suggestion) {
        this.suggestion = suggestion;
    }

    public void setStock(double stock) {
        this.stock = stock;
    }

    public void setSemaine(int semaine) {
        this.semaine = semaine;
    }

    public double getBesoinBrut() {
        return besoinBrut;
    }

    public double getLivraison() {
        return livraison;
    }

    public double getLancement() {
        return lancement;
    }

    public double getBesoinNet() {
        return besoinNet;
    }

    public double getSuggestion() {
        return suggestion;
    }

    public double getStock() {
        return stock;
    }

    public double getSemaine() {
        return semaine;
    }
    
    
}
