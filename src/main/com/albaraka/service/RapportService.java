package main.com.albaraka.service;

import main.com.albaraka.dao.ClientDAO;
import main.com.albaraka.dao.CompteDAO;
import main.com.albaraka.entity.Client;
import main.com.albaraka.entity.Compte;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class RapportService {

    private final ClientDAO clientDAO;
    private final CompteDAO compteDAO;


    public RapportService(ClientDAO clientDAO, CompteDAO compteDAO) {
        this.clientDAO = clientDAO;
        this.compteDAO = compteDAO;
    }

    public List<Client> topFiveClient() throws SQLException {
        List<Compte> comptes = compteDAO.findAll().stream().sorted(Comparator.comparing(Compte::getSolde)).limit(5).toList();
        List<Client> clients = new ArrayList<>();
        for (Compte compte : comptes) {
            clients.add(clientDAO.findById(compte.getIdClient()));
        }
        return clients;
    }


}
