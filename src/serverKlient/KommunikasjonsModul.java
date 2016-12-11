package serverKlient;

import spill.SimpleCheckersGUI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Bror on 08.12.2016.
 * Klasse for å holde på spill informasjon
 */
public class KommunikasjonsModul implements Runnable {

    Socket socket;
    private PrintWriter output;
    private BufferedReader input;
    boolean spiller1tur = false;
    boolean spiller2tur = false;

    public KommunikasjonsModul(Socket klientSocket) throws IOException {
        spiller1tur = true; //Sørger for at spiller 1 får første trekk
        this.socket = klientSocket;


        //Dette skal i teori kunne sende spillet fra en klient til en annen.
        SimpleCheckersGUI spillBrett = new SimpleCheckersGUI();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(klientSocket.getOutputStream());
        objectOutputStream.writeObject(spillBrett);

        objectOutputStream.close();


    }

    public void utfortTrekk() {
        spiller1tur = !spiller1tur;
        spiller2tur = !spiller2tur;

        sendBrett();


    }

    private void taImotBrett() {


    }

    private void sendBrett() {
        Socket socket;
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket();
            socket = serverSocket.accept();
        } catch (IOException e) {
            e.printStackTrace();
        }


        taImotBrett();
    }

    public boolean isSpiller1tur() {
        return spiller1tur;
    }

    public boolean isSpiller2tur() {
        return spiller2tur;
    }

    @Override
    public void run() {

    }
}
