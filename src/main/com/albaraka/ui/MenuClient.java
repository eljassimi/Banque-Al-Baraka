package main.com.albaraka.ui;

import main.com.albaraka.entity.Client;
import main.com.albaraka.service.ClientService;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class MenuClient {
    private final ClientService clientService;
    Scanner sc = new Scanner(System.in);

    public MenuClient() {
        this.clientService = new ClientService();
    }

    public void afficher() throws SQLException {
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

    private void ajouterClient() throws SQLException {
        System.out.println("\n--- AJOUT D'UN CLIENT ---");

        System.out.println("Nom du client :");
        String nom = sc.nextLine();
        System.out.println("Email du client :");
        String email = sc.nextLine();

        try {
            clientService.validerClient(nom, email);
            clientService.ajouterClient(nom, email);
            System.out.println("Client ajoute avec succes avec id : ");
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
    }

    private void modifierClient() throws SQLException {
        System.out.println("\n--- Modifier D'UN CLIENT ---");
        System.out.println("Entrer id du client : ");
        Long id = sc.nextLong();

        Optional<Client> client = clientService.rechercherParId(id);
        if (client.isEmpty()){
            System.out.println("Client non trouve");
            return;
        }
        System.out.println("Entrer nouveau nom du client : ");
        String nom = sc.nextLine();
        System.out.println("Entrer nouveau email du client : ");
        String email = sc.nextLine();
        try {
            clientService.validerClient(nom, email);
            clientService.modifierClient(id, nom, email);
        }catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
    }

    private void supprimerClient() throws SQLException {
        System.out.println("\n--- Supprimer D'UN CLIENT ---");
        System.out.println("Entrer id du client : ");
        Long id = sc.nextLong();
        Optional<Client> client = clientService.rechercherParId(id);
        if (client.isEmpty()){
            System.out.println("Client non trouve");
            return;
        }
        clientService.supprimerClient(id);
        System.out.println("Client supprime avec succes avec id : "+id);
    }

    public void rechercherParId() throws SQLException {
        System.out.println("\n--- Rechercher par ID ---");
        System.out.println("Entrer id du client : ");
        Long id = sc.nextLong();
        Optional<Client> client = clientService.rechercherParId(id);
        if (client.isEmpty()){
            System.out.println("Client non trouve");
            return;
        }
        System.out.println("Client trouvé");
        client.stream().forEach(System.out::println);
    }
    public void listerTousLesClients() throws SQLException {
        System.out.println("\n--- Lister tous les clients ---");
        clientService.listerTousLesClients().stream().forEach(e->System.out.println("Nom du client : "+e.name()+"\nEmail du client : "+e.email()));
    }
    public void rechercherParNom()throws SQLException {
        System.out.println("\n--- Rechercher par Nom ---");
        System.out.println("Entrer nom du client : ");
        String nom = sc.nextLine();
        List<Client> clients = clientService.rechercherParNom(nom);
        if (clients.isEmpty()){
            System.out.println("Client non trouve par ce nom : "+nom);
            return;
        }
        clients.stream().forEach(System.out::println);
    }

    public void afficherStatistiques() throws SQLException {
        System.out.println("\n--- Afficher statistiques ---");
        System.out.println("Entrer id du client : ");
        Long id = sc.nextLong();
        Optional<Client> client = clientService.rechercherParId(id);
        if (client.isEmpty()){
            System.out.println("Client non trouve");
            return;
        }
        clientService.obtenirStatistiques(id);
    }

}
