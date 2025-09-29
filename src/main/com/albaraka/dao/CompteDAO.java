package main.com.albaraka.dao;

import main.com.albaraka.config.DatabaseConfig;
import main.com.albaraka.entity.Compte;
import main.com.albaraka.entity.CompteCourant;
import main.com.albaraka.entity.CompteEpargne;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

public class CompteDAO {

    public void insert(Compte compte) {
        String sql = "INSERT INTO compte (numero, solde, idClient, typeCompte, decouvertAutorise, tauxInteret) VALUES (?, ?, ?, ?, ?, ?)";
        try(Connection c = DatabaseConfig.getConnection(); PreparedStatement stmt = c.prepareStatement(sql)){

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


}
