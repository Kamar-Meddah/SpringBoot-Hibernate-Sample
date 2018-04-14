package com.meddah.kamar.springdemo.Config;

import com.meddah.kamar.springdemo.Config.Filters.JwtAuthFilter;
import com.meddah.kamar.springdemo.Service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class AuthorizationConfig extends WebSecurityConfigurerAdapter {


    private final JwtAuthenticationEntryPoint unauthorizedHandler;
    private final AuthService authService;

    @Autowired
    public AuthorizationConfig(JwtAuthenticationEntryPoint unauthorizedHandler, AuthService authService) {
        this.unauthorizedHandler = unauthorizedHandler;
        this.authService = authService;
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        JwtAuthFilter authenticationTokenFilter = new JwtAuthFilter(this.authService);
        httpSecurity
                // we don't need CSRF because our token is invulnerable
                .csrf().disable()
                .httpBasic().disable()
                .exceptionHandling().authenticationEntryPoint( this.unauthorizedHandler ).and()
                // don't create session
                .sessionManagement().sessionCreationPolicy( SessionCreationPolicy.STATELESS ).and()
                .antMatcher( "/h2-console" ).authorizeRequests()
                .antMatchers( "/**" ).permitAll();

                // Custom JWT based security filter
                //.addFilterBefore( authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class );
    }
}
