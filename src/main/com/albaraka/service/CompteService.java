package main.com.albaraka.service;

import main.com.albaraka.dao.CompteDAO;
import main.com.albaraka.dao.TransactionDAO;
import main.com.albaraka.entity.Compte;
import main.com.albaraka.entity.CompteCourant;
import main.com.albaraka.entity.CompteEpargne;

import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class CompteService {

    private final CompteDAO compteDAO;
    private final TransactionDAO transactionDAO;

    public CompteService() {
        this.compteDAO = new CompteDAO();
        this.transactionDAO = new TransactionDAO();
    }

    public void creerCompteCourant(String numero, double soldeInitial, Long idClient, double decouvertAutorise) throws SQLException {
        CompteCourant compte = new CompteCourant(numero, soldeInitial, idClient, decouvertAutorise);
        compteDAO.insert(compte);
    }

    public void creerCompteEpargne(String numero, double soldeInitial, Long idClient, double tauxInteret) throws SQLException {
        CompteEpargne compte = new CompteEpargne(numero, soldeInitial, idClient, tauxInteret);
        compteDAO.insert(compte);
    }

    public void updateCompte(Compte compte) throws SQLException {
        compteDAO.update(compte);
    }

    public void deleteCompte(Long id) throws SQLException {
        compteDAO.delete(id);
    }

    public Optional<Compte> rechercherParId(Long id) throws SQLException {
        return compteDAO.findById(id);
    }

    public List<Compte> findAll() throws SQLException {
        return compteDAO.findAll();
    }

    public boolean mettreAJourSolde(Long idCompte, double nouveauSolde) throws SQLException {
        return compteDAO.updateSolde(idCompte, nouveauSolde);
    }
    public boolean mettreAJourParametres(Long idCompte, Double decouvertAutorise, Double tauxInteret) throws SQLException {
        return compteDAO.updateParametres(idCompte, decouvertAutorise, tauxInteret);
    }

    public List<Compte> rechercherParClient(Long idClient) throws SQLException {
        return compteDAO.findByClient(idClient);
    }

    public Optional<Compte> trouverCompteMaxSolde() throws SQLException {
        List<Compte> comptes = compteDAO.findAll();
        return comptes.stream()
                .max(Comparator.comparingDouble(Compte::getSolde));
    }

    public Optional<Compte> trouverCompteMinSolde() throws SQLException {
        List<Compte> comptes = compteDAO.findAll();
        return comptes.stream()
                .min(Comparator.comparingDouble(Compte::getSolde));
    }


}
