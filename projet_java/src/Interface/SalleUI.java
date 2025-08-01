package Interface;

import java_project.Salle;
import java_project.Salleservice;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import com.toedter.calendar.JDateChooser;

import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

public class SalleUI extends JFrame {

    private JTextField idField, designField, occupationField;
    private JTable table;
    private DefaultTableModel model;
    private JDateChooser dateSalle;
    private JComboBox<String> cbHeure;
    private final Salleservice service;

    public SalleUI() throws SQLException {
    	setJMenuBar(new MenuBarUI());
        setTitle("Gestion des Salles");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        service = new Salleservice();

        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createMainPanel(), BorderLayout.CENTER);

        chargerSalles();
    }

    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(52, 73, 94));
        header.setPreferredSize(new Dimension(getWidth(), 60));

        JLabel title = new JLabel("Gestion des Salles");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10));

        JButton backBtn = new JButton("← Retour à l'accueil");
        backBtn.setBackground(new Color(41, 128, 185));
        backBtn.setForeground(Color.WHITE);
        backBtn.setFocusPainted(false);
        backBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backBtn.addActionListener(e -> {
            try {
                new AccueillUI().setVisible(true);
            } catch (SQLException ex) {
                ex.printStackTrace();
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

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        formPanel.setBackground(Color.decode("#f9f9f9"));
        formPanel.setPreferredSize(new Dimension(300, getHeight()));

        JLabel formTitle = new JLabel("Formulaire de Salle");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        formTitle.setForeground(new Color(52, 73, 94));
        formTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(formTitle);
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        idField = createTextField("ID Salle");
        designField = createTextField("Désignation");
        occupationField = createTextField("Occupation");

        formPanel.add(idField);
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        formPanel.add(designField);
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        formPanel.add(occupationField);
        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        JButton btnAjouter = createStyledButton("Ajouter");
        JButton btnModifier = createStyledButton("Modifier");
        JButton btnSupprimer = createStyledButton("Supprimer");
        JButton btnEffacer = createStyledButton("Effacer");

        formPanel.add(btnAjouter);
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        formPanel.add(btnModifier);
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        formPanel.add(btnSupprimer);
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        formPanel.add(btnEffacer);

        mainPanel.add(formPanel, BorderLayout.WEST);

        model = new DefaultTableModel(new Object[]{"ID", "Désignation", "Occupation"}, 0) {
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        table = new JTable(model);
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);
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

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField searchField = new JTextField(20);
        searchField.setPreferredSize(new Dimension(250, 30));
        JComboBox<String> comboTri = new JComboBox<>(new String[]{"Désignation", "Occupation"});
        comboTri.setPreferredSize(new Dimension(150, 30));

        dateSalle = new JDateChooser();
        dateSalle.setDateFormatString("yyyy-MM-dd");
        dateSalle.setPreferredSize(new Dimension(150, 30));

        cbHeure = new JComboBox<>();
        for (int h = 8; h <= 16; h+=2) {
        	if(h != 12) {
        		cbHeure.addItem(String.format("%02d:00", h));
        	}
        }
        cbHeure.setPreferredSize(new Dimension(150, 30));

        dateSalle.addPropertyChangeListener("date", evt -> OccupationByDate());
        cbHeure.addActionListener(e -> OccupationByDate());
        
        searchPanel.add(new JLabel("Recherche :"));
        searchPanel.add(searchField);
        searchPanel.add(new JLabel(" Trier par :"));
        searchPanel.add(comboTri);
        searchPanel.add(new JLabel(" Date : "));
        searchPanel.add(dateSalle);
        searchPanel.add(new JLabel(" Heure :"));
        searchPanel.add(cbHeure);
        topPanel.add(searchPanel, BorderLayout.WEST);

        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                filtre();
            }

            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                filtre();
            }

            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                filtre();
            }

            private void filtre() {
                String text = searchField.getText().trim().toLowerCase();
                int col = comboTri.getSelectedIndex() + 1;
                if (text.length() == 0) {
                    sorter.setRowFilter(null);
                } else {
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text, col));
                }
            }
        });

        comboTri.addActionListener(e -> {
            int col = comboTri.getSelectedIndex() + 1;
            sorter.setSortKeys(List.of(new RowSorter.SortKey(col, SortOrder.ASCENDING)));
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Liste des Salles"));

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(topPanel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        btnAjouter.addActionListener(e -> ajouterSalle());
        btnModifier.addActionListener(e -> modifierSalle());
        btnSupprimer.addActionListener(e -> supprimerSalle());
        btnEffacer.addActionListener(e -> viderChamps());
        
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                int rowSel = table.convertRowIndexToModel(table.getSelectedRow());
                idField.setText(model.getValueAt(rowSel, 0).toString());
                designField.setText(model.getValueAt(rowSel, 1).toString());
                occupationField.setText(model.getValueAt(rowSel, 2).toString());
                idField.setEnabled(false);
            }
        });

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

    private void chargerSalles() {
        try {
        	if (dateSalle.getDate() != null) {
        		OccupationByDate();
        	} else {
        		model.setRowCount(0);
                List<Salle> salles = service.getToutesLesSalles();
                
                for (Salle s : salles) {
                    model.addRow(new Object[]{s.getIdsalle(), s.getDesign(), s.getOccupation()});
                }
                viderChamps();	
        	}
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur chargement : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void ajouterSalle() {
        if (!verifierChamps()) return;

        try {
            int id = Integer.parseInt(idField.getText().trim());
            Salle s = new Salle(id, designField.getText().trim(), occupationField.getText().trim());
            service.ajouterSalle(s);
            chargerSalles();
            JOptionPane.showMessageDialog(this, "Salle ajoutée !");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur ajout : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void modifierSalle() {
        if (!verifierChamps()) return;

        try {
            int id = Integer.parseInt(idField.getText().trim());
            Salle s = new Salle(id, designField.getText().trim(), occupationField.getText().trim());
            if (service.modifierSalle(s)) {
                chargerSalles();
                JOptionPane.showMessageDialog(this, "Salle modifiée !");
            } else {
                JOptionPane.showMessageDialog(this, "Salle introuvable.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur modification : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void supprimerSalle() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Sélectionnez une salle.", "Attention", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Supprimer cette salle ?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int id = Integer.parseInt(idField.getText().trim());
                if (service.supprimerSalle(id)) {
                    chargerSalles();
                    JOptionPane.showMessageDialog(this, "Salle supprimée !");
                } else {
                    JOptionPane.showMessageDialog(this, "Salle non trouvée.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Erreur suppression : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void viderChamps() {
        idField.setText("");
        designField.setText("");
        occupationField.setText("");
        idField.setEnabled(true);
        table.clearSelection();
    }

    private boolean verifierChamps() {
        if (idField.getText().trim().isEmpty() ||
            designField.getText().trim().isEmpty() ||
            occupationField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tous les champs sont requis.", "Attention", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        try {
            Integer.parseInt(idField.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "L'ID doit être un entier.", "Attention", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }
    
    private void OccupationByDate() {
        try {
            Date d = dateSalle.getDate();
            String heure = (String) cbHeure.getSelectedItem();
            String[] parts = heure.split(":");

            LocalDateTime dateTime = d.toInstant()
                                      .atZone(ZoneId.systemDefault())
                                      .toLocalDateTime()
                                      .withHour(Integer.parseInt(parts[0]))
                                      .withMinute(Integer.parseInt(parts[1]))
                                      .withSecond(0)
                                      .withNano(0);

            List<Salle> sallesLibre = service.getToutesLesSallesByDate(dateTime);
            List<Salle> sallesOccupe = service.getToutesLesSallesOccupe(dateTime);
            
            model.setRowCount(0);
            for (Salle s : sallesLibre) {
                model.addRow(new Object[]{s.getIdsalle(), s.getDesign(), s.getOccupation()});
            }
            for (Salle s : sallesOccupe) {
                model.addRow(new Object[]{s.getIdsalle(), s.getDesign(), s.getOccupation()});
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}
