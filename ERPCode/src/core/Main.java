package core;

/**
 * Classe main
 * 
 * @author Haehnel Jonathan & Arnaud Bletterer
 * @version 1.0
 * @since 12/10/2013
 */
public class Main 
{
  
    /**
     * Main du programme
     * @param args argument facultatif (chemin vers un fichier de configuration)
     */
    public static void main (String[] args) 
    {
        // Chemin de la configuration
        String configFile = "ressources/simulation_exemple.xml"; // par defaut
        boolean isJarRessource = true;
        if(args.length > 0)
        {
           configFile = args[0];
           isJarRessource = false;
        }
        
        // Lancement de la simulation
         Simulation sim = Simulation.getInstance();
            sim.simulate(configFile, isJarRessource);
    }
}

