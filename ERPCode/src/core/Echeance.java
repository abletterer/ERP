package core;

import java.util.ArrayList; 
import java.util.Calendar;


// <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
// #[regen=yes,id=DCE.20324B75-C81C-88FE-74B8-8BF805CD93EC]
// </editor-fold> 
public class Echeance {

    // <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
    // #[regen=yes,id=DCE.F15C322C-FA22-5492-C075-6B7871764C74]
    // </editor-fold> 
    private Calendar date;

    // <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
    // #[regen=yes,id=DCE.F8CDBE89-EE50-D146-D1C9-6B35DC2D19CB]
    // </editor-fold> 
    private ArrayList<Commande> listCommandes;

    // <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
    // #[regen=yes,id=DCE.7F71CC10-9C24-2980-38BA-DDD3C2EA3A0D]
    // </editor-fold> 
    public Echeance () 
    {
        this(Calendar.getInstance());
    }
    
    public Echeance (Calendar date) 
    {
        this.date = date;
        this.listCommandes = new ArrayList<Commande>();
    }

    // <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
    // #[regen=yes,regenBody=yes,id=DCE.41338A1E-3641-7F5D-539C-8795DE906F9C]
    // </editor-fold> 
    public Calendar getDate () {
        return date;
    }

    // <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
    // #[regen=yes,regenBody=yes,id=DCE.9D1F77E8-91BC-C7FF-602E-2DCB23E55D5A]
    // </editor-fold> 
    public void setDate (Calendar val) {
        this.date = val;
    }

    // <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
    // #[regen=yes,regenBody=yes,id=DCE.B857E643-457B-4C9A-5452-710D3BE89CF1]
    // </editor-fold> 
    public ArrayList<Commande> getListCommandes () {
        return listCommandes;
    }

    // <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
    // #[regen=yes,regenBody=yes,id=DCE.87F180A9-CA58-603F-B8F2-7D1FC5BE8AB6]
    // </editor-fold> 
    public void setListCommandes (ArrayList<Commande> val) {
        this.listCommandes = val;
    }

    // <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
    // #[regen=yes,id=DCE.F877BFBE-2662-A3F4-88A0-2DEEAB47FF01]
    // </editor-fold> 
    public int getTotalQuantite () {
        return 0;
    }

    // <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
    // #[regen=yes,id=DCE.FB4028DC-C07B-F295-0FE4-AE2EDB4416F6]
    // </editor-fold> 
    public String toString () {
        String res = "Echéance du "+this.date.toString()+"\n";
        for(int i=0; i<this.listCommandes.size(); ++i) {
            res += this.listCommandes.get(i).toString();
            if(i!=this.listCommandes.size()-1)
                res += "\n\n";
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

