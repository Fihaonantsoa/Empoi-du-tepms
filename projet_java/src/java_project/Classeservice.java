package java_project;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Classeservice {
    private Connection connection;

    public Classeservice() throws SQLException {
        connection = Database.getConnection();
    }

    public void createClasse(Classe classe) throws SQLException {
        String sql = "INSERT INTO CLASSE (idclasse, niveau) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, classe.getIdclasse());
            stmt.setString(2, classe.getNiveau());
            stmt.executeUpdate();
        }
    }

    public Classe readClasse(String idclasse) throws SQLException {
        String sql = "SELECT * FROM CLASSE WHERE idclasse = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, idclasse);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Classe(rs.getString("idclasse"), rs.getString("niveau"));
                } else {
                    return null;
                }
            }
        }
    }

    public List<Classe> readAllClasses() throws SQLException {
        String sql = "SELECT * FROM CLASSE";
        List<Classe> classes = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                classes.add(new Classe(rs.getString("idclasse"), rs.getString("niveau")));
            }
        }
        return classes;
    }

    public void updateClasse(Classe classe) throws SQLException {
        String sql = "UPDATE CLASSE SET niveau = ? WHERE idclasse = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, classe.getNiveau());
            stmt.setString(2, classe.getIdclasse());
            stmt.executeUpdate();
        }
    }

    public void deleteClasse(String idclasse) throws SQLException {
        String sql = "DELETE FROM CLASSE WHERE idclasse = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, idclasse);
            stmt.executeUpdate();
        }
    }

    public void close() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}
