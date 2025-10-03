# Analyse des Transactions Bancaires — Banque Al Baraka

> Application Java 17 (console) pour centraliser, analyser et détecter les anomalies sur les flux financiers.

---

## 🚀 But du projet

Fournir un outil interne permettant de :

* Centraliser les données clients, comptes et transactions.
* Détecter automatiquement les anomalies et transactions suspectes.
* Identifier les comptes inactifs et comportements inhabituels.
* Générer des rapports exploitables (Top 5 clients, rapport mensuel, alertes, etc.).

Ce dépôt contient l'architecture en couches : UI (console), Services (logique métier), DAO (JDBC), Entities (record / sealed), Utilitaires.

---

## Diagramme de classes

![Diagramme de classes](https://postimg.cc/r03fRnGG)

---

## Langages & technos

* Java 17 (records, sealed, switch expressions)
* JDBC (MySQL ou PostgreSQL)
* Stream API, Optional, lambda
* Maven ou Gradle

---

## Contrat des entités (extraits)

**Client (record)**

```java
public record Client(long id, String nom, String email) {}
```

**Compte (sealed)**

```java
public sealed class Compte permits CompteCourant, CompteEpargne {
    private final long id;
    private final String numero;
    private double solde;
    private final long idClient;

    // constructeurs, getters...
}

public final class CompteCourant extends Compte {
    private final double decouvertAutorise;
}

public final class CompteEpargne extends Compte {
    private final double tauxInteret;
}
```

**Transaction (record + enum)**

```java
public record Transaction(long id, LocalDateTime date, BigDecimal montant, TypeTransaction type, String lieu, long idCompte) {}

public enum TypeTransaction { VERSEMENT, RETRAIT, VIREMENT }
```

---

## Exemples de tables SQL (MySQL)

```sql
CREATE TABLE Client (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  nom VARCHAR(255) NOT NULL,
  email VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE Compte (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  numero VARCHAR(50) UNIQUE NOT NULL,
  solde DECIMAL(15,2) NOT NULL DEFAULT 0,
  idClient BIGINT NOT NULL,
  typeCompte VARCHAR(20) NOT NULL,
  decouvertAutorise DECIMAL(15,2),
  tauxInteret DECIMAL(5,4),
  FOREIGN KEY (idClient) REFERENCES Client(id) ON DELETE CASCADE
);

CREATE TABLE Transaction (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  date DATETIME NOT NULL,
  montant DECIMAL(15,2) NOT NULL,
  type ENUM('VERSEMENT','RETRAIT','VIREMENT') NOT NULL,
  lieu VARCHAR(255),
  idCompte BIGINT NOT NULL,
  FOREIGN KEY (idCompte) REFERENCES Compte(id) ON DELETE CASCADE
);
```

---

## DAO (contrats) — exemples d'interfaces

```java
public interface ClientDAO {
    Client create(Client c) throws SQLException;
    Optional<Client> findById(long id) throws SQLException;
    List<Client> findByName(String name) throws SQLException;
    List<Client> findAll() throws SQLException;
    boolean update(Client c) throws SQLException;
    boolean delete(long id) throws SQLException;
}

public interface CompteDAO {
    Compte create(Compte c) throws SQLException;
    Optional<Compte> findById(long id) throws SQLException;
    Optional<Compte> findByNumero(String numero) throws SQLException;
    List<Compte> findByClientId(long clientId) throws SQLException;
    boolean updateSolde(long id, BigDecimal nouveauSolde) throws SQLException;
}

public interface TransactionDAO {
    Transaction create(Transaction t) throws SQLException;
    List<Transaction> findByCompteId(long compteId) throws SQLException;
    List<Transaction> findAll() throws SQLException;
    List<Transaction> findByFilters(...);
}
```

---

## Services (fonctions clés)

* **ClientService** : CRUD client + statistiques (nombre de comptes, solde total, etc.)
* **CompteService** : création de compte courant/épargne, mise à jour paramètres, recherche, recherche max/min solde
* **TransactionService** : enregistrement, recherche/filtrage, regroupements, statistiques, détection d'anomalies
* **RapportService** : top 5 clients, rapport mensuel (transactions par type), comptes inactifs, transactions suspectes

**Détection d'anomalies (exemples de règles)**

* Montant > `SEUIL_HIGH_VALUE` (ex. 10_000)
* Lieu != pays habituel (nécessite heuristique / champ pays)
* Trop d'opérations en < 1 min (p.ex. > N opérations)

---

## UI (Console) — menu suggéré

```
1. Gérer les clients
2. Gérer les comptes
3. Enregistrer une transaction
4. Historique des transactions d'un compte
5. Lancer analyses & rapports
   5.1 Top 5 clients
   5.2 Transactions par type / mois
   5.3 Comptes inactifs
   5.4 Transactions suspectes
6. Paramètres (seuils, DB)
0. Quitter
```

---

## Configuration & compilation

### JDBC

* Configure `src/main/resources/application.properties` ou `config.properties` :

```
db.url=jdbc:pgsql://localhost:5432/albaraka
db.user=root
db.password=*****
```

---

## Bonnes pratiques & exigences techniques

* Java 17 features : use `record` pour objets immuables, `sealed` pour hiérarchies de comptes.
* Streams + Collectors pour agrégations et rapports.
* Tests unitaires (Junit 5) pour logique métier critique (détection d'anomalies, calculs de rapports).
* Gestion d'exceptions avec messages clairs pour l'utilisateur.
* Commits Git fréquents et descriptifs.

---