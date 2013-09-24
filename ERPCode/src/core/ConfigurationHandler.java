
package core;

import java.util.ArrayList;
import java.util.Calendar;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author jhaehnel
 */

public class ConfigurationHandler extends DefaultHandler 
{
	private Configuration configuration;

	private boolean inSimulation;
        private boolean inCommande;
        private boolean inEcheance;
        
        private Echeance currentEcheance;
        private String clientCommandeCourant;
        private int dureeExpeditionCourante;
	private ArrayList<Integer> listTaches;
	private ArrayList<Echeance> listEcheance;
        
	private StringBuffer buffer;

	public ConfigurationHandler()
        {
		super();
	}

        @Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
        {
		if(qName.equals("simulation")) // Balise ouvrante de premier niveau
                {
                        // Initalisation de la configuration et des listes
			configuration = Configuration.getInstance();
                        listTaches = new ArrayList<Integer>();
                        listEcheance = new ArrayList<Echeance>();
                        
			inSimulation = true;
		}
                else if(inSimulation) // Balise ouvrante de second niveau
                {
                    if(qName.equals("commande"))
                    {
                            inCommande = true;
                            clientCommandeCourant = attributes.getValue("client");

                            try
                            {
                                dureeExpeditionCourante = Integer.parseInt(attributes.getValue("duree_expedition"));
                            }
                            catch(Exception e)
                            {
                                System.err.println("Error: La durée d'expedition doit être un entier.");
                            }
                    }
                    else if(inCommande) // Balise ouvrante de troisieme niveau
                    {
                        if(qName.equals("echeance"))
                        {

                            try
                            {
                                String[] dateSplit = (attributes.getValue("date")).split("/");

                                // Construction de la date de l'echeance
                                Calendar date = Calendar.getInstance();
                                date.set(Integer.parseInt(dateSplit[2]), Integer.parseInt(dateSplit[1]), Integer.parseInt(dateSplit[0]));
                                date.add(Calendar.DATE, dureeExpeditionCourante); 

                                // Recherche d'un echéance deja existante
                                this.currentEcheance = null;
                                for(int i = 0; i < listEcheance.size(); i++)
                                {
                                    if(date.equals(listEcheance.get(i).getDate()))
                                        this.currentEcheance = listEcheance.get(i); break;
                                }

                                // Création si elle n'existe pas
                                if(this.currentEcheance == null)
                                {
                                    this.currentEcheance = new Echeance(date);
                                    this.listEcheance.add(currentEcheance);
                                }

                                inEcheance = true;   
                            }
                            catch(Exception e)
                            {
                                    System.err.println("Error: La date de l'echeance n'est pas dans le format valide (DD/MM/YYYY)");
                            }
                        }
                        else if(inEcheance)
                        {
                            buffer = new StringBuffer();
                            if(!qName.equals("quantite"))
                            {
                                System.err.println("Error: Balise ouvrante "+qName+" inconnue.");
                            }
                        }
                        else
                        {
                            System.err.println("Error: Balise ouvrante "+qName+" inconnue.");
                        }
                    }
                    else 
                    {
                            buffer = new StringBuffer();
                            if(!qName.matches("^tache$|^date$|^prix$|^boulons$"
                                    + "|^tempsLivraison$|^minStock$|^maxStock$|^enExploitation$"
                                    + "|^heureTravail$|^coutUsineHeure$|^jourTravail$|^pourcentageAugQuantite$"
                                    + "|^coursMensuelAcier$|^margeSouhaite$"))
                            {
                                System.err.println("Error: Balise ouvrante "+qName+" inconnue.");
                            }
                    }
                }
                else
                {
                    System.err.println("Error: Balise ouvrante "+qName+" ignoree.");
                }
	}
	
        
        @Override
	public void endElement(String uri, String localName, String qName) throws SAXException
        {
            if(inSimulation) // Balise fermante du premier niveau
            {
                switch (qName) 
                {
                    case "simulation":
                        // Rajout des echéances et des taches dans la simulation
                        this.configuration.setEcheances(listEcheance);
                        this.configuration.setTaches(listTaches);
                        
                        inSimulation = false;
                        break;
                    case "tache":
                        try
                        {
                            this.listTaches.add(Integer.parseInt(buffer.toString()));
                        }
                        catch(Exception e)
                        {
                            System.err.println("Error: La tache doit être un entier.");
                        }
                        break;
                    case "commande":
                        inCommande = false;
                        break;
                 case "prix":
                        try
                        {
                            this.configuration.setPrixBobine(Integer.parseInt(buffer.toString()));
                        }
                        catch( Exception e)
                        {
                             System.err.println("Error: Le prix d'une bobine doit etre un entier.");
                        }
                        break;
                    case "boulons":
                        try
                        {
                            this.configuration.setNbBoulonsBobine(Integer.parseInt(buffer.toString()));
                        }
                        catch( Exception e)
                        {
                             System.err.println("Error: Le nombre de boulons d'une bobine doit etre un entier.");
                        }
                        break;
                    case "tempsLivraison":
                        try
                        {
                            this.configuration.setTempsLivraisonBobine(Integer.parseInt(buffer.toString()));
                        }
                        catch( Exception e)
                        {
                             System.err.println("Error: Le temps de livraison d'une bobine doit etre un entier.");
                        }
                        break;
                    case "minStock":
                        try
                        {
                            this.configuration.setStockMinBobine(Integer.parseInt(buffer.toString()));
                        }
                        catch( Exception e)
                        {
                             System.err.println("Error: Le stock minimal doit etre un entier.");
                        }
                        break;
                    case "maxStock":
                        try
                        {
                            this.configuration.setStockMaxBobine(Integer.parseInt(buffer.toString()));
                        }
                        catch( Exception e)
                        {
                             System.err.println("Error: Le stock maximal d'une bobine doit etre un entier.");
                        }
                        break;
                    case "enExploitation":
                        try
                        {
                            this.configuration.setEnCoursBobine(Integer.parseInt(buffer.toString()));
                        }
                        catch( Exception e)
                        {
                             System.err.println("Error: Le nb de bobine en exploitation doit etre un entier.");
                        }
                        break;
                    case "heureTravail":
                        try
                        {
                            this.configuration.setTravailHeureJour(Integer.parseInt(buffer.toString()));
                        }
                        catch( Exception e)
                        {
                             System.err.println("Error: Le nombre d'heure de travail doit etre un entier.");
                        }
                        break;
                    case "jourTravail":
                        try
                        {
                            this.configuration.setTravailJourSemaine(Integer.parseInt(buffer.toString()));
                        }
                        catch( Exception e)
                        {
                             System.err.println("Error: Le nombre de jour de travail par semaine doit etre un entier.");
                        }
                        break;
                    case "coutUsineHeure":
                        try
                        {
                            this.configuration.setCoutUsineHeure(Integer.parseInt(buffer.toString()));
                        }
                        catch( Exception e)
                        {
                             System.err.println("Error: Le cout par heure de l'usine doit etre un entier.");
                        }
                        break;
                    case "pourcentageAugQuantite":
                        try
                        {
                            this.configuration.setAugmentationQuantiteCommande(Double.parseDouble(buffer.toString()));
                        }
                        catch( Exception e)
                        {
                             System.err.println("Error: Le pourcentage d'augmentation de la production doit etre un flottant.");
                        }
                        break;
                    case "coursMensuelAcier":
                        try
                        {
                            this.configuration.setAugmentationPrixAcierMois(Double.parseDouble(buffer.toString()));
                        }
                        catch( Exception e)
                        {
                             System.err.println("Error: Le cours mensuel de l'acier doit etre un flottant.");
                        }
                        break;
                    case "margeSouhaite":
                        try
                        {
                            this.configuration.setMargeSouhaite(Double.parseDouble(buffer.toString()));
                        }
                        catch( Exception e)
                        {
                             System.err.println("Error: La marge souhaitee doit être un flottant");
                        }
                        break;
                    default:
                        if(inCommande) // Balise ouvrante de second niveau
                        {
                            if(qName.equals("echeance"))
                            {
                                inEcheance = false;
                            }
                            else if(inEcheance) // Balise ouvrante de troisieme niveau
                            {
                                if(qName.equals("quantite"))
                                {
                                    try
                                    {
                                        this.currentEcheance.addCommande(this.clientCommandeCourant, Integer.parseInt(buffer.toString()));
                                    }
                                    catch( Exception e)
                                    {
                                         System.err.println("Error: La quantite d'une echeance doit etre un entier.");
                                    }
                                }
                                else
                                {
                                  System.err.println("Error: Balise fermante "+qName+"inconnue.");
                                }
                            }
                        }
                        else
                        {
                            System.err.println("Error: Balise fermante "+qName+" inconnue.");
                        }
                }

            }
            else
            {
                    System.err.println("Error: Balise fermante "+qName+" ignoree.");
            }
	}
        
        @Override
	public void characters(char[] ch,int start, int length) throws SAXException
        {
		String lecture = new String(ch,start,length);
		if(buffer != null) 
                    buffer.append(lecture);       
	}
        
        @Override
	public void startDocument() throws SAXException 
        {
		System.out.println(">> Début du parsing");
	}

        
        @Override
	public void endDocument() throws SAXException 
        {
		System.out.println(">> Fin du parsing");
	}
}
