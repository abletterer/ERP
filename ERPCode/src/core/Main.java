package core;


public class Main 
{
  
    public static void main (String[] args) 
    {
        if(Parser.parse("./src/ressources/simulation_exemple.xml"))
        {
            
            System.out.println(">> Configuration chargée !");
            Configuration conf = Configuration.getInstance();
            
            // Verification que le configuration est valide
            if(conf.getEcheances().size() > 0 && conf.getTaches().size() > 0)
            {
                System.out.println(">> Configuration valide !");
                System.out.println();
                System.out.println(Configuration.getInstance().toString());
            }
            else
            {
                 System.err.println(">> Configuration non valide !");
            }
        }
        else
        {
            System.err.println(" >> Echec de chargement de la configuration ! Exit...");
        }
    }
}

