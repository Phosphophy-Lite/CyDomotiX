package com.example.cydomotix.Repository;

import com.example.cydomotix.Model.Administration.DeletionRequest;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeletionRequestRepository extends ListCrudRepository<DeletionRequest, Integer> {
    // Supprime toutes les demandes pour un objet connecté donné (sauf celle en cours si besoin)
    void deleteByConnectedObject_Id(Integer connectedObjectId);

    // Supprime toutes les demandes pour un type d'objet donné
    void deleteByObjectType_Id(Integer objectTypeId);
}
