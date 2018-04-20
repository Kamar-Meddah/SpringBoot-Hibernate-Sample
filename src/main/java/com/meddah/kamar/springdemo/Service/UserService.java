package com.meddah.kamar.springdemo.Service;

import com.meddah.kamar.springdemo.Exception.UserException;
import com.meddah.kamar.springdemo.Model.User;
import com.meddah.kamar.springdemo.Repository.UserRepository;
import com.meddah.kamar.springdemo.Security.auth.AuthFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User create(User user) {
        try {
            user.setPassword( this.hash( user.getPassword() ) );
            return this.userRepository.save( user );
        } catch (Exception e) {
            return null;
        }
    }

    public boolean checkUsernameExist(String username) {
        return this.userRepository.findUserByUsername( username ) != null;
    }

    public boolean checkEmailExist(String email) {
        return this.userRepository.findUserByEmail( email ) != null;
    }

    public User update(User user) throws UserException {
        User authenticatedUser = AuthFactory.getUser();
        if (user.getPassword() != null) {
            authenticatedUser.setPassword( this.hash( user.getPassword() ) );
        }
        if (user.getEmail() != null && user.getEmail().matches( "^\\w+([.-]?\\w+)*@\\w+([.-]?\\w+)*(\\.\\w{2,3})+$" )) {
            authenticatedUser.setEmail( user.getEmail() );
        }
        return this.userRepository.save( authenticatedUser );
    }

    private String hash(String password) {
        return BCrypt.hashpw( password, BCrypt.gensalt() );
    }

    public Page<User> getAllPaginated(int page) {
        return this.userRepository.findAll( PageRequest.of( page, 20, Sort.by( Sort.Direction.ASC, "username" ) ) );
    }

    public Page<User> search(String query, int page) {
        return this.userRepository.findAllByUsernameLike( '%'+query+'%', PageRequest.of( page, 20, Sort.by( Sort.Direction.ASC, "username" ) ) );
    }

    public void deleteOne(UUID id) {
        this.userRepository.deleteById( id );
    }

    public void updateRole(String role, UUID id) {
        User user = this.userRepository.findUserById( id );
        user.setRole( role );
        user.setConfirmationToken( null );
        this.userRepository.save( user );
    }


}
