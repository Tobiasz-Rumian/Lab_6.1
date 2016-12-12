/**
 * Created by Tobiasz Rumian on 11.12.2016.
 */
public class PhoneBookTester {
    public static void main(String [] args){
        new Serwer();

        try{
            Thread.sleep(1000);
        } catch(Exception ignored){}

        new Klient("Ewa");

        new Klient("Adam");
    }
}
