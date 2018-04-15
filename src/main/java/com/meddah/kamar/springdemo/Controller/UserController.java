package com.meddah.kamar.springdemo.Controller;

import com.meddah.kamar.springdemo.Model.User;
import com.meddah.kamar.springdemo.Security.Annotation.Authenticated;
import com.meddah.kamar.springdemo.Security.auth.AuthFactory;
import com.meddah.kamar.springdemo.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.TreeMap;

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


    @PutMapping
    @Authenticated
    public Map<String, String> updateUser(@RequestBody User user) {
        Map<String, String> res = new TreeMap<>();
        if (this.userService.update( AuthFactory.getUser() ) != null) {
            res.put( "updated", String.valueOf( true ) );
            res.put( "message", "Successfully updated" );
        } else {
            res.put( "updated", String.valueOf( false ) );
            res.put( "message", "Not a valid user" );
        }
        return res;
    }
}
