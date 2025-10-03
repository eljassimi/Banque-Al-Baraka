package main.com.albaraka.ui;

import main.com.albaraka.entity.Client;
import main.com.albaraka.service.ClientService;
import main.com.albaraka.service.RapportService;
import main.com.albaraka.util.FormatUtil;

import java.sql.SQLException;
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
            System.out.println("5. Rapport d'analyse complet");
            System.out.println("6. Alertes");
            System.out.println("0. Retour au menu principal");
            int choix = sc.nextInt();

            try {
                switch (choix) {
                    case 1 -> afficherTop5Clients();
                    case 2 -> genererRapportMensuel();
                    case 3 -> detecterTransactionsSuspectes();
                    case 4 -> identifierComptesInactifs();
                    case 5 -> genererRapportComplet();
                    case 6 -> afficherAlertes();
                    case 0 -> continuer = false;
                    default -> System.out.println("Choix invalide");
                }
            } catch (Exception e) {
                System.err.println("Erreur: " + e.getMessage());
            }
        }
    }

    private void afficherTop5Clients()throws SQLException{
        System.out.println("===== TOP 5 CLIENTS =====");
        List<Client> topClients =  rapportService.topFiveClient();

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

}
