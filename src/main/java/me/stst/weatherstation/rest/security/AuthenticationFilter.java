package me.stst.weatherstation.rest.security;

import me.stst.weatherstation.repository.ApiTokenDAO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

public class AuthenticationFilter  extends AbstractAuthenticationProcessingFilter {
    private ApiTokenDAO apiTokenDAO;

    public AuthenticationFilter(final RequestMatcher requiresAuth, ApiTokenDAO apiTokenDAO) {
        super(requiresAuth);
        this.apiTokenDAO=apiTokenDAO;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException, IOException, ServletException {

        Optional tokenParam = Optional.ofNullable(httpServletRequest.getHeader(AUTHORIZATION)); //Authorization: Bearer TOKEN
        String token= httpServletRequest.getHeader(AUTHORIZATION);
        token= StringUtils.removeStart(token, "Bearer").trim();
        Authentication requestAuthentication = new UsernamePasswordAuthenticationToken(token, token);
        if (apiTokenDAO.findByToken(token)==null){
            throw new BadCredentialsException("Unauthorised");
        }
        return new UsernamePasswordAuthenticationToken(token, token, new ArrayList<>());

    }

    @Override
    protected void successfulAuthentication(final HttpServletRequest request, final HttpServletResponse response, final FilterChain chain, final Authentication authResult) throws IOException, ServletException {
        SecurityContextHolder.getContext().setAuthentication(authResult);
        chain.doFilter(request, response);
    }
}
