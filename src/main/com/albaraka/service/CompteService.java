package main.com.albaraka.service;

import main.com.albaraka.dao.CompteDAO;
import main.com.albaraka.dao.TransactionDAO;
import main.com.albaraka.entity.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

    public void effectuerVersement(String numero,Double montant,String lieu) throws SQLException {
        Optional<Compte> compte = compteDAO.findByNumero(numero);
        if (compte.isEmpty()){
            throw new SQLException("Aucun Compte avec ce numro !");
        }

        Double newSolde = compte.get().getSolde() + montant;
        boolean SoldeUpdate = mettreAJourSolde(compte.get().getId(),newSolde);

        if (!SoldeUpdate){
            throw new SQLException("Echec de mis a jour solde");
        }

        Transaction transaction = new Transaction(LocalDateTime.now(),montant, TypeTransaction.VERSEMENT, lieu, compte.get().getId());
        transactionDAO.insert(transaction);

    }

    public void effectuerRetrait(String numero,Double montant , String lieu) throws SQLException{
        Optional<Compte> compteOpt = compteDAO.findByNumero(numero);
        if (compteOpt.isEmpty()){
            throw new SQLException("Aucun Compte avec ce numro !");
        }

        Compte compte = compteOpt.get();

        if (!compte.peutDebiter(montant)) {
            throw new IllegalStateException("Solde insuffisant");
        }
        double newSolde = compte.getSolde() - montant;

        boolean SoldeUpdate = mettreAJourSolde(compte.getId(),newSolde);


        if (!SoldeUpdate){
            throw new SQLException("Echec de mis a jour solde");
        }

        Transaction transaction = new Transaction(LocalDateTime.now(),montant, TypeTransaction.RETRAIT, lieu, compte.getId());
        transactionDAO.insert(transaction);

    }

    public void effecturtVirement(String numeroSrc,String numeroDst,Double montant,String lieu) throws Exception {
        Optional<Compte> compteSrcOpt = compteDAO.findByNumero(numeroSrc);
        Optional<Compte> compteDstOpt = compteDAO.findByNumero(numeroDst);

        if (compteSrcOpt.isEmpty()){
            throw  new Exception("Compte source non trouver avec ce id : "+numeroSrc);
        }
        if (compteDstOpt.isEmpty()){
            throw  new Exception("Compte source non trouver avec ce id : "+numeroSrc);
        }

        Compte compteSrc = compteSrcOpt.get();
        Compte compteDst = compteDstOpt.get();

        if (!compteSrc.peutDebiter(montant)) {
            throw new IllegalStateException("Solde insuffisant");
        }

        double nouveauSoldeSource = compteSrc.getSolde() - montant;
        double nouveauSoldeDestination = compteDst.getSolde() + montant;

        boolean SrcSoldeUpdate = mettreAJourSolde(compteSrc.getId(),nouveauSoldeSource);
        boolean DstSoldeUpdate = mettreAJourSolde(compteDst.getId(),nouveauSoldeDestination);

        if (!SrcSoldeUpdate || !DstSoldeUpdate){
            throw new SQLException("Echec de mis a jour solde");
        }
        Transaction transaction1 = new Transaction(LocalDateTime.now(),montant, TypeTransaction.VIREMENT, lieu + " (vers " + compteDst.getNumero() + ")", compteDst.getId());
        Transaction transaction2 = new Transaction(LocalDateTime.now(),montant, TypeTransaction.VIREMENT, lieu + " (de " + compteSrc.getNumero() + ")", compteSrc.getId());
        transactionDAO.insert(transaction1);
        transactionDAO.insert(transaction2);
    }


}
