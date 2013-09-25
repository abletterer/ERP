package core;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;


// <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
// #[regen=yes,id=DCE.73AA51F6-5B3C-5C3D-4179-1F40411BD0DD]
// </editor-fold> 
public class Simulation {

    // <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
    // #[regen=yes,id=DCE.7D1C06D7-9455-8B1B-38E4-16E2E7DEAFB3]
    // </editor-fold> 
    private static Simulation instance;


    // <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
    // #[regen=yes,id=DCE.A086D108-AE9E-398D-5FBE-0D2B9792739A]
    // </editor-fold> 
    private Simulation () {
    }

    // <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
    // #[regen=yes,regenBody=yes,id=DCE.BE214756-D732-E155-548A-367395536393]
    // </editor-fold> 
    public static Simulation getInstance () 
    {
        if(Simulation.instance == null)
            Simulation.instance = new Simulation();
        
        return Simulation.instance;
    }

    // <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
    // #[regen=yes,id=DCE.AB0FFB05-C7CA-46DB-C914-F471ECBC7AC9]
    // </editor-fold> 
    public void simulate (String filename) 
    {
        // Configuration et execution de la simulation
        if(this.configure(filename))
            this.execute();
    }

    private void execute ()
    {
       this.processQ1(false);
    }

    private boolean configure (String filename) 
    {
        // Parsing du fichier de configuration XML
         if(Configuration.parse(filename))
         {
            
            System.out.println(">> Configuration chargée !");
            
            // Recuperation de la configuration parsée
            Configuration conf = Configuration.getInstance();
            
            // Verification que le configuration est valide
            if(conf.getEcheances().size() > 0 && conf.getTaches().size() > 0)
            {
                System.out.println(">> Configuration valide !");
                System.out.println();
                
                // Affichage de la configuration
                System.out.println(Configuration.getInstance().toString());
                
                return true;
            }
            else
            {
                 System.err.println(">> Configuration non valide !");
            }
        }
        else
        {
            System.err.println(" >> Echec de chargement de la configuration !");
        }
         
        return false;
    }

    // <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
    // #[regen=yes,id=DCE.4B0C3A85-C202-60DC-9B4D-2C154A22AD73]
    // </editor-fold> 
    public void processQ1 (boolean useAugmentationQuantite) {
        System.out.println("Question 1)");
        System.out.println(useAugmentationQuantite?"\t # Avec augmentation de la commande client":"\t # Sans augmentation de la commande client");
        
        Configuration configuration = Configuration.getInstance();
        
        long tempsUtilisationStockBobine = Math.round(configuration.getTempsConstruction()
         * (configuration.getStockMaxBobine()+configuration.getEnCoursBobine()));   //On réalise une troncature puisque qu'on souhaite réaliser une approximation
        
        //AJOUTER GESTION DEPASSEMENT QUANTITE COMMANDE
        
        int quantiteALivrer = 0;
        ArrayList<Echeance> echeances = configuration.getEcheances();
        
        for(int i=0; i<echeances.size(); ++i) {
            quantiteALivrer += echeances.get(i).getTotalQuantite();
        }
        
        Calendar dateDebut = Calendar.getInstance();
        
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd mm yyyy");
            dateDebut.setTime(sdf.parse("01 10 1960"));
        } catch (ParseException ex) {
            Logger.getLogger(Simulation.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        long quantiteProductionTheorique = (echeances.get(echeances.size()-1).getDate().getTimeInMillis()-dateDebut.getTimeInMillis());
        
        
        
        System.out.println("Il faut commander " + configuration.getStockMaxBobine()+configuration.getEnCoursBobine() + " nouvelles bobines toutes les " + tempsUtilisationStockBobine + " heure(s) (heures ouvrées) depuis la dernière commande fournisseur.");
    }

    // <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
    // #[regen=yes,id=DCE.65AE5BA6-4AC6-AEB2-180B-B54AD5CC8DA6]
    // </editor-fold> 
    private void processQ2 (boolean useAugmentationQuantite) {
        System.out.println("Question 2)");
        System.out.println(useAugmentationQuantite?"\t # Avec augmentation de la commande client":"\t # Sans augmentation de la commande client");
        
    }

    // <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
    // #[regen=yes,id=DCE.1DF3DFFB-3524-EA22-F30B-517918B81FF1]
    // </editor-fold> 
    private void processQ3 () {
    }

    // <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
    // #[regen=yes,id=DCE.A57E9BCB-8230-4833-BB04-15A56F7409E4]
    // </editor-fold> 
    private void processQ4 () {
    }

}

