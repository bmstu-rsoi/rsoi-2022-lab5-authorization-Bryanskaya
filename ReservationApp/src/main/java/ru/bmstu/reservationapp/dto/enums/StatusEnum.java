package ru.bmstu.reservationapp.dto.enums;

public enum StatusEnum {
    PAID("PAID"),
    REVERSED("REVERSED"),
    CANCELED("CANCELED");

    private String name;

    StatusEnum(String name) {
        this.name = name;
    }
}
