package java_project;

import java.io.FileOutputStream;
import java.sql.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

public class EDTStyleOriginal {

    static String[] jours = {"Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi", "Samedi"};
    static String[] heures = {"08:00", "10:00", "14:00", "16:00"};

    public static boolean Generer(LocalDate dateEmploi) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter fmtPDF = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.FRENCH);
        DateTimeFormatter heureFormat = DateTimeFormatter.ofPattern("HH:mm");

        Document doc = new Document(PageSize.A4.rotate(), 20, 20, 20, 20);

        try {
            Connection conn = Database.getConnection();

            LocalDate lundi = dateEmploi.with(DayOfWeek.MONDAY);
            LocalDateTime debut = lundi.atTime(0, 0);
            LocalDateTime fin = debut.plusDays(6).withHour(23).withMinute(59).withSecond(59);

            PdfWriter.getInstance(doc, new FileOutputStream("PDF/EDT-DU-" + fmtPDF.format(lundi) + ".pdf"));
            doc.open();

            Statement stmt = conn.createStatement();
            ResultSet rsClasses = stmt.executeQuery("SELECT DISTINCT niveau FROM classe ORDER BY niveau");

            Font titleFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
            Font bold = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
            Font normal = new Font(Font.FontFamily.HELVETICA, 14);

            while (rsClasses.next()) {
                String classe = rsClasses.getString("niveau");

                Paragraph titre = new Paragraph("Emploi du temps - " + classe, titleFont);
                titre.setAlignment(Element.ALIGN_CENTER);
                titre.setSpacingAfter(15);
                doc.add(titre);

                PdfPTable table = new PdfPTable(heures.length + 1);
                table.setWidthPercentage(100);
                table.setWidths(new float[]{2f, 2f, 2f, 2f, 2f});

                PdfPCell enteteVide = new PdfPCell(new Phrase("", bold));
                enteteVide.setBorderColorTop(null);
                enteteVide.setBorderColorLeft(null);
                enteteVide.setBackgroundColor(BaseColor.LIGHT_GRAY);
                enteteVide.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(enteteVide);

                for (String heure : heures) {
                    PdfPCell heureCell = new PdfPCell(new Phrase(heure, bold));
                    heureCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    heureCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(heureCell);
                }

                String sql = """
                    SELECT e.date, e.cours, concat(p.nom, ' ',p.prenom) AS prof, s.idsalle AS salle
                    FROM emploi_du_temps e
                    JOIN professeur p ON e.idprof = p.idprof
                    JOIN salle s ON e.idsalle = s.idsalle
                    JOIN classe c ON e.idclasse = c.idclasse
                    WHERE c.niveau = ? 
                    AND e.date BETWEEN TO_TIMESTAMP(?, 'yyyy-MM-dd HH24:MI:SS') 
                                   AND TO_TIMESTAMP(?, 'yyyy-MM-dd HH24:MI:SS')
                """;

                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, classe);
                ps.setString(2, debut.format(formatter));
                ps.setString(3, fin.format(formatter));
                ResultSet rs = ps.executeQuery();

                Map<String, Map<String, String>> grille = new LinkedHashMap<>();
                for (String jour : jours) {
                    grille.put(jour, new LinkedHashMap<>());
                    for (String heure : heures) {
                        grille.get(jour).put(heure, "");
                    }
                }

                while (rs.next()) {
                    LocalDateTime ldt = rs.getTimestamp("date").toLocalDateTime();
                    String jour = jourFr(ldt.getDayOfWeek().toString());
                    String heure = heureFormat.format(ldt.toLocalTime());

                    if (grille.containsKey(jour) && grille.get(jour).containsKey(heure)) {
                        String contenu = rs.getString("cours") + "\n" +
                                         rs.getString("prof") + "\nSalle " + rs.getString("salle");
                        grille.get(jour).put(heure, contenu);
                    }
                }

                for (String jour : jours) {
                    PdfPCell jourCell = new PdfPCell(new Phrase(jour, bold));
                    jourCell.setBackgroundColor(BaseColor.WHITE);
                    jourCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    jourCell.setVerticalAlignment(Element.ALIGN_CENTER);
                    jourCell.setFixedHeight(70);
                    table.addCell(jourCell);

                    for (String heure : heures) {
                        PdfPCell cell = new PdfPCell(new Phrase(grille.get(jour).get(heure), normal));
                        cell.setFixedHeight(55);
                        table.addCell(cell);
                    }
                }

                doc.add(table);
                doc.newPage();
            }

            doc.close();
            conn.close();
            System.out.println("PDF généré dans le style original !");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static String jourFr(String en) {
        return switch (en.toLowerCase()) {
            case "monday" -> "Lundi";
            case "tuesday" -> "Mardi";
            case "wednesday" -> "Mercredi";
            case "thursday" -> "Jeudi";
            case "friday" -> "Vendredi";
            case "saturday" -> "Samedi";
            default -> en;
        };
    }
}
