package main.com.albaraka.dao;

import main.com.albaraka.config.DatabaseConfig;
import main.com.albaraka.entity.Transaction;
import main.com.albaraka.entity.TypeTransaction;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {

    public void insert(Transaction transaction) throws SQLException{
         String sql = "INSERT INTO transaction (date, montant, type, lieu, idCompte) VALUES (?, ?, ?, ?, ?)";
         try(Connection c = DatabaseConfig.getConnection(); PreparedStatement stmt = c.prepareStatement(sql)){

             stmt.setTimestamp(1, Timestamp.valueOf(transaction.date()));
             stmt.setDouble(2, transaction.montant());
             stmt.setString(3, transaction.type().name());
             stmt.setString(4, transaction.lieu());
             stmt.setLong(5, transaction.idCompte());

             int affectedRows = stmt.executeUpdate();
             if (affectedRows == 0) {
                 throw new SQLException("Echec de l'insertion du Transaction");
             }
         }
    }

    public void update(Transaction transaction) throws SQLException {
        String sql = "UPDATE transaction SET date = ?, montant = ?, type = ?, lieu = ?, idcompte = ? WHERE id = ?";

        try (Connection c = DatabaseConfig.getConnection();
             PreparedStatement stmt = c.prepareStatement(sql)) {

            stmt.setTimestamp(1, Timestamp.valueOf(transaction.date()));
            stmt.setDouble(2, transaction.montant());
            stmt.setString(3, transaction.type().name());
            stmt.setString(4, transaction.lieu());
            stmt.setLong(5, transaction.idCompte());
            stmt.setLong(6, transaction.id());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Echec update la transaction avec id=" + transaction.id());
            }
        }
    }

    public List<Transaction> findAll() throws SQLException {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transaction";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                transactions.add(createTransactionFromResultSet(rs));
            }
        }
        return transactions;
    }
    public List<Transaction> findByCompte(Long idCompte) throws SQLException {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transaction WHERE idcompte = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, idCompte);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    transactions.add(createTransactionFromResultSet(rs));
                }
            }
        }
        return transactions;
    }


    private Transaction createTransactionFromResultSet(ResultSet rs) throws SQLException {
        return new Transaction(
                rs.getLong("id"),
                rs.getTimestamp("date").toLocalDateTime(),
                rs.getDouble("montant"),
                TypeTransaction.valueOf(rs.getString("type")),
                rs.getString("lieu"),
                rs.getLong("idCompte")
        );
    }


}
