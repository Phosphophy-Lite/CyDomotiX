package com.example.cydomotix.Repository;

import com.example.cydomotix.Model.Users.UserAction;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserActionRepository extends ListCrudRepository<UserAction, Integer> {
}
