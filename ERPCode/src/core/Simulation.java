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
        Calendar dateDebut = configuration.getDateDebut();
        
        int joursEcart = (int) (
                (echeances.get(echeances.size()-1).getDate().getTimeInMillis()
                -dateDebut.getTimeInMillis())/(1000*3600*24));  
        
        int tempsTravailTotal = joursEcart-(joursEcart/7*2);
        
        long productionBoulonsTheorique = Math.round(configuration.getTravailHeureJour()/configuration.getTempsConstruction()*configuration.getNbBoulonsBobine())*tempsTravailTotal;
        
        System.out.println("Production de boulons théorique : "+productionBoulonsTheorique);
        System.out.println("Nombre de boulons commandés : "+quantiteALivrer);
        
        System.out.println("Il faut commander " + (configuration.getStockMaxBobine()+configuration.getEnCoursBobine()) + " nouvelles bobines toutes les " + tempsUtilisationStockBobine + " heure(s) (heures ouvrées) depuis la dernière commande fournisseur.");
    }
    
    private void simulateExecutionContrats() {
        Configuration configuration = Configuration.getInstance();
        ArrayList<Echeance> echeances = configuration.getEcheances();
        Calendar dateDebut = configuration.getDateDebut();
        
        String client;
        Echeance lastEcheance;
        int lastQuantite;
        int joursEcart;
        int tempsTravailTotal = 0;
        int quantiteALivrer;
        long productionBoulonsTheorique;
        
        for(int i=0; i<configuration.getClients().size(); ++i) {
            //On calcule le nombre de boulons à produire pour chaque client
            client = configuration.getClients().get(i);
            lastEcheance = new Echeance();
            quantiteALivrer = 0;

            //Nombre de boulons commandés par le client courant
            for(int j=0; j<echeances.size(); ++j) {
                lastQuantite = quantiteALivrer;
                quantiteALivrer += echeances.get(j).getTotalQuantiteByClient(client);
            
                //Nombre de boulons que l'entreprise peut produire pour le client courant
                joursEcart = (int) (
                (lastEcheance.getDate().getTimeInMillis()
                -dateDebut.getTimeInMillis())/(1000*3600*24));  

                tempsTravailTotal = joursEcart-(joursEcart/7*2);

                if(quantiteALivrer!=lastQuantite)
                    lastEcheance = echeances.get(j);
            }

            productionBoulonsTheorique = Math.round(configuration.getTravailHeureJour()/configuration.getTempsConstruction()*configuration.getNbBoulonsBobine())*tempsTravailTotal;

            System.out.println("Production de boulons théorique pour le client " + client + ": "+productionBoulonsTheorique);
            System.out.println("Nombre de boulons commandés par le client " + client + ": "+quantiteALivrer);
        }
    }
    
    private int getTotalQuantiteCommandeeClient(String client) {
        int res =0;
        Configuration configuration = Configuration.getInstance();
        ArrayList<Commande> commandesEcheance;
        
        for(int i=0; i<configuration.getEcheances().size(); ++i) {
            //On parcourt l'ensemble des échéances
            commandesEcheance = configuration.getEcheances().get(i).getListCommandes();
            for(int j=0; j<commandesEcheance.size(); j++) {
                //On cherche le client à travers toutes les commandes l'échéance courante
                if(commandesEcheance.get(i).getClient().equals(client)) {
                    res += commandesEcheance.get(i).getQuantite();
                }
            }
        }
        
        return res;
    }
    
    private Echeance getTotalEcheancesClient(String client) {
        Echeance res = new Echeance();
        Configuration configuration = Configuration.getInstance();
        ArrayList<Commande> commandesEcheance;
        
        for(int i=0; i<configuration.getEcheances().size(); ++i) {
            //On parcourt l'ensemble des échéances
            commandesEcheance = configuration.getEcheances().get(i).getListCommandes();
            for(int j=0; j<commandesEcheance.size(); j++) {
                //On cherche le client à travers toutes les commandes l'échéance courante
                if(commandesEcheance.get(i).getClient().equals(client)) {
                    res.addCommande(client, commandesEcheance.get(i).getQuantite());
                }
            }
        }
        
        return res;
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

