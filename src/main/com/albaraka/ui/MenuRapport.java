package main.com.albaraka.ui;

import main.com.albaraka.entity.Client;
import main.com.albaraka.entity.Compte;
import main.com.albaraka.entity.Transaction;
import main.com.albaraka.service.ClientService;
import main.com.albaraka.service.RapportService;
import main.com.albaraka.util.FormatUtil;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class MenuRapport {
    private final RapportService rapportService;
    private final ClientService clientService;
    Scanner sc = new Scanner(System.in);

    public MenuRapport() {
        this.rapportService = new RapportService();
        this.clientService = new ClientService();
    }

    public void afficher() {
        boolean continuer = true;

        while (continuer) {
            System.out.println("===== RAPPORTS ET ANALYSES =====");
            System.out.println("1. Top 5 des clients par solde");
            System.out.println("2. Rapport mensuel");
            System.out.println("3. Détecter transactions suspectes");
            System.out.println("4. Identifier comptes inactifs");
            System.out.println("5. Alertes");
            System.out.println("0. Retour au menu principal");
            int choix = sc.nextInt();

            try {
                switch (choix) {
                    case 1 -> afficherTop5Clients();
                    case 2 -> genererRapportMensuel();
                    case 3 -> detecterTransactionsSuspectes();
                    case 4 -> identifierComptesInactifs();
                    case 5 -> afficherAlertes();
                    case 0 -> continuer = false;
                    default -> System.out.println("Choix invalide");
                }
            } catch (Exception e) {
                System.err.println("Erreur: " + e.getMessage());
            }
        }
    }

    private void afficherTop5Clients() throws SQLException {
        System.out.println("===== TOP 5 CLIENTS =====");
        List<Client> topClients = rapportService.topFiveClient();

        if (topClients.isEmpty()) {
            System.out.println("Aucun client trouve");
        } else {
            System.out.println("Classement des clients par solde total:");
            System.out.println();

            for (int i = 0; i < topClients.size(); i++) {
                Client client = topClients.get(i);
                double soldeTotal = clientService.calculerSoldeTotal(client.id());
                System.out.println(String.format("%d - %s", i + 1, client.name()));
                System.out.println("   Email: " + client.email());
                System.out.println("   Solde total: " + FormatUtil.formaterMontant(soldeTotal));
                System.out.println();
            }
        }
    }

    private void genererRapportMensuel() throws SQLException {
        System.out.println("\n--- RAPPORT MENSUEL ---");
        sc.nextLine();
        System.out.println("L'annee : ");
        int annee = sc.nextInt();
        System.out.println("Mois (1-12)");
        int mois = sc.nextInt();
        if (mois < 1 || mois > 12) {
            System.err.println("Mois invalide utilisation du mois actuel.");
            mois = LocalDateTime.now().getMonthValue();
        }
        String rapport = rapportService.GenerateRapportMonsuel(annee, mois);
        System.out.println(rapport);
    }

    private void detecterTransactionsSuspectes() throws SQLException {
        System.out.println("\n--- DÉTECTION DE TRANSACTIONS SUSPECTES ---");

        sc.nextLine();
        double seuilMontant = 10000.0;
        String paysHabituel = "France";
        int nombreMaxParMinute = 5;

        List<Transaction> suspectes = rapportService.detecterTransactionsSuspectes(
                seuilMontant, paysHabituel, nombreMaxParMinute);

        if (suspectes.isEmpty()) {
            System.out.println("Aucun Transaction suspecte detectee.");
        } else {
            System.out.println("Transactions suspectes detectee:");
            System.out.println();

            for (Transaction transaction : suspectes) {
                System.out.println("- " + transaction.type() +
                        " de " + FormatUtil.formaterMontant(transaction.montant()) +
                        " le " + transaction.date() +
                        " a " + transaction.lieu());
            }

            System.out.println("\nTotal: " + suspectes.size() + " transactions suspecte");
        }
    }

    private void identifierComptesInactifs() throws SQLException {
        System.out.println("\n--- IDENTIFICATION DES COMPTES INACTIFS ---");

        sc.nextLine();
        System.out.println("Nombre de jours inactif: ");
        int joursInactivite = sc.nextInt();

        List<Compte> comptesInactifs = rapportService.identifierComptesInactifs(joursInactivite);

        if (comptesInactifs.isEmpty()) {
            System.out.println("Aucun compte inactif");
        } else {
            System.out.println("Comptes inactifs depuis plus de " + joursInactivite + " jours:");
            for (Compte compte : comptesInactifs) {
                System.out.println("- " + compte.getNumero()
                        + " - Solde: " + FormatUtil.formaterMontant(compte.getSolde())
                        + " - Client: " + compte.getIdClient());
            }
            System.out.println("\nTotal: " + comptesInactifs.size() + " comptes inactifs");
        }
    }

    public void afficherAlertes() throws Exception{
        System.out.println("===== ALERTS =====");
        List<String> alerts = rapportService.genererAlertes();
        alerts.forEach(System.out::println);
    }
}
