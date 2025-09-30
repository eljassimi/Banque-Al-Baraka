package main.com.albaraka.dao;



import main.com.albaraka.config.DatabaseConfig;
import main.com.albaraka.entity.Client;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientDAO {
    
    public void insert(Client client) throws SQLException {
        String sql = "INSERT INTO client (name, email) VALUES (?, ?)";
        try (Connection c = DatabaseConfig.getConnection(); PreparedStatement stmt = c.prepareStatement(sql)) {
            stmt.setString(1, client.name());
            stmt.setString(2, client.email());
            stmt.executeUpdate();
        }
    }

    public void update(Client client) throws SQLException {
        String sql = "UPDATE client SET nom = ?, email = ? WHERE id = ?";
        try (Connection c = DatabaseConfig.getConnection(); PreparedStatement stmt = c.prepareStatement(sql)) {
            stmt.setString(1, client.name());
            stmt.setString(2, client.email());
            stmt.setLong(3, client.id());
        }
    }

    public void delete(Long id) throws SQLException {
        String sql = "DELETE FROM client WHERE id = ?";
        try (Connection c = DatabaseConfig.getConnection(); PreparedStatement stmt = c.prepareStatement(sql)) {
            stmt.setLong(1, id);
        }
    }
    public List<Client> findAll() throws SQLException {
        String sql = "SELECT * FROM client ORDER BY id";
        List<Client> clients = new ArrayList<>();
        try (Connection c = DatabaseConfig.getConnection(); PreparedStatement stmt = c.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Client client = new Client(rs.getLong("id"), rs.getString("name"), rs.getString("email"));
                clients.add(client);
            }
        }
        return clients;
    }

    public Client findById(long id) throws SQLException {
        String sql = "SELECT * FROM client WHERE id = ?";
        try (Connection c = DatabaseConfig.getConnection(); PreparedStatement stmt = c.prepareStatement(sql)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Client(rs.getLong("id"), rs.getString("name"), rs.getString("email"));
            }
        }
        return null;
    }
}
