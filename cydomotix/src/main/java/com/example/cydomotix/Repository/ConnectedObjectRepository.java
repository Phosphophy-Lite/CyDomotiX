package com.example.cydomotix.Repository;

import com.example.cydomotix.Model.ConnectedObject;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConnectedObjectRepository extends CrudRepository<ConnectedObject, Long> {

    //
}
