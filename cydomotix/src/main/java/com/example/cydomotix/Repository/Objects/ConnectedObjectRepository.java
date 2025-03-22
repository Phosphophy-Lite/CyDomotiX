package com.example.cydomotix.Repository.Objects;

import com.example.cydomotix.Model.Objects.ConnectedObject;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConnectedObjectRepository extends ListCrudRepository<ConnectedObject, Integer> {
    Optional<ConnectedObject> findByName(String name);
}
