package core;


// <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
// #[regen=yes,id=DCE.73AA51F6-5B3C-5C3D-4179-1F40411BD0DD]
// </editor-fold> 
public class Simulation {

    // <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
    // #[regen=yes,id=DCE.7D1C06D7-9455-8B1B-38E4-16E2E7DEAFB3]
    // </editor-fold> 
    private static Simulation instance;


    // <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
    // #[regen=yes,id=DCE.A086D108-AE9E-398D-5FBE-0D2B9792739A]
    // </editor-fold> 
    private Simulation () {
    }

    // <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
    // #[regen=yes,regenBody=yes,id=DCE.BE214756-D732-E155-548A-367395536393]
    // </editor-fold> 
    public static Simulation getInstance () 
    {
        if(Simulation.instance == null)
            Simulation.instance = new Simulation();
        
        return Simulation.instance;
    }

    // <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
    // #[regen=yes,id=DCE.AB0FFB05-C7CA-46DB-C914-F471ECBC7AC9]
    // </editor-fold> 
    public void simulate (String filename) 
    {
        // Configuration et execution de la simulation
        if(this.configure(filename))
            this.execute();
    }

    private void execute ()
    {
       this.processQ1(false);
    }

    private boolean configure (String filename) 
    {
        // Parsing du fichier de configuration XML
         if(Configuration.parse(filename))
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

    // <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
    // #[regen=yes,id=DCE.4B0C3A85-C202-60DC-9B4D-2C154A22AD73]
    // </editor-fold> 
    public void processQ1 (boolean useAugmentationQuantite) {
        System.out.println("Question 1)");
        System.out.println(useAugmentationQuantite?"\t # Avec augmentation de la commande client":"\t # Sans augmentation de la commande client");
        
        Configuration configuration1 = Configuration.getInstance();
        
        double test = configuration1.getTempsConstruction()/configuration1.getTravailHeureJour()
         * (configuration1.getStockMaxBobine()-configuration1.getStockMinBobine()+configuration1.getEnCoursBobine());
        
        System.out.println("Il faut commander 2 nouvelles bobines tous les "+test/24+" heure(s) depuis la dernière commande de bobine au maximum");
    }

    // <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
    // #[regen=yes,id=DCE.65AE5BA6-4AC6-AEB2-180B-B54AD5CC8DA6]
    // </editor-fold> 
    private void processQ2 (boolean useAugmentationQuantite) {
        System.out.println("Question 2)");
        System.out.println(useAugmentationQuantite?"\t # Avec augmentation de la commande client":"\t # Sans augmentation de la commande client");
        
    }

    // <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
    // #[regen=yes,id=DCE.1DF3DFFB-3524-EA22-F30B-517918B81FF1]
    // </editor-fold> 
    private void processQ3 () {
    }

    // <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
    // #[regen=yes,id=DCE.A57E9BCB-8230-4833-BB04-15A56F7409E4]
    // </editor-fold> 
    private void processQ4 () {
    }

}

