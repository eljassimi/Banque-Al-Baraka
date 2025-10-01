package main.com.albaraka.ui;

import java.sql.SQLException;
import java.util.Scanner;

public class MenuPrincipale {
    private final MenuClient menuClient;

    public MenuPrincipale() {
        this.menuClient = new MenuClient();
    }

    public void afficher() throws SQLException {
        Scanner sc = new Scanner(System.in);
        boolean continuer = true;

        while (continuer) {
            System.out.println("----- MENU PRINCIPAL ------");
            System.out.println("1. Gestion des clients");
            System.out.println("2. Gestion des comptes");
            System.out.println("3. Gestion des transactions");
            System.out.println("4. Rapports et analyses");
            System.out.println("0. Quitter");

            int choix = sc.nextInt();

                switch (choix) {
                    case 1 -> menuClient.afficher();
                    case 0 -> {
                        continuer = false;
                        System.out.println("------ Application Closed ------");
                    }
                    default -> System.out.println("Choix invalide");
                }

        }
    }

}
