package galgeleg;

import brugerautorisation.transport.rmi.Brugeradmin;

import javax.xml.ws.Endpoint;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;


public class GalgelegServer {

    public static void main(String[] args) throws RemoteException, MalformedURLException {
        //java.rmi.registry.LocateRegistry.createRegistry(1099);
        Galgelogik galgelogik = new Galgelogik();


        //Naming.rebind("rmi://localhost/brugeradmin", );

        Endpoint.publish("http://[::]:9979/galgeleg", galgelogik);
        System.out.println("Galgelegs server er opstartet");

        galgelogik.nulstil();
    }
}