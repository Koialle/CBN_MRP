package model;

import util.*;

/**
 *
 * @author Mélanie DUBREUIL
 * @author Ophélie EOUZAN
 * 
 */

public class Main {
    public static void main (String[] args){
        LecteurExcel er = new LecteurExcel();
        Nomenclature n = new Nomenclature();
        
        // ETAPE 1 : Calculer sur combien de semaines se déroule l'exercice (OK)
        n.setNbSemaines(er.retournerNbSemaines("C://exemple_calcul_besoin.xlsx"));
        
        // ETAPE 2 : Instancier les articles (OK)
        er.creerArticles("C://exemple_calcul_besoin.xlsx", n);
        n.supprimerPremierArticle();
        
        // ETAPE 3 : Créer les liens (OK)
        er.creerLiens("C://exemple_calcul_besoin.xlsx", n);
        n.supprimerPremierLien();
        
        // ETAPE 4 : Calculer le niveau de chaque article dans la nomenclature (OK)
        // ie. ordre des articles à traiter
        n.calculNiveauNomenclature();
        
        n.calculStockDisponible();
        
        // ETAPE 5 : Calculer les besoins bruts (initialisation) (OK)
        er.initialisationBesoinsBruts("C://exemple_calcul_besoin.xlsx", n);
        n.initialiserBesoinsNets();
        
        // ETAPE 6 : Calculer les livraisons attendues (OK)
        er.calculerArrivéesPrévues("C://exemple_calcul_besoin.xlsx", n);
        
        // ETAPE 7 : Calculer les besoins nets pour tous les articles
        n.calculsBesoinsNets();
        n.afficherBesoinsNets();
   }
}
    
    