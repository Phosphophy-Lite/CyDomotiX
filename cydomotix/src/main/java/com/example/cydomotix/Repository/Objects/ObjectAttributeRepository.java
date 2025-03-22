package com.example.cydomotix.Repository.Objects;

import com.example.cydomotix.Model.Objects.ObjectAttribute;
import com.example.cydomotix.Model.Objects.ObjectType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ObjectAttributeRepository extends CrudRepository<ObjectAttribute, Integer> {

    /*
        Lorsqu'on définit une méthode dans un repository en utilisant une convention de nommage,
        Spring Data JPA sait automatiquement ce que cette méthode doit faire.
        Par exemple, findByTypeObject suit une convention de nommage particulière qui dit à Spring Data que cette méthode
        doit chercher des entités ObjectAttribute dont le champ objectType correspond à une valeur donnée.
     */

    // Rechercher les attributs d'un type d'objet
    List<ObjectAttribute> findByObjectType(ObjectType objectType);
}
