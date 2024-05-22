package app;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

//Classe UserManager
public class UserManager {
    private static final String FILE_NAME = "users.dat";
    private List<User> users;

    //Costruttore della classe
    public UserManager() {
        loadUsers();
    }

    //Metodo per estrarre l'User
    public List<User> getUsers() {
        return users;
    }

    //Metodo per aggiungere l'User
    public void addUser(User user) {
        users.add(user);
        saveUsers();
    }

    //Metodo per rimuovere l'User
    public void removeUser(User user) {
        users.remove(user);
        saveUsers();
    }

    //Metodo per salvare l'User
    public void saveUsers() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Metodo per caricare l'User (@ aggiunta per eliminare un avviso di eclipse su un metodo usato all'interno)
	@SuppressWarnings("unchecked")
	private void loadUsers() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            users = (List<User>) ois.readObject();
        } catch (FileNotFoundException e) {
            users = new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            users = new ArrayList<>();
        }
    }
}
