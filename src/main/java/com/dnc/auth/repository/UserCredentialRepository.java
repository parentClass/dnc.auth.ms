package com.dnc.auth.repository;

import com.dnc.auth.model.UserCredentialDTO;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserCredentialRepository extends MongoRepository<UserCredentialDTO, String> {

    @Query("{'username': ?0, 'is_deleted': false}")
    Optional<UserCredentialDTO> findByUsername(String username);

    @Query("{'email': ?0, 'is_deleted': false}")
    Optional<UserCredentialDTO> findByEmail(String email);

    @Query("{'_id': ?0, 'is_deleted': false}")
    Optional<UserCredentialDTO> findByOid(String oid);
}
