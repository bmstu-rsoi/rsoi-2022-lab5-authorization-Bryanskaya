package ru.bmstu.reservationapp.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.bmstu.reservationapp.exception.data.jwtToken.JwtEmptyException;
import ru.bmstu.reservationapp.exception.data.jwtToken.TokenExpiredException;
import ru.bmstu.reservationapp.repository.TokenRepository;
import ru.bmstu.reservationapp.service.TokenService;

import java.util.Date;


@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {
    @Autowired
    private TokenRepository tokenRepository;

    public boolean validateToken(String token) {
        if (token == null || token.isEmpty())
            throw new JwtEmptyException(HttpStatus.UNAUTHORIZED);

        if (isExpired(token))
            throw new TokenExpiredException();

        return true;
    }

    public boolean isExpired(String token) {
        Date expirationDate = tokenRepository.getExpirationDate(token);
        return expirationDate.before(new Date());
    }
}
