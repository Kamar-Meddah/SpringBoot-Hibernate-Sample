package com.meddah.kamar.springdemo.Config.Filters;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        System.out.print( request.getHeader( "Authorization" ) );
        System.out.print( SecurityContextHolder.getContext().getAuthentication() );

        if (request.getMethod().equals( "GET" )) {
            chain.doFilter( request, response );
        } else {
            if (request.getHeader( "Authorization" ) != null) {
                chain.doFilter( request, response );
            } else {
                response.sendError( 401 );
            }
        }
    }
}
