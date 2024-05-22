package app;
import javax.swing.*;
import java.awt.*;

//Classe UserSelectionScreen
public class UserSelectionScreen extends JFrame {
    private UserManager userManager;
    private DefaultListModel<User> userModel;
    private JList<User> userList;
    private JTextField userField;
    private JButton selectButton;
    private JButton createButton;
    private JButton removeButton;

    //Costruttore della classe
    public UserSelectionScreen() {
        userManager = new UserManager();
        
        setTitle("Seleziona o crea un utente");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        userModel = new DefaultListModel<>();
        userManager.getUsers().forEach(userModel::addElement);
        userList = new JList<>(userModel);
        userList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(userList);
        add(scrollPane, BorderLayout.CENTER);

        userField = new JTextField();

        selectButton = new JButton("Seleziona Utente");
        selectButton.setBackground(Color.YELLOW);
        selectButton.setOpaque(true);
        
        createButton = new JButton("Crea Utente");
        createButton.setBackground(Color.GREEN);
        createButton.setOpaque(true);
        
        removeButton = new JButton("Rimuovi Utente");
        removeButton.setBackground(Color.RED);
        removeButton.setOpaque(true);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());
        inputPanel.add(userField, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 3));
        buttonPanel.add(selectButton);
        buttonPanel.add(createButton);
        buttonPanel.add(removeButton);
        inputPanel.add(buttonPanel, BorderLayout.EAST);
        add(inputPanel, BorderLayout.SOUTH);

        selectButton.addActionListener(e -> selectUser());
        createButton.addActionListener(e -> createUser());
        removeButton.addActionListener(e -> removeUser());

        setVisible(true);
    }

    //Metodo per selezionare l'User
    private void selectUser() {
        User selectedUser = userList.getSelectedValue();
        if (selectedUser != null) {
            new App(selectedUser, userManager);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Seleziona un utente", "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

  //Metodo per creare l'User
    private void createUser() {
        String userName = userField.getText().trim();
        if (!userName.isEmpty()) {
            User newUser = new User(userName);
            userManager.addUser(newUser);
            userModel.addElement(newUser);
            userField.setText("");
        } else {
            JOptionPane.showMessageDialog(this, "Il nome utente non può essere vuoto", "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

  //Metodo per rimuovere l'User
    private void removeUser() {
        User selectedUser = userList.getSelectedValue();
        if (selectedUser != null) {
            userManager.removeUser(selectedUser);
            userModel.removeElement(selectedUser);
        } else {
            JOptionPane.showMessageDialog(this, "Seleziona un utente da rimuovere", "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }
}
