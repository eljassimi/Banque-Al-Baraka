package main.com.albaraka.ui;

import main.com.albaraka.entity.Transaction;
import main.com.albaraka.entity.TypeTransaction;
import main.com.albaraka.service.TransactionService;
import main.com.albaraka.util.FormatUtil;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class MenuTransaction {
    private final TransactionService transactionService;
    Scanner sc = new Scanner(System.in);

    public MenuTransaction() {
        this.transactionService = new TransactionService();
    }

    public void afficher() {
        boolean continuer = true;

        while (continuer) {
            System.out.println("======= GESTION DES TRANSACTIONS =======");
            System.out.println("1. Lister les transactions d'un compte");
            System.out.println("2. Lister les transactions d'un client");
            System.out.println("3. Filtrer par montant minimum");
            System.out.println("4. Filtrer par type");
            System.out.println("5. Filtrer par plage de dates");
            System.out.println("6. Filtrer par lieu");
            System.out.println("7. Regrouper par type");
            System.out.println("8. Regrouper par période");
            System.out.println("9. Statistiques par client");
            System.out.println("10. Statistiques par compte");
            System.out.println("11. Détecter transactions suspectes");
            System.out.println("12. Statistiques globales");
            System.out.println("0. Retour au menu principal");

            int choix = sc.nextInt();

            try {
                switch (choix) {
                    case 1 -> listerParCompte();
                    case 2 -> listerParClient();
                    case 3 -> filtrerParMontant();
                    case 4 -> filtrerParType();
                    case 5 -> filtrerParDate();
                    case 6 -> filtrerParLieu();
                    case 7 -> regrouperParType();
                    case 8 -> regrouperParPeriode();
                    case 9 -> statistiquesParClient();
                    case 10 -> statistiquesParCompte();
                    case 11 -> detecterSuspectes();
                    case 12 -> afficherStatistiquesGlobales();
                    case 0 -> continuer = false;
                    default -> System.out.println("Choix invalide. Veuillez réessayer.");
                }
            } catch (Exception e) {
                System.err.println("Erreur: " + e.getMessage());
            }
        }
    }

    private void listerParCompte() throws SQLException {
        System.out.println("\n--- TRANSACTIONS D'UN COMPTE ---");

        System.out.println("ID du compte: ");
        Long idCompte = sc.nextLong();

        List<Transaction> transactions = transactionService.listerTransactionsParCompte(idCompte);
        afficherTransactions(transactions);
    }

    private void listerParClient() throws SQLException {
        System.out.println("\n--- TRANSACTIONS D'UN CLIENT ---");
        System.out.println("ID du client: ");
        Long idClient = sc.nextLong();

        List<Transaction> transactions = transactionService.listerTransactionsParClient(idClient);
        afficherTransactions(transactions);
    }

    public void filtrerParMontant()throws SQLException{
        System.out.println("\n--- TRANSACTIONS FILTRER PAR MONTANT ---");
        System.out.println("Entrer le Montant : ");
        Double montant = sc.nextDouble();
        List<Transaction> transactions = transactionService.filtrerParMontant(montant);
        afficherTransactions(transactions);
    }

    public void filtrerParType() throws SQLException{
        System.out.println("\n--- TRANSACTIONS FILTRER PAR TYPE ---");
        System.out.println("Entrer le Type: ");
        String type = sc.nextLine();
        List<Transaction> transactions = transactionService.filtrerParType(TypeTransaction.valueOf(type));
        afficherTransactions(transactions);
    }

    public void filtrerParDate() throws  SQLException{
        System.out.println("\n--- TRANSACTIONS FILTRER PAR DATE ---");
        System.out.println("Entrer le Date (yyyy-mm-jj): ");
        LocalDateTime date = LocalDateTime.parse(sc.nextLine());
        List<Transaction> transactions = transactionService.filtrerParDate(date);
        afficherTransactions(transactions);
    }

    public void filtrerParLieu() throws SQLException{
        System.out.println("\n--- TRANSACTIONS FILTRER PAR Lieu ---");
        System.out.println("Entrer le Lieu : ");
        String lieu = sc.nextLine();
        List<Transaction> transactions = transactionService.filtrerParLieu(lieu);
        afficherTransactions(transactions);
    }

    private void regrouperParType() throws SQLException {
        System.out.println("\n--- REGROUPEMENT PAR TYPE ---");

        transactionService.regrouperParType()
                .forEach((type, transactions) -> {
                    double total = transactions.stream().mapToDouble(Transaction::montant).sum();
                    System.out.printf("\n%s:\n  Nombre: %d\n  Total: %s\n  Moyenne: %s\n",
                            type,
                            transactions.size(),
                            FormatUtil.formaterMontant(total),
                            FormatUtil.formaterMontant(total / transactions.size()));
                });
    }

    private void regrouperParPeriode()throws Exception{
        System.out.println("\n--- REGROUPEMENT PAR Periode ---");
        transactionService.regrouperParType()
                .forEach((type, transactions) -> {
                    double total = transactions.stream().mapToDouble(Transaction::montant).sum();
                    System.out.printf("\n%s:\n  Nombre: %d\n  Total: %s\n  Moyenne: %s\n",
                            type,
                            transactions.size(),
                            FormatUtil.formaterMontant(total),
                            FormatUtil.formaterMontant(total / transactions.size()));
                });
    }


    private void afficherTransactions(List<Transaction> transactions) {
        if (transactions.isEmpty()) {
            System.out.println("Aucune transaction trouvée.");
            return;
        }

        System.out.println("Nombre de transactions: " + transactions.size());
        System.out.println();

        for (Transaction transaction : transactions) {
            System.out.println("ID       : " + transaction.id());
            System.out.println("Date     : " + FormatUtil.formaterDateTime(transaction.date()));
            System.out.println("Type     : " + transaction.type());
            System.out.println("Montant  : " + FormatUtil.formaterMontant(transaction.montant()));
            System.out.println("Lieu     : " + transaction.lieu());
            System.out.println("Compte   : " + transaction.idCompte());
            System.out.println("----------------------------");
        }
    }



}
