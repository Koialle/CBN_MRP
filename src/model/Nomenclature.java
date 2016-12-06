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

    public int getNbSemaines() {
        return nbSemaines;
    }
    
    public void setNbSemaines(int nbSemaines) {
        this.nbSemaines = nbSemaines;
    }
    
    public void ajouterLien(Lien l){
        this.listeLiens.add(l);
    }
    
    public void ajouterArticle(Article a){
            this.listeArticles.add(a);
    }
    
    public void calculNiveauNomenclature(){
        HashSet<Article> traités = new HashSet<>();
        HashSet<Article> aTraiter = new HashSet<>();
        
        // Calcul du niveau 0 : Il s'agit de tous les articles qui ne sont pas des composants
        for (Article a : listeArticles){
            if(isComposéNiveauZero(a)){
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
                if(isComposéMilieu(a)){
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
    
    public boolean isComposéMilieu(Article a){
        for (Lien l : listeLiens){
            if(l.getComposé().equals(a)){
               return true; 
            }
        }
        return false;
    }
    
    public boolean isComposéNiveauZero(Article a){
        for (Lien l : listeLiens){
            if(l.getComposant().equals(a)){
               return false; 
            }
        }
        return true;
    }
    
    public HashSet<Article> getComposants(Article a, HashSet<Article> aTraiter){
        for (Lien l : listeLiens){
            if(l.getComposé().equals(a)){
               aTraiter.add(l.getComposant());
            }
        }
        return aTraiter;
    }
    
    public void initialiserBesoinsNets(){
        for (Article a : listeArticles){
            a.initialiserBesoinsNets(this.nbSemaines);
        }
    }
    
    public void afficherBesoinsNets(){
        for (Article a : listeArticles){
            a.afficherBesoinsNets();
        }
    }
    
    public void calculsBesoinsNets(){
        int niveauCourant = 0;
        ArrayList<Article> traités = new ArrayList<>();
        ArrayList<Article> aTraiter = récupérerArticlesNiveauCourant(niveauCourant);
        
        while(this.listeArticles.size() != traités.size()){
            for(Article a : aTraiter){
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
    
    public void majBesoinBrut(Article a){
        HashMap<Article,Double> listeParents = rechercheParents(a);
        for (int semaine = 1; semaine < this.getNbSemaines(); semaine ++){
            BesoinNet bn = a.getBesoinNet(semaine);
            for (Map.Entry<Article,Double> e : listeParents.entrySet()){
                bn.setBesoinBrut(bn.getBesoinBrut() + (e.getKey().getBesoinNet(semaine).getLancement()*e.getValue()));
            }
        }
    }
    
    /**
    * Retourne la liste des parents (avec le coefficient associé) d'un article dans la nomenclature.
    *
    * @param  a  Article pour lequel on souhaite rechercher les parents
    * @return  HashMap dont la clé est le parent de l'article et la valeur le coefficient du lien.
    */
    public HashMap<Article,Double> rechercheParents(Article a){
        HashMap<Article,Double> listeParents = new HashMap<>();
        for (Lien l : listeLiens){
            if(l.getComposant().equals(a)){
                listeParents.put(l.getComposé(),l.getCoefficientLien());
            }
        }
        return listeParents;
    }
    
    public ArrayList<Article> récupérerArticlesNiveauCourant (int niveau){
        ArrayList<Article> aTraiter = new ArrayList<>();
        for (Article a : listeArticles){
            if(a.getNiveauNomenclature() == niveau){
                aTraiter.add(a);
            }
        }
        return aTraiter;
    }
    
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
    
    public void afficherLiens(){
        for(Lien l : this.listeLiens){
            System.out.println(l.getComposé().getCodeArticle());
            System.out.println(l.getComposant().getCodeArticle());
            System.out.println(l.getCoefficientLien());
            System.out.println("\n");
        }
    }
}
