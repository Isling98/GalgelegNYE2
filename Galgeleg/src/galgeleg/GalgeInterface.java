package galgeleg;

public interface GalgeInterface extends java.rmi.Remote {
    void nulstil() throws java.rmi.RemoteException;
    void gætBogstav(String bogstav) throws java.rmi.RemoteException;
    boolean erSpilletSlut() throws java.rmi.RemoteException;
    void logStatus() throws java.rmi.RemoteException;
    String klientOutput() throws java.rmi.RemoteException;
}