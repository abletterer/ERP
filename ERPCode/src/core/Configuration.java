package core;

import java.util.ArrayList; 

// <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
// #[regen=yes,id=DCE.5A66AA99-1892-3EC1-92BA-630C1C36914F]
// </editor-fold> 
public class Configuration {

    // <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
    // #[regen=yes,id=DCE.84C3A93D-F49C-7631-2D61-638E05400F44]
    // </editor-fold> 
    private static Configuration instance;

    // <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
    // #[regen=yes,id=DCE.8E19C5D4-611D-10DB-D301-C799DB6CAD09]
    // </editor-fold> 
    private ArrayList<Integer> taches;

    // <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
    // #[regen=yes,id=DCE.307BDA09-E95C-09F3-D345-0A5CA138DD86]
    // </editor-fold> 
    private int nbBoulonsBobine = 1000;

    // <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
    // #[regen=yes,id=DCE.291C9BD9-FBCB-ED0B-C1C4-12CE6C909790]
    // </editor-fold> 
    private int prixBobine = 10000;

    // <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
    // #[regen=yes,id=DCE.2BE02683-0A81-EEA6-DA08-FA8E5DDD2F99]
    // </editor-fold> 
    private int travailHeureJour = 7;

    // <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
    // #[regen=yes,id=DCE.E8C3145A-5B09-4176-2A7F-508EC06D6D06]
    // </editor-fold> 
    private int travailJourSemaine = 5;

    // <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
    // #[regen=yes,id=DCE.D5456896-9082-FBDD-A525-BFC3948C935B]
    // </editor-fold> 
    private int stockMinBobine = 1;

    // <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
    // #[regen=yes,id=DCE.6D5FF16F-0D3D-0526-DDE8-14AB14C5EC34]
    // </editor-fold> 
    private int stockMaxBobine = 2;

    // <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
    // #[regen=yes,id=DCE.99781F35-9AD3-0A8F-D84B-7F25301DDBC7]
    // </editor-fold> 
    private int enCoursBobine = 1;

    // <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
    // #[regen=yes,id=DCE.04134933-BA8C-C1B8-6AD2-E33EB6A2E172]
    // </editor-fold> 
    private int tempsLivraisonBobine = 10;

    // <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
    // #[regen=yes,id=DCE.E10E12CE-CD5B-56B7-67F4-372D18452522]
    // </editor-fold> 
    private double augmentationPrixAcierMois = 2.0;

    // <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
    // #[regen=yes,id=DCE.C16B5521-88E6-EF52-5F32-CB7962D59189]
    // </editor-fold> 
    private int coutUsineHeure = 1000;

    // <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
    // #[regen=yes,id=DCE.C63D093F-E2F7-0C67-975C-F6795B017D62]
    // </editor-fold> 
    private double augmentationQuantiteCommande = 10.0;

    // <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
    // #[regen=yes,id=DCE.EB53316E-5CF6-0D99-C82D-655766C3E146]
    // </editor-fold> 
    private double margeSouhaite = 70.0;

    // <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
    // #[regen=yes,id=DCE.F3F4B6E0-B54F-4EFD-A7FE-AC7F40F5904A]
    // </editor-fold> 
    private ArrayList<Echeance> echeances;

    // <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
    // #[regen=yes,id=DCE.9895685E-E702-4E12-DB7A-0E84D6C637E3]
    // </editor-fold> 
    private Configuration () {
    }

    // <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
    // #[regen=yes,regenBody=yes,id=DCE.70889475-2318-9B64-CF32-88D4B03C9C2F]
    // </editor-fold> 
    public static Configuration getInstance () {
        return instance;
    }

    // <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
    // #[regen=yes,regenBody=yes,id=DCE.B5A548F3-1065-A7C7-B80B-9C2F0CE5C2A5]
    // </editor-fold> 
    public double getAugmentationPrixAcierMois () {
        return augmentationPrixAcierMois;
    }

    // <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
    // #[regen=yes,regenBody=yes,id=DCE.271986B2-8267-5553-163C-4369D4034795]
    // </editor-fold> 
    public void setAugmentationPrixAcierMois (double val) {
        this.augmentationPrixAcierMois = val;
    }

    // <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
    // #[regen=yes,regenBody=yes,id=DCE.B206F314-0798-0097-9AED-0E5C0202D501]
    // </editor-fold> 
    public double getAugmentationQuantiteCommande () {
        return augmentationQuantiteCommande;
    }

    // <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
    // #[regen=yes,regenBody=yes,id=DCE.2F593C81-A7D9-54B0-3A25-03DF93599332]
    // </editor-fold> 
    public void setAugmentationQuantiteCommande (double val) {
        this.augmentationQuantiteCommande = val;
    }

    // <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
    // #[regen=yes,regenBody=yes,id=DCE.601CC076-1819-DB05-1731-147D7FA396AA]
    // </editor-fold> 
    public int getCoutUsineHeure () {
        return coutUsineHeure;
    }

    // <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
    // #[regen=yes,regenBody=yes,id=DCE.860AF4C8-0900-C625-B4B0-7272D2AA5D94]
    // </editor-fold> 
    public void setCoutUsineHeure (int val) {
        this.coutUsineHeure = val;
    }

    // <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
    // #[regen=yes,regenBody=yes,id=DCE.AF275071-F060-2B9D-5D7F-53C2C55ABE06]
    // </editor-fold> 
    public ArrayList<Echeance> getEcheances () {
        return echeances;
    }

    // <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
    // #[regen=yes,regenBody=yes,id=DCE.DB0CF52C-1F80-BF00-CDA4-E3C4025AEC72]
    // </editor-fold> 
    public void setEcheances (ArrayList<Echeance> val) {
        this.echeances = val;
    }

    // <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
    // #[regen=yes,regenBody=yes,id=DCE.B82E1E7A-FB74-9546-89C6-0553BCD61684]
    // </editor-fold> 
    public int getEnCoursBobine () {
        return enCoursBobine;
    }

    // <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
    // #[regen=yes,regenBody=yes,id=DCE.CD98E8D0-228D-3DF0-E331-B454B52F4673]
    // </editor-fold> 
    public void setEnCoursBobine (int val) {
        this.enCoursBobine = val;
    }

    // <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
    // #[regen=yes,regenBody=yes,id=DCE.37130B26-EDC5-B6CB-CB84-9B975E4E7E6B]
    // </editor-fold> 
    public void setInstance (Configuration val) {
        this.instance = val;
    }

    // <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
    // #[regen=yes,regenBody=yes,id=DCE.0F08E4FA-453E-3573-F526-00619BDE19BE]
    // </editor-fold> 
    public double getMargeSouhaite () {
        return margeSouhaite;
    }

    // <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
    // #[regen=yes,regenBody=yes,id=DCE.A10CF5BA-1A8F-B11B-CA8F-1568DA30EE73]
    // </editor-fold> 
    public void setMargeSouhaite (double val) {
        this.margeSouhaite = val;
    }

    // <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
    // #[regen=yes,regenBody=yes,id=DCE.736F05F6-89FB-1A2A-EE65-C67160D581EE]
    // </editor-fold> 
    public int getNbBoulonsBobine () {
        return nbBoulonsBobine;
    }

    // <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
    // #[regen=yes,regenBody=yes,id=DCE.103D9211-F1FE-B7C3-112D-F8264CD18BB7]
    // </editor-fold> 
    public void setNbBoulonsBobine (int val) {
        this.nbBoulonsBobine = val;
    }

    // <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
    // #[regen=yes,regenBody=yes,id=DCE.2A7FF1E8-39BF-9E9B-A617-9C2C2A568E9C]
    // </editor-fold> 
    public int getPrixBobine () {
        return prixBobine;
    }

    // <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
    // #[regen=yes,regenBody=yes,id=DCE.DDC050BE-674E-6FD2-87A4-B6A9D43108CB]
    // </editor-fold> 
    public void setPrixBobine (int val) {
        this.prixBobine = val;
    }

    // <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
    // #[regen=yes,regenBody=yes,id=DCE.39545AC9-C7D0-6F6B-7B88-DA04F079AF9A]
    // </editor-fold> 
    public int getStockMaxBobine () {
        return stockMaxBobine;
    }

    // <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
    // #[regen=yes,regenBody=yes,id=DCE.41434946-B9E7-1E85-20B4-F1D798343026]
    // </editor-fold> 
    public void setStockMaxBobine (int val) {
        this.stockMaxBobine = val;
    }

    // <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
    // #[regen=yes,regenBody=yes,id=DCE.6A74444D-C7F4-CC5C-9059-AA18F20ED0F8]
    // </editor-fold> 
    public int getStockMinBobine () {
        return stockMinBobine;
    }

    // <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
    // #[regen=yes,regenBody=yes,id=DCE.9A4F733F-262D-CEC8-DE82-D5C8DB61234D]
    // </editor-fold> 
    public void setStockMinBobine (int val) {
        this.stockMinBobine = val;
    }

    // <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
    // #[regen=yes,regenBody=yes,id=DCE.15BE9542-4F6C-0408-E198-12D14653DB8E]
    // </editor-fold> 
    public ArrayList<Integer> getTaches () {
        return taches;
    }

    // <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
    // #[regen=yes,regenBody=yes,id=DCE.FBEF6CBA-A8B8-BFD3-5A04-7BAED1D93BE0]
    // </editor-fold> 
    public void setTaches (ArrayList<Integer> val) {
        this.taches = val;
    }

    // <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
    // #[regen=yes,id=DCE.D8B60A83-DF3D-1CEC-52F1-04CDBC90C56C]
    // </editor-fold> 
    public int getTempsConstruction () {
        return 0;
    }

    // <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
    // #[regen=yes,regenBody=yes,id=DCE.7928F0EF-49BE-3A85-1CB6-F6B235756BF4]
    // </editor-fold> 
    public int getTempsLivraisonBobine () {
        return tempsLivraisonBobine;
    }

    // <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
    // #[regen=yes,regenBody=yes,id=DCE.8841AAC5-3AC7-FD45-A677-66481F44D75E]
    // </editor-fold> 
    public void setTempsLivraisonBobine (int val) {
        this.tempsLivraisonBobine = val;
    }

    // <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
    // #[regen=yes,regenBody=yes,id=DCE.ED7082EF-0823-A456-6CAA-46E2574A6DDF]
    // </editor-fold> 
    public int getTravailHeureJour () {
        return travailHeureJour;
    }

    // <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
    // #[regen=yes,regenBody=yes,id=DCE.4A20A1AA-FA9D-DF12-026C-66FEE1F5F87C]
    // </editor-fold> 
    public void setTravailHeureJour (int val) {
        this.travailHeureJour = val;
    }

    // <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
    // #[regen=yes,regenBody=yes,id=DCE.1F7FAD19-E5F9-2E0B-8143-FFFC6D5658DE]
    // </editor-fold> 
    public int getTravailJourSemaine () {
        return travailJourSemaine;
    }

    // <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
    // #[regen=yes,regenBody=yes,id=DCE.84BAC2D6-0AF3-8F3F-5B1E-372B3ED3CEF9]
    // </editor-fold> 
    public void setTravailJourSemaine (int val) {
        this.travailJourSemaine = val;
    }

    // <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
    // #[regen=yes,id=DCE.55975020-2BF5-4CA3-B761-40D9BE0371F1]
    // </editor-fold> 
    public String toString () {
        return null;
    }

}

