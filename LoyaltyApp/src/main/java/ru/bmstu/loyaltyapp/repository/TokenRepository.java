package ru.bmstu.loyaltyapp.repository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import ru.bmstu.loyaltyapp.config.AppParams;
import ru.bmstu.loyaltyapp.exception.data.jwtToken.JwtParsingException;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;


@Repository
public class TokenRepository {
    @Autowired
    private AppParams appParams;


    public String getUsername(String token) {
        return _getClaim(token, Claims::getSubject);
    }

    public Date getExpirationDate(String token) {
        return _getClaim(token, Claims::getExpiration);
    }

    private <T> T _getClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims;
        String jwtToken = token.replace(appParams.jwtPrefix, "");

        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(appParams.jwtSecret));
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
