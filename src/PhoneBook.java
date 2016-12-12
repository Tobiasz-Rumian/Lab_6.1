import java.io.*;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * Created by Tobiasz Rumian on 11.12.2016.
 */
public class PhoneBook implements Serializable {
    private static final long serialVersionUID = 1L;
    private ConcurrentSkipListMap<String,String> book = new ConcurrentSkipListMap<>();

    public PhoneBook(){}

    public String get(String nick){
        if(!book.containsKey(nick)) return "Podany nick nie istnieje";
        return book.get(nick);
    }

    public String put(String nick,String number){
        if(number.length()<9||number.length()>9) return "Numer powinien posiadać 9 cyfr!";
        if(book.containsKey(nick)) return "Podany nick jest już zajęty";
        book.put(nick,number);
        return ok();
    }

    public String replace(String nick, String number){
        if(number.length()<9||number.length()>9) return "Numer powinien posiadać 9 cyfr!";
        if(!book.containsKey(nick)) return "Podany nick nie istnieje";
        book.replace(nick,number);
        return ok();
    }

    public String delete(String nick){
        if(!book.containsKey(nick)) return "Podany nick nie istnieje";
        book.remove(nick);
        return ok();
    }

    public String list(){
        StringBuilder listBuilder = new StringBuilder();
        for (ConcurrentSkipListMap.Entry<String, String> entry : book.entrySet()) {
            listBuilder.append("Nick: ").append(entry.getKey()).append("   -   Number: ").append(entry.getValue()).append("\n");
        }
        return listBuilder.toString();
    }

    public String save(String fileName) throws Exception {
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName));
        out.writeObject(book);
        out.close();
        return ok();
    }


    public String load(String fileName) throws Exception {
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName));
        book = (ConcurrentSkipListMap<String,String>)in.readObject();
        in.close();
        return ok();
    }

    private String ok(){
        return "OK";
    }

}
