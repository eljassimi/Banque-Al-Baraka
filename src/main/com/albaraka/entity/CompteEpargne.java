package main.com.albaraka.entity;

public final class CompteEpargne extends Compte {
    private final double tauxInteret;

    public CompteEpargne(String numero, double solde, Long idClient, double tauxInteret) {
        super(numero, solde, idClient);
        this.tauxInteret = tauxInteret;
    }

    public CompteEpargne(Long id, String numero, double solde, Long idClient, double tauxInteret) {
        super(id, numero, solde, idClient);
        this.tauxInteret = tauxInteret;
    }


    public double getTauxInteret() {
        return tauxInteret;
    }

    public double calculerInterets(double montant) {
        return montant * tauxInteret ;
    }

    @Override
    public String toString() {
        return String.format("CompteEpargne{id=%d, numero='%s', solde=%.2f, taux=%.2f%%}",
                id, numero, solde, tauxInteret);
    }
}