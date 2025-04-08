package com.example.cydomotix.Repository.Objects;

import com.example.cydomotix.Model.Objects.ConnectedObject;
import com.example.cydomotix.Model.Objects.UsageEvent;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsageEventRepository extends CrudRepository<UsageEvent, Integer> {
    List<UsageEvent> findByConnectedObjectOrderByTimestamp(ConnectedObject connectedObject);
}

