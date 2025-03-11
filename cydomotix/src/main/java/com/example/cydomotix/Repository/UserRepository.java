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
public interface UserRepository extends CrudRepository<User, Integer> {

    // Here, include custom CRUD methods

    Optional<User> findByUsername(String username); // Find user by username for login
}
