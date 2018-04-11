package com.meddah.kamar.springdemo.Configs;

import com.meddah.kamar.springdemo.Configs.Filters.JwtAuthFilter;
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

    @Autowired
    public AuthorizationConfig(JwtAuthenticationEntryPoint unauthorizedHandler) {
        this.unauthorizedHandler = unauthorizedHandler;
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        JwtAuthFilter authenticationTokenFilter = new JwtAuthFilter();
        httpSecurity
                // we don't need CSRF because our token is invulnerable
                .httpBasic().disable()
                .csrf().disable()
                //  we need CORS support
                .cors().and()
                .exceptionHandling().authenticationEntryPoint(this.unauthorizedHandler).and()

                // don't create session
                .sessionManagement().sessionCreationPolicy( SessionCreationPolicy.STATELESS ).and()
                .authorizeRequests()

                .antMatchers( "/**" ).permitAll().and()
                .antMatcher( "/h2-console" ).authorizeRequests()
                .antMatchers( "/**" ).permitAll().and()
                .antMatcher( "/fuck" )

                // Custom JWT based security filter
                .addFilterBefore( authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class );


    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins( Arrays.asList( "*" ) );
        configuration.setAllowedMethods( Arrays.asList( "GET", "POST", "DELETE", "PUT" ) );
        configuration.setAllowedHeaders( Arrays.asList( "Content-Type", "Authorization", "X-Requested-With" ) );
        configuration.setAllowCredentials( false );
        configuration.setMaxAge( (long) 86400 );
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration( "/**", configuration );
        return source;
    }

}
