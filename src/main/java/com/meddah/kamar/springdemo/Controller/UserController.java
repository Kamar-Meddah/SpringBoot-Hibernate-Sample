package com.meddah.kamar.springdemo.Controller;

import com.meddah.kamar.springdemo.Model.User;
import com.meddah.kamar.springdemo.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("api/user/")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public Map<String, String> createUser(@RequestBody User user) {
        Map<String, String> res = new TreeMap<>();
        if (this.userService.checkEmailExist( user.getEmail() )) {
            res.put( "created", "false" );
            res.put( "message", "Email already exist" );
        } else if (this.userService.checkUsernameExist( user.getUsername() )) {
            res.put( "created", "false" );
            res.put( "message", "username already exist" );
        } else {
            res.put( "created", String.valueOf( this.userService.create( user ) != null ) );
            res.put( "message", "Request has been sent Successfully" );
        }
        return res;
    }
}
