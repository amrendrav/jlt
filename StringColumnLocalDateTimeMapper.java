package com.tii.php.util.usertype;

import org.jadira.usertype.spi.shared.AbstractStringColumnMapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;

public class StringColumnLocalDateTimeMapper extends AbstractStringColumnMapper<LocalDateTime> {

    public static final DateTimeFormatter DATETIME_FORMATTER = new DateTimeFormatterBuilder().appendPattern("yyyy-MM-dd'T'HH:mm:ss").appendOptional(new DateTimeFormatterBuilder().appendLiteral('.').appendFraction(ChronoField.NANO_OF_SECOND, 1, 9, false).toFormatter()).toFormatter();

    private static final long serialVersionUID = -6885561256539185520L;

    @Override
    public LocalDateTime fromNonNullValue(String s) {
        return LocalDateTime.parse(s, DATETIME_FORMATTER);
    }

    @Override
    public String toNonNullValue(LocalDateTime value) {
        return DATETIME_FORMATTER.format(value);
    }
}

