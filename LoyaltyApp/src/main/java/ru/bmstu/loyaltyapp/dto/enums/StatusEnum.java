package ru.bmstu.loyaltyapp.dto.enums;

import java.util.HashMap;
import java.util.Map;

public enum StatusEnum {
    BRONZE("BRONZE"),
    SILVER("SILVER"),
    GOLD("GOLD");

    private String name;

    public static final Map<StatusEnum, Integer> convert = new HashMap<>(){{
        put(BRONZE, 5);
        put(SILVER, 7);
        put(GOLD, 10);
    }};

    StatusEnum(String name) {
        this.name = name;
    }

    public StatusEnum next() {
        StatusEnum res = null;
        switch (this) {
            case BRONZE -> res = SILVER;
            case SILVER, GOLD -> res = GOLD;
        }
        return res;
    }

    public StatusEnum prev() {
        StatusEnum res = null;
        switch (this) {
            case GOLD -> res = SILVER;
            case SILVER -> res = BRONZE;
        }
        return res;
    }
}
