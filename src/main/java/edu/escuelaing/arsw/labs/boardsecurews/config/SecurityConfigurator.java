package edu.escuelaing.arsw.labs.boardsecurews.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import edu.escuelaing.arsw.labs.boardsecurews.cache.TokenCache;

@Configuration
public class SecurityConfigurator {

    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityConfigurator.class);
    public static final String TOKEN_COOKIE_NAME = "BoardToken";

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @SuppressWarnings("java:S112")
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.httpBasic()
                .and()
                .authorizeHttpRequests()
                .antMatchers("/").authenticated()
                .and()
                .formLogin().successHandler(new AuthenticationSuccessHandler() {

                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                            Authentication authentication) throws IOException, ServletException {
                        LOGGER.info("Login success");
                        String token = TokenCache.generateAndAddNewToken();
                        if (!token.equals("")) {
                            response.addCookie(new Cookie(TOKEN_COOKIE_NAME, token));
                        }
                        response.sendRedirect("/");
                    }

                })
                .and()
                .logout().permitAll().logoutSuccessHandler(new LogoutSuccessHandler() {

                    @Override
                    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
                            Authentication authentication) throws IOException, ServletException {
                        LOGGER.info("Logout success");
                        for (Cookie cookie : request.getCookies()) {
                            if (cookie.getName().equals(TOKEN_COOKIE_NAME)) {
                                TokenCache.getTokens().remove(cookie.getValue());
                            }
                        }
                        response.addCookie(new Cookie(TOKEN_COOKIE_NAME, null));
                        response.sendRedirect("/login");
                    }

                })
                .and()
                .build();
    }

    @Bean
    InMemoryUserDetailsManager userDetailsService() {
        UserDetails userDetails = User.builder()
                .username("daniel")
                .password(passwordEncoder().encode("password"))
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(userDetails);
    }
}
