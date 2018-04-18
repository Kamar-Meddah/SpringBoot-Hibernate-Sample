package com.meddah.kamar.springdemo.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.meddah.kamar.springdemo.Exception.UserException;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {
    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Column(name = "username", nullable = false, unique = true)
    private String username;
    @Column(name = "email", nullable = false, unique = true)
    private String email;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "role")
    private String role;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "reset_token", unique = true)
    private String resetToken;
    @JsonIgnore
    @Column(name = "remember_token", unique = true)
    @Size(max = 305)
    private String rememberToken;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column(name = "confirmation_token", unique = true)
    private String confirmationToken;
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User() {
    }

    public void setEmail(String email) throws UserException {
        if (!((email.matches( "^\\w+([.-]?\\w+)*@\\w+([.-]?\\w+)*(\\.\\w{2,3})+$" )))) {
            throw new UserException( "Invalid Email" );
        }
        this.email = email;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                ", resetToken='" + resetToken + '\'' +
                ", rememberToken='" + rememberToken + '\'' +
                ", confirmationToken='" + confirmationToken + '\'' +
                '}';
    }

}
