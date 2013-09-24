package core;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.*;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


// <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
// #[regen=yes,id=DCE.EAB6C2F0-2794-579B-E679-5907CDE44183]
// </editor-fold> 
public class Parser 
{
        
    // <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
    // #[regen=yes,id=DCE.67D60A7D-3766-7281-15E8-1C750482A5FE]
    // </editor-fold> 
    private static Parser instance;

    // <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
    // #[regen=yes,id=DCE.1F191914-CC0C-8F44-EF70-5A7540AA6BB8]
    // </editor-fold> 
    private Parser () 
    {}

    // <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
    // #[regen=yes,regenBody=yes,id=DCE.57AFF2E7-160A-59A5-9177-BAC9C80DDC9C]
    // </editor-fold> 
    public static Parser getInstance () 
    {
        if(Parser.instance == null)
            Parser.instance = new Parser();
        
        return instance;
    }

    // <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
    // #[regen=yes,id=DCE.FE6B3FBF-CAAF-7204-1A62-2BD369EA275F]
    // </editor-fold> 
    public boolean parse (String filename)
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
            Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
        }
         catch (IOException ex) 
        {
            Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
        }
            
        return false;
    }

}

