
package model;

/**
 * Class BesoinNet
 * @author Epulapp
 */
public class BesoinNet
{
    private int semaine;
    
    private int besoinBrut;
    private int besoinNet;
    private int livraison;
    private int suggestion;
    private int lancement;
    private int stock;

    public BesoinNet(int semaine)
    {
        this.semaine = semaine;
    }
    
    public int getSemaine()
    {
        return semaine;
    }

    public void setSemaine(int semaine)
    {
        this.semaine = semaine;
    }

    public int getBesoinBrut()
    {
        return besoinBrut;
    }

    public void setBesoinBrut(int besoinBrut)
    {
        this.besoinBrut = besoinBrut;
    }

    public int getBesoinNet() {
        return besoinNet;
    }

    public void setBesoinNet(int besoinNet)
    {
        this.besoinNet = besoinNet;
    }

    public int getLivraison()
    {
        return livraison;
    }

    public void setLivraison(int livraison)
    {
        this.livraison = livraison;
    }

    public int getSuggestion()
    {
        return suggestion;
    }

    public void setSuggestion(int suggestion)
    {
        this.suggestion = suggestion;
    }

    public int getLancement()
    {
        return lancement;
    }

    public void setLancement(int lancement)
    {
        this.lancement = lancement;
    }

    public int getStock()
    {
        return stock;
    }

    public void setStock(int stock)
    {
        this.stock = stock;
    }
}
