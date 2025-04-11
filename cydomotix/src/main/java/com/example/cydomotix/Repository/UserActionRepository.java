package com.example.cydomotix.Repository;

import com.example.cydomotix.Model.Users.ActionType;
import com.example.cydomotix.Model.Users.UserAction;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;

@Repository
public interface UserActionRepository extends ListCrudRepository<UserAction, Integer> {
    @Query("SELECT DISTINCT ua.author FROM UserAction ua " +
            "WHERE ua.actionType = :actionType AND ua.timestamp BETWEEN :start AND :end")
    List<String> findDistinctUsernamesByActionTypeAndDateBetween(
            @Param("actionType") ActionType actionType,
            @Param("start") ZonedDateTime start,
            @Param("end") ZonedDateTime end);

}
