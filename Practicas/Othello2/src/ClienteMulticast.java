/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;
import javax.swing.JFileChooser;
import javax.swing.UIManager;

public class ClienteMulticast {


    public static void main(String[] args) throws IOException {//throws IOException {
        InetAddress gpo = null;
        try {
            MulticastSocket cl = new MulticastSocket(9999);
            cl.setLoopbackMode(false);
            System.out.println("Cliente escuchando puerto" + cl.getLocalPort());
            cl.setReuseAddress(true);
            //////////////////////////////////////////////////////////////////// control sobre el numero del puerto
            try {
                gpo = InetAddress.getByName("228.1.1.1");
            } catch (UnknownHostException u) {
                System.out.println("Direccion no valida");
            }
            /////////////////////////////////////////////////////////////////////
            cl.joinGroup(gpo);
            System.out.println("Unido al grupo");
            for (;;) {
                DatagramPacket p = new DatagramPacket(new byte[50], 50);
                cl.receive(p);
                String msj = new String(p.getData());
                System.out.println("Datagram recibido..." + msj);
                System.out.println("Servidor descubierto: " + p.getAddress() + ":" + p.getPort());
                String[] puertos = msj.split("\\.");
                System.out.println(puertos.length - 1);

                for (int n = 0; n < puertos.length - 1; n++) {
                    System.out.println("puerto: " + puertos[n]);
                    InetAddress srv = InetAddress.getByName("127.0.0.1");
                    Socket cliente = new Socket(srv, Integer.parseInt(puertos[n]));
                    PrintWriter pw = new PrintWriter(new OutputStreamWriter(cliente.getOutputStream()));
                    BufferedReader br = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
                    BufferedReader br2 = new BufferedReader(new InputStreamReader(System.in));
                    String estado = br.readLine();
                    System.out.println(estado);

                    if (estado.equals("ACEPTADO")) {
                        //////////////////////////////////////////Generacion de matriz

                        /////////////////////////////////////////////////////////////////
                        System.out.println("Cliente conectado al servidor con el puerto " + puertos[n]);
                        int myX, myY, yourX, yourY;
                        boolean pintaPrimero = true;
                        String linea2 = "";
                        String posicion = "";
                        String mensajeContrario = "";
                        String respuesta = "";
                        try {
                            UIManager.setLookAndFeel(
                                    "com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
                        } catch (Exception e) {
                        }
                        Othello app = new Othello();
                        posicion = br.readLine();
                        for (;;) {
                            try {
                                if (posicion.equals("primero")) {
                                    System.out.println("Tu turno");
                                    app.setTurn(true);
                                    app.setEsprimero(true);
                                    while (app.getTurn()) {
                                        System.out.print("");
                                    }
                                    myX = app.getMyx();
                                    myY = app.getMyy();
                                    System.out.println(myX + " " + myY);
                                    pw.println(Integer.toString(myX));
                                    pw.println(Integer.toString(myY));
                                    pw.flush();
                                    System.out.println("Turno del oponente");
                                    app.setTurn(false);
                                    /* while (!app.getTurn()) {
                            System.out.print("2");
                        }*/
                                    yourX = Integer.parseInt(br.readLine());
                                    yourY = Integer.parseInt(br.readLine());
                                    app.setYourx(yourX);
                                    app.setYoury(yourY);
                                    app.setTurn(true);

//////////////////////////////////////////////////////////////////////////////////////////7
                                } else if (posicion.equals("segundo")) {
                                    System.out.println("Turno del oponente");
                                    app.setEsprimero(false);
                                    app.setTurn(false);
                                    if (pintaPrimero) {
                                        yourX = Integer.parseInt(br.readLine());
                                        yourY = Integer.parseInt(br.readLine());
                                        System.out.println(yourX + " " + yourY);
                                        app.setYourx(yourX);
                                        app.setYoury(yourY);
                                        app.pintarPrimero();
                                    }
                                    pintaPrimero = false;
                                    System.out.println("Tu turno");
                                    app.setTurn(true);
                                    while (app.getTurn()) {
                                        System.out.print("");
                                    }
                                    myX = app.getMyx();
                                    myY = app.getMyy();
                                    System.out.println("Antes del pw" + myX + ", " + myY);
                                    pw.println(Integer.toString(myX));
                                    pw.println(Integer.toString(myY));
                                    pw.flush();
                                    //pw.write("true");
                                    System.out.println("");
                                    app.setTurn(false);
                                    /* while(!app.getTurn()){
                           // System.out.print("2");
                        }*/
                                    yourX = Integer.parseInt(br.readLine());
                                    yourY = Integer.parseInt(br.readLine());
                                    app.setYourx(yourX);
                                    app.setYoury(yourY);
                                    app.setTurn(true);
                                }
                            } catch (Exception ie) {
                                System.out.println("Ganaste");
                                return;
                            }
                            // String eco = br.readLine();
                            //System.out.println("ECO: "+eco);

                        }//for
                    } else {
                        System.out.println("Cliente rechazado para el servidor con el puerto " + puertos[n]);
                    }

                }
                try {
                    Thread.sleep(3000);//cada tres segundos se va a enciar el mismo mensaje porque se manada a dormir el servidor tres segundos
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
