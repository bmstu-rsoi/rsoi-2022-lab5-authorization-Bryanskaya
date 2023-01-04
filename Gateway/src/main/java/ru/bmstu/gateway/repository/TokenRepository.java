package ru.bmstu.gateway.repository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import ru.bmstu.gateway.controller.exception.data.token.JwtParsingException;
import ru.bmstu.gateway.dto.TokenRequest;
import ru.bmstu.gateway.dto.TokenResponse;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Slf4j
@Repository
public class TokenRepository extends BaseRepository {
    public TokenResponse generateToken(TokenRequest tokenRequest) {
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(appParams.jwtSecret));

        String jwt = Jwts.builder()
                .setSubject(tokenRequest.getUsername())
                .claim("clientId", tokenRequest.getClientId())
                .claim("clientSecret", tokenRequest.getClientSecret())
                .claim("scope", tokenRequest.getScope())
                .claim("grant_type", tokenRequest.getGrant_type())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + appParams.jwtTokenValidity * 1000))
                .signWith(key)
                .compact();

        return new TokenResponse().setAccessToken(jwt);
    }

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
