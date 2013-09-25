package core;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Simulation {

    private static Simulation instance;


    private Simulation () {
    }

    public static Simulation getInstance () 
    {
        if(Simulation.instance == null)
            Simulation.instance = new Simulation();
        
        return Simulation.instance;
    }

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

    public void processQ1 (boolean useAugmentationQuantite) {
        System.out.println("Question 1)");
        System.out.println(useAugmentationQuantite?"\t # Avec augmentation de la commande client":"\t # Sans augmentation de la commande client");
        
        Configuration configuration = Configuration.getInstance();
        
        long tempsUtilisationStockBobine = Math.round(configuration.getTempsConstruction()
         * (configuration.getStockMaxBobine()+configuration.getEnCoursBobine()));
        
        //AJOUTER GESTION DEPASSEMENT QUANTITE COMMANDE
        
        //Calcul de la quantité à livrer
        int quantiteALivrer = 0;
        ArrayList<Echeance> echeances = configuration.getEcheances();
        
        for(int i=0; i<echeances.size(); ++i) {
            quantiteALivrer += echeances.get(i).getTotalQuantite();
        }
        
        //Calcul de la quantité théorique que l'on peut produire au maximum pendant la période donnée
        Calendar dateDebut = Calendar.getInstance();
        dateDebut.clear();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("d-MM-y");
            dateDebut.setTime(sdf.parse("01-10-1969"));
        } catch (ParseException ex) {
            Logger.getLogger(Simulation.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        int joursEcart = (int) (
                (echeances.get(echeances.size()-1).getDate().getTimeInMillis()
                -dateDebut.getTimeInMillis())/(1000*3600*24));  
        
        int tempsTravailTotal = joursEcart-(joursEcart/7*2);
        
        long productionBoulonsTheorique = Math.round(configuration.getTravailHeureJour()/configuration.getTempsConstruction()*configuration.getNbBoulonsBobine())*tempsTravailTotal;
        
        System.out.println("Production de boulons théorique : "+productionBoulonsTheorique);
        System.out.println("Nombre de boulons commandés : "+quantiteALivrer);
        
        //Calcul de quantité pour chacun des clients
        
        String client;
        Echeance lastEcheance;
        int lastQuantiteClient;
        int joursEcartClient;
        int tempsTravailTotalClient;
        int quantiteALivrerClient;
        long productionBoulonsTheoriqueClient;
        for(int i=0; i<configuration.getClients().size(); ++i) {
            //On calcule le nombre de boulons à produire pour chaque client et on le compare avec le nombre de boulons que l'on sait produire
            client = configuration.getClients().get(i);
            lastEcheance = new Echeance();
            quantiteALivrerClient = 0;

            //Nombre de boulons commandés par le client courant
            for(int j=0; j<echeances.size(); ++j) {
                lastQuantiteClient = quantiteALivrerClient;
                quantiteALivrerClient += echeances.get(j).getTotalQuantiteByClient(client);

                if(quantiteALivrerClient!=lastQuantiteClient)
                    lastEcheance = echeances.get(j);
            }
            
            //Nombre de boulons que l'entreprise peut produire pour le client courant
            joursEcartClient = (int) (
            (lastEcheance.getDate().getTimeInMillis()
            -dateDebut.getTimeInMillis())/(1000*3600*24));  

            tempsTravailTotalClient = joursEcartClient-(joursEcartClient/7*2);

            productionBoulonsTheoriqueClient = Math.round(configuration.getTravailHeureJour()/configuration.getTempsConstruction()*configuration.getNbBoulonsBobine())*tempsTravailTotalClient;

            System.out.println("Production de boulons théorique pour le client " + client + ": "+productionBoulonsTheoriqueClient);
            System.out.println("Nombre de boulons commandés par le client " + client + ": "+quantiteALivrerClient);
        }
        
        if(quantiteALivrer>productionBoulonsTheorique) {
            //Si l'ensemble des commandes n'est pas réalisable alors on regarde au niveau des commandes de chaque client
        }
        
        System.out.println("Il faut commander " + (configuration.getStockMaxBobine()+configuration.getEnCoursBobine()) + " nouvelles bobines toutes les " + tempsUtilisationStockBobine + " heure(s) (heures ouvrées) depuis la dernière commande fournisseur.");
    }

    private void processQ2 (boolean useAugmentationQuantite) {
        System.out.println("Question 2)");
        System.out.println(useAugmentationQuantite?"\t # Avec augmentation de la commande client":"\t # Sans augmentation de la commande client");
        
    }

    private void processQ3 () {
    }

    private void processQ4 () {
    }

}

