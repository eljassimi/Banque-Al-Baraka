package main.com.albaraka.ui;

import main.com.albaraka.entity.Compte;
import main.com.albaraka.entity.CompteCourant;
import main.com.albaraka.service.ClientService;
import main.com.albaraka.service.CompteService;

import java.sql.SQLException;
import java.util.Scanner;

public class MenuCompte {
    private final CompteService compteService;
    Scanner sc = new Scanner(System.in);

    public MenuCompte(){
        this.compteService = new CompteService();
    }

    public void afficher(){
        boolean continuer = true;

        while (continuer) {
            System.out.println("GESTION DES COMPTES");
            System.out.println("1. Créer un compte courant");
            System.out.println("2. Créer un compte épargne");
            System.out.println("3. Effectuer un versement");
            System.out.println("4. Effectuer un retrait");
            System.out.println("5. Effectuer un virement");
            System.out.println("6. Rechercher un compte par ID");
            System.out.println("7. Rechercher un compte par numéro");
            System.out.println("8. Lister les comptes d'un client");
            System.out.println("9. Statistiques d'un compte");
            System.out.println("10. Compte avec solde maximum");
            System.out.println("11. Compte avec solde minimum");
            System.out.println("0. Retour au menu principal");

            int choix = sc.nextInt();

            try {
                switch (choix) {
                    case 1 -> creerCompteCourant();
                    case 2 -> creerCompteEpargne();
                    case 3 -> effectuerVersement();
                    case 4 -> effectuerRetrait();
                    case 5 -> effectuerVirement();
                    case 6 -> rechercherParId();
                    case 7 -> rechercherParNumero();
                    case 8 -> listerComptesClient();
                    case 9 -> afficherStatistiques();
                    case 10 -> afficherCompteMaxSolde();
                    case 11 -> afficherCompteMinSolde();
                    case 0 -> continuer = false;
                    default -> System.out.println("Choix invalide");
                }
            } catch (Exception e) {
                System.err.println("Erreur: " + e.getMessage());
            }
        }
    }

    public void creerCompteCourant() throws SQLException {
        System.out.println("====== Creation de compte courant ======");
        System.out.println("Entrer le Numero du compte : ");
        String numero = sc.nextLine();
        System.out.println("Entrer le id du client de ce compte : ");
        Long idClient = sc.nextLong();
        System.out.println("Entrer le Solde Initial de ce compte : ");
        Double solde = sc.nextDouble();
        System.out.println("Entrer le decouvert autorisé dans ce compte : ");
        Double devouvert  = sc.nextDouble();
        compteService.creerCompteCourant(numero,solde,idClient,devouvert);
        System.out.println("Compte Courant a ete crée");
    }

    public void creerCompteEpargne() throws SQLException {
        System.out.println("====== Creation de compte Epargne ======");
        System.out.println("Entrer le Numero du compte : ");
        String numero = sc.nextLine();
        System.out.println("Entrer le id du client de ce compte : ");
        Long idClient = sc.nextLong();
        System.out.println("Entrer le Solde Initial de ce compte : ");
        Double solde = sc.nextDouble();
        System.out.println("Entrer le taux d'intret de ce compte : ");
        Double tauxInteret = sc.nextDouble();
        compteService.creerCompteEpargne(numero,solde,idClient,tauxInteret);
        System.out.println("Compte Courant a ete crée");
    }

    public void effectuerVersement()throws SQLException{
        System.out.println("======= VERSEMENT A UN COMPTE =======");
        System.out.println("Entrer le numero du compte :");
        String numero = sc.nextLine();
        System.out.println("Entrer le Montant : ");
        Double montant = sc.nextDouble();
        System.out.println("Entrer le lieu : ");
        String lieu = sc.nextLine();

        compteService.effectuerVersement(numero,montant,lieu);

    }
}
