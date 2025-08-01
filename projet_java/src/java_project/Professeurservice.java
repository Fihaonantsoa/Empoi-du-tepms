package java_project;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Professeurservice {

    public void ajouterProfesseur(Professeur prof) throws SQLException {
        String sql = "INSERT INTO professeur (idprof, nom, prenom, grade) VALUES (?, ?, ?, ?)";
        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, prof.getIdprof());
            stmt.setString(2, prof.getNom());
            stmt.setString(3, prof.getPrenoms());
            stmt.setString(4, prof.getGrade());
            stmt.executeUpdate();
        }
    }

    public Professeur trouverProfParId(String idprof) throws SQLException {
        String sql = "SELECT * FROM professeur WHERE idprof = ?";
        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, idprof);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Professeur(
                    rs.getString("idprof"),
                    rs.getString("nom"),
                    rs.getString("prenom"),
                    rs.getString("grade")
                );
            }
        }
        return null;
    }

    public List<Professeur> getTousLesProfs() throws SQLException {
        List<Professeur> profs = new ArrayList<>();
        String sql = "SELECT * FROM professeur";
        try (Connection conn = Database.getConnection(); Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                profs.add(new Professeur(
                    rs.getString("idprof"),
                    rs.getString("nom"),
                    rs.getString("prenom"),
                    rs.getString("grade")
                ));
            }
        }
        return profs;
    }

    public boolean modifierProfesseur(Professeur prof) throws SQLException {
        String sql = "UPDATE professeur SET nom = ?, prenom = ?, grade = ? WHERE idprof = ?";
        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, prof.getNom());
            stmt.setString(2, prof.getPrenoms());
            stmt.setString(3, prof.getGrade());
            stmt.setString(4, prof.getIdprof());
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean supprimerProfesseur(String idprof) throws SQLException {
        String sql = "DELETE FROM professeur WHERE idprof = ?";
        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, idprof);
            return stmt.executeUpdate() > 0;
        }
    }
}
