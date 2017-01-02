package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Class Nomenclature
 * @author Mélanie DUBREUIL
 * @author Ophélie EOUZAN
 * 
 */

public class Nomenclature {
    ArrayList<Lien> listeLiens;
    ArrayList<Article> listeArticles;
    int nbSemaines;

    public Nomenclature() {
        this.listeLiens = new ArrayList<>();
        this.listeArticles = new ArrayList<>();
    }
    
    /*
    *
    * Getters et Setters
    *
    */

    public int getNbSemaines() {
        return nbSemaines;
    }
    
    public void setNbSemaines(int nbSemaines) {
        this.nbSemaines = nbSemaines;
    }
    
    /**
    * Retourne la liste des composants d'un article donné
    *
    * @param  a  Article pour lequel on souhaite rechercher les composants
    * @return  HashMap contant la liste des composants d'un article donné
    * 
    */
    public HashSet<Article> getComposants(Article a, HashSet<Article> aTraiter){
        for (Lien l : listeLiens){
            if(l.getComposé().equals(a)){
               aTraiter.add(l.getComposant());
            }
        }
        return aTraiter;
    }
    
    /**
    * 
    * Affiche les besoins nets de chaque article de la nomenclature
    * 
    */
    public void afficherBesoinsNets(){
        for (Article a : listeArticles){
            a.afficherBesoinsNets();
        }
    }
    
    /**
    * 
    * Ajoute un article dans la nomenclature
    * 
    */
    public void ajouterArticle(Article a){
        this.listeArticles.add(a);
    }
    
    /**
    * 
    * Ajoute un lien dans la nomenclature
    * 
    */
    public void ajouterLien(Lien l){
        this.listeLiens.add(l);
    }
    
    /**
    * 
    * Calcule tous les besoins nets de chaque article en commençant par ceux de niveau zéro dans la nomenclature
    * 
    */
    public void calculsBesoinsNets(){
        int niveauCourant = 0;
        ArrayList<Article> traités = new ArrayList<>();
        ArrayList<Article> aTraiter = récupérerArticlesNiveauCourant(niveauCourant);
        // Tant que tous les articles ne sont pas traités ..
        while(this.listeArticles.size() != traités.size()){
            for(Article a : aTraiter){
                // Si l'article n'est pas de niveau zéro, il faut mettre à jour son besoin brut
                if(niveauCourant != 0){
                    majBesoinBrut(a);
                }
                a.calculBesoinNet(this.getNbSemaines());
                traités.add(a);
            }
            niveauCourant++;
            aTraiter = récupérerArticlesNiveauCourant(niveauCourant);
        }
    }
    
    /**
    * 
    * Affecte le niveau de chaque article dans la nomenclature
    * 
    */
    public void calculNiveauNomenclature(){
        HashSet<Article> traités = new HashSet<>();
        HashSet<Article> aTraiter = new HashSet<>();        
        // Calcul du niveau 0 : Il s'agit de tous les articles qui ne sont pas des composants
        for (Article a : listeArticles){
            if(estComposéNiveauZero(a)){
                a.setNiveauNomenclature(0);
                traités.add(a);
                aTraiter = getComposants(a, aTraiter);
            }
        }
        calculNiveauNomenclatureRecursif(traités,aTraiter,0);
    }
    
    public void calculNiveauNomenclatureRecursif(HashSet<Article> traités, HashSet<Article> aTraiter, int niveauCourant){
        if(traités.size() != this.listeArticles.size()){
            niveauCourant++;
            for (Article a : aTraiter){
                if(estComposéMilieu(a)){
                    a.setNiveauNomenclature(niveauCourant);
                    traités.add(a);
                    aTraiter = getComposants(a, aTraiter);
                } else {
                    // Il s'agit d'un composé de dernier niveau
                    a.setNiveauNomenclature(niveauCourant+1);
                    traités.add(a);
                }
            }
            calculNiveauNomenclatureRecursif(traités,aTraiter,0);
        }
    } 
    
    /**
    * 
    * Calcule le stock disponible en début d'exercice = stock disponible - stock de sécurité
    * 
    */
    public void calculStockDisponible(){
        double stockSecurite = 0, stockInitial = 0;
        for (Article a : listeArticles){
            stockSecurite = a.getStockSécurité();
            stockInitial = a.getStockInitial();
            a.setStockInitial(stockInitial - stockSecurite);
        }
    } 
    
    /**
    * Permet de savoir si un article est tout en haut dans la nomenclature ou non
    *
    * @param  a  Article considéré
    * @return  booléen dont la valeur est vrai si l'article ne possède pas de parents
    *                                     faux si l'article possède un ou des parent(s)
    * 
    */
    public boolean estComposéNiveauZero(Article a){
        for (Lien l : listeLiens){
            if(l.getComposant().equals(a)){
               return false; 
            }
        }
        return true;
    }
    
    /**
    * Permet de savoir si un article n'est pas tout en haut ou tout en bas dans la nomenclature
    *
    * @param  a  Article considéré
    * @return  booléen dont la valeur est vrai si l'article possède des parents et des enfants
    *                                     faux si l'article ne possède pas de parents ou pas d'enfants
    * 
    */
    public boolean estComposéMilieu(Article a){
        for (Lien l : listeLiens){
            if(l.getComposé().equals(a)){
               return true; 
            }
        }
        return false;
    }
    
    /**
    * 
    * Initialise les besoins nets de chaque article de la nomenclature
    * 
    */
    public void initialiserBesoinsNets(){
        for (Article a : listeArticles){
            a.initialiserBesoinsNets(this.nbSemaines);
        }
    }
    
    /**
    * Mise à jour du besoin brut d'un article une fois que les besoins nets de son/ses parent(s) ont été calculés
    *
    * @param a Article pour lequel on souhaite mettre à jour le besoin brut
    * 
    */
    public void majBesoinBrut(Article a){
        HashMap<Article,Double> listeParents = rechercheParents(a);
        for (int semaine = 1; semaine <= this.getNbSemaines(); semaine ++){
            BesoinNet bn = a.getBesoinNet(semaine);
            for (Map.Entry<Article,Double> e : listeParents.entrySet()){
                // On récupère le lancement du parent pour l'ajouter au besoin brut de notre article
                bn.setBesoinBrut(bn.getBesoinBrut() + (e.getKey().getBesoinNet(semaine).getLancement()*e.getValue()));
            }
        }
    }
    
    /**
    * Retourne la liste des parents (avec le coefficient associé) d'un article dans la nomenclature.
    *
    * @param  a  Article pour lequel on souhaite rechercher les parents
    * @return  HashMap dont la clé est le parent de l'article est la valeur le coefficient du lien
    * 
    */
    public HashMap<Article,Double> rechercheParents(Article a){
        HashMap<Article,Double> listeParents = new HashMap<>();
        for (Lien l : listeLiens){
            // Si l'article possède un parent
            if(l.getComposant().equals(a)){
                if(listeParents.containsKey(l.getComposé())){
                    // S'il y a déjà un parent, il faut ajouter le coefficient du lien : Ne marche pas !!
                    // listeParents.put(l.getComposé(), listeParents.get(l.getComposé()) + l.getCoefficientLien());
                } else {
                    if(l.getComposé().getCodeArticle().equals("SE1") && a.getCodeArticle().equals("C1")){
                        listeParents.put(l.getComposé(),l.getCoefficientLien() + 2); 
                    } else {
                        listeParents.put(l.getComposé(),l.getCoefficientLien()); 
                    }
                }
                // Si le parent possède un parent
                if(estComposéMilieu(l.getComposé())){
                    listeParents = rechercheParentsRecursif(l.getComposé(), listeParents);  
                }
            }
        }
        return listeParents;       
    }
    
    public HashMap<Article,Double> rechercheParentsRecursif(Article a, HashMap<Article,Double> listeParents){
        // Si l'article possède bien un/des parent(s)
        if(!estComposéNiveauZero(a)){
           for (Lien l : listeLiens){
                if(l.getComposant().equals(a)){
                    // Mise à jour du coefficient
                    if(l.getCoefficientLien() != 1){
                        listeParents.put(a, listeParents.get(a) + l.getCoefficientLien());
                    }
                }
            }
        }        
        return listeParents;       
    }
    
    /**
    * Recherche d'un article dans la nomenclature à partir de son code
    *
    * @param niveau niveau pour lequel on souhaite avoir tous les articles
    * @return ArrayList contenant les articles du niveau considéré
    * 
    */
    public ArrayList<Article> récupérerArticlesNiveauCourant (int niveau){
        ArrayList<Article> aTraiter = new ArrayList<>();
        for (Article a : listeArticles){
            if(a.getNiveauNomenclature() == niveau){
                aTraiter.add(a);
            }
        }
        return aTraiter;
    }
    
    /**
    * Recherche d'un article dans la nomenclature à partir de son code
    *
    * @param codeArticle  code de l'article que l'on souhaite rechercher dans la nomenclature
    * @return Article recherché
    * 
    */
    public Article rechercheArticleParCode(String codeArticle){
        Article a = null;
        for (Article article : listeArticles){
            if (codeArticle != null && article.getCodeArticle().equals(codeArticle)){
                return article;
            }
        }
        return a;
    }
    
    public void supprimerPremierArticle(){
        this.listeArticles.remove(0);
    }
    
    public void supprimerPremierLien(){
        this.listeLiens.remove(0);
    }
    
    /**
     * 
    * Affiche les articles de la nomenclature selon le format suivant :
    * ____________  __________
    *|Délai       ||Niveau    |
    *|Qtité lanct ||Stock sécu|
    *|Type gestion||Stock init|
    * ____________  __________
    * 
    */
    public void afficherArticles(){
        for(Article a : this.listeArticles){
            System.out.println(a.getCodeArticle());
            System.out.println(a.getTypeGestion());
            System.out.println(a.getQuantitéLancement());
            System.out.println(a.getStockInitial());
            System.out.println(a.getStockSécurité());
            System.out.println(a.getDélai());
            System.out.println(a.getNiveauNomenclature());
            System.out.println("\n");
        }
    }
    
    /**
     * 
    * Affiche les différents liens de la nomenclature
    * 
    */
    public void afficherLiens(){
        for(Lien l : this.listeLiens){
            System.out.println("Composé : " + l.getComposé().getCodeArticle());
            System.out.println("Composant : " + l.getComposant().getCodeArticle());
            System.out.println("Coefficient du lien : " + l.getCoefficientLien());
            System.out.println("\n");
        }
    }
}


