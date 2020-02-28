package me.stst.weatherstation.security.rest;

import me.stst.weatherstation.domain.ApiToken;
import me.stst.weatherstation.repository.ApiTokenDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.Collection;


@Controller
public class AuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {
    private ApiTokenDAO apiTokenDAO;

    private static final String REALM = "REST";
    private static final String AUTHENTICATION_SCHEME = "Bearer";

    public AuthenticationProvider(ApiTokenDAO apiTokenDAO) {
        this.apiTokenDAO = apiTokenDAO;
    }

    private boolean validateToken(String token)  {
        boolean ret=false;
        ApiToken apiToken=apiTokenDAO.findByToken(token);
        if (apiToken!=null){
            ret=true;
        }
        return ret;
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) throws AuthenticationException {

    }


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        System.out.println("-");
        String username=authentication.getName();
        Object credentials=authentication.getCredentials();
        /*if (!(credentials instanceof String)){
            throw new IllegalArgumentException("Credentials is not instance of String");
        }*/
        String password= ((String) credentials);
        if (!validateToken(password)){
            throw new BadCredentialsException("Unauthorized");
        }
        return new UsernamePasswordAuthenticationToken(username, password, new ArrayList<>());
    }

    @Override
    protected UserDetails retrieveUser(String s, UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) throws AuthenticationException {
        System.out.println("+");
        Object token = usernamePasswordAuthenticationToken.getCredentials();
        if (token==null){
            throw new UsernameNotFoundException("No token provided");
        }
        if (validateToken(token.toString())){
            return new User(token.toString(),token.toString(),new ArrayList<>());
        }
        return null;
    }
}
