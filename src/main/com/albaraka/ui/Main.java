package main.com.albaraka.ui;

import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    private static MenuPrincipale menuPrincipal;

    public static void main(String[] args) throws SQLException {
        menuPrincipal = new MenuPrincipale();
        menuPrincipal.afficher();
    }
}