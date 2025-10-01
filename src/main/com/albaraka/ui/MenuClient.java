package main.com.albaraka.ui;

import main.com.albaraka.service.ClientService;

import java.util.Scanner;

public class MenuClient {
    private final ClientService clientService;

    public MenuClient() {
        this.clientService = new ClientService();
    }

    public void afficher(){
        Scanner sc = new Scanner(System.in);
        boolean continuer = true;

        while (continuer) {
            System.out.println("------GESTION DES CLIENTS------");
            System.out.println("1. Ajouter un client");
            System.out.println("2. Modifier un client");
            System.out.println("3. Supprimer un client");
            System.out.println("4. Rechercher un client par ID");
            System.out.println("5. Rechercher des clients par nom");
            System.out.println("6. Lister tous les clients");
            System.out.println("7. Statistiques d'un client");
            System.out.println("0. Retour au menu principal");

            int choix = sc.nextInt();

                switch (choix) {
                    case 1 -> ajouterClient();
                    case 2 -> modifierClient();
                    case 3 -> supprimerClient();
                    case 4 -> rechercherParId();
                    case 5 -> rechercherParNom();
                    case 6 -> listerTousLesClients();
                    case 7 -> afficherStatistiques();
                    case 0 -> continuer = false;
                    default -> System.out.println("Choix invalide");
                }
        }
    }
}
