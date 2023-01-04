package ru.bmstu.loyaltyapp.handler;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Error {
    private int code;
    private String message;
}
