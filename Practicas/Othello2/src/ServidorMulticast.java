

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;
import java.util.Random;

public class ServidorMulticast {

    public static void main(String args[]) {
        ArrayList<Servidor> servidores = new ArrayList<Servidor>();
        int puerto = 9000;
        Random rand = new Random();
        int cantidadServidores = rand.nextInt(10);
        System.out.println("La cantidad de servidores que se crearon es: " + cantidadServidores);
        for(int i = 0; i < cantidadServidores; i++){
            Servidor s = new Servidor(puerto);
            servidores.add(s);
            servidores.get(i).start();
            puerto++;
        }
        InetAddress gpo = null;
        try{
            MulticastSocket s = new MulticastSocket(9879);//puerto de la computadora donde se corre el servidor NOTA: se puede escuchar y enviar al mismo servidor
            s.setReuseAddress(true);//otras aplicaciones u otros hilos de la misma aplicacion pueden usar el servidor.
            s.setTimeToLive(1);//solo soportan un salto, hasta un routter siguiente
            //String msj = "Este es un mensaje del servidor";
            //String[] puertos = new String[cantidadServidores];
            String msj = "";
            for(int i = 0; i < cantidadServidores; i++){
                //puertos[i] = Integer.toString(servidores.get(i).getPort());
                msj += Integer.toString(servidores.get(i).getPort());
                msj += ".";
            }
            byte[] b = msj.getBytes();//el multicast solo funciona con datagramas
            gpo = InetAddress.getByName("228.1.1.1");//direccion clase D
            s.joinGroup(gpo);//aunque nosostros seamos el servidor nos debemos unir al grupo multicast, al igual que el cliente
            for(;;){
                DatagramPacket p = new DatagramPacket(b, b.length, gpo, 9999);//puerto a donde se envian los datagramas
                s.send(p);
                System.out.println("Enviamos mensaje " + msj + " Con un TTL = " + s.getTimeToLive());
                try{
                    Thread.sleep(3000);//cada tres segundos se va a enciar el mismo mensaje porque se manada a dormir el servidor tres segundos
                }catch(InterruptedException ie){
                    ie.printStackTrace();
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

}
