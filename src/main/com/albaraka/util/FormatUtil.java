package main.com.albaraka.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FormatUtil {

    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public static String formaterDateTime(LocalDateTime date) {
        return date.format(DATETIME_FORMATTER);
    }

    public static String formaterMontant(double montant) {
        return String.format(montant+" €");
    }
}
