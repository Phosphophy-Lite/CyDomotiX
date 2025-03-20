package com.example.cydomotix.Repository.Objects;

import com.example.cydomotix.Model.Objects.ConnectedObject;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConnectedObjectRepository extends CrudRepository<ConnectedObject, Long> {

}
