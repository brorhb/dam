package serverKlient;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by Bror on 18.11.2016.
 */

public class Server {

    private Scanner scanner = new Scanner(System.in);
    public Thread thread;
    public ServerSocket serverSocket;

    int port = 22222;

    public Server() {
        nyServerSocket();
    }

    private void nyServerSocket() {
        System.out.println("skriv inn porten du ønsker serveren skal være på");
        port = scanner.nextInt(); //Henter inn innskrevet portnr
        try {
            serverSocket = new ServerSocket(port);
            thread = new Thread();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("nope, kan ikke lage ServerSocket");
        }
        klientLytter();
    }

    private void klientLytter() {
        try {
            Socket socket;
            System.out.println("Venter klienter...");
            while ((socket = serverSocket.accept()) != null) {
                new KommunikasjonsModul(socket);
                System.out.println("Ny spiller koblet til!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main (String[] args) {
        new Server();
    }

}
