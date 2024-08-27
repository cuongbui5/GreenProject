package com.example.greenproject.security.oauth2;

import com.example.greenproject.dto.req.RegisterRequest;
import com.example.greenproject.model.User;
import com.example.greenproject.model.enums.UserType;
import com.example.greenproject.security.SecurityUtils;
import com.example.greenproject.security.UserInfo;
import com.example.greenproject.service.AuthService;
import com.example.greenproject.utils.Constants;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final AuthService authService;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        System.out.println("onAuthenticationSuccess");
        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
        OAuth2User oauthUser = oauthToken.getPrincipal();
        String username = oauthUser.getAttribute("login");
        String email = oauthUser.getAttribute("email");
        System.out.println(username);
        System.out.println(email);
        User user;
        if(!authService.checkExistsByUsername(username)){
            RegisterRequest registerRequest=new RegisterRequest();
            registerRequest.setPassword(Constants.PASSWORD_GITHUB_USER);
            registerRequest.setPasswordConfirm(Constants.PASSWORD_GITHUB_USER);
            registerRequest.setUsername(username);
            registerRequest.setEmail(email);
            registerRequest.setUserType(UserType.GITHUB.name());
            user= authService.register(registerRequest);
        }else {
            System.out.println("created");
            user=authService.findByUsername(username).get();
        }
        System.out.println(user);

        UserInfo userInfo=new UserInfo();
        userInfo.setId(user.getId());
        userInfo.setUsername(user.getUsername());
        userInfo.setEmail(user.getEmail());
        userInfo.setRoles(user.getRoles());
        SecurityUtils.setJwtToClient(userInfo);
        String targetUrl = "http://localhost:3000/admin";
        getRedirectStrategy().sendRedirect(request, response, targetUrl);







    }
}
