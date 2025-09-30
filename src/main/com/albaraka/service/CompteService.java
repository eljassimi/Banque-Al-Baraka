package main.com.albaraka.service;

import main.com.albaraka.dao.CompteDAO;
import main.com.albaraka.dao.TransactionDAO;
import main.com.albaraka.entity.CompteCourant;
import main.com.albaraka.entity.CompteEpargne;

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

    public void creerCompteEpargne(String numero, double soldeInitial, Long idClient, double tauxInteret) throws SQLException {
        CompteEpargne compte = new CompteEpargne(numero, soldeInitial, idClient, tauxInteret);
        compteDAO.insert(compte);
    }

}
