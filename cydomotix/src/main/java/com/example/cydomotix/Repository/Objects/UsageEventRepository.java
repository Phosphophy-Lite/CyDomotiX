package com.example.cydomotix.Repository.Objects;

import com.example.cydomotix.Model.Objects.ConnectedObject;
import com.example.cydomotix.Model.Objects.UsageEvent;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

@Repository
public interface UsageEventRepository extends CrudRepository<UsageEvent, Integer> {
    List<UsageEvent> findByConnectedObjectOrderByTimestampAsc(ConnectedObject connectedObject);
    List<UsageEvent> findByConnectedObjectAndTimestampBeforeOrderByTimestampAsc(ConnectedObject connectedObject, ZonedDateTime timestamp);

}


