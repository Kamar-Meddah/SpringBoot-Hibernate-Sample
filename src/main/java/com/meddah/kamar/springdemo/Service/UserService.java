package com.meddah.kamar.springdemo.Service;

import com.meddah.kamar.springdemo.Model.User;
import com.meddah.kamar.springdemo.Repository.UserRepository;
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
            user.setPassword( BCrypt.hashpw( user.getPassword(), BCrypt.gensalt() ) );
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
}
