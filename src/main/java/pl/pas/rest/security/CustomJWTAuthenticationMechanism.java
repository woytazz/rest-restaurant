package pl.pas.rest.security;

import io.jsonwebtoken.Claims;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.security.enterprise.AuthenticationException;
import javax.security.enterprise.AuthenticationStatus;
import javax.security.enterprise.authentication.mechanism.http.HttpAuthenticationMechanism;
import javax.security.enterprise.authentication.mechanism.http.HttpMessageContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;

@ApplicationScoped
public class CustomJWTAuthenticationMechanism implements HttpAuthenticationMechanism {
    public final static String AUTHORIZATION_HEADER = "Authorization";
    public final static String BEARER = "Bearer ";

    @Inject
    JWTGeneratorVerifier jwtGeneratorVerifier;


    @Override
    public AuthenticationStatus validateRequest(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, HttpMessageContext httpMessageContext) throws AuthenticationException {
        if (httpServletRequest.getRequestURL().toString().endsWith("/api/login")) {
            return httpMessageContext.doNothing();
        }

        String authorizationHeader = httpServletRequest.getHeader(AUTHORIZATION_HEADER);
        if (authorizationHeader == null || !authorizationHeader.startsWith(BEARER)) {
            return httpMessageContext.responseUnauthorized();
        }

        String jwtSerializedToken = authorizationHeader.substring(BEARER.length()).trim();

        if (JWTGeneratorVerifier.validateJWTSignature(jwtSerializedToken)) {
            Claims claims = JWTGeneratorVerifier.decodeJWT(jwtSerializedToken);
            String login = claims.getSubject();
            String groups = (String) claims.get("auth");
            Date expirationDate = claims.getExpiration();
            boolean tokenExpired = new Date().after(expirationDate);

            if (tokenExpired) {
                return httpMessageContext.responseUnauthorized();
            }

            return httpMessageContext.notifyContainerAboutLogin(login, new HashSet<>(Arrays.asList(groups.split(","))));
        }
        return null;
    }
}
