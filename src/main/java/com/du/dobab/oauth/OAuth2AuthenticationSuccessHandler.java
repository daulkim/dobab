package com.du.dobab.oauth;

import com.du.dobab.jwt.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenUtil jwtTokenUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        logger.info("OAuth2AuthenticationSuccessHandler");
        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
        String name = (String) oauth2User.getAttributes().get("name");
        String email = (String) oauth2User.getAttributes().get("email");

        String jwt = jwtTokenUtil.generateToken(name, email, "ROLE_USER");
        String url = redirectUrl(jwt);

        if (response.isCommitted()) {
            logger.debug("응답이 이미 커밋된 상태입니다.");
            return;
        }

        getRedirectStrategy().sendRedirect(request, response, url);
    }

    private String redirectUrl(String token) {

        return UriComponentsBuilder
                .fromUriString("http://localhost:3000/oauth2/redirect")
                .queryParam("access_token", token)
                .build()
                .toUriString();
    }
}
