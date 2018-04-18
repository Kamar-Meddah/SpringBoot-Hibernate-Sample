package com.meddah.kamar.springdemo.Security.Aspect;

import com.meddah.kamar.springdemo.Model.User;
import com.meddah.kamar.springdemo.Service.AuthService;
import com.meddah.kamar.springdemo.Security.auth.AuthFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

@Component
@Aspect
public class AuthSecurityAspect {

    private final AuthService authService;

    @Autowired
    public AuthSecurityAspect(AuthService authService) {
        this.authService = authService;
    }


    @Around("@annotation(com.meddah.kamar.springdemo.Security.Annotation.Authenticated)")
    public Object checkIfAuthenticated(ProceedingJoinPoint pjp) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse();

        User user = this.authService.checkToken( request.getHeader( "Authorization" ) );
        if (user != null) {
            AuthFactory.setUser( user );
            return pjp.proceed();
        } else {
            response.sendError( 401 );
            return null;
        }

    }

    @Around("@annotation(com.meddah.kamar.springdemo.Security.Annotation.Admin)")
    public Object checkIfHasAdminRole(ProceedingJoinPoint pjp) throws Throwable {

        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse();

        User user = AuthFactory.getUser();
        if (Objects.equals( user.getRole(), "admin" )) {
            return pjp.proceed();
        } else {
            response.sendError( 401 );
            return null;
        }

    }

}
