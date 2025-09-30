package main.com.albaraka.service;

import main.com.albaraka.dao.TransactionDAO;
import main.com.albaraka.entity.Transaction;

import java.sql.SQLException;
import java.util.List;

public class TransactionService {

    private final TransactionDAO transactionDAO;

    public TransactionService() {
        this.transactionDAO = new TransactionDAO();
    }

    public List<Transaction> listerTransactionsParCompte(Long idCompte) throws SQLException {
        return transactionDAO.findByCompte(idCompte);
    }

    public List<Transaction> listerTransactionsParClient(Long idClient) throws SQLException {
        return transactionDAO.findByClient(idClient);
    }


}
