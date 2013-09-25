package core;

import java.util.ArrayList; 
import java.util.Calendar;


public class Echeance {

    private Calendar date;

    private ArrayList<Commande> listCommandes;
    
    public Echeance () 
    {
        this(Calendar.getInstance());
    }
    
    public Echeance (Calendar date) 
    {
        this.date = date;
        this.listCommandes = new ArrayList<Commande>();
    }
    
    public Calendar getDate () {
        return date;
    }

    public void setDate (Calendar val) {
        this.date = val;
    }

    public ArrayList<Commande> getListCommandes () {
        return listCommandes;
    }

    public void setListCommandes (ArrayList<Commande> val) {
        this.listCommandes = val;
    }

    /**
     * 
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
     * 
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

    public String toString () 
    {
        String res = "Echéance du "+this.date.get(Calendar.DAY_OF_MONTH)+"/" + (this.date.get(Calendar.MONTH)+1)+"/" +this.date.get(Calendar.YEAR)+"\n";
        for(int i=0; i<this.listCommandes.size(); ++i) 
        {
            res += this.listCommandes.get(i).toString();
        }
        
        return res;
    }

    public void addCommande(String client, int quantite) 
    {
        Commande newcommande = new Commande();
        newcommande.setClient(client);
        newcommande.setQuantite(quantite);
        
        this.listCommandes.add(newcommande);
    }

}

