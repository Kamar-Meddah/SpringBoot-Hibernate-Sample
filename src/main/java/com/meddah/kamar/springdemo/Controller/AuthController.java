package com.meddah.kamar.springdemo.Controller;

import com.meddah.kamar.springdemo.Model.User;
import com.meddah.kamar.springdemo.Security.Annotation.Authenticated;
import com.meddah.kamar.springdemo.Security.auth.AuthFactory;
import com.meddah.kamar.springdemo.Service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

@RestController
@RequestMapping("api/auth/")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }


    @PostMapping
    public Map<String, String> authenticate(HttpServletResponse response, @RequestBody User user) throws IOException {
        Map<String, String> res = new TreeMap<>();
        User resUser = this.authService.checkEmailOrUsernameExist( user.getUsername() );
        if (resUser != null) {
            if (this.authService.checkPassword( user.getPassword(), resUser.getPassword() )) {
                String token = this.authService.generateJWT( resUser );
                res.put( "token", token );
                resUser.setRememberToken( token );
                this.authService.update( resUser );
                response.setStatus( 200 );
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
        if (this.authService.checkToken( request.getHeader( "Authorization" ) ) != null){
            response.setStatus( 200 );
        }else{
            response.sendError( 406, "Invalid token" );
        }

    }


    @PatchMapping("{id}")
    @Authenticated
    public void logout(HttpServletResponse response, @PathVariable("id") String id) throws IOException {
        if(Objects.equals( AuthFactory.getUser().getId().toString(), id )){
            this.authService.logout( AuthFactory.getUser() );
            response.setStatus( 201 );
        }else{
            response.sendError( 406,"User is not logged" );
        }
    }


}
