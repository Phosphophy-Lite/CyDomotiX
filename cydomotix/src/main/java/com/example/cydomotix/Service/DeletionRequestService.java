package com.example.cydomotix.Service;

import com.example.cydomotix.Model.Administration.DeletionRequest;
import com.example.cydomotix.Model.Administration.DeletionTargetType;
import com.example.cydomotix.Model.Objects.ConnectedObject;
import com.example.cydomotix.Model.Objects.ObjectType;
import com.example.cydomotix.Model.Users.User;
import com.example.cydomotix.Repository.DeletionRequestRepository;
import com.example.cydomotix.Service.Objects.ConnectedObjectService;
import com.example.cydomotix.Service.Objects.ObjectTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;


@Service
public class DeletionRequestService {

    @Autowired
    private DeletionRequestRepository deletionRequestRepo;

    @Autowired
    private ConnectedObjectService connectedObjectService;

    @Autowired
    private ObjectTypeService objectTypeService;


    public void submitRequest(Integer id, DeletionTargetType type, String reason, User requester) {
        DeletionRequest request = new DeletionRequest();
        request.setReason(reason);
        request.setTargetType(type);

        if(type == DeletionTargetType.CONNECTED_OBJECT){
            ConnectedObject object = connectedObjectService.getConnectedObjectById(id);
            request.setConnectedObject(object);
        } else {
            ObjectType objectType = objectTypeService.getObjectTypeById(id);
            request.setObjectType(objectType);
        }

        request.setRequester(requester);
        request.setRequestDate(ZonedDateTime.now());
        deletionRequestRepo.save(request);
    }

    public List<DeletionRequest> getAllDeletionRequests() {
        return deletionRequestRepo.findAll();
    }

    @Transactional
    public void approveRequest(Integer requestId, String username) {
        DeletionRequest request = deletionRequestRepo.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Request with id " + requestId + " does not exist."));

        if(request.isForConnectedObject()){
            ConnectedObject connectedObject = connectedObjectService.getConnectedObjectById(request.getConnectedObject().getId());

            // Supprimer toutes les demandes liées à cet objet
            deletionRequestRepo.deleteByConnectedObject_Id(connectedObject.getId());

            connectedObjectService.deleteConnectedObject(connectedObject.getId(), username); //logger l'action utilisateur
        } else if (request.isForObjectType()) {
            ObjectType objectType = objectTypeService.getObjectTypeById(request.getObjectType().getId());
            // Supprimer toutes les demandes liées à ce type
            deletionRequestRepo.deleteByObjectType_Id(objectType.getId());
            objectTypeService.deleteObjectType(objectType.getId(), username); //logger l'action utilisateur
        }
    }

    public void rejectRequest(Integer requestId) {
        DeletionRequest request = deletionRequestRepo.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Request with id " + requestId + " does not exist."));
        deletionRequestRepo.delete(request);
    }
}

