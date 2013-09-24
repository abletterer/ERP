/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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
	private Configuration result;
        private ArrayList<Integer> listTaches;
	private ArrayList<Echeance> listEcheance;

	private boolean inSimulation;
        private boolean inCommande;
        private boolean inEcheance;
        
        private Echeance currentEcheance;
        private String clientCommandeCourant;
        private int dureeExpeditionCourante;
	
	private StringBuffer buffer;




	public ConfigurationHandler()
        {
		super();
	}

        @Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
        {
		if(qName.equals("simulation"))
                {
			result = Configuration.getInstance();
			inSimulation = true;
		}
                else if(qName.equals("commande"))
                {
			inCommande = true;
                        clientCommandeCourant = attributes.getValue("client");
                        
                        try
                        {
                            dureeExpeditionCourante = Integer.parseInt(attributes.getValue("duree_expedition"));
			}
                        catch(Exception e)
                        {
                            throw new SAXException(e);
			}
                }
                else if(qName.equals("echeance"))
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
	            }
                    catch(Exception e)
                    {
                            throw new SAXException("La date de l'echeance n'est pas dans le format valide (DD/MM/YYYY)");
		    }
                    
                    inEcheance = true;    
		}
                else 
                {
                        buffer = new StringBuffer();
                        if(!qName.matches("^tache$|^date$|^quantite$|^prix$|^boulons$"
                                + "|^tempsLivraison$|^minStock$|^maxStock$|^enExploitation$"
                                + "|^heureTravail$|^jourTravail$|^pourcentageAugQuantite$"
                                + "|^coursMensuelAcier$|^margeSouhaite$"))
                        {
                            throw new SAXException("Balise ouvrante "+qName+" inconnue.");
			}
		}
	}
	
        
        @Override
	public void endElement(String uri, String localName, String qName) throws SAXException
        {
            if(inSimulation)
            {
                // Balise du premier niveau
                switch (qName) {
                    case "simulation":
                        this.result.setEcheances(listEcheance);
                        this.result.setTaches(listTaches);
                        inSimulation = false;
                        break;
                    case "tache":
                        try
                        {
                            this.listTaches.add(Integer.parseInt(buffer.toString()));
                        }
                        catch(Exception e)
                        {
                            throw new SAXException(e);
                        }
                        break;
                    case "commande":
                        clientCommandeCourant = "";
                        dureeExpeditionCourante = 0;
                        inCommande = false;
                        break;
                    case "bobine":
                        inBobine = false;
                        break;
                    case "divers":
                        inDivers = false;
                        break;
                    default:
                        throw new SAXException("Balise "+qName+" inconnue.");
                }
                
                // Balise du second niveau
                if(inCommande)
                {
                    switch (qName) 
                    {
                        case "echeance":
                            inEcheance = false;
                            break;
                            
                        case "quantite":
                            // Balise du troisieme niveau
                            if(inEcheance)
                            {
                                try
                                {
                                    this.currentEcheance.addCommande(this.clientCommandeCourant, Integer.parseInt(buffer.toString()));
                                }
                                catch( Exception e)
                                {
                                     throw new SAXException("La quantite d'une echeance doit etre un entier.");
                                }
                            }
                            else
                            {
                              throw new SAXException("Balise "+qName+" ignoree.");
                            }
                            break;
                            
                        default:
                            throw new SAXException("Balise "+qName+" inconnue.");
                    }
		}
                else if(inBobine)
                {
                    switch (qName) {
                        case "prix":
                            try
                            {
                                this.result.setPrixBobine(Integer.parseInt(buffer.toString()));
                            }
                            catch( Exception e)
                            {
                                 throw new SAXException("Le prix d'une bobine doit etre un entier.");
                            }
                            break;
                        case "boulons":
                            try
                            {
                                this.result.setNbBoulonsBobine(Integer.parseInt(buffer.toString()));
                            }
                            catch( Exception e)
                            {
                                 throw new SAXException("Le nombre de boulons d'une bobine doit etre un entier.");
                            }
                            break;
                        case "tempsLivraison":
                            try
                            {
                                this.result.setTempsLivraisonBobine(Integer.parseInt(buffer.toString()));
                            }
                            catch( Exception e)
                            {
                                 throw new SAXException("Le temps de livraison d'une bobine doit etre un entier.");
                            }
                            break;
                        case "minStock":
                            try
                            {
                                this.result.setStockMinBobine(Integer.parseInt(buffer.toString()));
                            }
                            catch( Exception e)
                            {
                                 throw new SAXException("Le stock minimal doit etre un entier.");
                            }
                            break;
                        case "maxStock":
                            try
                            {
                                this.result.setStockMaxBobine(Integer.parseInt(buffer.toString()));
                            }
                            catch( Exception e)
                            {
                                 throw new SAXException("Le stock maximal d'une bobine doit etre un entier.");
                            }
                            break;
                        case "enExploitation":
                            try
                            {
                                this.result.setEnCoursBobine(Integer.parseInt(buffer.toString()));
                            }
                            catch( Exception e)
                            {
                                 throw new SAXException("Le nb de bobine en exploitation doit etre un entier.");
                            }
                            break;
                        default:
                            throw new SAXException("Balise "+qName+"ignoree.");
                    }
		}
                else if(inDivers)
                {
			 switch (qName) {
                        case "heureTravail":
                            try
                            {
                                this.result.setTravailHeureJour(Integer.parseInt(buffer.toString()));
                            }
                            catch( Exception e)
                            {
                                 throw new SAXException("Le nombre d'heure de travail doit etre un entier.");
                            }
                            break;
                        case "jourTravail":
                            try
                            {
                                this.result.setTravailJourSemaine(Integer.parseInt(buffer.toString()));
                            }
                            catch( Exception e)
                            {
                                 throw new SAXException("Le nombre de jour de travail par semaine doit etre un entier.");
                            }
                            break;
                        case "tempsLivraison":
                            try
                            {
                                this.result.setTempsLivraisonBobine(Integer.parseInt(buffer.toString()));
                            }
                            catch( Exception e)
                            {
                                 throw new SAXException("Le temps de livraison d'une bobine doit etre un entier.");
                            }
                            break;
                        case "minStock":
                            try
                            {
                                this.result.setStockMinBobine(Integer.parseInt(buffer.toString()));
                            }
                            catch( Exception e)
                            {
                                 throw new SAXException("Le stock minimal doit etre un entier.");
                            }
                            break;
                        case "maxStock":
                            try
                            {
                                this.result.setStockMaxBobine(Integer.parseInt(buffer.toString()));
                            }
                            catch( Exception e)
                            {
                                 throw new SAXException("Le stock maximal d'une bobine doit etre un entier.");
                            }
                            break;
                        case "enExploitation":
                            try
                            {
                                this.result.setEnCoursBobine(Integer.parseInt(buffer.toString()));
                            }
                            catch( Exception e)
                            {
                                 throw new SAXException("Le nb de bobine en exploitation doit etre un entier.");
                            }
                            break;
                        default:
                            throw new SAXException("Balise "+qName+"ignoree.");
                    }
		}
               
            }
            else
            {
                    throw new SAXException("Balise "+qName+" ignoree.");
            }
	}
	//détection de caractères
	public void characters(char[] ch,int start, int length)
			throws SAXException{
		String lecture = new String(ch,start,length);
		if(buffer != null) buffer.append(lecture);       
	}
	//début du parsing
	public void startDocument() throws SAXException {
		System.out.println("Début du parsing");
	}
	//fin du parsing
	public void endDocument() throws SAXException {
		System.out.println("Fin du parsing");
		System.out.println("Resultats du parsing");
		for(Personne p : annuaire){
			System.out.println(p);
		}
	}
}
