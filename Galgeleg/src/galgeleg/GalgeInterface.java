package galgeleg;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public interface GalgeInterface extends java.rmi.Remote {
    @WebMethod public void nulstil() throws java.rmi.RemoteException;
    @WebMethod public void gætBogstav(String bogstav) throws java.rmi.RemoteException;
    @WebMethod public boolean erSpilletSlut() throws java.rmi.RemoteException;
    @WebMethod public void logStatus() throws java.rmi.RemoteException;
    @WebMethod public String klientOutput() throws java.rmi.RemoteException;

}