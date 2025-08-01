package Interface;

import javax.swing.*;

import java_project.RoundedBorder;
import java_project.RoundedImagePanel;

import java.awt.*;
import java.sql.SQLException;

public class AccueillUI extends JFrame {

    private JPanel sidebar, content;
    private JLabel title;

    public AccueillUI() throws SQLException {
        setTitle("Système de Gestion");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1300, 900));

        initUI();
    }

    private void initUI() throws SQLException {
    	setJMenuBar(new MenuBarUI());
        setLayout(new BorderLayout());

        title = new JLabel("Tableau de bord", SwingConstants.LEFT);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(Color.decode("#2c3e50"));
        title.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 10));
        add(title, BorderLayout.NORTH);

        sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(Color.decode("#34495e"));
        sidebar.setPreferredSize(new Dimension(250, getHeight()));
        sidebar.setBorder(BorderFactory.createEmptyBorder(30, 20, 30, 20));

        String[] menuItems = {
            "Gérer les Professeurs",
            "Gérer les Salles",
            "Gérer les Classes",
            "Emploi du temps",
            "Quitter l'application"
        };

        for (String label : menuItems) {
            JButton btn = createSidebarButton(label);
            sidebar.add(btn);
            sidebar.add(Box.createRigidArea(new Dimension(0, 15)));

            switch (label) {
                case "Gérer les Professeurs":
                    btn.addActionListener(e -> {
                        new ProfesseurUI().setVisible(true);
                        this.dispose();
                    });
                    break;
                case "Gérer les Salles":
                    btn.addActionListener(e -> {
                        try {
                            new SalleUI().setVisible(true);
                        } catch (SQLException e1) {
                            e1.printStackTrace();
                        }
                        this.dispose();
                    });
                    break;
                case "Gérer les Classes":
                    btn.addActionListener(e -> {
                        new ClasseUI().setVisible(true);
                        this.dispose();
                    });
                    break;
                case "Emploi du temps":
                    btn.addActionListener(e -> {
                        new EmploiUI().setVisible(true);
                        this.dispose();
                    });
                    break;
                case "Quitter l'application":
                    btn.addActionListener(e -> System.exit(0));
                    break;
            }
        }

        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(Color.decode("#ecf0f1"));
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        JLabel welcomeText = new JLabel("Bienvenue dans le système de gestion d'emploi du temps", SwingConstants.CENTER);
        welcomeText.setAlignmentX(Component.CENTER_ALIGNMENT);
        welcomeText.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        welcomeText.setForeground(Color.decode("#2c3e50"));
        centerPanel.add(Box.createVerticalGlue());
        centerPanel.add(welcomeText);
        centerPanel.add(Box.createVerticalStrut(30));

        ImageIcon icon = new ImageIcon(getClass().getResource("/images/imgemploi.jpg"));
        Image img = icon.getImage().getScaledInstance(600, 400, Image.SCALE_SMOOTH);
        RoundedImagePanel imagePanel = new RoundedImagePanel(img);
        imagePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        imagePanel.setPreferredSize(new Dimension(600, 400));
        imagePanel.setMaximumSize(new Dimension(700, 500));

        centerPanel.add(imagePanel);
        centerPanel.add(Box.createVerticalGlue());

        content = new JPanel();
        content.setBackground(Color.decode("#ecf0f1"));
        content.setLayout(new BorderLayout());
        content.add(centerPanel, BorderLayout.CENTER);

        add(sidebar, BorderLayout.WEST);
        add(content, BorderLayout.CENTER);
    }

    private JButton createSidebarButton(String text) {
        JButton btn = new JButton(text);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setForeground(Color.WHITE);
        btn.setBackground(Color.decode("#ecf0f1"));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btn.setContentAreaFilled(false);
        btn.setOpaque(false);
        
        btn.setBorder(new RoundedBorder(10));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setForeground(new Color(41, 128, 250));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
            	btn.setForeground(Color.WHITE);
            }
        });
        return btn;
    }

}