package pl.pas.rest.security;

import io.jsonwebtoken.*;
import jakarta.xml.bind.DatatypeConverter;

import javax.crypto.spec.SecretKeySpec;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import java.security.Key;
import java.util.Date;

public class JWTGeneratorVerifier {
    private static final String SECRET = "cglOgmiEsN_LGo1Dtlft65vfL-QUPFpHZMk-iqvze7ZhfhATL91CPPXCY-N2_ONt6GbNRJ6TRcUQwnrBTvmiHfq3xqjOmFtwVtQDx-PqaMo4wJtwWQS4k6z0MCosS86n1YLvWTb8KN4G50zABbrMX-RY0N3_968j-2z8mGNCcaI";
    private static final long JWT_TIMEOUT_MS = 30 * 60 * 1000;

    public static String generateJWTString(CredentialValidationResult result) {
        //The JWT signature algorithm we will be using to sign the token
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        //We will sign our JWT with our ApiKey secret
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(SECRET);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        //Let's set the JWT Claims
        JwtBuilder builder = Jwts.builder()
                .setSubject(result.getCallerPrincipal().getName())
                .claim("auth", String.join(",", result.getCallerGroups()))
                .setIssuer("PAS Simple REST Api")
                .setExpiration(new Date(new Date().getTime() + JWT_TIMEOUT_MS))
                .setIssuedAt(now)
                .signWith(signatureAlgorithm, signingKey)
                .setHeaderParam("typ", "JWT");

        //Builds the JWT and serializes it to a compact, URL-safe string
        return builder.compact();
    }

    public static boolean validateJWTSignature(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static Claims decodeJWT(String jwt) {
        //This line will throw an exception if it is not a signed JWS (as expected)
        return Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(SECRET))
                .parseClaimsJws(jwt).getBody();
    }
}
