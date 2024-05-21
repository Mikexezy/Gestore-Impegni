package app;
// Importa le classi necessarie per la gui.
import javax.swing.*;
import java.awt.*;
// Importa la lista per gestire le liste.
import java.util.List;


public class App extends JFrame {

    // Dichiarazione delle variabili per gestire gli elementi dell'interfaccia grafica.
    private DefaultListModel<String> listModel;
    private JList<String> taskList;
    private JTextField taskField;
    private JButton addButton;
    private JButton removeButton;
    private JButton editButton;
    private JButton sortButton;
    private JButton backButton;
    private JComboBox<String> categoryComboBox;
    private JComboBox<String> filterComboBox;
    private JComboBox<String> priorityComboBox;
    private UserManager userManager;
    private User currentUser;

    // Costruttore dell'applicazione che prende come argomenti l'utente corrente e il gestore degli utenti.
    public App(User user, UserManager userManager) {
        // Inizializzazione delle variabili dell'interfaccia grafica e impostazione delle dimensioni e del titolo della finestra.
        this.currentUser = user;
        this.userManager = userManager;
        setTitle("Gestore impegni di " + user.getName());
        setSize(1600, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Inizializzazione della lista e del campo di inserimento per i task.
        listModel = new DefaultListModel<>();
        taskList = new JList<>(listModel);
        taskList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(taskList);
        add(scrollPane, BorderLayout.CENTER);

        // Inizializzazione dei componenti per l'inserimento di nuovi task.
        taskField = new JTextField();
        categoryComboBox = new JComboBox<>(new String[]{"Lavoro", "Personale", "Altro"});
        priorityComboBox = new JComboBox<>(new String[]{"Alta", "Media", "Bassa"});

        // Inizializzazione e disposizione dei pulsanti per l'aggiunta, la rimozione, la modifica, l'ordinamento e il ritorno alla selezione utente.
        addButton = new JButton("Aggiungi Task");
        removeButton = new JButton("Rimuovi Task");
        editButton = new JButton("Modifica Task");
        sortButton = new JButton("Ordina per priorità");
        backButton = new JButton("Torna alla selezione utente");

        // Creazione dei pannelli per l'inserimento dei task e dei pulsanti.
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());
        inputPanel.add(taskField, BorderLayout.CENTER);

        JPanel categoryPriorityPanel = new JPanel();
        categoryPriorityPanel.setLayout(new GridLayout(1, 2));
        categoryPriorityPanel.add(categoryComboBox);
        categoryPriorityPanel.add(priorityComboBox);

        inputPanel.add(categoryPriorityPanel, BorderLayout.WEST);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 5));
        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(editButton);
        buttonPanel.add(sortButton);
        buttonPanel.add(backButton);
        inputPanel.add(buttonPanel, BorderLayout.EAST);
        add(inputPanel, BorderLayout.SOUTH);

        // Creazione e aggiunta del menu a tendina per filtrare i task.
        filterComboBox = new JComboBox<>(new String[]{"Tutti", "Lavoro", "Personale", "Altro"});
        filterComboBox.addActionListener(e -> filterTasks());
        add(filterComboBox, BorderLayout.NORTH);

        // Aggiunta degli ascoltatori per i pulsanti.
        addButton.addActionListener(e -> addTask());
        removeButton.addActionListener(e -> removeTask());
        editButton.addActionListener(e -> editTask());
        sortButton.addActionListener(e -> sortTasks());
        backButton.addActionListener(e -> backToUserSelection());

        // Rendi l'applicazione visibile.
        setVisible(true);
    }

    // Metodo per aggiungere un nuovo task.
    private void addTask() {
        // Ottieni il testo inserito nel campo task.
        String task = taskField.getText().trim();
        // Ottieni la categoria e la priorità selezionate dalle relative caselle a discesa.
        String category = (String) categoryComboBox.getSelectedItem();
        String priority = (String) priorityComboBox.getSelectedItem();
        // Verifica che il campo task non sia vuoto.
        if (!task.isEmpty()) {
            // Costruisci la stringa che rappresenta il task completo.
            String taskWithDetails = category + ": " + task + " [" + priority + "]";
            // Aggiungi il task all'utente corrente e aggiungilo alla lista grafica.
            currentUser.addTask(taskWithDetails);
            listModel.addElement(taskWithDetails);
            // Svuota il campo task e salva gli utenti.
            taskField.setText("");
            userManager.saveUsers();
        } else {
            // Mostra un messaggio di errore se il campo task è vuoto.
            JOptionPane.showMessageDialog(this, "Il task non può essere vuoto", "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Metodo per rimuovere un task selezionato.
    private void removeTask() {
        // Ottieni l'indice del task selezionato nella lista.
        int selectedIndex = taskList.getSelectedIndex();
        if (selectedIndex != -1) {
            // Ottieni il task dalla lista e rimuovilo dall'utente corrente e dalla lista grafica.
            String task = listModel.getElementAt(selectedIndex);
            currentUser.removeTask(task);
            listModel.remove(selectedIndex);
            // Salva gli utenti.
            userManager.saveUsers();
        } else {
            // Mostra un messaggio di errore se nessun task è stato selezionato.
            JOptionPane.showMessageDialog(this, "Seleziona un task da rimuovere", "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Metodo per modificare un task selezionato.
    private void editTask() {
        // Ottieni l'indice del task selezionato nella lista.
        int selectedIndex = taskList.getSelectedIndex();
        if (selectedIndex != -1) {
            // Ottieni il task selezionato e mostra una finestra di dialogo per modificarlo.
            String oldTask = listModel.getElementAt(selectedIndex);
            String newTask = JOptionPane.showInputDialog(this, "Modifica Task", oldTask);
            if (newTask != null && !newTask.trim().isEmpty()) {
                // Rimuovi il vecchio task e aggiungi quello nuovo.
                currentUser.removeTask(oldTask);
                String[] parts = oldTask.split(": ", 2);
                String category = parts[0];
                parts = parts[1].split(" \\[");
                String priority = parts[1].replace("]", "");
                String taskWithDetails = category + ": " + newTask + " [" + priority + "]";
                currentUser.addTask(taskWithDetails);
                listModel.set(selectedIndex, taskWithDetails);
                userManager.saveUsers();
            }
       
        } else {
            // Mostra un messaggio di errore se nessun task è stato selezionato.
            JOptionPane.showMessageDialog(this, "Seleziona un task da modificare", "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Metodo per filtrare i task in base alla categoria selezionata.
    private void filterTasks() {
        // Ottieni la categoria selezionata dalla casella a discesa.
        String selectedCategory = (String) filterComboBox.getSelectedItem();
        // Cancella la lista attuale dei task.
        listModel.clear();
        // Se "Tutti" è selezionato, aggiungi tutti i task dell'utente corrente alla lista grafica.
        if ("Tutti".equals(selectedCategory)) {
            currentUser.getTasks().forEach(listModel::addElement);
        } else {
            // Altrimenti, filtra i task in base alla categoria e aggiungili alla lista grafica.
            currentUser.getTasks().stream()
                .filter(task -> task.startsWith(selectedCategory + ":"))
                .forEach(listModel::addElement);
        }
    }

    // Metodo per ordinare i task per priorità.
    private void sortTasks() {
        // Ottieni i task dell'utente corrente ordinati per priorità.
        List<String> sortedTasks = currentUser.getTasksSortedByPriority();
        // Cancella la lista attuale dei task.
        listModel.clear();
        // Aggiungi i task ordinati alla lista grafica.
        sortedTasks.forEach(listModel::addElement);
    }

    // Metodo per tornare alla schermata di selezione dell'utente.
    private void backToUserSelection() {
        // Crea una nuova istanza della schermata di selezione dell'utente e chiudi l'attuale finestra.
        new UserSelectionScreen();
        dispose();
    }

    // Metodo main per avviare l'applicazione.
    public static void main(String[] args) {
        // Avvia l'applicazione tramite l'interfaccia grafica Swing.
        SwingUtilities.invokeLater(() -> new UserSelectionScreen());
    }
}
