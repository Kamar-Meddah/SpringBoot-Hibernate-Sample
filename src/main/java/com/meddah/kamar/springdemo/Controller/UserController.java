package com.meddah.kamar.springdemo.Controller;

import com.meddah.kamar.springdemo.Model.User;
import com.meddah.kamar.springdemo.Security.Annotation.Authenticated;
import com.meddah.kamar.springdemo.Security.auth.AuthFactory;
import com.meddah.kamar.springdemo.Service.AuthService;
import com.meddah.kamar.springdemo.Service.EmailService;
import com.meddah.kamar.springdemo.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("api/user/")
public class UserController {

    private final UserService userService;
    private final AuthService authService;
    private final EmailService emailService;

    @Autowired
    public UserController(UserService userService, AuthService authService, EmailService emailService) {
        this.userService = userService;
        this.authService = authService;
        this.emailService = emailService;
    }

    @PostMapping
    public void createUser(HttpServletResponse response, @RequestBody User user) throws IOException {
        if (this.userService.checkEmailExist( user.getEmail() )) {
            response.sendError( 406, "Email already exist" );
        } else if (this.userService.checkUsernameExist( user.getUsername() )) {
            response.sendError( 406, "username already exist" );
        } else {
            User newUser = this.userService.create( user );
            this.emailService.sendEmail( newUser.getEmail(), "Welcome", String.join( "\n", "Hello " + newUser.getUsername(), "thx for your registration" ), String.join( "\n", "<h1 style='color: firebrick'>Hello " + newUser.getUsername() + "</h1>", "<p>Thx for your registration</p>" ) );
            response.setStatus( 201 );
        }
    }

    @PutMapping("{id}")
    @Authenticated
    public void updateUser(HttpServletResponse response, @RequestBody Map<String, String> input, @PathVariable("id") String id) throws IOException {
        if (Objects.equals( AuthFactory.getUser().getId().toString(), id )) {
            User user = new User( input.get( "email" ), input.get( "password" ) );
            // password update
            if (user.getPassword() != null) {
                if (this.authService.checkPassword( input.get( "oldPassword" ), AuthFactory.getUser().getPassword() )) {
                    this.userService.update( user );
                    response.setStatus( 201 );
                } else {
                    response.sendError( 406, "Wrong Password" );
                }
            }
            // end password update

            //  Email Update
            if (user.getEmail() != null) {
                if (this.authService.checkEmailOrUsernameExist( user.getEmail() ) == null) {
                    this.userService.update( user );
                    response.setStatus( 201 );
                } else {
                    response.sendError( 406, "Email already exist" );
                }
            }
            //  End Email Update
        } else {
            response.sendError( 401 );
        }
    }

    @PatchMapping("password")
    public void resetPassword() {
        //
    }

    @RequestMapping(value = "", method = RequestMethod.HEAD)
    public boolean checkEmailIsValid() {
        return false;
    }
}
