package core;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Classe singleton permettant de définir et d'executer une simulation
 * 
 * @author Haehnel Jonathan & Arnaud Bletterer
 * @version 1.0
 * @since 12/10/2013
 */
public class Simulation 
{

    private static Simulation instance;

    /**
     * Contructeur privée
     */
    private Simulation () {
    }

    /**
     * Retourne l'instance unique
     * @return singleton
     */
    public static Simulation getInstance () 
    {
        if(Simulation.instance == null)
            Simulation.instance = new Simulation();
        
        return Simulation.instance;
    }

    /**
     * Permet de configurer et d'executer la simulation
     * 
     * @param filename nom du fichier de configuration 
     */
    public void simulate (String filename, boolean isJarRessource) 
    {
        // Configuration et execution de la simulation
        if(this.configure(filename, isJarRessource))
            this.execute();
    }

    /**
     * Execute la simulation (repond au différentes questions)
     */
    private void execute ()
    {
       this.processQ1(false);
       this.processQ2(false);
       this.processQ3();
       this.processQ4();
    }

    /**
     * Configuration de la simulation
     * 
     * @param filename chemin vers fichier de configuration
     * @return true pour réussite, false sinon 
     */
    private boolean configure (String filename, boolean isJarRessource) 
    {
        // Parsing du fichier de configuration XML
         if(Configuration.parse(filename, isJarRessource))
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

    /**
     * Calcul de la question 1 
     * @param useAugmentationQuantite utilisation de l'augmentation de quantité
     */
    public void processQ1 (boolean useAugmentationQuantite) {
        System.out.println("\nQuestion 1)");
        System.out.println(useAugmentationQuantite?"\t # Avec augmentation de la commande client":"\t # Sans augmentation de la commande client");
        
        Configuration configuration = Configuration.getInstance();
        
        if(useAugmentationQuantite) {
            configuration.enableAugmentationQuantiteCommande(true);
        }
        
        long tempsUtilisationStockBobine = Math.round(configuration.getTempsConstruction()
         * (configuration.getStockMaxBobine()+configuration.getEnCoursBobine()));
        
        //Calcul de la quantité à livrer
        int quantiteALivrer = 0;
        ArrayList<Echeance> echeances = configuration.getEcheances();
        
        for(int i=0; i<echeances.size(); ++i) {
            quantiteALivrer += echeances.get(i).getTotalQuantite();
        }
        
        ArrayList<Long> quantitesTheorique = this.getProductionTheoriqueEcheances(false);
        long productionBoulonsTheorique = 0;
        
        for(int i=0; i<quantitesTheorique.size(); ++i) {
            productionBoulonsTheorique += quantitesTheorique.get(i);
        }
        
        long quantite = productionBoulonsTheorique-quantiteALivrer;
        
        //Calcul de la date à partir de laquelle il faut commander les bobines
        Calendar dateDebutCommande = configuration.getDateDebut();
        if(Math.round(tempsUtilisationStockBobine/configuration.getTravailHeureJour())>configuration.getTempsLivraisonBobine()) {
            //Si le temps de production avec la quantité de bobines disponibles est supérieur au temps que met une bobine pour être livrée
            dateDebutCommande = getDateTo(dateDebutCommande, tempsUtilisationStockBobine/configuration.getTravailHeureJour()-configuration.getTempsLivraisonBobine());
        }
        
        if(quantite>0) {
            //Si la production théorique est supérieure à la quantité à livrer
            Calendar dateFinProduction = (Calendar) echeances.get(echeances.size()-1).getDate().clone();   //Récupère la dernière date de livraison
            dateFinProduction = getDateFrom(dateFinProduction, (long) (quantite/
                    (configuration.getTravailHeureJour()/configuration.getTempsConstruction()*configuration.getNbBoulonsBobine())));
            
            String dateString = dateDebutCommande.get(Calendar.DAY_OF_MONTH) + "/"
                        + (dateDebutCommande.get(Calendar.MONTH)+1) + "/"
                        + dateDebutCommande.get(Calendar.YEAR);
            
            System.out.print("Il faut commander " + (configuration.getStockMaxBobine()+configuration.getEnCoursBobine()) 
                    + " nouvelles bobines à partir du " + dateString);
            
            dateString = dateFinProduction.get(Calendar.DAY_OF_MONTH) + "/" 
                    + (dateFinProduction.get(Calendar.MONTH)+1) + "/" 
                    + dateFinProduction.get(Calendar.YEAR);
            
            System.out.println(" toutes les " + tempsUtilisationStockBobine + " heure(s) (heures ouvrées) depuis la dernière commande fournisseur."
                    + " Il ne faudra plus commander de bobines à partir du " + dateString + ".");
        
            configuration.setDateFinProduction(dateFinProduction);
        }
        else {
            String dateString = dateDebutCommande.get(Calendar.DAY_OF_MONTH) + "/"
                        + (dateDebutCommande.get(Calendar.MONTH)+1) + "/"
                        + dateDebutCommande.get(Calendar.YEAR);
            
            System.out.print("Il faut commander " + (configuration.getStockMaxBobine()+configuration.getEnCoursBobine()) 
                    + " nouvelles bobines à partir du " + dateString + " toutes les " + tempsUtilisationStockBobine 
                    + " heure(s) (heures ouvrées) depuis la dernière commande fournisseur.");
            configuration.setDateFinProduction(configuration.getDateDebut());
        }
        
        if(useAugmentationQuantite) {
            configuration.enableAugmentationQuantiteCommande(false);
        }
    }
    
    /**
     * Calcul de la date située avant une date 'a' (séparé de 'jours' jours ouvrés)
     * @param a date de référence
     * @param jours jours ouvrés
     * @return nouvelle date
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
     * Calcul de la date située aprés une date 'a' (séparé de 'jours' jours ouvrés)
     * @param a date de référence
     * @param jours jours ouvrés
     * @return nouvelle date
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
     * Calcul du nombre de jours ouvrées entre deux dates
     * @param a date début
     * @param b date fin
     * @return nb de jours
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
     * La date correspond à un weekend ou non 
     * @param cal date à tester
     * @return Vrai si le jour passé en paramètre est Samedi ou Dimanche
     */
    private boolean isWeekend(Calendar cal) {
        return cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY ||
               cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY;
    }
    

    /**
     * Calcul des quantités théoriques que l'on peut produire au maximum et à plein régime pour chacune des échéances
     * @param utilisationDateFinProduction date de fin de production
     * @return quantités que l'on peut produire pour chacune des échéances
     */
    private ArrayList<Long> getProductionTheoriqueEcheances(boolean utilisationDateFinProduction) {
        ArrayList<Long> res = new ArrayList<>();
        Configuration configuration = Configuration.getInstance();
        Calendar lastDate = (Calendar) configuration.getDateDebut().clone();
        
        //On enlève le nombre de jours pendant lesquels l'entreprise ne produit qu'avec le nombre de bobines en stock jusqu'au temps d'attente de la réception des premières bobines commandées
        lastDate = getDateTo(lastDate, configuration.getTempsLivraisonBobine()
                -(int)Math.round(configuration.getTempsConstruction()/configuration.getTravailHeureJour()
                *(configuration.getStockMaxBobine()+configuration.getEnCoursBobine())));
        
        int joursEcart;
        boolean ajoutLastQuantite = false;
        
        for(int i=0; i<configuration.getEcheances().size(); ++i) {
            if(!utilisationDateFinProduction) {
                joursEcart = getNombreJoursOuvres(configuration.getEcheances().get(i).getDate() ,lastDate);  //Jours d'écart (ouvrés) entre l'échéance courante et la dernière échéance

                res.add(Math.round(configuration.getTravailHeureJour()
                        /configuration.getTempsConstruction()*configuration.getNbBoulonsBobine())*joursEcart);
            }
            else {
                if((configuration.getDateFinProduction().get(Calendar.DAY_OF_MONTH) == configuration.getDateDebut().get(Calendar.DAY_OF_MONTH)
                        && configuration.getDateFinProduction().get(Calendar.MONTH) == configuration.getDateDebut().get(Calendar.MONTH)
                        && configuration.getDateFinProduction().get(Calendar.YEAR) == configuration.getDateDebut().get(Calendar.YEAR))
                        || configuration.getDateFinProduction().compareTo(configuration.getEcheances().get(i).getDate())>=0) {
                    //Si la date de fin de production est supérieure à la date de l'échéance courante
                    joursEcart = getNombreJoursOuvres(configuration.getEcheances().get(i).getDate() ,lastDate);  //Jours d'écart (ouvrés) entre l'échéance courante et la dernière échéance

                    res.add(Math.round(configuration.getTravailHeureJour()
                            /configuration.getTempsConstruction()*configuration.getNbBoulonsBobine())*joursEcart);
                }
                else {
                    if(!ajoutLastQuantite) {
                        //Au traitement de la première commande après la date de fin de production
                        //On ajout la quantité produite depuis la dernière date jusqu'à la date de fin de production (pour avoir le reste produit)
                        ajoutLastQuantite = true;
                        res.add(Math.round(configuration.getTravailHeureJour()
                                /configuration.getTempsConstruction()*configuration.getNbBoulonsBobine()*getNombreJoursOuvres(lastDate, configuration.getDateFinProduction())));
                    }
                    res.add((long) 0);
                }
            }
            
            lastDate = (Calendar) configuration.getEcheances().get(i).getDate().clone();
            lastDate = getDateTo(lastDate, 1); //On ajoute 1 jour pour ne pas compter des jours de travail en double
        }
        
        return res;
    }

    /**
     * Exécution de la question 2
     * @param useAugmentationQuantite utilisation de l'augmentation de quantité 
     */
    private void processQ2 (boolean useAugmentationQuantite) {
        System.out.println("\nQuestion 2)");
        System.out.println(useAugmentationQuantite?"\t # Avec augmentation de la commande client":"\t # Sans augmentation de la commande client");
        
        if(useAugmentationQuantite) {
            Configuration.getInstance().enableAugmentationQuantiteCommande(true);
        }
        
        simulateExecutionContrats();
        
        if(useAugmentationQuantite) {
            Configuration.getInstance().enableAugmentationQuantiteCommande(false);
        }
    }
    
    /**
     * Simulation de l'éxécution des contrats
     * (utilisation dans la question 2)
     */
    private void simulateExecutionContrats() 
    {
        Configuration configuration = Configuration.getInstance();
        ArrayList<Echeance> echeances = configuration.getEcheances();
        ArrayList<Long> productionTheoriqueEcheances = this.getProductionTheoriqueEcheances(true);
        
        String client;
        int quantiteALivrer;
        String dateEcheance;
        long sommeProductionsTheoriqueEcheances=0;
        
        for(int j=0; j<echeances.size(); ++j) {
            for(int i=0; i<echeances.get(j).getListCommandes().size(); ++i) {
                //Si le client courant à une commande à l'échéance courante
                quantiteALivrer = echeances.get(j).getListCommandes().get(i).getQuantite();
                client = echeances.get(j).getListCommandes().get(i).getClient();
                if((configuration.getDateFinProduction().get(Calendar.DAY_OF_MONTH) == configuration.getDateDebut().get(Calendar.DAY_OF_MONTH)
                        && configuration.getDateFinProduction().get(Calendar.MONTH) == configuration.getDateDebut().get(Calendar.MONTH)
                        && configuration.getDateFinProduction().get(Calendar.YEAR) == configuration.getDateDebut().get(Calendar.YEAR))
                        || configuration.getDateFinProduction().compareTo(echeances.get(i).getDate())>0) {
                    if(j!=0) {
                            sommeProductionsTheoriqueEcheances = productionTheoriqueEcheances.get(j-1)+productionTheoriqueEcheances.get(j);
                    }
                    else {
                            sommeProductionsTheoriqueEcheances = productionTheoriqueEcheances.get(j);
                    }
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

    /**
     * Exécution de la question 3
     */
    private void processQ3 () {
        System.out.println("\nQuestion 3)");
        Configuration configuration = Configuration.getInstance();
        
        Calendar dateDebutContratClient;
        Calendar dateFinContratClient;
        int moisCourant;
        long coutRevient = 0;
        
        for(int i=0; i<configuration.getClients().size(); ++i) {
            dateFinContratClient = getDateFinContratClient(configuration.getClients().get(i));
            dateDebutContratClient = getDateDebutContratClient(configuration.getClients().get(i), dateFinContratClient);
            moisCourant = dateDebutContratClient.get(Calendar.MONTH);
            coutRevient = 0;
            
            while(dateDebutContratClient.get(Calendar.DAY_OF_MONTH)!=dateFinContratClient.get(Calendar.DAY_OF_MONTH)
                    || dateDebutContratClient.get(Calendar.MONTH)!=dateFinContratClient.get(Calendar.MONTH)
                    || dateDebutContratClient.get(Calendar.YEAR)!=dateFinContratClient.get(Calendar.YEAR)) {
                if(!isWeekend(dateDebutContratClient)) {
                    coutRevient += Math.round(configuration.getCoutUsineHeure()+configuration.getPrixBobineHeure())*configuration.getTravailHeureJour();
                }
                
                dateDebutContratClient.add(Calendar.DAY_OF_MONTH,1);
                
                if(dateDebutContratClient.get(Calendar.MONTH)!=moisCourant) {
                    //On augmente le prix de l'acier comme le mois vient de changer
                    configuration.augmentePrixAcierMois();
                    moisCourant = dateDebutContratClient.get(Calendar.MONTH);
                }
            }
            
            
            NumberFormat num = java.text.NumberFormat.getInstance(java.util.Locale.FRENCH); 
            
            System.out.println("Cout de revient de " + num.format(coutRevient) + " euro(s) pour la production de " 
                    + num.format(configuration.getTotalQuantiteCommandeeClient(configuration.getClients().get(i))) 
                    + " boulon(s) pour le client " + configuration.getClients().get(i) + ".\n");
            
            System.out.println("Nous vous proposons de fixer le prix de vente de " 
                    + num.format(configuration.getTotalQuantiteCommandeeClient(configuration.getClients().get(i))) +  " boulon(s) pour le client "
                    + configuration.getClients().get(i) + " à " 
                    + num.format(Math.round(coutRevient+coutRevient*(configuration.getMargeSouhaite()/100))) + " euro(s). Soit un prix unitaire de "
                    + num.format(Math.round(coutRevient/configuration.getTotalQuantiteCommandeeClient(configuration.getClients().get(i))))+ " euros par boulon.\n");
        }
    }
    
    /**
     * Cherche la date de fin du contrat pour un client X
     * @param client nom du client
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
     * Cherche la date de début du contrat pour un client X
     * @param client nom du client
     * @return la date à laquelle on commence à travailler pour le client (aussi appelée date de début de contrat)
     */
    private Calendar getDateDebutContratClient(String client, Calendar dateFinContratClient) {
        Calendar res;
        Configuration configuration = Configuration.getInstance();
        
        int quantiteCommandee = configuration.getTotalQuantiteCommandeeClient(client);
        
        //Calcul le nombre de jours ouvrés nécessaires pour produire la quantité commandée
        long nombreJoursProduction = Math.round(1/(configuration.getTravailHeureJour()
                /configuration.getTempsConstruction()*configuration.getNbBoulonsBobine())*quantiteCommandee);
        
        res = getDateFrom(dateFinContratClient, nombreJoursProduction);
        
        return res;
    }

    /**
     * Execution de la question 4
     */
    private void processQ4 () {
        processQ1(true);
        processQ2(true);
    }

}

