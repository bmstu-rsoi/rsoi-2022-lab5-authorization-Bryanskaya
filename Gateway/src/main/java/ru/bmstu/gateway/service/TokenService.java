package ru.bmstu.gateway.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.bmstu.gateway.controller.exception.data.token.JwtEmptyException;
import ru.bmstu.gateway.controller.exception.data.token.TokenExpiredException;
import ru.bmstu.gateway.repository.LoyaltyRepository;
import ru.bmstu.gateway.repository.TokenRepository;

import java.util.Date;

@Slf4j
@Service
public class TokenService {
    @Autowired
    private TokenRepository tokenRepository;
    @Autowired
    private LoyaltyRepository loyaltyRepository;


    public boolean isExpired(String token) {
        Date expirationDate = tokenRepository.getExpirationDate(token);
        return expirationDate.before(new Date());
    }

    public boolean isExists(String token) {
        loyaltyRepository.getLoyaltyInfoResponseByUsername(token);
        return true;
    }

    public boolean validateToken(String token) {
        if (token == null || token.isEmpty())
            throw new JwtEmptyException(HttpStatus.UNAUTHORIZED);

        if (isExists(token))
            if (!isExpired(token))
                return true;
            else
                throw new TokenExpiredException(HttpStatus.UNAUTHORIZED);
        return false;
    }

    public String getUsername(String token) {
        return tokenRepository.getUsername(token);
    }
}
