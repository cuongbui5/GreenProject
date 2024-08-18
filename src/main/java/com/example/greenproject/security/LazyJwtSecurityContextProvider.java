package com.example.greenproject.security;

import com.example.greenproject.dto.Error;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import java.io.OutputStream;
import java.util.Date;
import java.util.Objects;

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
                System.out.println("check token");

                var jwtToken = SecurityUtils.getToken(this.request);
                var decodedJWT = SecurityUtils.validate(jwtToken);
                UserInfo userInfo = SecurityUtils.getValueObject(decodedJWT);
                var authToken = new PreAuthenticatedAuthenticationToken(userInfo, null, userInfo.getAllAuthorities());

                if(decodedJWT.getExpiresAt().before(new Date(System.currentTimeMillis() + ONE_HOUR_MILLIS))){
                    System.out.println("refresh token");
                    SecurityUtils.setJwtToClient(userInfo);
                }

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                securityCtx.setAuthentication(authToken);
            } catch (Exception e) {
                System.out.println("catch exception");
                Error res = new Error(e.getMessage());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                OutputStream responseStream = response.getOutputStream();
                ObjectMapper mapper = new ObjectMapper();
                mapper.writeValue(responseStream, res);
                responseStream.flush();

                log.debug("Can't get authentication context: " + e.getMessage());
            }

        }

        return securityCtx.getAuthentication();
    }

    @Override
    public void setAuthentication(Authentication authentication) {
        securityCtx.setAuthentication(authentication);

    }
}
