package com.meddah.kamar.springdemo.Config.Filters;

import com.meddah.kamar.springdemo.Model.User;
import com.meddah.kamar.springdemo.Service.AuthService;
import com.meddah.kamar.springdemo.auth.AuthFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthFilter extends OncePerRequestFilter {

    private final AuthService authService;

    @Autowired
    public JwtAuthFilter(AuthService authService) {
        this.authService = authService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        User user = this.authService.checkToken( request.getHeader( "Authorization" ) );
        if (user != null) {
            AuthFactory.setUser( user );
            chain.doFilter( request, response );
        } else {
            response.sendError( 401 );
        }
    }

}
