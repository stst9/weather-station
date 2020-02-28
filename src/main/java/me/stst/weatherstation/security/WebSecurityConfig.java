package me.stst.weatherstation.security;

import me.stst.weatherstation.mvc.SpringAuthenticationProvider;
import me.stst.weatherstation.repository.ApiTokenDAO;
import me.stst.weatherstation.repository.UserDAO;
import me.stst.weatherstation.security.rest.AuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private ApiTokenDAO apiTokenDAO;


    @Configuration
    @Order(1)
    public class ApiWebSecurityConfig extends WebSecurityConfigurerAdapter {

        private final RequestMatcher PROTECTED_URLS = new OrRequestMatcher(
                new AntPathRequestMatcher("/rest/**")
        );

        AuthenticationProvider provider;

        public ApiWebSecurityConfig() {
            super();
            this.provider = new me.stst.weatherstation.security.rest.AuthenticationProvider(apiTokenDAO);
        }

        @Override
        protected void configure(final AuthenticationManagerBuilder auth) {
            auth.authenticationProvider(provider);
        }

        @Override
        public void configure(final WebSecurity webSecurity) {
        }

        @Override
        public void configure(HttpSecurity http) throws Exception {
            http.sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                    .exceptionHandling()
                    .and()
                    //.authenticationProvider(provider)
                    .addFilterBefore(authenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                    .authorizeRequests()
                    .antMatchers("/rest/**").authenticated()
                    .and()
                    .antMatcher("/rest/**")
                    .csrf().disable()
                    .formLogin().disable()
                    .httpBasic().disable()
                    .logout().disable();
        }

        @Bean
        AuthenticationFilter authenticationFilter() throws Exception {
            final AuthenticationFilter filter = new AuthenticationFilter(PROTECTED_URLS);
            filter.setAuthenticationManager(authenticationManager());
            //filter.setAuthenticationSuccessHandler(successHandler());
            return filter;
        }

        @Bean
        AuthenticationEntryPoint forbiddenEntryPoint() {
            return new HttpStatusEntryPoint(HttpStatus.FORBIDDEN);
        }
    }

    @Configuration
    @Order(2)
    public class ApiTokenSecurityConfig extends WebSecurityConfigurerAdapter {


        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .authorizeRequests()
                    .antMatchers("/api-public/**").permitAll()
                    .antMatchers("/**").authenticated()
                    .and()
                    .formLogin()
                    .loginPage("/auth/login")
                    .permitAll()
                    .and()
                    .logout()
                    .permitAll()
                    .and()
                    .csrf()
                    .ignoringAntMatchers("/api-public/**")
            ;
        }

        @Override
        public void configure(AuthenticationManagerBuilder builder) throws Exception {
            builder.authenticationProvider(new SpringAuthenticationProvider(userDAO));
        }

    }

    /*
    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        UserDetails user =
                User.withDefaultPasswordEncoder()
                        .username("test")
                        .password("test123")
                        .roles("USER")
                        .build();

        return new InMemoryUserDetailsManager(user);
    }
     */
}
