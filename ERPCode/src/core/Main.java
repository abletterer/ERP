package core;


public class Main 
{
  
    public static void main (String[] args) 
    {
       Simulation sim = Simulation.getInstance();
       sim.simulate("./src/ressources/simulation_exemple.xml");
    }
}

