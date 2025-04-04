package com.example.cydomotix.Repository;

import com.example.cydomotix.Model.Users.VerificationToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VerificationTokenRepository extends CrudRepository<VerificationToken, Integer> {
    Optional<VerificationToken> findByToken(String token);
}
