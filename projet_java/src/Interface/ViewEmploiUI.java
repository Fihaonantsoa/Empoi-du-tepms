package Interface;

import java_project.EmploiDuTemps;
import java_project.EmploiDuTempsService;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import com.toedter.calendar.JDateChooser;
import java.awt.*;
import java.sql.SQLException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

public class ViewEmploiUI extends JFrame {

    private JTable table;
    private DefaultTableModel model;
    private JComboBox<String> classeCombo;
    private EmploiDuTempsService service;
    private JDateChooser dateChooser;
    private JLabel titreLabel;

    private final String[] jours = {"Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi", "Samedi"};
    private final String[] horaires = {"08h–10h", "10h–12h", "14h–16h", "16h–18h"};

    public ViewEmploiUI() throws SQLException {
    	setJMenuBar(new MenuBarUI());
        setTitle("Emploi du Temps de la Classe");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        service = new EmploiDuTempsService();

        add(Entete(), BorderLayout.NORTH);
        add(createMainPanel(), BorderLayout.CENTER);
        
        chargerClasses();
    }

    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(52, 73, 94));
        header.setPreferredSize(new Dimension(getWidth(), 80));

        titreLabel = new JLabel("Emploi du Temps", SwingConstants.CENTER);
        titreLabel.setForeground(Color.WHITE);
        titreLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titreLabel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10));

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        titlePanel.add(titreLabel, BorderLayout.WEST);

        JButton backBtn = new JButton("← Retour à l'accueil");
        backBtn.setBackground(new Color(41, 128, 185));
        backBtn.setForeground(Color.WHITE);
        backBtn.setFocusPainted(false);
        backBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backBtn.addActionListener(e -> {
        	AccueillUI main;
			try {
				main = new AccueillUI();
				main.setVisible(true);
	        	this.dispose();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}

        });
        backBtn.setMaximumSize(new Dimension(200, 40));

        JPanel Pbtn = new JPanel();
        Pbtn.setBackground(new Color(52, 73, 94));
        Pbtn.add(backBtn);
        
        header.add(titlePanel, BorderLayout.CENTER);
        header.add(Pbtn, BorderLayout.EAST);

        return header;
    }

    private JPanel createMiddlePanel() {
        JPanel middle = new JPanel(new BorderLayout());
        middle.setPreferredSize(new Dimension(getWidth(), 50));
        
        classeCombo = new JComboBox<>();
        classeCombo.setPreferredSize(new Dimension(200, 40));

        dateChooser = new JDateChooser();
        dateChooser.setPreferredSize(new Dimension(240, 40));
        dateChooser.setDate(new Date());

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setOpaque(false);
        rightPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));
        rightPanel.add(new JLabel("Classe : "));
        rightPanel.add(classeCombo);
        rightPanel.add(new JLabel("Date : "));
        rightPanel.add(dateChooser);

        classeCombo.addActionListener(e -> rechargerAffichage());
        dateChooser.addPropertyChangeListener("date", evt -> rechargerAffichage());
        
        middle.add(rightPanel, BorderLayout.EAST);
        
        return middle;
    }
    
    private JPanel Entete() {
    	JPanel entete = new JPanel(new BorderLayout());
    	
    	entete.add(createHeaderPanel(), BorderLayout.NORTH);
    	entete.add(createMiddlePanel(), BorderLayout.SOUTH);
    	
    	return entete;
    }
    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());

        model = new DefaultTableModel(horaires, 0);
        for (String jour : jours) {
            model.addRow(new Object[horaires.length]);
        }
        model.setColumnIdentifiers(horaires);

        table = new JTable(model);
        table.setRowHeight(100);
        table.setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.setDefaultRenderer(Object.class, new MultiLineCellRenderer());
        DefaultTableModel rowModel = new DefaultTableModel(new Object[]{"Jours"}, 0);
        for (String jour : jours) {
            rowModel.addRow(new Object[]{jour});
        }
        JTable rowTable = new JTable(rowModel);
        rowTable.setRowHeight(table.getRowHeight());
        rowTable.setEnabled(false);
        rowTable.setPreferredScrollableViewportSize(new Dimension(80, 0));
        rowTable.setFont(new Font("Segoe UI", Font.BOLD, 13));
        rowTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {{
            setHorizontalAlignment(SwingConstants.CENTER);
        }});

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setRowHeaderView(rowTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Emploi du temps"));

        mainPanel.add(scrollPane, BorderLayout.CENTER);
        return mainPanel;
    }
    
        
    private void chargerClasses() {
        try {
            List<EmploiDuTemps> tous = service.getTousLesEmploisFormat();
            Set<String> classes = new TreeSet<>();
            for (EmploiDuTemps emploi : tous) {
                classes.add(emploi.getIdclasse());
            }
            for (String id : classes) {
                classeCombo.addItem(id);
            }
            if (!classes.isEmpty()) {
                classeCombo.setSelectedIndex(0);
                rechargerAffichage();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur : " + e.getMessage());
        }
    }

    private void rechargerAffichage() {
        String idclasse = (String) classeCombo.getSelectedItem();
        if (idclasse != null && dateChooser.getDate() != null) {
            afficherEmploi(idclasse);
        }
    }

    private void afficherEmploi(String idclasse) {
        try {
            List<EmploiDuTemps> tous = service.getTousLesEmploisFormat();
            Date selected = dateChooser.getDate();
            LocalDate date = selected.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate lundi = date.with(DayOfWeek.MONDAY);
            LocalDate dimanche = date.with(DayOfWeek.SUNDAY);

            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.FRENCH);
            titreLabel.setText("Emploi du Temps — " + idclasse + " — semaine du " + fmt.format(lundi));

            for (int i = 0; i < jours.length; i++) {
                for (int j = 0; j < horaires.length; j++) {
                    model.setValueAt("", i, j);
                }
            }

            for (EmploiDuTemps emploi : tous) {
                if (!emploi.getIdclasse().equals(idclasse)) continue;
                LocalDateTime dt = emploi.getDate();
                LocalDate jourCours = dt.toLocalDate();
                if (jourCours.isBefore(lundi) || jourCours.isAfter(dimanche)) continue;

                int jour = dt.getDayOfWeek().getValue() - 1;
                int col = getCreneau(dt.getHour());

                if (jour >= 0 && jour < jours.length && col != -1) {
                    String contenu = emploi.getCours() + "\n" + emploi.getIdprof() + "\nSalle " + emploi.getIdsalle();
                    String old = (String) model.getValueAt(jour, col);
                    model.setValueAt((old == null || old.isEmpty()) ? contenu : old + "\n———\n" + contenu, jour, col);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur affichage emploi : " + e.getMessage());
        }
    }

    private int getCreneau(int heure) {
        return switch (heure) {
            case 8 -> 0;
            case 10 -> 1;
            case 14 -> 2;
            case 16 -> 3;
            default -> -1;
        };
    }

    static class MultiLineCellRenderer extends JTextArea implements TableCellRenderer {
        public MultiLineCellRenderer() {
            setLineWrap(true);
            setWrapStyleWord(true);
            setOpaque(true);
        }

        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            setText(value == null ? "" : value.toString());
            setFont(table.getFont());
            setBackground(isSelected ? table.getSelectionBackground() : Color.WHITE);
            return this;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new ViewEmploiUI().setVisible(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }
}
