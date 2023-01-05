package ru.bmstu.gateway.repository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import ru.bmstu.gateway.controller.exception.data.token.JwtParsingException;
import ru.bmstu.gateway.controller.exception.data.token.KeyFactoryErrorException;

import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.function.Function;

@Slf4j
@Repository
public class TokenRepository extends BaseRepository {
    public String getUsername(String token) {
        return _getClaim(token, Claims::getSubject);
    }

    public Date getExpirationDate(String token) {
        return _getClaim(token, Claims::getExpiration);
    }

    private <T> T _getClaim(String token, Function<Claims, T> claimsResolver) {
        String jwtToken = token.replace(appParams.jwtPrefix, "");

        BigInteger modulus = new BigInteger(1, Base64.getUrlDecoder().decode(appParams.modulus));
        BigInteger exponent = new BigInteger(1, Base64.getUrlDecoder().decode(appParams.exponent));

        Key key;
        try {
            key = KeyFactory.getInstance("RSA").generatePublic(new RSAPublicKeySpec(modulus, exponent));
        } catch (Exception e) {
            throw new KeyFactoryErrorException(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }

        Claims claims;
        try {
            claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(jwtToken)
                    .getBody();
        } catch (Exception e) {
            throw new JwtParsingException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return claimsResolver.apply(claims);
    }
}
