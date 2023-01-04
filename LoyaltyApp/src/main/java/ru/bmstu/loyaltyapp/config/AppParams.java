package ru.bmstu.loyaltyapp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AppParams {
    @Value(value = "${const.jwt.token.prefix}")
    public String jwtPrefix;
    @Value(value = "${const.jwt.secret}")
    public String jwtSecret;
}
