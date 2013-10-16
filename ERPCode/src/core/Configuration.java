package core;


import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList; 
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Classe singleton permettant de définir la configuration (contrat) de la simulation
 * 
 * @author Haehnel Jonathan & Arnaud Bletterer
 * @version 1.0
 * @since 12/10/2013
 */
public class Configuration 
{

    private static Configuration instance;
    
    private ArrayList<Integer> taches;
    private ArrayList<Echeance> echeances;
    private ArrayList<String> clients;
    
    private int nbBoulonsBobine = 1000;
    private int prixBobine = 10000;
    private int travailHeureJour = 7;
    private int travailJourSemaine = 5;
    private int stockMinBobine = 1;
    private int stockMaxBobine = 2;
    private int enCoursBobine = 1;
    private int tempsLivraisonBobine = 10;
    private double augmentationPrixAcierMois = 2.0;
    private int coutUsineHeure = 1000;
    private double augmentationQuantiteCommande = 10.0;
    private boolean enableAugmentationQuantiteCommande = false;
    private double margeSouhaite = 70.0;
    private double tempsConstruction = -1.0;
    
    private Calendar dateDebut;
    private Calendar dateFinProduction;

    /**
     * Constructeur
     */
    private Configuration () 
    {
        this.taches = new ArrayList<>();
        this.clients = new ArrayList<>();
        
        dateDebut = Calendar.getInstance();
        dateDebut.clear();
        
        try 
        {
            SimpleDateFormat sdf = new SimpleDateFormat("d-MM-y");
            dateDebut.setTime(sdf.parse("01-10-1969"));
        } 
        catch (ParseException ex) 
        {
            Logger.getLogger(Simulation.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        dateFinProduction = (Calendar) dateDebut.clone();
    }

    /**
     * Retourne l'instance unique
     * @return singleton
     */
    public static Configuration getInstance () 
    {
        if(Configuration.instance == null)
            Configuration.instance = new Configuration();
        
        return instance;
    }

    /**
     * Retourne le pourcentage de variation du prix de l'acier par mois
     * @return pourcentage
     */
    public double getAugmentationPrixAcierMois () {
        return augmentationPrixAcierMois;
    }

    /**
     * Affecte le pourcentage de variation du prix de l'acier par mois
     * @param val nouvelle augmentation en pourcentage
     */
    public void setAugmentationPrixAcierMois (double val) {
        this.augmentationPrixAcierMois = val;
    }
    
    /**
     * Augmente le prix de la bobine
     */
    public void augmentePrixAcierMois() {
        this.prixBobine *= (1+(this.augmentationPrixAcierMois/100));
    }

    /**
     * Retourne le pourcentage d'augmentations de toutes les commandes du contrat
     * @return pourcentage
     */
    public double getAugmentationQuantiteCommande () {
        return augmentationQuantiteCommande;
    }

    /**
     * Affecte le pourcentage d'augmentations de toutes les commandes du contrat
     * @param val pourcentage
     */
    public void setAugmentationQuantiteCommande (double val) {
        this.augmentationQuantiteCommande = val;
    }
    
    /**
     *  Met a jour les valeurs des commandes clients avec l'augmentation prévue dans le contrat
     * @param enable augmentation ou diminution
     */
    public void enableAugmentationQuantiteCommande(boolean enable) {
        if(enable!=this.enableAugmentationQuantiteCommande) {
            //Si l'augmentation n'était pas déjà active/n'était pas déjà inactive
            Commande commande;
            if(enable) {
                for(int i=0; i<this.echeances.size(); ++i) {
                    for(int j=0; j<this.echeances.get(i).getListCommandes().size(); ++j) {
                        commande = this.echeances.get(i).getListCommandes().get(j);
                        commande.setQuantite((int)Math.round(commande.getQuantite()*(1+this.getAugmentationQuantiteCommande()/100)));
                    }
                }
                this.enableAugmentationQuantiteCommande = true;
            }
            else {
                for(int i=0; i<this.echeances.size(); ++i) {
                    for(int j=0; j<this.echeances.get(i).getListCommandes().size(); ++j) {
                        commande = this.echeances.get(i).getListCommandes().get(j);
                        commande.setQuantite((int)Math.round(commande.getQuantite()/(1+this.getAugmentationQuantiteCommande()/100)));
                    }
                }
                this.enableAugmentationQuantiteCommande = false;
            }
        }
    }

    /**
     * Retourne la liste des clients
     * @return liste des clients
     */
    public ArrayList<String> getClients () {
        return clients;
    }

    /**
     * Affecte la liste des clients
     * @param val liste des clients
     */
    public void setClients (ArrayList<String> val) {
        this.clients = val;
    }
    
    /**
     * Ajoute un nouveau client
     * @param client nouveau client
     */
    public void addClient(String client) {
        if(!this.clients.contains(client))
            this.clients.add(client);
    }

    /**
     * Retourne le cout horaire de l'usine
     * @return cout horaire en euros
     */
    public int getCoutUsineHeure () {
        return coutUsineHeure;
    }

    /**
     * Affecte le cout horaire de l'usine
     * @param val cout horaire en euros
     */
    public void setCoutUsineHeure (int val) {
        this.coutUsineHeure = val;
    }

    /**
     * Retourne la liste des échances de la configuration
     * @return liste des échéances
     */
    public ArrayList<Echeance> getEcheances () {
        return echeances;
    }

    /**
     * Affecte la liste des échances de la configuration
     * @param val liste des échéances
     */
    public void setEcheances (ArrayList<Echeance> val) {
        this.echeances = val;
    }

    /**
     * Nombre de bobines en cours dans l'usine
     * @return bobine en cours
     */
    public int getEnCoursBobine () {
        return enCoursBobine;
    }

    /**
     * Affecte le nombre de bobines en cours dans l'usine
     * @param val bobine en cours
     */
    public void setEnCoursBobine (int val) {
        this.enCoursBobine = val;
    }



    /**
     * Marge souhaité sur le contrat
     * @return marge en pourcentage
     */
    public double getMargeSouhaite () {
        return margeSouhaite;
    }

    /**
     * Change la marge souhaité sur le contrat
     * @param val marge en pourcentage
     */
    public void setMargeSouhaite (double val) {
        this.margeSouhaite = val;
    }

    /**
     * Retourne le nombre de boulons produits par bobine
     * @return nb de boulons par bobine
     */
    public int getNbBoulonsBobine () {
        return nbBoulonsBobine;
    }

    /**
     * Change le nombre de boulons produits par bobine
     * @param val nb de boulons par bobine
     */
    public void setNbBoulonsBobine (int val) {
        this.nbBoulonsBobine = val;
    }

    /**
     * Retourne le prix de la bobine
     * @return prix bobine
     */
    public int getPrixBobine () {
        return prixBobine;
    }
    
    /**
     * Retourne le prix d'une bobine par heure
     * @return prix bobine / heure
     */
    public long getPrixBobineHeure() {
        return Math.round(this.prixBobine/this.getTempsConstruction());
    }

    /**
     * Affecte le prix d'une bobine
     * @param val prix de la bobine
     */
    public void setPrixBobine (int val) {
        this.prixBobine = val;
    }

    /**
     * Retourne le stock max de bobine
     * @return stock maximum
     */
    public int getStockMaxBobine () {
        return stockMaxBobine;
    }

    /**
     *Retourne le stock max de bobine
     * @param val stock maximum
     */
    public void setStockMaxBobine (int val) {
        this.stockMaxBobine = val;
    }

    /**
     * Retourne le stock de sécurité (mini) de bobine
     * @return stock minimum
     */
    public int getStockMinBobine () {
        return stockMinBobine;
    }

    /**
     * Retourne le stock de sécurité (mini) de bobine
     * @param val stock minimum
     */
    public void setStockMinBobine (int val) {
        this.stockMinBobine = val;
    }

    /**
     * Retourne la liste des taches
     * @return liste des taches
     */
    public ArrayList<Integer> getTaches () {
        return taches;
    }

    /**
     * Affecte la liste des taches
     * @param val liste des taches
     */
    public void setTaches (ArrayList<Integer> val) {
        this.taches = val;
    }

    /**
    * Pour calculer le temps maximum on regarde quelle est la tâche critique
    * Et on ajoute le temps unitaire de production de toutes les autres tâches
    * au temps total de production de la tâche critique
    * 
    * @return le temps de construction
    */
    public double getTempsConstruction () {
        if(this.tempsConstruction==-1.0) {
            //Si le temps de construction n'a pas encore été calculé
            double max = (double)this.taches.get(0), index_max = 0;

            for(int i=1; i< this.taches.size(); ++i) {
                //Recherche de la tâche ayant la durée maximum
                if(this.taches.get(i)>max) {
                    max = (double) this.taches.get(i);
                    index_max = i;
                }
            }

            this.tempsConstruction = max;

            for(int i=0; i < this.taches.size(); ++i) {
                //Pour chaque autre tâche on ajoute le temps unitaire au temps de la tâche critique
                if(i!=index_max) {
                    this.tempsConstruction+=this.taches.get(i)/(double)this.nbBoulonsBobine;
                }
            }
        }
        return this.tempsConstruction;
    }

    /**
     * Retourne le temps de livraison d'une bobine
     * @return durée en jours
     */
    public int getTempsLivraisonBobine () {
        return tempsLivraisonBobine;
    }

    /**
     * Affecte le temps de livraison d'une bobine
     * @param val durée en jours
     */
    public void setTempsLivraisonBobine (int val) {
        this.tempsLivraisonBobine = val;
    }

    /**
     * Retourne le nombre d'heures de travail par jour
     * @return heures travaillées
     */
    public int getTravailHeureJour () {
        return travailHeureJour;
    }

    /**
     * Affecte le nombre d'heures de travail par jour
     * @param val heures travaillées
     */
    public void setTravailHeureJour (int val) {
        this.travailHeureJour = val;
    }

    /**
     * Retourne le nombre de jours de travail par semaine
     * @return nb jours de travail
     */
    public int getTravailJourSemaine () {
        return travailJourSemaine;
    }
    
    /**
     * Affecte le nombre de jours de travail par semaine
     * @param val nb jours de travail
     */
    public void setTravailJourSemaine (int val) {
        this.travailJourSemaine = val;
    }
    
    /**
     * Retourne la date de début de travail dans l'usine
     * @return date de debut
     */
    public Calendar getDateDebut() {
        return this.dateDebut;
    }
    
    /**
     * Affecte la date de début de travail dans l'usine
     * @param dateDebut date debut
     */
    public void setDateDebut(Calendar dateDebut) {
        this.dateDebut = (Calendar) dateDebut.clone();
    }
    
    /**
     * Affecte la date de début de travail dans l'usine
     * Exemple de format de date valide : 01-10-1969 pour représenter le 1er octobre 1969
     * @param dateDebut date debut
     */
    public void setDateDebut(String dateDebut) {
        this.dateDebut.clear();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("d-MM-y");
            this.dateDebut.setTime(sdf.parse(dateDebut));
        } catch (ParseException ex) {
            Logger.getLogger(Simulation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Retourne la date de fin de la production
     * @return date de fin production
     */
    public Calendar getDateFinProduction() {
        return this.dateFinProduction;
    }
    
    /**
     * Affecte la date de fin de la production
     * @param dateFinProduction date de fin
     */
    public void setDateFinProduction(Calendar dateFinProduction) {
        this.dateFinProduction = (Calendar) dateFinProduction.clone();
    }
    
    /**
     * Retourne la quantité totale commandée par un client X
     * @param client nom du client
     * @return nb boulons totals
     */
    public int getTotalQuantiteCommandeeClient(String client) {
        int res = 0;
        
        for(int i=0; i<this.echeances.size(); ++i) {
            res += this.echeances.get(i).getTotalQuantiteByClient(client);
        }
        
        return res;
    }
    
    /**
     * Parsing du fichier de configuration
     * 
     * @param filename chemin du fichier XML
     * @return true si réussite, false sinon
     */
    public static boolean parse(String filename, boolean isJarRessource)
    {
        try 
        {  
            
            SAXParserFactory fabrique = SAXParserFactory.newInstance();
            SAXParser parseur;
            parseur = fabrique.newSAXParser();
            
            System.out.println(">> Ouverture de la configuration : " + filename);
            
            if(isJarRessource)
            {
                InputStream inputStream = Configuration.class.getClassLoader().getResourceAsStream(filename);

                DefaultHandler gestionnaire = new ConfigurationHandler();
                parseur.parse(inputStream, gestionnaire);
            }
            else
            {
                File file = new File(filename);
                
                DefaultHandler gestionnaire = new ConfigurationHandler();
                parseur.parse(file, gestionnaire);
            }
            
            return true;
            
        } 
        catch (ParserConfigurationException ex) 
        {
            Logger.getLogger(Configuration.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (SAXException ex) 
        {
            System.err.println("ERREUR SEVERE: Votre fichier de configuration ne respecte pas la norme XML !");
            //Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (Exception ex) 
        {
            System.err.println("ERREUR SEVERE: Impossible d'ouvrir votre fichier de configuration !");
            //ex.printStackTrace();
            //Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
        }
            
        return false;
    }

    @Override
    public String toString () {
        String res = "";
        
        res += "##############\n";
        res += "##HYPOTHESES##\n";
        res += "##############\n\n";
        res += "On commande le nombre de bobines disponibles au maximum dans le stock\n";
        res += "La date de commande des bobines est estimée à l'heure près.\n";
        res += "Lors du chargement de la configuration, les dates des échéances 'crées' corrrespondent aux dates de commande soustraites au temps de livraison.\n";
        res += "En cas de réussite de l'ensemble des contrats, il y a une quantité de boulons supplémentaire qui a été produite. Elle correspond à la production de stockMaxBobine bobine(s) en trop (dû a la derniere commande)\n";
        
        res += "#################\n";
        res += "##CONFIGURATION##\n";
        res += "#################\n\n";
        res += this.nbBoulonsBobine + " boulons réalisés avec une bobine.\n";
        res += this.prixBobine + " euros par bobine.\n";
        res += this.travailHeureJour + " heure(s) de travail par jour.\n";
        res += this.travailJourSemaine + " jour(s) de travail par semaine.\n";
        res += this.enCoursBobine + " bobine(s) en cours d'utilisation.\n";
        res += this.stockMaxBobine + " bobine(s) au maximum dans le stock.\n";
        res += "Stock de sécurité de "+this.stockMinBobine + " bobine(s).\n";
        res += this.tempsLivraisonBobine + " jour(s) d'attente pour la livraison des bobines.\n";
        res += this.augmentationPrixAcierMois + "% d'augmentation du prix de l'acier par mois.\n";
        res += "Cout horaire de l'usine de " + this.coutUsineHeure + " euros par heure.\n";
        res += "Cout horaire de la matière première (bobines) au " 
                + this.dateDebut.get(Calendar.DAY_OF_MONTH) + "/" + (this.dateDebut.get(Calendar.MONTH)+1) + "/" + this.dateDebut.get(Calendar.YEAR) 
                + " : " + this.getPrixBobineHeure() + " euro(s) par heure.\n";
        res += this.augmentationQuantiteCommande + "% d'augmentation de la quantité commandée.\n";
        res += this.margeSouhaite + "% de marge souhaitée.\n";
        
        res += "####TACHES####\n";
        for(int i=0; i<this.taches.size(); ++i) {
            //Affichage des tâches de production
            res += "Durée de la ";
            
            if(i==0) res += "1ère tâche : ";
            else  res+= i+1+"ème tâche : ";
            
            res += this.taches.get(i) + " heure(s)\n";
        }
        
        for(int i=0; i<this.echeances.size(); ++i) {
            res += this.echeances.get(i).toString();
        }
        
        res += "\n##############\n";
        res += "##SIMULATION##\n";
        res += "##############\n";
                
        return res;
    }

}

