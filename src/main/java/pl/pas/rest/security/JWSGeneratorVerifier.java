package pl.pas.rest.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.xml.bind.DatatypeConverter;
import pl.pas.rest.dto.UserGetDTO;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;

public class JWSGeneratorVerifier {
    private static final String SECRET = "cglOgmiEsN_LGo1Dtlft65vfL-QUPFpHZMk-iqvze7ZhfhATL91CPPXCY-N2_ONt6GbNRJ6TRcUQwnrBTvmiHfq3xqjOmFtwVtQDx-PqaMo4wJtwWQS4k6z0MCosS86n1YLvWTb8KN4G50zABbrMX-RY0N3_968j-2z8mGNCcaI";

    public static String generateJWSString(UserGetDTO userGetDTO) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(SECRET);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        JwtBuilder builder = Jwts.builder()
                .claim("uuid", userGetDTO.getUuid())
                .claim("isActive", userGetDTO.getIsActive())
                .claim("login", userGetDTO.getLogin())
                .claim("role", userGetDTO.getRole())
                .signWith(signatureAlgorithm, signingKey)
                .setHeaderParam("typ", "JWS");

        return builder.compact();
    }

    public static boolean validateJWSSignature(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static Claims decodeJWS(String jwt) {
        return Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(SECRET))
                .parseClaimsJws(jwt).getBody();
    }
}
