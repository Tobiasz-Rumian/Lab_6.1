/**
 * Created by Tobiasz Rumian on 11.12.2016.
 */
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;
import java.util.TreeMap;


public class PhoneBookServer extends JFrame implements Runnable {

    private JTextArea textArea = new JTextArea(15, 18);
    private JScrollPane scroll = new JScrollPane(textArea, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);


    private static final int SERVER_PORT = 15000;
    private String host;
    private ServerSocket server;


    PhoneBookServer() {
        super("SERVER");
        setSize(300, 340);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JPanel panel = new JPanel();
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);
        panel.add(scroll);
        setContentPane(panel);
        Thread t = new Thread(this);
        t.start();
        setVisible(true);
    }

    public synchronized void load(String fileName){

    }

    public synchronized void save(String fileName){

    }

    public synchronized void get(String nick){

    }

    public synchronized void put(String nick, String number){

    }

    public synchronized void replace(String nick, String number){

    }

    public synchronized void delete(String nick){

    }

    public synchronized void list(){

    }

    public synchronized void close(){

    }

    public synchronized void bye(){

    }
    /*LOAD nazwa_pliku” - wczytanie danych z pliku o podanej nazwie,
                    „SAVE nazwa_pliku” - zapis danych do pliku o podanej nazwie,
                    „GET imię” - pobranie numeru telefonu osoby o podanym imieniu,
                    „PUT imię numer” - zapis numeru telefonu osoby o podanym imieniu,
                    „REPLACE imie numer” - zmiana numeru telefonu dla osoby o podanym mieniu,
                    „DELETE imie” - usunięcie z kolekcji osoby o podanym imieniu,
                    „LIST” - przesłanie listy imion, które są zapamiętane w kolekcji,
                    „CLOSE” - zakończenie nasłuchu połączeń od nowych klientów i zamknięcie gniazda serwera
                    „BYE” - zakończenie komunikacji klienta z serwerem i zamknięcie strumieni danych*/
    synchronized public void wypiszOdebrane(UserThread k, String s) {
        String pom = textArea.getText();
        textArea.setText(k.getNazwa() + " >>> " + s + "\n" + pom);
    }

    synchronized public void wypiszWyslane(UserThread k, String s) {
        String pom = textArea.getText();
        textArea.setText(k.getNazwa() + " <<< " + s + "\n" + pom);
    }

    public void run() {
        Socket s;
        UserThread user;

        try {
            host = InetAddress.getLocalHost().getHostName();
            server = new ServerSocket(SERVER_PORT);
        } catch (IOException e) {
            e.getMessage();
            JOptionPane.showMessageDialog(null, "Gniazdko dla serwera nie może być utworzone");
            System.exit(0);
        }
        System.out.println("Serwer zostal uruchomiony na hoście " + host);

        while (true) {
            try {
                s = server.accept();
                if (s != null) {
                    user = new UserThread(this, s);
                }
            } catch (IOException e) {
                e.getMessage();
                JOptionPane.showMessageDialog(null, "BLAD SERWERA: Nie mozna polaczyc sie z klientem ");
            }
        }
    }

    public static void main(String[] args) {
        new PhoneBookServer();
    }
}
class UserThread implements Runnable {
    private Socket socket;
    private ObjectOutputStream output;
    private ObjectInputStream input;

    private String id;
    private PhoneBookServer phoneBookServer;

    UserThread(PhoneBookServer phoneBookServer, Socket socket) throws IOException{
        this.phoneBookServer = phoneBookServer;
        this.socket = socket;
        Thread t = new Thread(this); 
        t.start();
    }

    public ObjectOutputStream getOutput(){ return output; }

    public void run(){
        String m;
        boolean running=true;
        try{
            output = new ObjectOutputStream(socket.getOutputStream());
            input = new ObjectInputStream(socket.getInputStream());
            id = (String)input.readObject();
            while(running){

                m = (String)input.readObject();
                if(m.contains("$LOAD$"))phoneBookServer.load(null);
                else if(m.contains("$SAVE$"))phoneBookServer.save(null);
                else if(m.contains("$GET$"))phoneBookServer.get(null);
                else if(m.contains("$PUT$"))phoneBookServer.put(null,null);
                else if(m.contains("$REPLACE$"))phoneBookServer.replace(null,null);
                else if(m.contains("$DELETE$"))phoneBookServer.delete(null);
                else if(m.contains("$LIST$"))phoneBookServer.list();
                else if(m.contains("$CLOSE$")){
                    running=false;
                    phoneBookServer.close();
                }
                else if(m.contains("$BYE$"))running=false;

            }
            input.close();
            output.close();
            socket.close();
            socket = null;
        } catch(Exception ignored) {}
    }
}
