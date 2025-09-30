package main.com.albaraka.service;

import main.com.albaraka.dao.CompteDAO;
import main.com.albaraka.dao.TransactionDAO;
import main.com.albaraka.entity.CompteCourant;

import java.sql.SQLException;

public class CompteService {

    private final CompteDAO compteDAO;
    private final TransactionDAO transactionDAO;

    public CompteService() {
        this.compteDAO = new CompteDAO();
        this.transactionDAO = new TransactionDAO();
    }

    public void creerCompteCourant(String numero, double soldeInitial, Long idClient, double decouvertAutorise) throws SQLException {
        CompteCourant compte = new CompteCourant(numero, soldeInitial, idClient, decouvertAutorise);
        compteDAO.insert(compte);
    }

}
