package com.meddah.kamar.springdemo.Repository;

import com.meddah.kamar.springdemo.Model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.Size;
import java.util.UUID;

@Repository
public interface UserRepository extends CrudRepository<User, UUID> {

    User findUserByEmail(String email);

    User findUserByUsername(String username);

    User findUserByIdAndRememberToken(UUID id, @Size(max = 305) String rememberToken);

    User findUserByResetToken(String resetToken);

    Page<User> findAll(Pageable pageable);

    User findUserById(UUID id);
}
