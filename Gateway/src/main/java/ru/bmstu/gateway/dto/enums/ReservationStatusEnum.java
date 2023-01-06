package ru.bmstu.gateway.dto.enums;

public enum ReservationStatusEnum {
    PAID("PAID"),
    REVERSED("REVERSED"),
    CANCELED("CANCELED");

    private String name;

    ReservationStatusEnum(String name) {
        this.name = name;
    }
}
