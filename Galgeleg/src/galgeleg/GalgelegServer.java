package galgeleg;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;


public class GalgelegServer {

    public static void main(String[] args) throws RemoteException, MalformedURLException {
        java.rmi.registry.LocateRegistry.createRegistry(1099);
        GalgeInterface galgeInterface = new Galgelogik();

        Naming.rebind("rmi://localhost:1099/GalgelegServer", galgeInterface);
        System.out.println("Galgelegs server er opstartet");
    }
}