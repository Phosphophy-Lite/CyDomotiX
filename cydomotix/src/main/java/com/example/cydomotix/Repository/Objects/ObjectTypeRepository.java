package com.example.cydomotix.Repository.Objects;

import com.example.cydomotix.Model.Objects.ObjectType;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ObjectTypeRepository extends ListCrudRepository<ObjectType, Integer> {
    Optional<ObjectType> findByName(String name);
}
