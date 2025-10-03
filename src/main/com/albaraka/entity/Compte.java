package main.com.albaraka.entity;

import main.com.albaraka.util.GenerateId;

public sealed class Compte permits CompteCourant, CompteEpargne {

    protected final Long id;
    protected final String numero;
    protected final double solde;
    protected final Long idClient;

    protected Compte(String numero, double solde, Long idClient) {
        this.id = GenerateId.generateId();
        this.numero = numero;
        this.solde = solde;
        this.idClient = idClient;
    }

    protected Compte(Long id, String numero, double solde, Long idClient) {
        this.id = id;
        this.numero = numero;
        this.solde = solde;
        this.idClient = idClient;
    }


    public Long getId() { return id; }
    public String getNumero() { return numero; }
    public double getSolde() { return solde; }
    public Long getIdClient() { return idClient; }


    public boolean peutDebiter(double montant) {
        return solde >= montant;
    }

    @Override
    public String toString() {
        return String.format("Compte{id=%d, numero='%s', solde=%.2f",
                id, numero, solde);
    }
}