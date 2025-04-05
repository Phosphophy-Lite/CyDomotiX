package com.example.cydomotix.Service;

import com.example.cydomotix.Model.Administration.DeletionRequest;
import com.example.cydomotix.Model.Objects.ConnectedObject;
import com.example.cydomotix.Model.Users.User;
import com.example.cydomotix.Repository.DeletionRequestRepository;
import com.example.cydomotix.Service.Objects.ConnectedObjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class DeletionRequestService {

    @Autowired
    private DeletionRequestRepository deletionRequestRepo;

    @Autowired
    private ConnectedObjectService connectedObjectService;


    public void submitRequest(Integer objectId, String reason, User requester) {
        DeletionRequest request = new DeletionRequest();
        request.setReason(reason);
        ConnectedObject object = connectedObjectService.getConnectedObjectById(objectId);
        request.setConnectedObject(object);
        request.setRequester(requester);
        request.setRequestDate(LocalDateTime.now());
        deletionRequestRepo.save(request);
    }

    public List<DeletionRequest> getAllDeletionRequests() {
        return deletionRequestRepo.findAll();
    }

    public void approveRequest(Integer requestId) {
        DeletionRequest request = deletionRequestRepo.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Request with id " + requestId + " does not exist."));

        ConnectedObject connectedObject = connectedObjectService.getConnectedObjectById(request.getConnectedObject().getId());
        deletionRequestRepo.delete(request);
        connectedObjectService.deleteConnectedObject(connectedObject.getId());
    }

    public void rejectRequest(Integer requestId) {
        DeletionRequest request = deletionRequestRepo.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Request with id " + requestId + " does not exist."));
        deletionRequestRepo.delete(request);
    }
}

