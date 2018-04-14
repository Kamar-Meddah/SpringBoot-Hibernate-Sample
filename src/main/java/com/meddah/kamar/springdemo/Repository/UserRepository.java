package com.meddah.kamar.springdemo.Repository;

import com.meddah.kamar.springdemo.Model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserRepository extends CrudRepository<User, UUID> {

    User findUserByEmail(String email);
    User findUserByUsername(String username);
}
