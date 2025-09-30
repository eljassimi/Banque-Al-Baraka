package main.com.albaraka.entity;

import main.com.albaraka.util.GenerateId;

import java.time.LocalDateTime;

public record Transaction(
        Long id,
        LocalDateTime date,
        double montant,
        TypeTransaction type,
        String lieu,
        Long idCompte
) {

    public Transaction (LocalDateTime date, double montant, TypeTransaction type, String lieu, Long idCompte){
        this(GenerateId.generateId(), date != null ? date : LocalDateTime.now(), montant, type, lieu, idCompte);
        if (date == null) {
            throw new IllegalArgumentException("La date ne peut pas etre null");
        }
        if (montant <= 0) {
            throw new IllegalArgumentException("Le montant doit etre positif");
        }
        if (type == null) {
            throw new IllegalArgumentException("Le type doit etre soit : VERSEMENT, RETRAIT, VIREMENT");
        }
        if (lieu == null || lieu.trim().isEmpty()) {
            throw new IllegalArgumentException("Le lieu ne doit pas etre vide");
        }
        if (idCompte == null) {
            throw new IllegalArgumentException("L'ID du compte ne doit pas etre null");
        }
    }

    @Override
    public String toString() {
        return String.format("Transaction{id=%d, date=%s, montant=%.2f, type=%s, lieu='%s'}",
                id, date, montant, type, lieu);
    }
}