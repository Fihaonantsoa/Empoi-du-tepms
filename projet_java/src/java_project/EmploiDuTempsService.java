package java_project;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

public class EmploiDuTempsService {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public void ajouterEmploi(EmploiDuTemps emploi) throws SQLException {
    	
    	String sql = "INSERT INTO EMPLOI_DU_TEMPS (idsalle, idprof, idclasse, cours, date) VALUES (?, ?, ?, ?, TO_TIMESTAMP(?, 'YYYY-MM-DD HH24:MI:SS'))";
        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, emploi.getIdsalle());
            stmt.setString(2, emploi.getIdprof());
            stmt.setString(3, emploi.getIdclasse());
            stmt.setString(4, emploi.getCours());
            stmt.setString(5, emploi.getDate().format(formatter));
            stmt.executeUpdate();
        }
    }
    
    public boolean modifierEmploi(int oldIdsalle, String oldIdprof, String oldIdclasse, LocalDateTime oldDate, EmploiDuTemps nouveau) throws SQLException {
        String sql = "UPDATE EMPLOI_DU_TEMPS SET idsalle = ?, idprof = ?, idclasse = ?, cours = ?, date = TO_TIMESTAMP(?, 'YYYY-MM-DD HH24:MI:SS')" +
                     "WHERE idsalle = ? AND idprof = ? AND idclasse = ? AND date = TO_TIMESTAMP(?, 'YYYY-MM-DD HH24:MI:SS')";
        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, nouveau.getIdsalle());
            stmt.setString(2, nouveau.getIdprof());
            stmt.setString(3, nouveau.getIdclasse());
            stmt.setString(4, nouveau.getCours());
            stmt.setString(5, nouveau.getDate().format(formatter));
            
            stmt.setInt(6, oldIdsalle);
            stmt.setString(7, oldIdprof);
            stmt.setString(8, oldIdclasse);
            stmt.setString(9, oldDate.format(formatter));
            
            return stmt.executeUpdate() > 0;
        }
    }

    public EmploiDuTemps getEmploi(int idsalle, String idprof, String idclasse, LocalDateTime date) throws SQLException {
        String sql = "SELECT * FROM EMPLOI_DU_TEMPS WHERE idsalle = ? AND idprof = ? AND idclasse = ? AND date = ?";
        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idsalle);
            stmt.setString(2, idprof);
            stmt.setString(3, idclasse);
            stmt.setString(4, date.format(formatter));
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new EmploiDuTemps(
                    rs.getInt("idsalle"),
                    rs.getString("idprof"),
                    rs.getString("idclasse"),
                    rs.getString("cours"),
                    rs.getTimestamp("date").toLocalDateTime()
                );
            }
        }
        return null;
    }


    public List<EmploiDuTemps> getTousLesEmplois() throws SQLException {
        List<EmploiDuTemps> emplois = new ArrayList<>();
        String sql = "SELECT * FROM EMPLOI_DU_TEMPS";
        try (Connection conn = Database.getConnection(); Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                emplois.add(new EmploiDuTemps(
                    rs.getInt("idsalle"),
                    rs.getString("idprof"),
                    rs.getString("idclasse"),
                    rs.getString("cours"),
                    rs.getTimestamp("date").toLocalDateTime()
                ));
            }
        }
        return emplois;
    }
    
    public List<EmploiDuTemps> getTousLesEmploisFormat() throws SQLException {
        List<EmploiDuTemps> emplois = new ArrayList<>();
        String sql = "SELECT s.idsalle , concat(p.nom, ' ',p.prenom) as idprof, c.niveau as idclasse, e.cours, e.date FROM emploi_du_temps e join salle s on s.idsalle=e.idsalle join professeur p "
        		+ "on p.idprof=e.idprof join classe c on c.idclasse=e.idclasse";
        try (Connection conn = Database.getConnection(); Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                emplois.add(new EmploiDuTemps(
                    rs.getInt("idsalle"),
                    rs.getString("idprof"),
                    rs.getString("idclasse"),
                    rs.getString("cours"),
                    rs.getTimestamp("date").toLocalDateTime()
                ));
            }
        }
        return emplois;
    }

    public boolean supprimerEmploi(int idsalle, String idprof, String idclasse, LocalDateTime date) throws SQLException {
        String sql = "DELETE FROM EMPLOI_DU_TEMPS WHERE idsalle = ? AND idprof = ? AND idclasse = ? AND date = TO_TIMESTAMP(?, 'YYYY-MM-DD HH24:MI:SS')";
        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idsalle);
            stmt.setString(2, idprof);
            stmt.setString(3, idclasse);
            stmt.setString(4, date.format(formatter));
            return stmt.executeUpdate() > 0;
        }
    }
    
    public boolean CheckSalle(EmploiDuTemps emploi) throws SQLException {
    	String sql = "SELECT idsalle FROM EMPLOI_DU_TEMPS WHERE idsalle = ? AND date = TO_TIMESTAMP(?, 'YYYY-MM-DD HH24:MI:SS')";
        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, emploi.getIdsalle());
            stmt.setString(2, emploi.getDate().format(formatter));
            return stmt.executeQuery().next();
        }
    }
    
    public String CheckProf(EmploiDuTemps emploi) throws SQLException {
    	String sql = "SELECT idprof FROM EMPLOI_DU_TEMPS WHERE idprof = ? AND date = TO_TIMESTAMP(?, 'YYYY-MM-DD HH24:MI:SS')";
        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, emploi.getIdprof());
            stmt.setString(2, emploi.getDate().format(formatter));
            return stmt.executeQuery().toString();
        }
    }
    
    public String CheckClasse(EmploiDuTemps emploi) throws SQLException {
    	String sql = "SELECT idclasse FROM EMPLOI_DU_TEMPS WHERE idclasse = ? AND date = TO_TIMESTAMP(?, 'YYYY-MM-DD HH24:MI:SS')";
        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, emploi.getIdclasse());
            stmt.setString(2, emploi.getDate().format(formatter));
            return stmt.executeQuery().toString();
        }
    }

}
