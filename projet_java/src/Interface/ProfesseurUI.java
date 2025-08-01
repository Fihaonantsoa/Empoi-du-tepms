package Interface;

import java_project.Professeur;
import java_project.Professeurservice;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class ProfesseurUI extends JFrame {

	private JComboBox<String> gradeComboBox;
    private JTextField idField, nomField, prenomField;
    private JTable table;
    private DefaultTableModel tableModel;
    private Professeurservice service;

    public ProfesseurUI() {
    	setJMenuBar(new MenuBarUI());
        setTitle("Gestion des Professeurs");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        service = new Professeurservice();

        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createMainPanel(), BorderLayout.CENTER);

        chargerProfesseurs();
    }

    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(44, 62, 80));
        header.setPreferredSize(new Dimension(getWidth(), 60));

        JLabel title = new JLabel("Gestion des Professeurs", SwingConstants.LEFT);
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

        JLabel formTitle = new JLabel("Formulaire de Professeur");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        formTitle.setForeground(new Color(52, 73, 94));
        formTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        leftPanel.add(formTitle);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        idField = createTextField("ID Professeur");
        nomField = createTextField("Nom");
        prenomField = createTextField("Prénom(s)");

        JLabel gradeLabel = new JLabel("Grade");
        gradeLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        gradeComboBox = new JComboBox<>(new String[]{
        	"Professeur titulaire", "Assistant", "Maître Assistant", "Maître de Conférences",
        	"Assistant d’Enseignement Supérieur et de Recherche", "Docteur HDR", "Docteur en Informatique",
        	"Doctorant en informatique",
            "Chargé de cours", "Ingénieur", "Technicien", "Chercheur", "Vacataire"
        });

        gradeComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gradeComboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        leftPanel.add(idField);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        leftPanel.add(nomField);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        leftPanel.add(prenomField);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        leftPanel.add(gradeLabel);
        leftPanel.add(gradeComboBox);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        JButton addBtn = createStyledButton("Ajouter");
        addBtn.setMinimumSize(new Dimension(Integer.MAX_VALUE, 40));
        
        JButton updateBtn = createStyledButton("Modifier");
        updateBtn.setMinimumSize(new Dimension(Integer.MAX_VALUE, 40));
        
        JButton deleteBtn = createStyledButton("Supprimer");
        deleteBtn.setMinimumSize(new Dimension(Integer.MAX_VALUE, 40));
        
        JButton cancelBtn = createStyledButton("Effacer");
        cancelBtn.setMinimumSize(new Dimension(Integer.MAX_VALUE, 40));

        leftPanel.add(addBtn);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        leftPanel.add(updateBtn);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        leftPanel.add(deleteBtn);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        leftPanel.add(cancelBtn);

        addBtn.addActionListener(e -> ajouterProfesseur());
        updateBtn.addActionListener(e -> modifierProfesseur());
        deleteBtn.addActionListener(e -> supprimerProfesseur());
        cancelBtn.addActionListener(e -> viderChamps());

        tableModel = new DefaultTableModel(new Object[]{"ID", "Nom", "Prénoms", "Grade"}, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(28);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.setSelectionBackground(new Color(52, 152, 219));
        table.setSelectionForeground(Color.WHITE);

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Liste des Professeurs"));

        // Barre de recherche + filtre
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));

        JLabel searchLabel = new JLabel("Rechercher :");
        JTextField searchField = new JTextField(15);
        searchField.setPreferredSize(new Dimension(250, 30));

        JComboBox<String> filterCombo = new JComboBox<>(new String[]{"ID", "Nom", "Prénoms", "Grade"});
        filterCombo.setPreferredSize(new Dimension(150, 30));
        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(new JLabel("  par"));
        searchPanel.add(filterCombo);

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);

        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                filter();
            }

            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                filter();
            }

            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                filter();
            }

            public void filter() {
                int columnIndex = filterCombo.getSelectedIndex();
                String text = searchField.getText();
                if (text.trim().length() == 0) {
                    sorter.setRowFilter(null);
                } else {
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text, columnIndex));
                }
            }
        });

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                int row = table.convertRowIndexToModel(table.getSelectedRow());
                idField.setText(tableModel.getValueAt(row, 0).toString());
                nomField.setText(tableModel.getValueAt(row, 1).toString());
                prenomField.setText(tableModel.getValueAt(row, 2).toString());
                gradeComboBox.setSelectedItem(tableModel.getValueAt(row, 3).toString());
                idField.setEnabled(false);
            }
        });

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(Color.WHITE);
        rightPanel.add(searchPanel, BorderLayout.NORTH);
        rightPanel.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(rightPanel, BorderLayout.CENTER);

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

    private void chargerProfesseurs() {
        try {
            tableModel.setRowCount(0);
            List<Professeur> profs = service.getTousLesProfs();
            for (Professeur p : profs) {
                tableModel.addRow(new Object[]{p.getIdprof(), p.getNom(), p.getPrenoms(), p.getGrade()});
            }
            viderChamps();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur chargement : " + e.getMessage());
        }
    }

    private void ajouterProfesseur() {
        if (!verifierChamps()) return;
        Professeur p = new Professeur(
                idField.getText().trim(),
                nomField.getText().trim(),
                prenomField.getText().trim(),
                gradeComboBox.getSelectedItem().toString()
        );
        try {
            service.ajouterProfesseur(p);
            chargerProfesseurs();
            JOptionPane.showMessageDialog(this, "Professeur ajouté !");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur d'ajout : " + e.getMessage());
        }
    }

    private void modifierProfesseur() {
        if (!verifierChamps()) return;
        Professeur p = new Professeur(
                idField.getText().trim(),
                nomField.getText().trim(),
                prenomField.getText().trim(),
                gradeComboBox.getSelectedItem().toString()
        );
        try {
            if (service.modifierProfesseur(p)) {
                chargerProfesseurs();
                JOptionPane.showMessageDialog(this, "Modification réussie !");
            } else {
                JOptionPane.showMessageDialog(this, "Professeur introuvable.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur modification : " + e.getMessage());
        }
    }

    private void supprimerProfesseur() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Sélectionnez un professeur.");
            return;
        }

        String id = idField.getText().trim();
        int confirm = JOptionPane.showConfirmDialog(this, "Supprimer ce professeur ?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                if (service.supprimerProfesseur(id)) {
                    chargerProfesseurs();
                    JOptionPane.showMessageDialog(this, "Suppression réussie !");
                } else {
                    JOptionPane.showMessageDialog(this, "Professeur non trouvé.");
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Erreur suppression : " + e.getMessage());
            }
        }
    }

    private void viderChamps() {
        idField.setText("");
        nomField.setText("");
        prenomField.setText("");
        gradeComboBox.setSelectedIndex(-1);
        idField.setEnabled(true);
        table.clearSelection();
    }

    private boolean verifierChamps() {
        if (idField.getText().trim().isEmpty() || nomField.getText().trim().isEmpty()
                /*|| prenomField.getText().trim().isEmpty() */|| gradeComboBox.getSelectedItem().toString().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tous les champs sont requis.");
            return false;
        }
        return true;
    }
}
