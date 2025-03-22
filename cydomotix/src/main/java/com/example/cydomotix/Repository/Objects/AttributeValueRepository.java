package com.example.cydomotix.Repository.Objects;

import com.example.cydomotix.Model.Objects.AttributeValue;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttributeValueRepository extends CrudRepository<AttributeValue, Integer> {
}
