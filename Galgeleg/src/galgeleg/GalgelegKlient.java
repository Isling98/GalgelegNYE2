package galgeleg;

import java.rmi.Naming;
import java.util.Scanner;
import brugerautorisation.transport.rmi.Brugeradmin;

public class GalgelegKlient {

    public static void main(String[] args) throws Exception{
        GalgeInterface galgeInterface = (GalgeInterface) Naming.lookup("rmi://localhost:1099/GalgelegServer");




        Scanner scan = new Scanner(System.in);
        boolean active = true;
        String gæt;
        String bruger;
        String kodeord;
        Boolean adgangNægtet = true;

        while(adgangNægtet) {
            System.out.println("Bruger:");
            bruger = scan.next();
            System.out.println("Adgangskode:");
            kodeord = scan.next();

            try{
                Brugeradmin ba = (Brugeradmin) Naming.lookup("rmi://javabog.dk/brugeradmin");
                ba.hentBruger(bruger, kodeord);
                adgangNægtet = false;
                System.out.println("Bruger godkendt");
            } catch (Exception e){
                //e.printStackTrace();
                System.out.println("Bruger ikke godkendt\n" + "Prøv igen.");
            }
        }

        while (active){
            System.out.println("Indtast dit bogstav: ");
            gæt = scan.next();
            galgeInterface.gætBogstav(gæt);
            System.out.println(galgeInterface.klientOutput());
            galgeInterface.logStatus();

            if (galgeInterface.erSpilletSlut()){
                active = false;
            }
        }
        System.out.println("Spillet er slut og forbindelsen lukkes.");
    }
}