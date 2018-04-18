package com.meddah.kamar.springdemo.Controller;

import com.meddah.kamar.springdemo.Model.User;
import com.meddah.kamar.springdemo.Security.Annotation.Authenticated;
import com.meddah.kamar.springdemo.Security.auth.AuthFactory;
import com.meddah.kamar.springdemo.Service.AuthService;
import com.meddah.kamar.springdemo.Service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.PathParam;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.UUID;

@RestController
@RequestMapping("api/auth/")
public class AuthController {

    private final AuthService authService;
    private final EmailService emailService;

    @Autowired
    public AuthController(AuthService authService, EmailService emailService) {
        this.authService = authService;
        this.emailService = emailService;
    }


    @PostMapping
    public Map<String, String> authenticate(HttpServletResponse response, @RequestBody User user) throws IOException {
        Map<String, String> res = new TreeMap<>();
        User resUser = this.authService.checkEmailOrUsernameExist( user.getUsername() );
        if (resUser != null) {
            if (this.authService.checkPassword( user.getPassword(), resUser.getPassword() )) {
                if(resUser.getConfirmationToken() == null) {
                    String token = this.authService.generateJWT( resUser );
                    res.put( "token", token );
                    resUser.setRememberToken( token );
                    this.authService.update( resUser );
                    response.setStatus( 200 );
                }else{
                    response.sendError( 406, "Account is not yet confirmed by the admin" );
                }
            } else {
                response.sendError( 406, "Wrong password" );
            }
        } else {
            response.sendError( 406, "Username Or Email Does not exist" );
        }
        return res;
    }

    @RequestMapping(method = RequestMethod.HEAD)
    public void checkToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (this.authService.checkToken( request.getHeader( "Authorization" ) ) != null) {
            response.setStatus( 200 );
        } else {
            response.sendError( 406, "Invalid token" );
        }

    }


    @DeleteMapping("logout")
    @Authenticated
    public void logout(HttpServletResponse response, @PathParam("id") String id) throws IOException {
        if (Objects.equals( AuthFactory.getUser().getId().toString(), id )) {
            this.authService.logout( AuthFactory.getUser() );
            response.setStatus( 201 );
        } else {
            response.sendError( 406, "User is not logged" );
        }
    }

    @PutMapping
    public void resetPassword(HttpServletResponse response, @RequestBody User user) throws IOException {
        User resUser = this.authService.findByResetToken( user.getResetToken() );
        if (resUser != null) {
            resUser.setPassword( this.authService.hash( user.getPassword() ) );
            resUser.setResetToken( null );
            this.authService.update( resUser );
            response.setStatus( 201 );
        } else {
            response.sendError( 406, "Reset token is invalid" );
        }
    }

    @PatchMapping
    public void checkEmailIsValid(HttpServletResponse response, @RequestBody User user) throws IOException {
        if (user.getEmail().matches( "^\\w+([.-]?\\w+)*@\\w+([.-]?\\w+)*(\\.\\w{2,3})+$" )) {
            user = this.authService.checkEmailOrUsernameExist( user.getEmail() );
            if (user != null) {
                user.setResetToken( UUID.randomUUID().toString().replaceAll( "-", "" ) );
                this.authService.update( user );
                this.emailService.sendEmail( user.getEmail(), "Password recovery", String.join( "\n", "Hello " + user.getUsername(), "Your Recovery Token is : " + user.getResetToken() ), String.join( "\n", "<h1 style='color: firebrick'>Hello " + user.getUsername() + "</h1>", "<p>Your Recovery token is : " + user.getResetToken() + "</p>" ) );
                response.setStatus( 200 );
            } else {
                response.sendError( 406, "Not a valide Email" );
            }
        } else {
            response.sendError( 406, "Email not found" );
        }

    }


}
