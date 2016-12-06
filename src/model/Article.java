package model;

import static java.lang.Double.max;
import java.util.HashMap;
import java.util.Map;

/**
 * Class Article
 * @author Mélanie DUBREUIL
 * @author Ophélie EOUZAN
 */

public class Article {
    String codeArticle, typeGestion;
    double quantitéLancement, niveauNomenclature, stockSécurité, stockInitial, délai;
    HashMap <Integer, BesoinNet> listeBesoinsNets;

    public Article() {
        listeBesoinsNets = new HashMap<>();
    }
    
    public String getTypeGestion() {
        return typeGestion;
    }

    public double getQuantitéLancement() {
        return quantitéLancement;
    }

    public double getNiveauNomenclature() {
        return niveauNomenclature;
    }

    public double getStockSécurité() {
        return stockSécurité;
    }

    public double getStockInitial() {
        return stockInitial;
    }

    public double getDélai() {
        return délai;
    }

    public HashMap<Integer, BesoinNet> getListeBesoinsNets() {
        return listeBesoinsNets;
    }
    
    public void setCodeArticle(String codeArticle) {
        this.codeArticle = codeArticle;
    }

    public void setTypeGestion(String typeGestion) {
        this.typeGestion = typeGestion;
    }

    public void setQuantitéLancement(double quantitéLancement) {
        this.quantitéLancement = quantitéLancement;
    }

    public void setNiveauNomenclature(double niveauNomenclature) {
        this.niveauNomenclature = niveauNomenclature;
    }

    public void setStockSécurité(double stockSécurité) {
        this.stockSécurité = stockSécurité;
    }

    public void setStockInitial(double stockInitial) {
        this.stockInitial = stockInitial;
    }

    public void setListeBesoinsNets(HashMap<Integer, BesoinNet> listeBesoinsNets) {
        this.listeBesoinsNets = listeBesoinsNets;
    }

    public void setDélai(double délai) {
        this.délai = délai;
    }

    public String getCodeArticle() {
        return codeArticle;
    }
    
    public void calculBesoinBrut(int semaine, double besoin){
        BesoinNet bn;
        if(this.getListeBesoinsNets().containsKey(semaine)) {
            bn = this.listeBesoinsNets.get(semaine);
            bn.setBesoinBrut(bn.getBesoinBrut() + besoin);
        } else {
            bn = new BesoinNet(besoin, 0, 0, 0, 0, 0, semaine);
        }
        this.listeBesoinsNets.put(semaine, bn);
    }
    
    public void initialiserBesoinsNets(int nbSemaines){
        if(this.listeBesoinsNets.isEmpty()){
            for (int semaine = 1; semaine < nbSemaines ; semaine++){
                this.listeBesoinsNets.put(semaine, new BesoinNet(0, 0, 0, 0, 0, 0, semaine));
            }
        }
    }
    public BesoinNet getBesoinNet(int semaine){
        return listeBesoinsNets.get(semaine);
    }
    
    public void calculBesoinNet(int nbSemainesExercice){
        for (int semaine = 1; semaine < nbSemainesExercice ; semaine++){
            
            /* 1 - Calcul du besoin net */
            double besoinNet = 0;
            if(semaine != 1){
                besoinNet = max(0,this.getBesoinNet(semaine).getBesoinBrut() 
                - this.getBesoinNet(semaine-1).getStock()
                - this.getBesoinNet(semaine).getLivraison());
            } else {
                besoinNet = max(0,this.getBesoinNet(semaine).getBesoinBrut() 
                - this.getStockInitial()
                - this.getBesoinNet(semaine).getLivraison());
            }
            this.getBesoinNet(semaine).setBesoinNet(besoinNet);
            
            /* 2 - Calcul de la suggestion de vente */
            calculerSuggestion(semaine);
            
            /* 3 - Calcul de stock en fin de période*/
            double stock = 0;
            if(semaine != 1){
                stock = this.getBesoinNet(semaine-1).getStock()
                    + this.getBesoinNet(semaine).getSuggestion()
                    + this.getBesoinNet(semaine).getLivraison()
                    - this.getBesoinNet(semaine).getBesoinBrut(); 
            } else {
                stock = this.getStockInitial()
                    + this.getBesoinNet(semaine).getSuggestion()
                    + this.getBesoinNet(semaine).getLivraison()
                    - this.getBesoinNet(semaine).getBesoinBrut();
            }
            this.getBesoinNet(semaine).setStock(stock); 
        }
    }
    
    public double calculerSuggestion(int semaine){  
        switch(typeGestion){
            case ">" :
                if(this.getBesoinNet(semaine).getBesoinNet() < this.getQuantitéLancement()){
                    this.getBesoinNet(semaine).setSuggestion(this.getQuantitéLancement());
                    if ((semaine-(int)this.getDélai()) > 0){
                        this.getBesoinNet(semaine-(int)this.getDélai()).setLancement(this.getQuantitéLancement());
                    }
                } else {
                    this.getBesoinNet(semaine).setSuggestion(this.getBesoinNet(semaine).getBesoinNet());
                    if ((semaine-(int)this.getDélai()) > 0){
                        this.getBesoinNet(semaine-(int)this.getDélai()).setLancement(this.getBesoinNet(semaine).getBesoinNet());
                    }
                }
            break;
            case "=" :
                if(this.getBesoinNet(semaine).getBesoinNet() != 0){
                    double quantitéX = this.getBesoinNet(semaine).getBesoinNet();
                    this.getBesoinNet(semaine).setSuggestion(quantitéX);
                    if ((semaine-(int)this.getDélai()) > 0){
                        this.getBesoinNet(semaine-(int)this.getDélai()).setLancement(quantitéX);
                    }
                }
            break;
            case "X":
                if(this.getBesoinNet(semaine).getBesoinNet() != 0){
                    double quantitéX = this.getQuantitéLancement();
                    while (quantitéX < this.getBesoinNet(semaine).getBesoinNet()){
                        quantitéX = quantitéX + quantitéX;
                    }
                    this.getBesoinNet(semaine).setSuggestion(quantitéX);
                    if ((semaine-(int)this.getDélai()) > 0){
                        this.getBesoinNet(semaine-(int)this.getDélai()).setLancement(quantitéX);
                    } else {
                        
                    }
                }
            break;
            default:
        }
        return 0;
    }
    
    public void afficherBesoinsNets(){
        System.out.println("/*******************");
        System.out.println("/*******************");
        System.out.println("/* Article : " + this.getCodeArticle());
        System.out.println("/*******************");
        System.out.println("/*******************");
        for (Map.Entry<Integer,BesoinNet> e : listeBesoinsNets.entrySet()) {
            System.out.println("================================");
            System.out.println("===========SEMAINE "+ e.getKey().toString() + "============");
            System.out.println("================================");
            BesoinNet bn = e.getValue();
            System.out.println("BB  :" + bn.getBesoinBrut() + " | BN :"+bn.getBesoinNet());
            System.out.println("OL  :" + bn.getLivraison()+ " | SUGG :"+bn.getSuggestion());
            System.out.println("LCT :" + bn.getLancement()+ " | S :"+bn.getStock());
            System.out.println("\n");
        }
    }
}
