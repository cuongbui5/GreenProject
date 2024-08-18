package com.example.greenproject.config;

import com.example.greenproject.security.CustomAccessDeniedHandler;
import com.example.greenproject.security.CustomAuthenticationEntryPoint;
import com.example.greenproject.security.LazySecurityContextProviderFilter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.session.SessionManagementFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Collections;
import java.util.List;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   LazySecurityContextProviderFilter lazySecurityContextProviderFilter,
                                                   CustomAccessDeniedHandler customAccessDeniedHandler,
                                                   CustomAuthenticationEntryPoint customAuthenticationEntryPoint) throws Exception {
        String[] apiPrivate={"/api/*/delete/**","/api/*/create/**","/api/*/update/**"};


        //http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(s->s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(a -> {
                            a.requestMatchers("/secured/**").authenticated();
                            a.requestMatchers("/admin/**").hasAuthority("ADMIN");
                            a.anyRequest().permitAll();
                        }
                )
                //.oauth2Login(oauth2->oauth2.defaultSuccessUrl("/api/hello"))//localhost:3000
                //.formLogin(f->f.defaultSuccessUrl("/api/hello",true))
                .addFilterAfter(lazySecurityContextProviderFilter, SessionManagementFilter.class)
                .exceptionHandling(e->e
                        .accessDeniedHandler(customAccessDeniedHandler)
                        .authenticationEntryPoint(customAuthenticationEntryPoint)
                );



        return http.build();

    }


}
