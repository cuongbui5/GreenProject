package com.example.greenproject.config;

import com.example.greenproject.security.CustomAccessDeniedHandler;
import com.example.greenproject.security.CustomAuthenticationEntryPoint;
import com.example.greenproject.security.LazySecurityContextProviderFilter;
import com.example.greenproject.security.oauth2.OAuth2LoginSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.session.SessionManagementFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   LazySecurityContextProviderFilter lazySecurityContextProviderFilter,
                                                   CustomAccessDeniedHandler customAccessDeniedHandler,
                                                   CustomAuthenticationEntryPoint customAuthenticationEntryPoint,
                                                   OAuth2LoginSuccessHandler auth2LoginSuccessHandler) throws Exception {
        String[] apiPrivate={"/api/*/delete/**","/api/*/create/**","/api/*/update/**"};



        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(s->s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(a -> {
                            //a.requestMatchers("/admin/**").hasAuthority("ADMIN");
                            a.requestMatchers("/api/auth/**","/order-status","/forgotPassword/**").permitAll();
                            a.anyRequest().authenticated();
                        }
                )
                .oauth2Login(oauth2->oauth2.successHandler(auth2LoginSuccessHandler)
                        .failureHandler((request, response, exception) -> {
                    System.out.println("Error oauth2:"+exception.getMessage());


                }))
                .addFilterAfter(lazySecurityContextProviderFilter, SessionManagementFilter.class)
                .exceptionHandling(e->e
                        .accessDeniedHandler(customAccessDeniedHandler)
                        .authenticationEntryPoint(customAuthenticationEntryPoint)
                );



        return http.build();

    }


}
