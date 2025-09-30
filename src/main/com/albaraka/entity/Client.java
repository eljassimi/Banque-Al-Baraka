package main.com.albaraka.entity;

import main.com.albaraka.util.GenerateId;

public record Client(Long id, String name, String email) {

    public Client(String name, String email) {
        this(GenerateId.generateId(), name, email);

        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom ne doit pas etre vide");
        }
        if (email == null || email.trim().isEmpty() || !email.contains("@")) {
            throw new IllegalArgumentException("L'email n'est pas valide");
        }
    }

    @Override
    public String toString() {
        return String.format("Client{id=%d, name='%s', email='%s'}", id, name, email);
    }
}
