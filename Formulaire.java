import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.io.*;
import java.sql.*;
import java.util.Properties;

public class Formulaire extends JFrame implements ActionListener {

// Initialisation des composants


    Container container = getContentPane();
    JLabel userLabel = new JLabel("Nom d'utilisateur");
    JLabel passwordLabel = new JLabel("Mot de Passe");
    JTextField userTextField = new JTextField();
    JPasswordField passwordField = new JPasswordField();
    JButton loginButton = new JButton("Se connecter");


//Initialisation paramètres de connexion
    String url ="jdbc:mysql://localhost"; // URL de connexion
    String password; // pwd

    {
        password = passwordField.getText();
    }

    String driverName = "com.mysql.jdbc.Driver";
	Connection connection;
	DriverPropertyInfo[] required;
	Properties props = new Properties();
	Driver driver;

    //COnstructeur sans paramètre
    Formulaire() {
        setLayoutManager();
        setLocationAndSize();
        addComponentsToContainer();
        addActionEvent();

    }

    //Application du Layout sur la fenêtre
    public void setLayoutManager() {
        container.setLayout(null);
    }

    //Fonction pour attribuer une position et une taille à chaque composants
    public void setLocationAndSize() {
        userLabel.setBounds(300, 150, 100, 30);
        passwordLabel.setBounds(300, 220, 100, 30);
        userTextField.setBounds(400, 150, 150, 30);
        passwordField.setBounds(400, 220, 150, 30);
        loginButton.setBounds(400, 300, 150, 30);



    }

    //Fonction pour ajouter les composants au container
    public void addComponentsToContainer() {
        container.add(userLabel);
        container.add(passwordLabel);
        container.add(userTextField);
        container.add(passwordField);
        container.add(loginButton);

    }

    public void addActionEvent() {
        loginButton.addActionListener(this);

    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) {
            String userText;
            String pwdText;
            userText = userTextField.getText();
            pwdText = passwordField.getText();


                try{
                    connect();
                    dispose();
                    new Requete(connection);
                }
                catch(SQLException ex){
                    JOptionPane.showMessageDialog(this, "Mot de passe ou identifiant invlide");
                }
                catch(ClassNotFoundException ex){ex.printStackTrace();
                }





    }
}

    public void connect() throws SQLException, ClassNotFoundException {
		Class.forName(driverName);
		driver = DriverManager.getDriver(url);
		required = driver.getPropertyInfo(url, props);
		//connection = DriverManager.getConnection(url, props);
		connection = DriverManager.getConnection(url, userTextField.getText(), passwordField.getText());
        System.out.println("Connect� � "+url);
        JOptionPane.showMessageDialog(this, "Connexion réussie");

	}

    public static void main(String[] a) {
        Formulaire frame = new Formulaire();
        frame.setTitle("Authentificateur");
        frame.setVisible(true);
        frame.setBounds(10, 10, 800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

    }

}


