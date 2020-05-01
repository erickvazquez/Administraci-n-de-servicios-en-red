package x;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.*;
import java.util.Arrays;
//import Suma.*;
	
public class servidor implements Suma {
	
    public servidor() {}

    public int suma(String nombre,byte[] original) throws NoSuchAlgorithmException, IOException {
        File f=new File(nombre);
        String archivo=f.getAbsolutePath();
        if(f.exists() && f.isFile()){
              MessageDigest md = MessageDigest.getInstance("MD5");
              md.update(Files.readAllBytes(Paths.get(archivo)));
              byte[] digest = md.digest();
              System.out.println(Arrays.toString(digest));
              if(Arrays.toString(digest).equals(Arrays.toString(original))){
                  return 1;
              }
        }
        return 0;
    }
	
    public static void main(String args[]) {
	try {
            java.rmi.registry.LocateRegistry.createRegistry(1099); //puerto default del rmiregistry
            System.out.println("RMI registro listo.");
	} catch (Exception e) {
            System.out.println("Excepcion RMI del registry:");
            e.printStackTrace();
	  }//catch

	try {
            System.setProperty("java.rmi.server.codebase","file:/c:/Temp/Suma/");
	    servidor obj = new servidor();
	    Suma stub = (Suma) UnicastRemoteObject.exportObject(obj, 0);

	    // Ligamos el objeto remoto en el registro
	    Registry registry = LocateRegistry.getRegistry();
	    registry.bind("Suma", stub);

	    System.out.println("Servidor listo...");
	} catch (Exception e) {
	    System.err.println("Excepción del servidor: " + e.toString());
	    e.printStackTrace();
	}
    }
}

