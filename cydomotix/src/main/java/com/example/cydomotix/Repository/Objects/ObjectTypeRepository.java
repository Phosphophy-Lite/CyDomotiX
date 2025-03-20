package com.example.cydomotix.Repository.Objects;

import com.example.cydomotix.Model.Objects.ObjectType;
import com.example.cydomotix.Model.User;
import org.springframework.data.repository.ListCrudRepository;

import java.util.Optional;

public interface ObjectTypeRepository extends ListCrudRepository<ObjectType, Integer> {
    Optional<ObjectType> findByName(String name);
}
