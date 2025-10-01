package main.com.albaraka.service;

import main.com.albaraka.dao.ClientDAO;
import main.com.albaraka.dao.CompteDAO;
import main.com.albaraka.dao.TransactionDAO;
import main.com.albaraka.entity.Client;
import main.com.albaraka.entity.Compte;
import main.com.albaraka.entity.Transaction;
import main.com.albaraka.entity.TypeTransaction;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class RapportService {

    private final ClientDAO clientDAO;
    private final CompteDAO compteDAO;
    private final TransactionDAO transactionDAO;
    private final TransactionService transactionService;


    public RapportService(ClientDAO clientDAO, CompteDAO compteDAO, TransactionDAO transactionDAO, TransactionService transactionService) {
        this.clientDAO = clientDAO;
        this.compteDAO = compteDAO;
        this.transactionDAO = transactionDAO;
        this.transactionService = transactionService;
    }

    public List<Client> topFiveClient() throws SQLException {
        List<Compte> comptes = compteDAO.findAll().stream().sorted(Comparator.comparing(Compte::getSolde)).limit(5).toList();
        List<Client> clients = new ArrayList<>();
        for (Compte compte : comptes) {
            clients.add(clientDAO.findById(compte.getIdClient()));
        }
        return clients;
    }

    public String GenerateRapportMonsuel(int mois, int annee) throws SQLException {
        LocalDateTime debutDeMois = LocalDateTime.of(annee,mois,1,0,0);
        LocalDateTime finDeMois = debutDeMois.plusMonths(1);

        List<Transaction> transactions  = transactionDAO.findAll().stream().filter(e->e.date().isAfter(debutDeMois) && e.date().isBefore(finDeMois)).toList();

        Map<TypeTransaction, List<Transaction>> parType =transactions.stream().collect(Collectors.groupingBy(Transaction::type));
        StringBuilder rapport = new StringBuilder();
        rapport.append(String.format("--- RAPPORT MENSUEL %d-%02d ===\n", annee, mois));

        rapport.append(String.format("--- RAPPORT PAR TYPE %d-%02d ===\n", annee, mois));
        parType.forEach((type, trans) -> {

            long nombre = trans.size();
            double total = trans.stream().mapToDouble(Transaction::montant).sum();
            double moyenne = trans.stream().mapToDouble(Transaction::montant).average().orElse(0.0);

            rapport.append(String.format("%s:\n", type));
            rapport.append(String.format("  Nombre: %d\n", nombre));
            rapport.append(String.format("  Total: %.2f €\n", total));
            rapport.append(String.format("  Moyenne: %.2f €\n\n", moyenne));

            long totalTransactions = transactions.size();
            double volumeTotal = transactions.stream().mapToDouble(Transaction::montant).sum();
            double moyenneGlobale = transactions.stream().mapToDouble(Transaction::montant).average().orElse(0.0);

            rapport.append("--- RESUME GLOBAL ---\n");
            rapport.append(String.format("Total des transactions: %d\n", totalTransactions));
            rapport.append(String.format("Volume total: %.2f €\n", volumeTotal));
            rapport.append(String.format("Moyenne par transaction: %.2f €\n", moyenneGlobale));


        });
            return rapport.toString();
    }

    public List<Transaction> detecterTransactionsSuspectes(double seuilMontant, String paysHabituel, int nombreMaxParMinute) throws SQLException {
        List<Transaction> suspectes = new ArrayList<>();

        List<Transaction> montantEleve = transactionService.detecterTransactionsSuspectes(seuilMontant);
        suspectes.addAll(montantEleve);

        List<Transaction> lieuxInhabituels = transactionService.detecterLieuxInhabituels(paysHabituel);
        suspectes.addAll(lieuxInhabituels);

        List<Compte> comptes = compteDAO.findAll();
        for (Compte compte : comptes) {
            List<Transaction> frequenceExcessive = transactionService.detecterFrequenceExcessive(
                    compte.getId(), nombreMaxParMinute);
            suspectes.addAll(frequenceExcessive);
        }

        return suspectes.stream()
                .sorted((t1, t2) -> t2.date().compareTo(t1.date()))
                .collect(Collectors.toList());
    }

    public List<Compte> identifierComptesInactifs(LocalDateTime seuilDate) throws SQLException {
        return compteDAO.findAll().stream()
                .filter(compte -> {
                    try {
                        return transactionDAO.findByCompte(compte.getId())
                                .stream()
                                .noneMatch(t -> t.date().isAfter(seuilDate));
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }).toList();
    }



}
