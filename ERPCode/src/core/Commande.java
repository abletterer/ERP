package core;


/**
 * Classe permettant de représenter une commande d'un client avec une certaine quantité de boulons
 * 
 * @author Haehnel Jonathan & Arnaud Bletterer
 * @version 1.0
 * @since 12/10/2013
 */
public class Commande
{

    private String client;
    private int quantite;

    
    public Commande () {
    }

    /**
     * Retourne le client de la commande
     * @return client
     */
    public String getClient () {
        return client;
    }

    /**
     * Affecte le client de la commande
     * @param val client
     */
    public void setClient (String val) {
        this.client = val;
    }


    /**
     * Retourne la quantité commandée
     * @return nb boulons
     */
    public int getQuantite () {
        return quantite;
    }

    /**
     * Affecte la quantité commandée
     * @param val nb boulons
     */
    public void setQuantite (int val) {
        this.quantite = val;
    }
    
    @Override
    public String toString () {
        return      "##Commande## : Client: " + this.client
                +   " Quantite : " + this.quantite + "\n";
    }

}

