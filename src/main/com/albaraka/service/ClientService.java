package main.com.albaraka.service;

import main.com.albaraka.dao.ClientDAO;
import main.com.albaraka.dao.CompteDAO;
import main.com.albaraka.entity.Client;
import main.com.albaraka.entity.Compte;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ClientService {


    private final ClientDAO clientDAO;
    private final CompteDAO compteDAO;

    public ClientService() {
        this.clientDAO = new ClientDAO();
        this.compteDAO = new CompteDAO();
    }


    public void ajouterClient(String nom, String email) throws SQLException {
        Client client = new Client(nom, email);
        clientDAO.insert(client);
    }

    public void validerClient(String nom, String email) {
        if (nom == null || nom.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom ne peut pas etre vide");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("L email ne peut pas etre vide");
        }
        if (!email.contains("@")) {
            throw new IllegalArgumentException("L email doit contenir un @");
        }
    }

    public void modifierClient(Long id, String nom, String email) throws SQLException {
        Client client = new Client(id, nom, email);
        clientDAO.update(client);
    }

    public void supprimerClient(Long id) throws SQLException {
        List<Compte> comptes = compteDAO.findByClient(id);
        if (!comptes.isEmpty()) {
            throw new IllegalStateException("Impossibel de Supprimer ce client car il possede des comptes");
        }
        clientDAO.delete(id);
    }

    public Optional<Client> rechercherParId(Long id) throws SQLException {
        return Optional.ofNullable(clientDAO.findById(id));
    }

    public List<Client> rechercherParNom(String nom) throws SQLException {
        return clientDAO.findAll().stream().filter(e->e.name().equals(nom)).toList();
    }

    public List<Client> listerTousLesClients() throws SQLException {
        return clientDAO.findAll();
    }

    public double calculerSoldeTotal(Long idClient) throws SQLException {
        List<Compte> comptes = compteDAO.findByClient(idClient);
        return comptes.stream()
                .mapToDouble(Compte::getSolde)
                .sum();
    }
    public int calculerNombreCompte(Long idClient) throws SQLException {
        return compteDAO.findByClient(idClient).size();
    }

    public String obtenirStatistiques(Long idClient) throws SQLException {
        Optional<Client> clientOpt = Optional.ofNullable(clientDAO.findById(idClient));
        if (clientOpt.isEmpty()) {
            return "Client non trouve";
        }

        Client client = clientOpt.get();
        List<Compte> comptes = compteDAO.findByClient(idClient);
        double soldeTotal = calculerSoldeTotal(idClient);

        return String.format(
                "=== Statistiques du client %s ===\n" +
                        "Email: %s\n" +
                        "Nombre de comptes: %d\n" +
                        "Solde total: %.2f €\n" +
                client.name(),
                client.email(),
                comptes.size(),
                soldeTotal
        );
    }

}
