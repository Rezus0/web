package com.example.web_2.config;

import com.example.web_2.user.AppUserDetailsService;
import com.example.web_2.user.UserRepository;
import com.example.web_2.user.user_role.Role;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.DelegatingSecurityContextRepository;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;

@Configuration
public class SecurityConfig {

    private final UserRepository userRepository;

    public SecurityConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new DelegatingSecurityContextRepository(
                new RequestAttributeSecurityContextRepository(),
                new HttpSessionSecurityContextRepository()
        );
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity httpSecurity, SecurityContextRepository securityContextRepository) throws Exception {
        httpSecurity.
                authorizeHttpRequests(
                        authorizationManager ->
                                authorizationManager.
                                        requestMatchers(PathRequest.toStaticResources().atCommonLocations())
                                        .permitAll()
                                        .requestMatchers("/", "/login", "/register",
                                                "/login-error", "/bootstrap.min.css")
                                        .permitAll()
                                        .requestMatchers(HttpMethod.GET, "/brand", "/model")
                                        .permitAll()
                                        .requestMatchers("/offer", "/user   ", "/user-role/**",
                                                "/offer/add", "/brand/add", "/model/add",
                                                "/brand/update", "/model/update", "/offer/update")
                                        .hasRole(Role.ADMIN.name())
                                        .requestMatchers(HttpMethod.DELETE, "/brand/*")
                                        .hasRole(Role.ADMIN.name())
                                        .requestMatchers(HttpMethod.PUT, "/**")
                                        .hasRole(Role.ADMIN.name())
                                        .anyRequest().authenticated()
                )
                .formLogin(
                        formLoginConfigurer ->
                                formLoginConfigurer
                                        .loginPage("/login")
                                        .usernameParameter("username")
                                        .passwordParameter("password")
                                        .defaultSuccessUrl("/")
                                        .failureForwardUrl("/login-error")
                )
                .logout(
                        logoutConfigurer ->
                                logoutConfigurer
                                        .logoutUrl("/logout")
                                        .logoutSuccessUrl("/")
                                        .invalidateHttpSession(true)
                )
                .securityContext(
                        securityContext -> securityContext.
                                securityContextRepository(securityContextRepository)
                );
        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(8);
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new AppUserDetailsService(userRepository);
    }
}