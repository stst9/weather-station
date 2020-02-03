package me.stst.weatherstation.rest.security;

import me.stst.weatherstation.domain.ApiToken;
import me.stst.weatherstation.domain.User;
import me.stst.weatherstation.repository.ApiTokenDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;

import javax.annotation.Priority;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;


@Controller
public class AuthenticationProvider implements org.springframework.security.authentication.AuthenticationProvider {
    @Autowired
    private ApiTokenDAO apiTokenDAO;
    private static final String REALM = "example";
    private static final String AUTHENTICATION_SCHEME = "Bearer";



    private boolean validateToken(String token)  {
        boolean ret=false;
        ApiToken apiToken=apiTokenDAO.findByToken(token);
        if (apiToken!=null){
            ret=true;
        }
        return ret;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username=authentication.getName();
        Object credentials=authentication.getCredentials();
        if (!(credentials instanceof String)){
            throw new IllegalArgumentException("Credentials is not instance of String");
        }
        String password= ((String) credentials);
        if (!validateToken(password)){
            throw new BadCredentialsException("Unauthorized");
        }
        return new UsernamePasswordAuthenticationToken(username, password, new ArrayList<>());
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return false;
    }
}
