package main.com.albaraka.entity;

public final class CompteCourant extends Compte {
    private final double decouvertAutorise;

    public CompteCourant(String numero, double solde, Long idClient, double decouvertAutorise) {
        super(numero, solde, idClient);
        this.decouvertAutorise = decouvertAutorise;
    }

    public double getDecouvertAutorise() {
        return decouvertAutorise;
    }


    @Override
    public boolean peutDebiter(double montant) {
        return solde + decouvertAutorise >= montant;
    }


    @Override
    public String toString() {
        return String.format("CompteCourant{id=%d, numero='%s', solde=%.2f, decouvert=%.2f}",
                id, numero, solde, decouvertAutorise);
    }
}