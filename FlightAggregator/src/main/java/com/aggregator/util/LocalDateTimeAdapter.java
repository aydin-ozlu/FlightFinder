package com.aggregator.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeAdapter {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

    // (stub’a set ederken)
    public static String marshal(LocalDateTime ldt) {
        return (ldt == null) ? null : ldt.format(formatter);
    }

    // (stub’tan get ederken)
    public static LocalDateTime unmarshal(String value) {
        return (value == null || value.isEmpty()) ? null : LocalDateTime.parse(value, formatter);
    }
}
