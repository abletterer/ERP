package core;


public class Main 
{
    public static void main (String[] args) 
    {
        if(Parser.parse("./src/ressources/simulation_exemple.xml"))
        {
            System.out.println("Configuration chargée !");
        }
        else
        {
            System.out.println("Echec de chargement de la configuration ! Exit...");
        }
    }
}

