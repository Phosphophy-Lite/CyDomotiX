package com.example.cydomotix.Repository;

import com.example.cydomotix.Model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/*
    Repository :
    => sert à communiquer avec la BDD
    => à exécuter des opérations CRUD (Create, Read, Update, Delete) une entité de la BDD (Student par ex)
    => interface CrudRepository : implémente automatiquement les opérations CRUD SQL
 */


@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    // Ici, inclure des méthodes CRUD personnalisées
    // Ces méthodes sont automatiquement reconnues par CrudRepository comme des opérations CRUD (Créer, Lire, Mettre à jour, Supprimer) sur les attributs d'un utilisateur dans la base de données
    // Ces méthodes communiquent automatiquement avec la base de données, envoyant des requêtes SQL pour créer, lire, mettre à jour ou supprimer

    Optional<User> findByUsername(String username);
    Optional<User> findById(Integer id);
}