package com.meddah.kamar.springdemo.Repository;

import com.meddah.kamar.springdemo.Model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.Size;
import java.util.UUID;

@Repository
public interface UserRepository extends CrudRepository<User, UUID> {

    User findUserByEmail(String email);
    User findUserByUsername(String username);
    User findUserByRememberToken(@Size(max = 305) String rememberToken);
    User findUserByIdAndRememberToken(UUID id, @Size(max = 305) String rememberToken);
}
