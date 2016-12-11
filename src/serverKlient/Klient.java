package serverKlient;

import spill.DamSpill;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by Bror on 08.12.2016.
 */
public class Klient implements Runnable {

    int port = 22222;
    String ip = "127.0.0.1";
    Socket socket;
    private DataOutputStream dos;
    private DataInputStream dis;
    public Thread thread;
    private Scanner scanner = new Scanner(System.in);

    public Klient() throws IOException, ClassNotFoundException {
        forbindelse();
    }

    public static void main (String[] args) throws IOException, ClassNotFoundException {
        new Klient();
    }

    public boolean forbindelse() throws ClassNotFoundException {
            System.out.println("boooom");
            try {
                System.out.println("Skriv inn ip-adressen til serveren");
                ip = scanner.nextLine(); //Henter inn info som er skrevet i konsoll.
                System.out.println("skriv inn en porten til serveren");
                port = scanner.nextInt(); //Henter inn innskrevet portnr
                socket = new Socket(ip, port);
                dos = new DataOutputStream(socket.getOutputStream());

                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                System.out.println("Jeg kjører");
                DamSpill spillStatus = (DamSpill) objectInputStream.readObject();
                spillStatus.LagOgVisGUI();

                dis = new DataInputStream(socket.getInputStream());
                System.out.println("greaaaaat success?");
                thread = new Thread();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        return true;

    }

    public DataInputStream getDataInputStream() {
        return dis;
    }

    public DataOutputStream getDataOutputStream() {
        return dos;
    }


    @Override
    public void run() {
        try {
            new KommunikasjonsModul(socket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
