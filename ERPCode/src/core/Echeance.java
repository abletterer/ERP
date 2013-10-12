package core;

import java.util.ArrayList; 
import java.util.Calendar;

/**
 * Classe permettant de représenter une échéance d'un contrat
 * 
 * @author Haehnel Jonathan & Arnaud Bletterer
 * @version 1.0
 * @since 12/10/2013
 */
public class Echeance {

    private Calendar date;
    private ArrayList<Commande> listCommandes;
    
    /**
     * Constructeur simple
     */
    public Echeance () 
    {
        this(Calendar.getInstance());
    }
    
     /**
     * Constructeur 
     */
    public Echeance (Calendar date) 
    {
        this.date = date;
        this.listCommandes = new ArrayList<>();
    }
    
     /**
      * Retourne la date de l'échance 
      * @return date de l'échéance
      */
    public Calendar getDate () {
        return date;
    }

    /**
     * Affecte la date de l'échéance
     * @param val date de l'échéance
     */
    public void setDate (Calendar val) {
        this.date = val;
    }

    /**
     * Retourne les commandes
     * @return liste des commandes
     */
    public ArrayList<Commande> getListCommandes () {
        return listCommandes;
    }

    /**
     * Change la liste des commandes
     * @param val nouvelle liste de commande
     */
    public void setListCommandes (ArrayList<Commande> val) {
        this.listCommandes = val;
    }
    
    /**
     * Ajoute une commande à l'échéance
     * @param client nom du client
     * @param quantite nb boulons
     */
    public void addCommande(String client, int quantite) 
    {
        Commande newcommande = new Commande();
        newcommande.setClient(client);
        newcommande.setQuantite(quantite);
        
        this.listCommandes.add(newcommande);
    }

    /**
     * Retourne la quantité totale de boulons de l'échance
     * @return La quantite de boulons commandés tous clients confondus
     */
    public int getTotalQuantite () {
        int res=0;
        
        for(int i=0; i<this.listCommandes.size(); ++i) {
            res += this.listCommandes.get(i).getQuantite();
        }
        
        return res;
    }
    
    /**
     * Retourne la quantité totale de boulons de l'échance par client
     * @return La quantite de boulons commandés pour un client spécifique
     */
    public int getTotalQuantiteByClient (String client) {
        int res=0;
        
        for(int i=0; i<this.listCommandes.size(); ++i) {
            if(this.listCommandes.get(i).getClient().equals(client))
                res += this.listCommandes.get(i).getQuantite();
        }
        
        return res;
    }

    @Override
    public String toString () 
    {
        String res = "Echéance du "+this.date.get(Calendar.DAY_OF_MONTH)+"/" + (this.date.get(Calendar.MONTH)+1)+"/" +this.date.get(Calendar.YEAR)+"\n";
        for(int i=0; i<this.listCommandes.size(); ++i) 
        {
            res += this.listCommandes.get(i).toString();
        }
        
        return res;
    }

}

