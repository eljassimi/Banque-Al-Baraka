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
        String sql = "INSERT INTO compte (id,numero, solde, idclient, typecompte, decouvertautorise, tauxinteret) VALUES (?,?, ?, ?, ?::type_compte, ?, ?)";
        try (Connection c = DatabaseConfig.getConnection();
             PreparedStatement stmt = c.prepareStatement(sql)) {

            stmt.setLong(1,compte.getId());
            stmt.setString(2, compte.getNumero());
            stmt.setDouble(3, compte.getSolde());
            stmt.setLong(4, compte.getIdClient());

            if (compte instanceof CompteCourant cc) {
                stmt.setString(5, "COURANT");
                stmt.setDouble(6, cc.getDecouvertAutorise());
                stmt.setNull(7, Types.DOUBLE);
            } else if (compte instanceof CompteEpargne ce) {
                stmt.setString(5, "EPARGNE");
                stmt.setNull(6, Types.DOUBLE);
                stmt.setDouble(7, ce.getTauxInteret());
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

    public Optional<Compte> findByNumero(String numero)throws SQLException{
        String sql = "SELECT * FROM compte where numero = ?";
        try(Connection c = DatabaseConfig.getConnection();PreparedStatement ps = c.prepareStatement(sql)){
            ps.setString(1,numero);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return Optional.of(createCompteFromResultSet(rs));
            }
            return Optional.empty();
        }
    }

    public boolean updateSolde(Long id, double nouveauSolde) throws SQLException {
        String sql = "UPDATE compte SET solde = ? WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, nouveauSolde);
            stmt.setLong(2, id);

            return stmt.executeUpdate() > 0;
        }
    }

    public boolean updateParametres(Long id, Double decouvertAutorise, Double tauxInteret) throws SQLException {
        String sql = "UPDATE compte SET decouvertAutorise = ?, tauxInteret = ? WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, decouvertAutorise);
            stmt.setObject(2, tauxInteret);
            stmt.setLong(3, id);

            return stmt.executeUpdate() > 0;
        }
    }

    public List<Compte> findByClient(Long idClient) throws SQLException {
        String sql = "SELECT * FROM compte WHERE idClient = ?";
        List<Compte> comptes = new ArrayList<>();

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, idClient);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    comptes.add(createCompteFromResultSet(rs));
                }
            }
        }
        return comptes;
    }

    private Compte createCompteFromResultSet(ResultSet rs) throws SQLException {
        long id = rs.getLong("id");
        String numero = rs.getString("numero");
        double solde = rs.getDouble("solde");
        long idClient = rs.getLong("idClient");
        String type = rs.getString("typeCompte");

        if ("COURANT".equalsIgnoreCase(type)) {
            double decouvert = rs.getDouble("decouvertAutorise");
            return new CompteCourant(id,numero, solde, idClient, decouvert);
        } else if ("EPARGNE".equalsIgnoreCase(type)) {
            double tauxInteret = rs.getDouble("tauxInteret");
            return new CompteEpargne(id,numero, solde, idClient, tauxInteret);
        } else {
            throw new SQLException("Type de compte inconnu: " + type);
        }
    }
}
