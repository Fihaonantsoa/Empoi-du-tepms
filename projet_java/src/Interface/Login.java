package Interface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;

public class Login extends JFrame {

    private JTextField userField;
    private JPasswordField passField;

    public Login() {
        super("Connexion au système");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 380);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.decode("#ecf0f1"));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
            BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));

        JLabel title = new JLabel("Authentification", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(Color.decode("#2c3e50"));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.add(title);
        formPanel.add(Box.createRigidArea(new Dimension(0, 25)));

        userField = createTextField("Nom d'utilisateur");
        passField = createPasswordField("Mot de passe");

        formPanel.add(userField);
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        formPanel.add(passField);
        formPanel.add(Box.createRigidArea(new Dimension(0, 25)));

        JButton loginButton = new JButton("Se connecter");
        styleButton(loginButton);
        formPanel.add(loginButton);

        loginButton.addActionListener((ActionEvent e) -> {
            try {
                checkCredentials();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        mainPanel.add(formPanel, BorderLayout.CENTER);
        add(mainPanel, BorderLayout.CENTER);
    }

    private JTextField createTextField(String placeholder) {
        JTextField field = new JTextField();
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createTitledBorder(placeholder));
        return field;
    }

    private JPasswordField createPasswordField(String placeholder) {
        JPasswordField field = new JPasswordField();
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createTitledBorder(placeholder));
        return field;
    }

    private void styleButton(JButton button) {
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setBackground(new Color(41, 128, 185));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 15));
        button.setPreferredSize(new Dimension(150, 40));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
    }

    private void checkCredentials() throws SQLException {
        String username = userField.getText().trim();
        String password = new String(passField.getPassword()).trim();

        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Le nom d'utilisateur est requis.", "Erreur", JOptionPane.ERROR_MESSAGE);
            userField.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Le mot de passe est requis.", "Erreur", JOptionPane.ERROR_MESSAGE);
            passField.requestFocus();
            return;
        }

        if (username.equals("admin") && password.equals("admin123")) {
            AccueillUI accueil = new AccueillUI();
            accueil.setVisible(true);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Identifiants incorrects.", "Erreur", JOptionPane.ERROR_MESSAGE);
            passField.setText("");
        }
    }

}
