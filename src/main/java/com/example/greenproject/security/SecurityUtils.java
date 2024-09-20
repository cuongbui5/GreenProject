package com.example.greenproject.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

public class SecurityUtils {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String AUTHORIZATION_PREFIX = "Bearer_";
    private static final int SIX_HOURS_MILLISECOND = 1000 * 60 * 60 * 6;
    private static final int SIX_HOURS = 3600 * 6;
    private static final String USER_CLAIM = "user";
    private static final String ISSUER = "auth0";
    private static final String SECRET_KEY = "DCAWFLEFUWQDAJGDSIDHADUGADAJSGDYSAFCHOAFYCFOE";

    private static final Algorithm ALGORITHM = Algorithm.HMAC256(SECRET_KEY);



    public static String createToken(UserInfo userDetails) {
        try{
            System.out.println("createToken");
            var builder = JWT.create();
            var tokenJson = OBJECT_MAPPER.writeValueAsString(userDetails);
            builder.withClaim(USER_CLAIM, tokenJson);
            return builder
                    .withIssuedAt(new Date())
                    .withIssuer(ISSUER)
                    .withExpiresAt(new Date(System.currentTimeMillis() + SIX_HOURS_MILLISECOND))//  + SIX_HOURS_MILLISECOND
                    .sign(ALGORITHM);

        }catch (Exception e){
            e.printStackTrace();
        }
        return null;

    }

    public static void setJwtToClient(UserInfo userInfo) throws IOException {
        System.out.println("setJwtToClient");

        String token = createToken(userInfo);
        Cookie tokenCookie = new Cookie(AUTHORIZATION_HEADER, AUTHORIZATION_PREFIX + token);
        tokenCookie.setMaxAge(SIX_HOURS);
        //tokenCookie.setMaxAge(10);
        tokenCookie.setHttpOnly(true);
        tokenCookie.setPath("/");

        Cookie userInfoCookie = new Cookie("Authority", userInfo.getAuthorities().get(0));
        userInfoCookie.setMaxAge(SIX_HOURS);
        userInfoCookie.setPath("/");
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert attributes != null;
        HttpServletResponse response= attributes.getResponse();
        assert response != null;
        response.addCookie(tokenCookie);
        response.addCookie(userInfoCookie);

    }



    @SneakyThrows
    public static DecodedJWT validate(String token) {
        var verifier = JWT.require(ALGORITHM)
                .withIssuer(ISSUER)
                .build();
        return verifier.verify(token);
    }



    public static UserInfo getValueObject(DecodedJWT decodedJWT) {
        try{

            String userClaim = decodedJWT.getClaims().get(USER_CLAIM).asString();
            UserInfo userInfo= OBJECT_MAPPER.readValue(userClaim, UserInfo.class);
            System.out.println("User:"+userInfo);
            return userInfo;

        }catch (Exception e){
            e.printStackTrace();
        }
        return null;


    }



    public static String getToken(HttpServletRequest req) {
        var cookies = req.getCookies();
        if (cookies != null) {
            var authCookie = Arrays.stream(cookies)
                    .filter(e -> e.getName().equals(AUTHORIZATION_HEADER))
                    .findFirst()
                    .orElseThrow();
            String authorizationHeader = authCookie.getValue();
            //Assert.isTrue(authorizationHeader.startsWith(AUTHORIZATION_PREFIX), "Authorization header must start with '" + AUTHORIZATION_PREFIX + "'.");
            return authorizationHeader.substring(AUTHORIZATION_PREFIX.length());
        }
        return null;





    }


    public static UserInfo getSession(){
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken) {
            throw new AccessDeniedException("Not authorized.");
        }
        return (UserInfo) authentication.getPrincipal();
    }
}
