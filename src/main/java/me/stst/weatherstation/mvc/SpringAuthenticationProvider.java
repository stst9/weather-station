package me.stst.weatherstation.mvc;

import me.stst.weatherstation.domain.User;
import me.stst.weatherstation.repository.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;

@Component
public class SpringAuthenticationProvider implements AuthenticationProvider {
    private UserDAO userDAO;

    public SpringAuthenticationProvider(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username=authentication.getName();
        Object credentials=authentication.getCredentials();
        if (!(credentials instanceof String)){
            throw new IllegalArgumentException("Credentials is not instance of String");
        }
        String password= ((String) credentials);
        User user=userDAO.findByLogin(username);
        if (user==null|| !BCrypt.checkpw(password,user.getPassword())){
            throw new BadCredentialsException("Authentication failed for "+username);
        }
        return new UsernamePasswordAuthenticationToken(username, password, new ArrayList<>());
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(UsernamePasswordAuthenticationToken.class);
    }
}
