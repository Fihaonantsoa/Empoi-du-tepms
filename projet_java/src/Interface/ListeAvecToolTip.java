package Interface;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ListeAvecToolTip extends JFrame {
    private JTable table;

    // Exemple de données (simulées comme dans une base de données)
    private Object[][] donnees = {
        {"P001", "Ordinateur", "Un PC portable 16Go RAM"},
        {"P002", "Souris", "Souris optique sans fil"},
        {"P003", "Clavier", "Clavier mécanique rétroéclairé"},
        {"P004", "Écran", "Écran 24 pouces Full HD"}
    };

    private String[] colonnes = {"Code", "Nom", "Description"};

    public ListeAvecToolTip() {
        setTitle("Tableau avec ToolTip et Sélection");
        setSize(600, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        DefaultTableModel model = new DefaultTableModel(donnees, colonnes) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Rendre le tableau non modifiable
            }
        };

        table = new JTable(model) {
            @Override
            public String getToolTipText(MouseEvent e) {
                java.awt.Point p = e.getPoint();
                int row = rowAtPoint(p);
                int col = columnAtPoint(p);
                if (row > -1 && col > -1) {
                    return getValueAt(row, 2).toString(); // Montre la description dans le tooltip
                }
                return null;
            }
        };

        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setRowHeight(25);

        // Tooltip activé
        ToolTipManager.sharedInstance().registerComponent(table);

        // Récupérer valeur de la cellule sur clic
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int ligne = table.getSelectedRow();
                if (ligne > -1) {
                    String code = table.getValueAt(ligne, 0).toString();
                    String nom = table.getValueAt(ligne, 1).toString();
                    System.out.println("Produit sélectionné : " + code + " - " + nom);

                    // → Ici tu peux appeler une fonction pour interroger ta base de données
                    // ex : rechercherProduitParCode(code);
                }
            }
        });

        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ListeAvecToolTip().setVisible(true));
    }
}