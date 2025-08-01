package Interface;

import java_project.Classe;
import java_project.Classeservice;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class ClasseUI extends JFrame {

    private JTextField idField;
    private JTextField niveauField;
    private JTable classeTable;
    private DefaultTableModel tableModel;
    private Classeservice service;

    public ClasseUI() {
    	setJMenuBar(new MenuBarUI());
        setTitle("Gestion des Classes");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        try {
            service = new Classeservice();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur de base de données : " + e.getMessage());
            System.exit(1);
        }

        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createMainPanel(), BorderLayout.CENTER);

        chargerDonnees();
    }

    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(44, 62, 80));
        header.setPreferredSize(new Dimension(getWidth(), 60));

        JLabel title = new JLabel("Gestion des Classes", SwingConstants.LEFT);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(Color.WHITE);
        title.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10));

        JButton backBtn = new JButton("← Retour à l'accueil");
        backBtn.setBackground(new Color(41, 128, 185));
        backBtn.setForeground(Color.WHITE);
        backBtn.setFocusPainted(false);
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backBtn.addActionListener(e -> {
            try {
                new AccueillUI().setVisible(true);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Erreur ouverture accueil : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
            dispose();
        });
        
        JPanel rightPanel = new JPanel();
        rightPanel.setOpaque(false);
        rightPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));
        rightPanel.add(backBtn);

        header.add(title, BorderLayout.WEST);
        header.add(rightPanel, BorderLayout.EAST);

        return header;
    }

    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        leftPanel.setBackground(Color.decode("#f9f9f9"));
        leftPanel.setPreferredSize(new Dimension(300, getHeight()));

        JLabel formTitle = new JLabel("Formulaire de Classe");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        formTitle.setForeground(new Color(52, 73, 94));
        formTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        leftPanel.add(formTitle);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        idField = createTextField("ID Classe");
        niveauField = createTextField("Niveau");

        leftPanel.add(idField);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        leftPanel.add(niveauField); 
        leftPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        JButton addBtn = createStyledButton("Ajouter");
        JButton updateBtn = createStyledButton("Modifier");
        JButton deleteBtn = createStyledButton("Supprimer");
        JButton cancelBtn = createStyledButton("Effacer");

        leftPanel.add(addBtn);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        leftPanel.add(updateBtn);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        leftPanel.add(deleteBtn);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        leftPanel.add(cancelBtn);

        addBtn.addActionListener(e -> ajouterClasse());
        updateBtn.addActionListener(e -> modifierClasse());
        deleteBtn.addActionListener(e -> supprimerClasse());
        cancelBtn.addActionListener(e -> viderChamps());
        
        tableModel = new DefaultTableModel(new Object[]{"ID Classe", "Niveau"}, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        classeTable = new JTable(tableModel);
        classeTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        classeTable.setRowHeight(28);
        classeTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        classeTable.setSelectionBackground(new Color(52, 152, 219));
        classeTable.setSelectionForeground(Color.WHITE);

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

        for (int i = 0; i < classeTable.getColumnCount(); i++) {
            classeTable.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }

        JScrollPane scrollPane = new JScrollPane(classeTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Liste des Classes"));

        classeTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && classeTable.getSelectedRow() != -1) {
                int row = classeTable.getSelectedRow();
                idField.setText(tableModel.getValueAt(row, 0).toString());
                niveauField.setText(tableModel.getValueAt(row, 1).toString());
                idField.setEnabled(false);
            }
        });

        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        return mainPanel;
    }

    private JTextField createTextField(String placeholder) {
        JTextField field = new JTextField();
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createTitledBorder(placeholder));
        return field;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(41, 128, 185));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        return button;
    }

    private void chargerDonnees() {
        try {
            tableModel.setRowCount(0);
            List<Classe> classes = service.readAllClasses();
            for (Classe c : classes) {
                tableModel.addRow(new Object[]{c.getIdclasse(), c.getNiveau()});
            }
            viderChamps();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur chargement données : " + e.getMessage());
        }
    }

    private void viderChamps() {
        idField.setText("");
        niveauField.setText("");
        idField.setEnabled(true);
        classeTable.clearSelection();
    }

    private boolean verifierChamps() {
        if (idField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Le champ ID Classe est requis.");
            idField.requestFocus();
            return false;
        }

        if (niveauField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Le champ Niveau est requis.");
            niveauField.requestFocus();
            return false;
        }

        return true;
    }

    private void ajouterClasse() {
        if (!verifierChamps()) return;

        Classe classe = new Classe(idField.getText().trim(), niveauField.getText().trim());
        try {
            service.createClasse(classe);
            chargerDonnees();
            JOptionPane.showMessageDialog(this, "Classe ajoutée !");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur ajout : " + e.getMessage());
        }
    }

    private void modifierClasse() {
        if (!verifierChamps()) return;

        Classe classe = new Classe(idField.getText().trim(), niveauField.getText().trim());
        try {
            service.updateClasse(classe);
            chargerDonnees();
            JOptionPane.showMessageDialog(this, "Classe modifiée !");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur modification : " + e.getMessage());
        }
    }

    private void supprimerClasse() {
        int selectedRow = classeTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une classe.");
            return;
        }

        String id = idField.getText().trim();

        int confirm = JOptionPane.showConfirmDialog(this,
                "Supprimer la classe : " + id + " ?", "Confirmation", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                service.deleteClasse(id);
                chargerDonnees();
                JOptionPane.showMessageDialog(this, "Classe supprimée !");
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Erreur suppression : " + e.getMessage());
            }
        }
    }

}
