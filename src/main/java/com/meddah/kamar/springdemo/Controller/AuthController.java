package com.meddah.kamar.springdemo.Controller;

import com.meddah.kamar.springdemo.Model.User;
import com.meddah.kamar.springdemo.Service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.TreeMap;

@CrossOrigin(
        allowCredentials = "false",
        origins = "*",
        methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT},
        allowedHeaders = {"Content-Type", "Authorization", "X-Requested-With"},
        maxAge = 86400
)
@RestController
@RequestMapping("api/auth/")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }


    @PostMapping("")
    public Map<String, String> authenticate(@RequestBody User user) {
        Map<String, String> res = new TreeMap<>();
        User resUser = this.authService.checkEmailOrUsernameExist( user.getUsername() );
        if (resUser != null) {
            if (this.authService.checkPassword( user.getPassword(), resUser.getPassword() )) {
                String token = this.authService.generateJWT( resUser );
                res.put( "token", token );
                res.put( "message", "Successfully Logged" );
                resUser.setRememberToken( token );
                this.authService.update( resUser );
            } else {
                res.put( "token", null );
                res.put( "message", "Wrong password" );
            }
        } else {
            res.put( "token", null );
            res.put( "message", "Username Or Email Does not exist" );
        }
        return res;
    }

    @GetMapping
    public Map<String, Boolean> checkToken(HttpServletRequest request) {
        Map<String,Boolean> res = new TreeMap<>();
        res.put( "valid", this.authService.checkToken( request.getHeader( "Authorization" ) ) != null );
        return res;
    }

    @PutMapping
    public boolean logout(HttpServletRequest request){
        return this.authService.logout(request.getHeader( "Authorization" ));
    }

}
