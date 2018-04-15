package com.meddah.kamar.springdemo.Service;

import com.meddah.kamar.springdemo.Config.BaseConfig;
import com.meddah.kamar.springdemo.Model.User;
import com.meddah.kamar.springdemo.Repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Service
public class AuthService {

    private final UserRepository userRepository;

    @Autowired
    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User checkEmailOrUsernameExist(String username) {
        if (username.matches( "^\\w+([.-]?\\w+)*@\\w+([.-]?\\w+)*(\\.\\w{2,3})+$" )) {
            return this.userRepository.findUserByEmail( username );
        } else {
            return this.userRepository.findUserByUsername( username );
        }
    }

    public String generateJWT(User user) {
        String token = null;
        token = Jwts.builder()
                .setIssuer( user.getUsername() )
                .setSubject( String.valueOf( user.getId() ) )
                .setIssuedAt( Date.from( Instant.ofEpochMilli( System.currentTimeMillis() ) ) )
                .setExpiration( Date.from( Instant.ofEpochMilli( System.currentTimeMillis() + BaseConfig.jwtExp ) ) )
                .setAudience( String.valueOf( user.getRole() ) )
                .setId( UUID.randomUUID().toString() )
                .signWith( SignatureAlgorithm.HS256, BaseConfig.jwtSecret ).compact();
        return token;
    }

    public boolean checkPassword(String PlainPassword, String EncodedPassword) {
        boolean res = new BCryptPasswordEncoder().matches( PlainPassword, EncodedPassword );
        return res;
    }

    public void update(User user) {
        this.userRepository.save( user );
    }

    public User checkToken(String token) {
        User user;
        try {
            Jwts.parser().setSigningKey(BaseConfig.jwtSecret).parseClaimsJws( token );
            user = this.userRepository.findUserByRememberToken( token );
        } catch (Exception e) {
            user = null;
        }
        return user;
    }

}
