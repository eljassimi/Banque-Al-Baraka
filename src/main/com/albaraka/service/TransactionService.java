package main.com.albaraka.service;

import main.com.albaraka.dao.TransactionDAO;
import main.com.albaraka.entity.Compte;
import main.com.albaraka.entity.Transaction;
import main.com.albaraka.entity.TypeTransaction;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class TransactionService {

    private final TransactionDAO transactionDAO;

    public TransactionService() {
        this.transactionDAO = new TransactionDAO();
    }

    public List<Transaction> listerTransactionsParCompte(Long idCompte) throws SQLException {
        return transactionDAO.findByCompte(idCompte).stream().sorted(Comparator.comparing(Transaction::date)).toList();
    }

    public List<Transaction> listerTransactionsParClient(Long idClient) throws SQLException {
        return transactionDAO.findByClient(idClient).stream().sorted(Comparator.comparing(Transaction::date)).toList();
    }

    public List<Transaction> filtrerParMontant(double montantMin) throws SQLException {
        return transactionDAO.findAll().stream().filter(e->e.montant()>=montantMin).collect(Collectors.toList());
    }

    public List<Transaction> filtrerParType(TypeTransaction type) throws SQLException {
        return transactionDAO.findAll().stream().filter(e->e.type().equals(type)).collect(Collectors.toList());
    }

    public List<Transaction> filtrerParDate(LocalDateTime date) throws SQLException {
        return transactionDAO.findAll().stream().filter(e->e.date().isAfter(date)).collect(Collectors.toList());
    }

    public List<Transaction> filtrerParLieu(String lieu) throws SQLException {
        return transactionDAO.findAll().stream().filter(e->e.lieu().equals(lieu)).collect(Collectors.toList());
    }

}
