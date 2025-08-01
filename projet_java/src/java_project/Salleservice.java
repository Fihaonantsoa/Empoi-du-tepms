package java_project;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.print.attribute.DateTimeSyntax;

public class Salleservice {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    public void ajouterSalle(Salle salle) throws SQLException {
        String sql = "INSERT INTO salle (idsalle, design, occupation) VALUES (?, ?, ?)";
        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, salle.getIdsalle());
            stmt.setString(2, salle.getDesign());
            stmt.setString(3, salle.getOccupation());
            stmt.executeUpdate();
        }
    }

    public Salle trouverSalleParId(int id) throws SQLException {
        String sql = "SELECT * FROM salle WHERE idsalle = ?";
        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Salle(rs.getInt("idsalle"), rs.getString("design"), rs.getString("occupation"));
            }
        }
        return null;
    }

    public List<Salle> getToutesLesSalles() throws SQLException {
        List<Salle> salles = new ArrayList<>();
        String sql = "SELECT * FROM salle ORDER BY occupation";
        try (Connection conn = Database.getConnection(); Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                salles.add(new Salle(rs.getInt("idsalle"), rs.getString("design"), rs.getString("occupation")));
            }
        }
        return salles;
    }

    public List<Salle> getToutesLesSallesByDate(LocalDateTime dateTime) throws SQLException {
        List<Salle> salles = new ArrayList<>();
        String sql = "SELECT DISTINCT s.idsalle, s.design, s.occupation "
        			+ "FROM salle s "
        			+ "WHERE idsalle NOT IN "
        			+ "(SELECT s.idsalle " 
        			+ "FROM salle s " 
        			+ "JOIN emploi_du_temps e ON s.idsalle = e.idsalle "
        			+ "WHERE e.date = ?)";
        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setTimestamp(1, Timestamp.valueOf(dateTime));
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                salles.add(new Salle(rs.getInt("idsalle"), rs.getString("design"), "libre"));
            }
        }
        return salles;
    }
    
    public List<Salle> getToutesLesSallesOccupe(LocalDateTime dateTime) throws SQLException {
        List<Salle> salles = new ArrayList<>();
        String sql = "SELECT DISTINCT s.idsalle, s.design, s.occupation " 
                	+ "FROM salle s " 
                	+ "JOIN emploi_du_temps e ON s.idsalle = e.idsalle "
                	+ "WHERE e.date = ?";
        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setTimestamp(1, Timestamp.valueOf(dateTime));
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                salles.add(new Salle(rs.getInt("idsalle"), rs.getString("design"), "occupée"));
            }
        }
        return salles;
    }
    
    public boolean modifierSalle(Salle salle) throws SQLException {
        String sql = "UPDATE salle SET design = ?, occupation = ? WHERE idsalle = ?";
        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, salle.getDesign());
            stmt.setString(2, salle.getOccupation());
            stmt.setInt(3, salle.getIdsalle());
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean supprimerSalle(int id) throws SQLException {
        String sql = "DELETE FROM salle WHERE idsalle = ?";
        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }
}
