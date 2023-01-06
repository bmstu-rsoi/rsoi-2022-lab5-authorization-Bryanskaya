package ru.bmstu.gateway.dto.enums;

public enum UserStatusEnum {
    BRONZE("BRONZE"),
    SILVER("SILVER"),
    GOLD("GOLD");

    private String name;

    UserStatusEnum(String name) {
        this.name = name;
    }
}