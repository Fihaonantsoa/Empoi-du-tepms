package Interface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.sql.SQLException;

public class MenuBarUI extends JMenuBar {

    public MenuBarUI() {
        
    	UIManager.put("Menu.font", new Font("Segoe UI", Font.PLAIN, 12));
        UIManager.put("MenuItem.font", new Font("Segoe UI", Font.PLAIN, 12));

        JMenu menuFichier = new JMenu("Fichier");
        menuFichier.setMnemonic(KeyEvent.VK_F);

        JMenuItem homeItem = createMenuItem("Accueil", KeyEvent.VK_H, e -> {
			try {
				Home();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		});
        JMenuItem profItem = createMenuItem("Gérer les Professeurs", KeyEvent.VK_P, e -> openProfesseurUI());
        JMenuItem salleItem = createMenuItem("Gérer les Salles", KeyEvent.VK_S, e -> {
			try {
				openSalleUI();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		});
        JMenuItem classeItem = createMenuItem("Gérer les Classes", KeyEvent.VK_C, e -> openClasseUI());
        JMenuItem emploiItem = createMenuItem("Emploi du temps", KeyEvent.VK_E, e -> openEmploiUI());
        JMenuItem quitterItem = createMenuItem("Quitter", KeyEvent.VK_Q, e -> System.exit(0));

        menuFichier.add(homeItem);
        menuFichier.addSeparator();
        menuFichier.add(profItem);
        menuFichier.add(salleItem);
        menuFichier.add(classeItem);
        menuFichier.addSeparator();
        menuFichier.add(emploiItem);
        menuFichier.addSeparator();
        menuFichier.add(quitterItem);

        JMenu menuOutils = new JMenu("Outils");
        menuOutils.setMnemonic(KeyEvent.VK_O);

        JMenuItem exportPDF = createMenuItem("Exporter en PDF", KeyEvent.VK_X, e -> exportToPDF());
        JMenuItem voirEmploi = createMenuItem("Voir Emploi du Temps", KeyEvent.VK_V, e -> {
			try {
				viewEmploiUI();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		});

        menuOutils.add(exportPDF);
        menuOutils.add(voirEmploi);

        add(menuFichier);
        add(menuOutils);
    }

    private JMenuItem createMenuItem(String text, int keyEvent, java.awt.event.ActionListener action) {
        JMenuItem item = new JMenuItem(text);
        item.setAccelerator(KeyStroke.getKeyStroke(keyEvent, KeyEvent.CTRL_DOWN_MASK));
        item.addActionListener(action);
        return item;
    }

    private void openProfesseurUI() {
        ProfesseurUI prof = new ProfesseurUI();
        prof.setVisible(true);
        fermerFenetreParente();
    }

    private void openSalleUI() throws SQLException {
        SalleUI salle = new SalleUI();
        salle.setVisible(true);
        fermerFenetreParente();
    }

    private void openClasseUI() {
        ClasseUI classe = new ClasseUI();
        classe.setVisible(true);
        fermerFenetreParente();
    }

    private void Home() throws SQLException {
        AccueillUI home = new AccueillUI();
        home.setVisible(true);
        fermerFenetreParente();
    }
    
    private void openEmploiUI() {
        EmploiUI emploi = new EmploiUI();
        emploi.setVisible(true);
        fermerFenetreParente();
    }
    
    private void viewEmploiUI() throws SQLException {
        ViewEmploiUI emploi = new ViewEmploiUI();
        emploi.setVisible(true);
        fermerFenetreParente();
    }

    private void exportToPDF() {
		try {
			EmploiExport Emploi = new EmploiExport();
			Emploi.setVisible(true);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
    }
    
    public void fermerFenetreParente() {
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        if (frame != null) {
            frame.dispose();
        }
    }
}