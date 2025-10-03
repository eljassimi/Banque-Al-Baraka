package main.com.albaraka.ui;

import main.com.albaraka.service.TransactionService;

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

}
