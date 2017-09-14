package com.tii.php.util.usertype;

import org.jadira.usertype.spi.shared.AbstractStringColumnMapper;

import java.time.ZoneId;

public class StringColumnZoneIdMapper extends AbstractStringColumnMapper<ZoneId> {

    private static final long serialVersionUID = 4205713919952452881L;

    @Override
    public ZoneId fromNonNullValue(String s) {
        return ZoneId.of(s);
    }

    @Override
    public String toNonNullValue(ZoneId value) {
        return value.toString();
    }
}
