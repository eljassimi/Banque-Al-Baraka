package main.com.albaraka.dao;

import main.com.albaraka.config.DatabaseConfig;
import main.com.albaraka.entity.Compte;
import main.com.albaraka.entity.CompteCourant;
import main.com.albaraka.entity.CompteEpargne;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CompteDAO {

    public void insert(Compte compte) {
        String sql = "INSERT INTO compte (numero, solde, idClient, typeCompte, decouvertAutorise, tauxInteret) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection c = DatabaseConfig.getConnection();
             PreparedStatement stmt = c.prepareStatement(sql)) {

            stmt.setString(1, compte.getNumero());
            stmt.setDouble(2, compte.getSolde());
            stmt.setLong(3, compte.getIdClient());

            if (compte instanceof CompteCourant cc) {
                stmt.setString(4, "COURANT");
                stmt.setDouble(5, cc.getDecouvertAutorise());
                stmt.setNull(6, Types.DOUBLE);
            } else if (compte instanceof CompteEpargne ce) {
                stmt.setString(4, "EPARGNE");
                stmt.setNull(5, Types.DOUBLE);
                stmt.setDouble(6, ce.getTauxInteret());
            }

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Echec de l'insertion du compte");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(Compte compte) {
        String sql = "UPDATE compte SET numero = ?, solde = ?, idClient = ?, typeCompte = ?, decouvertAutorise = ?, tauxInteret = ? WHERE id = ?";
        try (Connection c = DatabaseConfig.getConnection();
             PreparedStatement stmt = c.prepareStatement(sql)) {

            stmt.setString(1, compte.getNumero());
            stmt.setDouble(2, compte.getSolde());
            stmt.setLong(3, compte.getIdClient());

            if (compte instanceof CompteCourant cc) {
                stmt.setString(4, "COURANT");
                stmt.setDouble(5, cc.getDecouvertAutorise());
                stmt.setNull(6, Types.DOUBLE);
            } else if (compte instanceof CompteEpargne ce) {
                stmt.setString(4, "EPARGNE");
                stmt.setNull(5, Types.DOUBLE);
                stmt.setDouble(6, ce.getTauxInteret());
            }

            stmt.setLong(7, compte.getId());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Échec de la mise à jour, aucun compte trouvé avec cet id.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean delete(Long id) throws SQLException {
        String sql = "DELETE FROM compte WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    public List<Compte> findAll() throws SQLException {
        List<Compte> comptes = new ArrayList<>();

        String sql = "SELECT * FROM compte";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                comptes.add(createCompteFromResultSet(rs));
            }
        }
        return comptes;
    }

    public Optional<Compte> findById(Long id) throws SQLException {
        String sql = "SELECT * FROM compte WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(createCompteFromResultSet(rs));
                }
                return Optional.empty();
            }
        }
    }

    private Compte createCompteFromResultSet(ResultSet rs) throws SQLException {
        long id = rs.getLong("id");
        String numero = rs.getString("numero");
        double solde = rs.getDouble("solde");
        long idClient = rs.getLong("idClient");
        String type = rs.getString("typeCompte");

        if ("COURANT".equalsIgnoreCase(type)) {
            double decouvert = rs.getDouble("decouvertAutorise");
            return new CompteCourant(numero, solde, idClient, decouvert);
        } else if ("EPARGNE".equalsIgnoreCase(type)) {
            double tauxInteret = rs.getDouble("tauxInteret");
            return new CompteEpargne(numero, solde, idClient, tauxInteret);
        } else {
            throw new SQLException("Type de compte inconnu: " + type);
        }
    }
}
