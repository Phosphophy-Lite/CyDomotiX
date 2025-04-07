package com.example.cydomotix.Repository;

import com.example.cydomotix.Model.Administration.DeletionRequest;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeletionRequestRepository extends ListCrudRepository<DeletionRequest, Integer> {
}
