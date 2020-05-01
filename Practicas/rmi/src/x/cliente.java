
package x;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.util.Arrays;
import javax.swing.JFileChooser;

public class cliente {

    private cliente() {}

    public static void main(String[] args) {

	String host = (args.length < 1) ? null : args[0];
	try {
		
	    Registry registry = LocateRegistry.getRegistry(host);	
            	   //también puedes usar getRegistry(String host, int port)
	    
	    Suma stub = (Suma) registry.lookup("Suma");
	    
            JFileChooser jf=new JFileChooser();
            int r=jf.showOpenDialog(null);
            if(r==JFileChooser.APPROVE_OPTION){
                  File f=jf.getSelectedFile();//Manejador
                  String nombre=f.getName();//Nombre
                  String archivo=f.getAbsolutePath();

              MessageDigest md = MessageDigest.getInstance("MD5");
              md.update(Files.readAllBytes(Paths.get(archivo)));
              byte[] digest = md.digest();
              System.out.println(Arrays.toString(digest));
              
              int response = stub.suma(nombre,digest);
              if(response==1){
                System.out.println("El archivo fue encontrado en el servidor.");
              }else{                  
                System.out.println("El archivo no fue encontrado en el servidor.");
              }
	    }
	} catch (Exception e) {
	    System.err.println("Excepción del cliente: " + e.toString());
	    e.printStackTrace();
	}
    }
}
