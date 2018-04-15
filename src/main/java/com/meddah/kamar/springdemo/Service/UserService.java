package com.meddah.kamar.springdemo.Service;

import com.meddah.kamar.springdemo.Model.User;
import com.meddah.kamar.springdemo.Repository.UserRepository;
import com.meddah.kamar.springdemo.Security.auth.AuthFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

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

    public User update(User user) {
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
}
