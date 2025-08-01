package Interface;

import com.toedter.calendar.JDateChooser;
import java_project.*;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.function.BiConsumer;

public class EmploiUI extends JFrame {

    private JComboBox<Integer> cbSalle;
    private JComboBox<ComboItemProf> cbProf;
    private JComboBox<ComboItemClasse> cbClasse;
    private JTextField tfCours;
    private JDateChooser dateChooser;
    private JComboBox<String> cbHeure;
    private JTable emploiTable;
    private DefaultTableModel tableModel;

    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private Salleservice salleService;
    private Professeurservice profService;
    private Classeservice classeService;
    private EmploiDuTempsService emploiService;

    public EmploiUI() {
    	setJMenuBar(new MenuBarUI());
        setTitle("Gestion d'Emploi du Temps");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        try {
            salleService = new Salleservice();
            profService = new Professeurservice();
            classeService = new Classeservice();
            emploiService = new EmploiDuTempsService();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur de base de données : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createMainPanel(), BorderLayout.CENTER);

        chargerCombos();
        chargerTable();
    }

    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(44, 62, 80));
        header.setPreferredSize(new Dimension(getWidth(), 60));

        JLabel title = new JLabel("Gestion d'Emploi du Temps", SwingConstants.LEFT);
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

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.decode("#f9f9f9"));
        formPanel.setPreferredSize(new Dimension(340, getHeight()));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.weightx = 1.0;

        JLabel formTitle = new JLabel("Emploi du temps");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        formTitle.setForeground(new Color(52, 73, 94));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        formPanel.add(formTitle, gbc);

        BiConsumer<String, JComponent> addChamp = (label, comp) -> {
            gbc.gridwidth = 1;
            gbc.gridy++;
            gbc.gridx = 0;

            JLabel jLabel = new JLabel(label);
            jLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
            formPanel.add(jLabel, gbc);

            gbc.gridx = 1;
            if (comp instanceof JComboBox) {
                ((JComboBox<?>) comp).setFont(new Font("Segoe UI", Font.PLAIN, 14));
            } else if (comp instanceof JTextField) {
                ((JTextField) comp).setFont(new Font("Segoe UI", Font.PLAIN, 14));
            }
            formPanel.add(comp, gbc);
        };

        cbSalle = new JComboBox<>();
        cbProf = new JComboBox<>();
        cbClasse = new JComboBox<>();
        tfCours = new JTextField();
        dateChooser = new JDateChooser();
        dateChooser.setDateFormatString("yyyy-MM-dd");

        cbHeure = new JComboBox<>();
        for (int h = 8; h <= 16; h+=2) {
        	if(h != 12) {
        		cbHeure.addItem(String.format("%02d:00", h));
        	}
        }

        addChamp.accept("Salle:", cbSalle);
        addChamp.accept("Professeur:", cbProf);
        addChamp.accept("Classe:", cbClasse);
        addChamp.accept("Cours:", tfCours);
        addChamp.accept("Date:", dateChooser);
        addChamp.accept("Heure:", cbHeure);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.NORTHWEST;

        JPanel btnPanel = new JPanel();
        btnPanel.setLayout(new BoxLayout(btnPanel, BoxLayout.Y_AXIS));
        btnPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        btnPanel.setBackground(Color.decode("#f9f9f9"));

        JButton btnAjouter = createStyledButton("Ajouter");
        JButton btnModifier = createStyledButton("Modifier");
        JButton btnSupprimer = createStyledButton("Supprimer");
        JButton btnEffacer = createStyledButton("Effacer");

        makeFullWidth(btnAjouter);
        makeFullWidth(btnModifier);
        makeFullWidth(btnSupprimer);
        makeFullWidth(btnEffacer);

        btnPanel.add(btnAjouter);
        btnPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        btnPanel.add(btnModifier);
        btnPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        btnPanel.add(btnSupprimer);
        btnPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        btnPanel.add(btnEffacer);

        formPanel.add(btnPanel, gbc);

        btnAjouter.addActionListener(e -> {
            try {
                ajouterEmploi();
            } catch (Exception ex) {
                showError("Erreur ajout : " + ex.getMessage());
            }
        });
        btnModifier.addActionListener(e -> modifierEmploi());
        btnSupprimer.addActionListener(e -> supprimerEmploi());
        btnEffacer.addActionListener(e -> clearForm());
        
        JPanel rightPanel = new JPanel(new BorderLayout());

        JPanel topToolsPanel = new JPanel(new BorderLayout());
        topToolsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPanel resPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        resPanel.setPreferredSize(new Dimension(400, 30));
        topToolsPanel.add(resPanel, BorderLayout.WEST);
        
        JTextField searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(200, 30));
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.setToolTipText("Rechercher...");
        resPanel.add(searchField);


        JPanel ExportPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        ExportPanel.setPreferredSize(new Dimension(600, 40));
        topToolsPanel.add(ExportPanel, BorderLayout.EAST);

        JButton ImprimeBtn = new JButton("Exportation en PDF");
        ImprimeBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        ImprimeBtn.setBackground(new Color(39, 174, 96));
        ImprimeBtn.setForeground(Color.WHITE);
        ImprimeBtn.setFocusPainted(false);
        ImprimeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        ImprimeBtn.addActionListener(e -> {
			try {
				EmploiExport Emploi = new EmploiExport();
				Emploi.setVisible(true);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
        
        JButton pdfButton = new JButton("Détaille d'Emploi du Temps");
        pdfButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        pdfButton.setBackground(new Color(39, 174, 96));
        pdfButton.setForeground(Color.WHITE);
        pdfButton.setFocusPainted(false);
        pdfButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        pdfButton.addActionListener(e -> {
			try {
				viewemploi();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		});
        
        ExportPanel.add(ImprimeBtn);        
        ExportPanel.add(pdfButton);

        rightPanel.add(topToolsPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new Object[]{"Salle", "Professeur", "Classe", "Cours", "Date/Heure"}, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        emploiTable = new JTable(tableModel);
        emploiTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        emploiTable.setRowHeight(28);
        emploiTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        emploiTable.setSelectionBackground(new Color(52, 152, 219));
        emploiTable.setSelectionForeground(Color.WHITE);

        emploiTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && emploiTable.getSelectedRow() != -1) {
                remplirChamps(emploiTable.getSelectedRow());
            }
        });

        JScrollPane scrollPane = new JScrollPane(emploiTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Liste des Emplois"));
        rightPanel.add(scrollPane, BorderLayout.CENTER);

        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filterTable(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filterTable(); }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filterTable(); }

            private void filterTable() {
                String text = searchField.getText().toLowerCase();
                TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
                emploiTable.setRowSorter(sorter);
                sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
            }
        });

        mainPanel.add(formPanel, BorderLayout.WEST);
        mainPanel.add(rightPanel, BorderLayout.CENTER);

        return mainPanel;
    }
    
    private void viewemploi() throws SQLException {
        ViewEmploiUI emploi = new ViewEmploiUI();
        emploi.setVisible(true);
        this.dispose();
    }

    private void makeFullWidth(JButton button) {
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
    }
    
    private JComboBox createComboBox() {
        JComboBox combo = new JComboBox<>();
        combo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        return combo;
    }

    private JTextField createTextField(String placeholder) {
        JTextField tf = new JTextField();
        tf.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tf.setBorder(BorderFactory.createTitledBorder(placeholder));
        return tf;
    }

    private JPanel createLabeledPanel(String labelText, JComponent comp) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        JLabel label = new JLabel(labelText + ": ");
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        panel.add(label, BorderLayout.WEST);
        panel.add(comp, BorderLayout.CENTER);
        panel.setOpaque(false);
        return panel;
    }

    private JButton createStyledButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(41, 128, 185));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setMinimumSize(new Dimension(Integer.MAX_VALUE, 40));
        return btn;
    }

    private void chargerCombos() {
        try {
            cbSalle.removeAllItems();
            List<Salle> salles = salleService.getToutesLesSalles();
            for (Salle s : salles) cbSalle.addItem(s.getIdsalle());

            cbProf.removeAllItems();
            List<Professeur> profs = profService.getTousLesProfs();
            for (Professeur p : profs) cbProf.addItem(new ComboItemProf(p.getIdprof(), p.getNom()));

            cbClasse.removeAllItems();
            List<Classe> classes = classeService.readAllClasses();
            for (Classe c : classes) cbClasse.addItem(new ComboItemClasse(c.getIdclasse(), c.getNiveau()));

        } catch (SQLException e) {
            showError("Erreur chargement listes : " + e.getMessage());
        }
    }

    private void chargerTable() {
        try {
            tableModel.setRowCount(0);
            List<EmploiDuTemps> emplois = emploiService.getTousLesEmplois();
            for (EmploiDuTemps e : emplois) {
                tableModel.addRow(new Object[]{
                        e.getIdsalle(),
                        e.getIdprof(),
                        e.getIdclasse(),
                        e.getCours(),
                        e.getDate().format(dtf)
                });
            }
            clearForm();
        } catch (SQLException e) {
            showError("Erreur chargement table : " + e.getMessage());
        }
    }

    private void ajouterEmploi() throws Exception {
    	String classeValue = "", profValue = "";
        if (!verifierChamps()) return;
        EmploiDuTemps e = getEmploiDepuisForm();
        String message = "";
        if (emploiService.CheckSalle(e)) {
        	message = "Cette salle est déjà pris pendant cette heure";
        	showMessage(message);
        	return;
        }
        emploiService.ajouterEmploi(e);
        chargerTable();
        showMessage("Emploi ajouté !"); 
    }

    private void modifierEmploi() { 
        int selectedRow = emploiTable.getSelectedRow();
        if (selectedRow == -1) {
            showMessage("Veuillez sélectionner un emploi du temps à modifier.");
            return;
        }
        if (!verifierChamps()) return;

        try {
            int oldIdsalle = (Integer) tableModel.getValueAt(selectedRow, 0);
            String oldIdprof = (String) tableModel.getValueAt(selectedRow, 1);
            String oldIdclasse = (String) tableModel.getValueAt(selectedRow, 2);
            LocalDateTime oldDate = LocalDateTime.parse((String) tableModel.getValueAt(selectedRow, 4), dtf);

            EmploiDuTemps nouveau = getEmploiDepuisForm();

            if (emploiService.modifierEmploi(oldIdsalle, oldIdprof, oldIdclasse, oldDate, nouveau)) {
                chargerTable();
                showMessage("Emploi du temps modifié !");
            } else {
                showError("Erreur : emploi du temps non trouvé.");
            }
        } catch (Exception ex) {
            showError("Erreur de la modification : " + ex.getMessage());
        }
    }

    private void supprimerEmploi() {
        int selectedRow = emploiTable.getSelectedRow();
        if (selectedRow == -1) {
            showMessage("Veuillez sélectionner un emploi du temps à supprimer.");
            return;
        }

        try {
            int ids = (Integer) tableModel.getValueAt(selectedRow, 0);
            String ip = (String) tableModel.getValueAt(selectedRow, 1);
            String ic = (String) tableModel.getValueAt(selectedRow, 2);
            LocalDateTime dt = LocalDateTime.parse((String) tableModel.getValueAt(selectedRow, 4), dtf);

            
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Supprimer l'emploi du temps : " + ids + " " + ip + " " + ic+ " " + dt + " ?", "Confirmation", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                if (emploiService.supprimerEmploi(ids, ip, ic, dt)) {
                    chargerTable();
                    showMessage("Emploi du temps supprimé !");
                } else {
                    showError("Échec de la suppression.");
                }
            }
            
        } catch (Exception e) {
            showError("Erreur de la suppression : " + e.getMessage());
        }
    }

    private void remplirChamps(int row) {
        try {
        	cbSalle.setSelectedItem((Integer) tableModel.getValueAt(row, 0));
            
        	String idProfFromTable = (String) tableModel.getValueAt(row, 1);

        	for (int i = 0; i < cbProf.getItemCount(); i++) {
        	    ComboItemProf item = cbProf.getItemAt(i);
        	    if (item.getId().equals(idProfFromTable)) {
        	        cbProf.setSelectedIndex(i);
        	        break;
        	    }
        	}
        	
        	String idClasseFromTable = (String) tableModel.getValueAt(row, 2);

        	for (int i = 0; i < cbClasse.getItemCount(); i++) {
        	    ComboItemClasse item = cbClasse.getItemAt(i);
        	    if (item.getId().equals(idClasseFromTable)) {
        	    	cbClasse.setSelectedIndex(i);
        	        break;
        	    }
        	}
            
        	tfCours.setText((String) tableModel.getValueAt(row, 3));

            String dateHeure = (String) tableModel.getValueAt(row, 4);
            LocalDateTime ldt = LocalDateTime.parse(dateHeure, dtf);
            Date date = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());

            dateChooser.setDate(date);
            cbHeure.setSelectedItem(String.format("%02d:%02d", ldt.getHour(), ldt.getMinute()));
        } catch (Exception e) {
            showError("Erreur chargement champs : " + e.getMessage());
        }
    }

    private EmploiDuTemps getEmploiDepuisForm() throws Exception {
    	String idProf = "", idClasse = "";
        Date d = dateChooser.getDate();
        if (d == null) throw new Exception("Date obligatoire");
        String heure = (String) cbHeure.getSelectedItem();
        if (heure == null) throw new Exception("Heure obligatoire");
        String[] parts = heure.split(":");

        LocalDateTime dateTime = d.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
                .withHour(Integer.parseInt(parts[0]))
                .withMinute(Integer.parseInt(parts[1]))
                .withSecond(0)
                .withNano(0);
        
        ComboItemProf selectedProf = (ComboItemProf) cbProf.getSelectedItem();
        if (selectedProf != null) {
            idProf = selectedProf.getId();
        }

        ComboItemClasse selectedClasse = (ComboItemClasse) cbClasse.getSelectedItem();
        if (selectedClasse != null) {
            idClasse = selectedClasse.getId();
        }

        return new EmploiDuTemps(
                (Integer) cbSalle.getSelectedItem(),
                idProf,
                idClasse,
                tfCours.getText().trim(),
                dateTime
        );
    }

    private boolean verifierChamps() {
        if (cbSalle.getSelectedItem() == null) {
            showMessage("Veuillez choisir une salle.");
            return false;
        }
        if (cbProf.getSelectedItem() == null) {
            showMessage("Veuillez choisir un professeur.");
            return false;
        }
        if (cbClasse.getSelectedItem() == null) {
            showMessage("Veuillez choisir une classe.");
            return false;
        }
        if (tfCours.getText().trim().isEmpty()) {
            showMessage("Le champ cours est vide.");
            return false;
        }
        if (dateChooser.getDate() == null) {
            showMessage("Veuillez choisir une date.");
            return false;
        }
        if (cbHeure.getSelectedItem() == null) {
            showMessage("Veuillez choisir une heure.");
            return false;
        }
        return true;
    }

    private void clearForm() {
        cbSalle.setSelectedIndex(-1);
        cbProf.setSelectedIndex(-1);
        cbClasse.setSelectedIndex(-1);
        tfCours.setText("");
        dateChooser.setDate(null);
        cbHeure.setSelectedIndex(0);
        emploiTable.clearSelection();
    }

    private void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Info", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Erreur", JOptionPane.ERROR_MESSAGE);
    }

}
