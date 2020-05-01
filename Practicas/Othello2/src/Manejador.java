/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

class Manejador extends Thread {

    Socket cl;
    Socket cl2;
    int i = 0;

    public Manejador(Socket cl, Socket cl2) {
        this.cl = cl;
        this.cl2 = cl2;
    }//constructor

    public void run() {
       try {
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(cl.getOutputStream()));
            BufferedReader br = new BufferedReader(new InputStreamReader(cl.getInputStream()));
            String linea = "";
            PrintWriter pw2 = new PrintWriter(new OutputStreamWriter(cl2.getOutputStream()));
            BufferedReader br2 = new BufferedReader(new InputStreamReader(cl2.getInputStream()));
            String linea2 = "";
            
            for (;;) {
                try{
                if (i < 1) {
                    pw.println("primero");
                    pw2.println("segundo");
                    pw.flush();
                    pw2.flush();
                    i++;
                }
                linea = br.readLine();
                linea2 = br.readLine();
                System.out.println("Coordenadas jugador1: " + linea + " " + linea2);
                pw2.println(linea);
                pw2.println(linea2);
                pw2.flush();
                linea = br2.readLine();
                linea2 = br2.readLine();
                System.out.println("Coordenadas jugador2: " + linea + " " + linea2);
                pw.println(linea);
                pw.println(linea2);
                pw.flush();
                }catch(Exception e){
                }
            }//for
        } catch (IOException io) {
            io.printStackTrace();
        }//catch
    }//run
}
