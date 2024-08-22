package uz.booker.bookstore.configuration.security;


import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import uz.booker.bookstore.filter.JwtAuthenticationFilter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;
import static uz.booker.bookstore.enums.Role.*;
import static uz.booker.bookstore.util.ApiListUtil.*;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SecurityConfiguration {

    AuthenticationProvider authenticationProvider;

    JwtAuthenticationFilter jwtAuthenticationFilter;

    LogoutHandler logoutHandler;


    @Bean
    public SecurityFilterChain securityFilter(HttpSecurity http) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request ->
                        request.requestMatchers(SWAGGER_LIST).permitAll()
                                .requestMatchers(AUTH_LIST_URL)
                                .permitAll()
                                .requestMatchers("/api/v1/register-Admin").hasAuthority(SUPER_ADMIN.name())
                                .requestMatchers(BOOK_LIST_URL).hasAuthority(USER.name())
                                .requestMatchers(WHITE_LIST_URL).authenticated()
                                .requestMatchers(USER_ORDER_CART).hasAnyRole(ADMIN.name(), USER.name())
                                .requestMatchers(USER_ORDER_CART).hasAnyAuthority(ADMIN.name(), USER.name())
                                .anyRequest()
                                .authenticated()
                )

                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(logout -> logout
                        .logoutUrl("/api/v1/logout")
                        .logoutSuccessUrl("/api/v1/login")
                        .addLogoutHandler(logoutHandler)
                        .logoutSuccessHandler((request, response, authentication)
                                -> SecurityContextHolder.clearContext()))

        ;

        return http.build();
    }


}
