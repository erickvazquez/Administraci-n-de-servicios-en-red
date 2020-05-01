package x;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.security.NoSuchAlgorithmException;

public interface Suma extends Remote {
    int suma(String nombre,byte[] original) throws RemoteException,NoSuchAlgorithmException, IOException;
}
