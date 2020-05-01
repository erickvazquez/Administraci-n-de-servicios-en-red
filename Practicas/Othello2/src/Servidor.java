

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Servidor extends Thread{

    int port, hilos, clientes = 0;
    ArrayList<Socket> sockets = new ArrayList<Socket>();

    public Servidor(int port) {
        this.port = port;
    }

    public void run() {
        try {
            ServerSocket s = new ServerSocket(port);
            System.out.println("Servidor listo en el puerto " + s.getLocalPort());
            for (;;) {
                //while (sockets.size() <= 0 || (sockets.size() % 2) != 0) {
                //}
                if (hilos < 2) {
                    Socket cl = s.accept();
                    //sockets.add(clientes, cl);
                    clientes++;
                    PrintWriter pw = new PrintWriter(new OutputStreamWriter(cl.getOutputStream()));
                    String estado = "ACEPTADO";
                    System.out.println("Cliente aceptado");
                    pw.println(estado);
                    pw.flush();
                    Socket cl2 = s.accept();
                    //sockets.add(clientes, cl2);
                    clientes++;
                    PrintWriter pw2 = new PrintWriter(new OutputStreamWriter(cl2.getOutputStream()));
                    String estado2 = "ACEPTADO";
                    pw2.println(estado2);
                    pw2.flush();
                    System.out.println("Usuarios conectados..\n");
                    Manejador m = new Manejador(cl, cl2);
                    hilos++;
                    System.out.println("Cantidad actual de hilos: " + hilos);
                    m.start();
                } else {
                    Socket cl = s.accept();
                    PrintWriter pw = new PrintWriter(new OutputStreamWriter(cl.getOutputStream()));
                    String estado = "RECHAZADO";
                    System.out.println("Cliente Rechazao");
                    pw.println(estado);
                    pw.flush();
                    Socket cl2 = s.accept();
                    PrintWriter pw2 = new PrintWriter(new OutputStreamWriter(cl2.getOutputStream()));
                    String estado2 = "RECHAZADO";
                    pw2.println(estado2);
                    pw2.flush();
                    System.out.println("Cliente Rechazao");
                    //break;
                }
            }//for
        } catch (Exception e) {
            e.printStackTrace();
        }//catch
    }

    public boolean disponibilidad() {
        if(hilos <= 2) return true;
        else return false;
    }

    public void agregarUsuario(Socket cl) {
        sockets.add(cl);
    }

    public int getPort() {
        return port;
    }

}
