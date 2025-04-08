package com.example.cydomotix.Repository.Objects;

import com.example.cydomotix.Model.Objects.ConnectedObject;
import com.example.cydomotix.Model.Objects.PowerChangeEvent;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PowerChangeEventRepository extends CrudRepository<PowerChangeEvent, Integer> {
    List<PowerChangeEvent> findByConnectedObjectOrderByTimestamp(ConnectedObject connectedObject);
}
