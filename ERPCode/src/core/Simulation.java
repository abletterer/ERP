package core;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


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
       this.processQ2(false);
       this.processQ3();
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
        
        if(useAugmentationQuantite) {
            Configuration.getInstance().enableAugmentationQuantiteCommande(true);
        }
        
        Configuration configuration = Configuration.getInstance();
        
        long tempsUtilisationStockBobine = Math.round(configuration.getTempsConstruction()
         * (configuration.getStockMaxBobine()+configuration.getEnCoursBobine()));
        
        //Calcul de la quantité à livrer
        int quantiteALivrer = 0;
        ArrayList<Echeance> echeances = configuration.getEcheances();
        
        for(int i=0; i<echeances.size(); ++i) {
            quantiteALivrer += echeances.get(i).getTotalQuantite();
        }
        
        ArrayList<Long> quantitesTheorique = this.getProductionTheoriqueEcheances();
        long productionBoulonsTheorique = 0;
        
        for(int i=0; i<quantitesTheorique.size(); ++i) {
            productionBoulonsTheorique += quantitesTheorique.get(i);
        }
        
        long quantite = productionBoulonsTheorique-quantiteALivrer;
        
        if(quantite>0) {
            //Si la production théorique est supérieure à la quantité à livrer
            Calendar dateFinCommande = (Calendar) echeances.get(echeances.size()-1).getDate().clone();   //Récupère la dernière date de livraison
            
            dateFinCommande = getDateFrom(dateFinCommande,quantite/(Math.round(configuration.getTravailHeureJour()/configuration.getTempsConstruction()*configuration.getNbBoulonsBobine())));
            String dateString= dateFinCommande.get(Calendar.DAY_OF_MONTH) + "/" 
                    + (dateFinCommande.get(Calendar.MONTH)+1) + "/" 
                    + dateFinCommande.get(Calendar.YEAR);
            System.out.println("Il faut commander " + (configuration.getStockMaxBobine()+configuration.getEnCoursBobine()) 
                    + " nouvelles bobines toutes les " + tempsUtilisationStockBobine + " heure(s) (heures ouvrées) depuis la dernière commande fournisseur."
                    + " Il ne faudra plus commander de bobines à partir du " + dateString + ".");
        }
        else {
            System.out.println("Il faut commander " + (configuration.getStockMaxBobine() + configuration.getEnCoursBobine())
                    + " nouvelles bobines toutes les " + tempsUtilisationStockBobine 
                    + " heure(s) (heures ouvrées) depuis la dernière commande fournisseur.");
        }
        
        if(useAugmentationQuantite) {
            Configuration.getInstance().enableAugmentationQuantiteCommande(false);
        }
    }
    
    /**
     * @return la date située avant la date 'a' (séparé de 'jours' jours ouvrés)
     */
    private Calendar getDateFrom(Calendar a, long jours) {
        Calendar res = (Calendar) a.clone();
        while(jours>0){
            if(!isWeekend(res)) {
                --jours;
            }
            res.add(Calendar.DAY_OF_MONTH, -1);
        }
        return res;
    }
    
    /**
     * @return la date située après la date 'a' (séparé de 'jours' jours ouvrés)
     */
    private Calendar getDateTo(Calendar a, long jours) {
        Calendar res = (Calendar) a.clone();
        while(jours>0){
            if(!isWeekend(res)) {
                --jours;
            }
            res.add(Calendar.DAY_OF_MONTH, 1);
        }
        return res;
    }
    
    /**
     * @return le nombre de jours ouvrés entre deux dates
     */
    private int getNombreJoursOuvres(Calendar a, Calendar b) {
        int res = 0;
        Calendar from, to;
        if(a.compareTo(b)<=0) {
            from = (Calendar) a.clone();
            to = (Calendar) b.clone();
        }
        else {
            from = (Calendar) b.clone();
            to = (Calendar) a.clone();
        }

        while(!from.equals(to)) {
            if(!isWeekend(from)) {
                ++res;
            }
            from.add(Calendar.DAY_OF_MONTH,1);
        }
        ++res;

        return res;
    }
    
    /**
     * 
     * @return Vrai si le jour passé en paramètre est Samedi ou Dimanche
     */
    private boolean isWeekend(Calendar cal) {
        return cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY ||
               cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY;
    }
    
    /**
     * 
     * @return Les quantités que l'on peut produire pour chacune des échéances
     */
    private ArrayList<Long> getProductionTheoriqueEcheances() {
        ArrayList<Long> res = new ArrayList<>();
        Configuration configuration = Configuration.getInstance();
        Calendar lastDate = (Calendar) configuration.getDateDebut().clone();
        
        //On enlève le nombre de jours pendant lesquels l'entreprise ne produit qu'avec le nombre de bobines en stock jusqu'au temps d'attente de la réception des premières bobines commandées
        lastDate = getDateTo(lastDate, configuration.getTempsLivraisonBobine()
                -(int)Math.round(configuration.getTempsConstruction()/configuration.getTravailHeureJour()
                *(configuration.getStockMaxBobine()+configuration.getEnCoursBobine())));
        
        int joursEcart;
        
        for(int i=0; i<configuration.getEcheances().size(); ++i) {
            joursEcart = getNombreJoursOuvres(configuration.getEcheances().get(i).getDate() ,lastDate);  //Jours d'écart (ouvrés) entre l'échéance courante et la dernière échéance
            
            res.add(Math.round(configuration.getTravailHeureJour()
                    /configuration.getTempsConstruction()*configuration.getNbBoulonsBobine())*joursEcart);
            
            lastDate = (Calendar) configuration.getEcheances().get(i).getDate().clone();
            lastDate.add(Calendar.DAY_OF_MONTH, 1); //On ajoute 1 jour pour ne pas compter des jours de travail en double
        }
        
        return res;
    }

    private void processQ2 (boolean useAugmentationQuantite) {
        System.out.println("Question 2)");
        System.out.println(useAugmentationQuantite?"\t # Avec augmentation de la commande client":"\t # Sans augmentation de la commande client");
        
        simulateExecutionContrats();
    }
    
    private void simulateExecutionContrats() {
        Configuration configuration = Configuration.getInstance();
        ArrayList<Echeance> echeances = configuration.getEcheances();
        ArrayList<Long> productionTheoriqueEcheances = this.getProductionTheoriqueEcheances();
        
        String client;
        int quantiteALivrer;
        String dateEcheance;
        long sommeProductionsTheoriqueEcheances;
        
        for(int j=0; j<echeances.size(); ++j) {
            for(int i=0; i<echeances.get(j).getListCommandes().size(); ++i) {
                //Si le client courant à une commande à l'échéance courante
                quantiteALivrer = echeances.get(j).getListCommandes().get(i).getQuantite();
                client = echeances.get(j).getListCommandes().get(i).getClient();
                 
                if(j!=0) {
                    sommeProductionsTheoriqueEcheances = productionTheoriqueEcheances.get(j-1)+productionTheoriqueEcheances.get(j);
                }
                else {
                    sommeProductionsTheoriqueEcheances = productionTheoriqueEcheances.get(j);
                }
                
                sommeProductionsTheoriqueEcheances -= quantiteALivrer;  //On soustrait la quantité à livrer au client
                productionTheoriqueEcheances.set(j,sommeProductionsTheoriqueEcheances); //Mise a jour de la nouvelle valeur de production théorique restante

                dateEcheance = echeances.get(j).getDate().get(Calendar.DAY_OF_MONTH) + "/"
                        + (echeances.get(j).getDate().get(Calendar.MONTH)+1) + "/"
                        + echeances.get(j).getDate().get(Calendar.YEAR);

                if(sommeProductionsTheoriqueEcheances>0) {
                    //Si la quantité théorique de production est supérieure à la commande
                    System.out.println("Commande pour le client " + client + " à l'échéance du "
                            + dateEcheance + " réalisable. Production en avance de " 
                            + sommeProductionsTheoriqueEcheances + " boulon(s).");
                }
                else {
                    System.out.println("Commande pour le client " + client 
                            + " à l'échéance du "+ dateEcheance + " non réalisable. Production en retard de "
                            + -sommeProductionsTheoriqueEcheances + " boulon(s).");
                }
            }
        }
    }

    private void processQ3 () {
        Configuration configuration = Configuration.getInstance();
        
        Calendar dateDebutContratClient;
        Calendar dateFinContratClient;
        int moisCourant;
        double coutRevient = 0.0;
        
        for(int i=0; i<configuration.getClients().size(); ++i) {
            dateFinContratClient = getDateFinContratClient(configuration.getClients().get(i));
            dateDebutContratClient = getDateDebutContratClient(configuration.getClients().get(i), dateFinContratClient);
            moisCourant = dateDebutContratClient.get(Calendar.MONTH);
            while(dateDebutContratClient.get(Calendar.DAY_OF_MONTH)!=dateFinContratClient.get(Calendar.DAY_OF_MONTH)
                    || dateDebutContratClient.get(Calendar.MONTH)!=dateFinContratClient.get(Calendar.MONTH)
                    || dateDebutContratClient.get(Calendar.YEAR)!=dateFinContratClient.get(Calendar.YEAR)) {
                if(!isWeekend(dateDebutContratClient)) {
                    coutRevient += configuration.getCoutUsineHeure()*configuration.getTravailHeureJour()+configuration.getPrixBobineHeure()*configuration.getTravailHeureJour();
                }
                dateDebutContratClient.add(Calendar.DAY_OF_MONTH,1);
                if(dateDebutContratClient.get(Calendar.MONTH)!=moisCourant) {
                    //On augmente le prix de l'acier comme le mois vient de changer
                    configuration.augmentePrixAcierMois();
                    moisCourant = dateDebutContratClient.get(Calendar.MONTH);
                }
            }
            System.out.println("Cout de revient de " + coutRevient + " euro(s) pour la production de " 
                    + configuration.getTotalQuantiteCommandeeClient(configuration.getClients().get(i)) 
                    + " boulon(s) pour le client "+ configuration.getClients().get(i));
        }
        
    }
    
    /**
     * 
     * @return la date de la dernière commande pour le client passé en paramètre (aussi appelée date de fin de contrat)
     */
    private Calendar getDateFinContratClient(String client) {
        Calendar res = Calendar.getInstance();
        Configuration configuration = Configuration.getInstance();
        ArrayList<Echeance> echeances = configuration.getEcheances();
        
        Commande commande;
        
        for(int i=0; i<echeances.size(); ++i) {
            for(int j=0; j<echeances.get(i).getListCommandes().size(); ++j) {
                commande = configuration.getEcheances().get(i).getListCommandes().get(j);
                if(client.equals(commande.getClient())) {
                    //Si la date correspondant au client que nous recherchons
                    res = (Calendar) configuration.getEcheances().get(i).getDate();
                }
            }
        }
        
        return (Calendar) res.clone();
    }
    
    /**
     * 
     * @return la date à laquelle on commence à travailler pour le client (aussi appelée date de début de contrat)
     */
    private Calendar getDateDebutContratClient(String client, Calendar dateFinContratClient) {
        Calendar res;
        Configuration configuration = Configuration.getInstance();
        ArrayList<Echeance> echeances = configuration.getEcheances();
        ArrayList<Long> productionTheoriqueEcheances = this.getProductionTheoriqueEcheances();
        
        Commande commande;
        int quantiteCommandee = configuration.getTotalQuantiteCommandeeClient(client);
        
        //Calcul le nombre de jours ouvrés nécessaires pour produire la quantité commandée
        long nombreJoursProduction = Math.round(1/(configuration.getTravailHeureJour()
                /configuration.getTempsConstruction()*configuration.getNbBoulonsBobine())*quantiteCommandee);
        
        res = getDateFrom(dateFinContratClient, nombreJoursProduction);
        
        return res;
    }

    private void processQ4 () {
        processQ1(true);
        processQ2(true);
    }

}

