package Interface;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

import javax.swing.*;

import com.toedter.calendar.JDateChooser;

import java_project.EDTStyleOriginal;
import java_project.RoundedImagePanel;

public class EmploiExport extends JFrame {
	
	private JDateChooser DateEmploi;
	private JButton ExportBtn, CancelBtn;
	private String DateInfo = "";
	private JLabel EmploiLabel;
	
	public EmploiExport() {
        setSize(450, 600);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.decode("#ecf0f1"));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        JPanel formPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
        formPanel.setSize(new Dimension(400, 300));

        JPanel Ptitle = new JPanel(new BorderLayout());
        Ptitle.setPreferredSize(new Dimension(325, 50));
        Ptitle.setBackground(new Color(41, 128, 185));
        
        JLabel title = new JLabel("Exportation en PDF", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(Color.decode("#ffffff"));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        Ptitle.add(title);
        formPanel.add(Ptitle);
        
        DateEmploi = new JDateChooser();
        DateEmploi.setDateFormatString("yyyy-MM-dd");
        DateEmploi.setFont(new Font("Calibri", Font.BOLD, 14));
        DateEmploi.setPreferredSize(new Dimension(325, 40));
        DateEmploi.addPropertyChangeListener("date", evt -> afficherEmploi());
        formPanel.add(Box.createRigidArea(new Dimension(30, 20)));
        formPanel.add(DateEmploi);

        formPanel.add(Box.createRigidArea(new Dimension(300, 20)));
        
        EmploiLabel = new JLabel("Emploi du Temps", SwingConstants.CENTER);
        EmploiLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        EmploiLabel.setFont(new Font("Calibri", Font.PLAIN, 18));
        EmploiLabel.setForeground(new Color(41, 128, 185)); 
        formPanel.add(Box.createVerticalGlue());
        formPanel.add(EmploiLabel);
        
        ImageIcon icon = new ImageIcon(getClass().getResource("/images/ExportEmploi.jpg"));
        Image img = icon.getImage().getScaledInstance(600, 400, Image.SCALE_SMOOTH);
        RoundedImagePanel imagePanel = new RoundedImagePanel(img);
        imagePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        imagePanel.setPreferredSize(new Dimension(325, 200));
        imagePanel.setMaximumSize(new Dimension(325, 200));

        formPanel.add(imagePanel);    
        
        ExportBtn = createStyledButton("Exporter en PDF");
        ExportBtn.addActionListener(e -> PGenerer(DateEmploi.getDate()));
        CancelBtn = createStyledButton("Annuler");
        CancelBtn.addActionListener(e -> this.dispose());
        
        formPanel.add(Box.createRigidArea(new Dimension(300, 20)));
        formPanel.add(ExportBtn);
        formPanel.add(CancelBtn);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        add(mainPanel, BorderLayout.CENTER);
	}
	
	private void afficherEmploi() {
		if (DateEmploi.getDate() != null) {
			Date selected = DateEmploi.getDate();
			LocalDate date = selected.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	        LocalDate lundi = date.with(DayOfWeek.MONDAY);
	        LocalDate dimanche = date.with(DayOfWeek.SUNDAY);
	        
	        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.FRENCH);
	        DateInfo = "Emploi du Temps du " + fmt.format(lundi);
	        
	        EmploiLabel.setText(DateInfo);
		}
	}
	
	private void PGenerer(Date date) {
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        if (EDTStyleOriginal.Generer(localDate)) {
        	JOptionPane.showMessageDialog(this, "PDF generer avec succées !");
        } else {
        	JOptionPane.showMessageDialog(this, "Une erreur s'est produit lors de la generation !");
        }
	}
	
    private JButton createStyledButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(41, 128, 185));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setPreferredSize(new Dimension(160, 40));
        return btn;
    }

    private void Expoter() {
    	
    }
	
    private JComboBox createComboBox() {
        JComboBox combo = new JComboBox<>();
        combo.setPreferredSize(new Dimension(325, 40));
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        return combo;
    }

}
