
package model;

import java.util.HashMap;

/**
 * Class Article
 * @author Epulapp
 */
public class Article
{
    private String codeArticle;
    private int quantiteLancement;
    private String typeGestion;
    private int niveauNomenclature;
    private int stockSecurite;
    private int stockInitial;
    private HashMap<Integer, BesoinNet> listeBesoinsNets;

    public Article()
    {
        this.listeBesoinsNets = new HashMap();
    }
    
    public void calculBesoinBrut()
    {
        
    }
    
    public void calculBesoinNet()
    {
        
    }
    
    public void modifierLancement()
    {
        
    }
}
