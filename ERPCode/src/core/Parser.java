package core;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.*;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


public class Parser 
{
    public static boolean parse (String filename)
    {
        try 
        {  
            SAXParserFactory fabrique = SAXParserFactory.newInstance();
            SAXParser parseur;
            parseur = fabrique.newSAXParser();
            
            File fichier = new File(filename);
            DefaultHandler gestionnaire = new ConfigurationHandler();
            parseur.parse(fichier, gestionnaire);
            
            return true;
            
        } 
        catch (ParserConfigurationException ex) 
        {
            Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (SAXException ex) 
        {
            System.err.println("ERREUR SEVERE: Votre fichier de configuration ne respecte pas la norme XML !");
            //Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
        }
         catch (IOException ex) 
        {
            System.err.println("ERREUR SEVERE: Impossible d'ouvrir votre fichier de configuration !");
            //Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
        }
            
        return false;
    }

}

