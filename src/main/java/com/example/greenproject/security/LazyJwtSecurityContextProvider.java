package com.example.greenproject.security;

import com.example.greenproject.dto.res.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j

@RequiredArgsConstructor
public class LazyJwtSecurityContextProvider implements SecurityContext {
    private final HttpServletRequest request;
    private final HttpServletResponse response;
    private final SecurityContext securityCtx;
    private static final long ONE_HOUR_MILLIS = 3600000L;
    @SneakyThrows
    @Override
    public Authentication getAuthentication() {
        if (securityCtx.getAuthentication() == null || securityCtx.getAuthentication() instanceof AnonymousAuthenticationToken) {
            try {
                System.out.println("Check token");
                var jwtToken = SecurityUtils.getToken(this.request);
                System.out.println(jwtToken);
                var decodedJWT = SecurityUtils.validate(jwtToken);
                UserInfo userInfo = SecurityUtils.getValueObject(decodedJWT);
                if(userInfo == null) {
                    throw new RuntimeException("Invalid token");
                }
                var authToken = new PreAuthenticatedAuthenticationToken(userInfo, null, getAllAuthorities(userInfo.getAuthorities()));

                if(decodedJWT.getExpiresAt().before(new Date(System.currentTimeMillis() + ONE_HOUR_MILLIS))){
                    System.out.println("Refresh token");
                    SecurityUtils.setJwtToClient(userInfo);
                }

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                securityCtx.setAuthentication(authToken);
            } catch (Exception e) {
                ErrorResponse res = new ErrorResponse(HttpStatus.FORBIDDEN.value(),e.getMessage());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                OutputStream responseStream = response.getOutputStream();
                ObjectMapper mapper = new ObjectMapper();
                mapper.writeValue(responseStream, res);
                responseStream.flush();


            }

        }

        return securityCtx.getAuthentication();
    }

    public List<GrantedAuthority> getAllAuthorities(List<String> authorities) {
        if (authorities == null) return new ArrayList<>();
        return authorities.stream().map(s -> (GrantedAuthority) () -> s).toList();
    }

    @Override
    public void setAuthentication(Authentication authentication) {
        securityCtx.setAuthentication(authentication);

    }
}
