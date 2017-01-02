package model;

import static java.lang.Double.max;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
    
    /*
    *
    * Getters et Setters
    *
    */
    
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
    
    public BesoinNet getBesoinNet(int semaine){
        return listeBesoinsNets.get(semaine);
    }
    
    /*
    *
    * Méthodes utilisées dans le cadre de l'utilisation de la collection HashMap
    *
    */
    
    @Override
    public boolean equals(Object o) {
        if(o == null) {
            return false;
        }
        if(!(o instanceof Article)) {
            return false;
        }        
        Article other = (Article) o;        
        if(!this.codeArticle.equals(other.getCodeArticle())){
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.codeArticle);
        return hash;
    }
    
    /**
    * Calcul du besoin brut d'un article pour une semaine S
    *
    * @param  semaine  numéro de la semaine pour laquelle on souhaite calculer le besoin brut
    * @param besoin valeur du besoin brut lu dans le fichier excel
    * 
    */
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
    
    /**
    * Calcul de besoin net de chaque article sur le nombre de semaines de l'exercice
    *
    * @param  nbSemainesExercice  Nombres de semaines de l'exerice
    * 
    */
    public void calculBesoinNet(int nbSemainesExercice){
        for (int semaine = 1; semaine <= nbSemainesExercice ; semaine++){            
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
            calculSuggestion(semaine);
            
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
    
    /**
    * Retourne la liste des parents (avec le coefficient associé) d'un article dans la nomenclature.
    *
    * @param  semaine  Semaine courante du calcul
    */
    public void calculSuggestion(int semaine){
        // Prise en compte du type de gestion
        switch(typeGestion){
            case ">" :
                // Suggestion et livraison uniquement s'il y a un besoin net
                if(this.getBesoinNet(semaine).getBesoinNet() != 0){
                    // Si le besoin net est inférieur à la quantité de lancement, suggestion = quantité de lancement
                    if(this.getBesoinNet(semaine).getBesoinNet() < this.getQuantitéLancement()){
                        this.getBesoinNet(semaine).setSuggestion(this.getQuantitéLancement());
                        // On regarde que le délai ne soit pas en dehors de l'exercice
                        if ((semaine-(int)this.getDélai()) > 0){
                            this.getBesoinNet(semaine-(int)this.getDélai()).setLancement(this.getQuantitéLancement());
                        }
                    } else {
                        // Sinon, suggestion = besoin net
                        this.getBesoinNet(semaine).setSuggestion(this.getBesoinNet(semaine).getBesoinNet());
                        if ((semaine-(int)this.getDélai()) > 0){
                            this.getBesoinNet(semaine-(int)this.getDélai()).setLancement(this.getBesoinNet(semaine).getBesoinNet());
                        }
                    }
                }
            break;
            case "=" :
                // Suggestion et livraison uniquement s'il y a un besoin net
                if(this.getBesoinNet(semaine).getBesoinNet() != 0){
                    // Suggestion = quantité de lancement
                    double quantitéX = this.getBesoinNet(semaine).getBesoinNet();
                    this.getBesoinNet(semaine).setSuggestion(quantitéX);
                    if ((semaine-(int)this.getDélai()) > 0){
                        this.getBesoinNet(semaine-(int)this.getDélai()).setLancement(quantitéX);
                    }
                }
            break;
            case "X":
                // Suggestion et livraison uniquement s'il y a un besoin net
                if(this.getBesoinNet(semaine).getBesoinNet() != 0){
                    // suggestion = multiple de la quantité de lancement
                    double quantitéX = this.getQuantitéLancement();
                    while (quantitéX <= this.getBesoinNet(semaine).getBesoinNet()){
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
    }
    
    /**
    * Initialisation de tous les besoins nets de tous les articles à zéro
    *
    * @param  nbSemaines  Nombres de semaines de l'exerice
    * 
    */
    public void initialiserBesoinsNets(int nbSemaines){
        if(this.listeBesoinsNets.isEmpty()){
            for (int semaine = 1; semaine <= nbSemaines ; semaine++){
                this.listeBesoinsNets.put(semaine, new BesoinNet(0, 0, 0, 0, 0, 0, semaine));
            }
        }
    }
    
    /**
    * Affiche les besoins nets calculés chaque semaine pour chaque article selon le format suivant :
    * ____________  __________
    *|Besoin brut ||Besoin net|
    *|Livraison   ||Suggestion|
    *|Lancement   ||Stock     |
    * ____________  __________
    * 
    */
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
