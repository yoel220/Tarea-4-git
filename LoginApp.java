import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;

// Singleton Pattern
public class LoginApp {
    private static LoginApp instance; // Singleton instance
    private HashMap<String, String> users = new HashMap<>(); // Encapsulamiento: users es un atributo privado que almacena usuarios y contraseñas.
    private JFrame loginFrame; // Encapsulamiento: loginFrame es un atributo privado que representa la ventana de inicio de sesión.
    private JFrame mainFrame; // Encapsulamiento: mainFrame es un atributo privado que representa la ventana principal.
    private JTextField usernameField; // Encapsulamiento: usernameField es un atributo privado que representa el campo de texto para el nombre de usuario.
    private JPasswordField passwordField; // Encapsulamiento: passwordField es un atributo privado que representa el campo de contraseña.

    // Private constructor for Singleton
    private LoginApp() {
        users.put("admin", "admin"); // Inicializar usuarios predeterminados
        createLoginFrame(); // Llama a un método para crear la ventana de inicio de sesión.
    }

    // Method to get the singleton instance
    public static LoginApp getInstance() {
        if (instance == null) {
            instance = new LoginApp();
        }
        return instance;
    }

    public void addUser(String username, String password) {
        users.put(username, password);
    }

    public boolean userExists(String username) {
        return users.containsKey(username);
    }

    public String getPassword(String username) {
        return users.get(username);
    }

    private void createLoginFrame() {
        // Abstracción: Método que crea la interfaz de inicio de sesión.
        loginFrame = new JFrame("Login de Usuarios");
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setSize(400, 300);
        loginFrame.setLayout(new GridBagLayout());
        loginFrame.setLocationRelativeTo(null);

        // Componentes de login
        JLabel usernameLabel = new JLabel("Usuario:");
        usernameField = new JTextField(15);
        JLabel passwordLabel = new JLabel("Contraseña:");
        passwordField = new JPasswordField(15);
        JButton loginButton = new JButton("Iniciar Sesión");
        JButton registerButton = new JButton("Registrarse");

        // Configurar layout
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0; gbc.gridy = 0; loginFrame.add(usernameLabel, gbc);
        gbc.gridx = 1; loginFrame.add(usernameField, gbc);
        gbc.gridx = 0; gbc.gridy = 1; loginFrame.add(passwordLabel, gbc);
        gbc.gridx = 1; loginFrame.add(passwordField, gbc);
        gbc.gridx = 0; gbc.gridy = 2; loginFrame.add(loginButton, gbc);
        gbc.gridx = 1; loginFrame.add(registerButton, gbc);

        // Acciones de botones
        loginButton.addActionListener(e -> login()); 
        registerButton.addActionListener(e -> register()); 

        loginFrame.setVisible(true);
    }

    private void login() {
        // Abstracción: Método que maneja la lógica de inicio de sesión.
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(loginFrame, "Debe ingresar su usuario y contraseña.");
            return;
        }

        if (userExists(username) && getPassword(username).equals(password)) {
            JOptionPane.showMessageDialog(loginFrame, "Inicio de sesión exitoso.");
            loginFrame.setVisible(false);
            showMainFrame(); 
        } else {
            JOptionPane.showMessageDialog(loginFrame, "Usuario o contraseña incorrectos.");
        }
    }

    private void register() {
         JFrame registerFrame = FrameFactory.createRegisterFrame(this); 
         registerFrame.setVisible(true);
   }

   private void showMainFrame() {
       // Abstracción: Método que muestra la ventana principal.
       mainFrame = new JFrame("Panel Principal");
       mainFrame.setSize(600, 400);
       mainFrame.setLayout(new BorderLayout());
       mainFrame.setLocationRelativeTo(null);

       String[] columnNames = {"Usuario"};
       String[][] data = users.keySet().stream()
           .map(username -> new String[]{username})
           .toArray(String[][]::new);

       JTable userTable = new JTable(data, columnNames);
       JScrollPane scrollPane = new JScrollPane(userTable);

       JPanel buttonPanel = new JPanel();
       JButton logoutButton = new JButton("Cerrar Sesión");
       JButton updateButton = new JButton("Actualizar Usuario");
       JButton deleteButton = new JButton("Eliminar Usuario");

       buttonPanel.add(updateButton);
       buttonPanel.add(deleteButton);
       buttonPanel.add(logoutButton);

       mainFrame.add(scrollPane, BorderLayout.CENTER);
       mainFrame.add(buttonPanel, BorderLayout.SOUTH);

       logoutButton.addActionListener(e -> {
           mainFrame.dispose();
           loginFrame.setVisible(true); 
       });

       updateButton.addActionListener(e -> {
           int selectedRow = userTable.getSelectedRow();
           if (selectedRow == -1) {
               JOptionPane.showMessageDialog(mainFrame,"Seleccione un usuario para actualizar.");
               return;
           }
           
           String selectedUser =(String) userTable.getValueAt(selectedRow ,0 );
           String newPassword= JOptionPane.showInputDialog(mainFrame,"Ingrese nueva contraseña para " + selectedUser);
           
           if (newPassword != null && !newPassword.isEmpty()) {
               addUser(selectedUser,newPassword); 
               JOptionPane.showMessageDialog(mainFrame,"Contraseña actualizada.");
           }
       });

       deleteButton.addActionListener(e -> {
           int selectedRow=userTable.getSelectedRow();
           if(selectedRow==-1){
               JOptionPane.showMessageDialog(mainFrame,"Seleccione un usuario para eliminar.");
               return;
           }
           
           String selectedUser=(String)userTable.getValueAt(selectedRow ,0 );
           int choice=JOptionPane.showConfirmDialog(mainFrame,"¿Está seguro de que desea eliminar a "+selectedUser+"?");
           
           if(choice==JOptionPane.YES_OPTION){
               users.remove(selectedUser); 
               JOptionPane.showMessageDialog(mainFrame,"Usuario eliminado.");
               showMainFrame(); 
           }
      });

      mainFrame.setVisible(true);
   }

   public static void main(String[] args) {
      SwingUtilities.invokeLater(() -> LoginApp.getInstance()); 
   }
}

// Factory Pattern
class FrameFactory {
    public static JFrame createRegisterFrame(LoginApp app) {
         JFrame registerFrame = new JFrame("Registro de Usuarios");
         registerFrame.setSize(400, 400);
         registerFrame.setLayout(new GridBagLayout());
         registerFrame.setLocationRelativeTo(null);

         JTextField regUsernameField = new JTextField(15);
         JTextField regFirstNameField = new JTextField(15);
         JTextField regLastNameField = new JTextField(15);
         JTextField regPhoneField = new JTextField(15);
         JTextField regEmailField = new JTextField(15);        
         JPasswordField regPasswordField = new JPasswordField(15);
         JPasswordField regConfirmPasswordField = new JPasswordField(15);

         GridBagConstraints gbc = new GridBagConstraints();
         gbc.insets = new Insets(10, 10, 10, 10);

         addRegisterFields(registerFrame, gbc,
                           regUsernameField,
                           regFirstNameField,
                           regLastNameField,
                           regPhoneField,
                           regEmailField,
                           regPasswordField,
                           regConfirmPasswordField);

         JButton registerSubmitButton = new JButton("Registrar");
         gbc.gridx = 1; gbc.gridy = 7; registerFrame.add(registerSubmitButton, gbc);

         registerSubmitButton.addActionListener(e -> {
             String username = regUsernameField.getText();
             String password = new String(regPasswordField.getPassword());
             String confirmPassword = new String(regConfirmPasswordField.getPassword());
             String firstName = regFirstNameField.getText();
             String lastName = regLastNameField.getText();
             String phone = regPhoneField.getText();
             String email = regEmailField.getText();

             if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() ||
                 firstName.isEmpty() || lastName.isEmpty() || phone.isEmpty() || email.isEmpty()) {
                 JOptionPane.showMessageDialog(registerFrame, "Todos los campos son obligatorios.");
                 return;
             }

             if (!password.equals(confirmPassword)) {
                 JOptionPane.showMessageDialog(registerFrame, "Las contraseñas no coinciden.");
                 return;
             }

             app.addUser(username,password); // Guardar solo el nombre de usuario y contraseña por simplicidad.
             JOptionPane.showMessageDialog(registerFrame, "Registro exitoso.");
             registerFrame.dispose();
         });

         return registerFrame;
     }

     private static void addRegisterFields(JFrame frame, GridBagConstraints gbc,
                                            JTextField... fields) {
         String[] labels = {"Nombre de Usuario:", "Nombre:", "Apellido:", "Teléfono:", 
                            "Correo Electrónico:", "Contraseña:", "Confirmar Contraseña:"};

         for (int i = 0; i < labels.length; i++) {
             gbc.gridx = 0; gbc.gridy = i;
             frame.add(new JLabel(labels[i]), gbc);
             gbc.gridx = 1;
             frame.add(fields[i], gbc);
         }
     }
}