package com.tii.php.util.usertype;

import org.jadira.usertype.spi.shared.AbstractMultiColumnUserType;
import org.jadira.usertype.spi.shared.ColumnMapper;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * Persist {@link ZonedDateTime} via Hibernate. The offset will be stored in an extra column.
 */
public class PersistentZonedDateTimeAsStringAndStringZone extends AbstractMultiColumnUserType<ZonedDateTime> {

    private static final long serialVersionUID = -1335371912886315820L;

    private static final ColumnMapper<?, ?>[] COLUMN_MAPPERS = new ColumnMapper<?, ?>[] { new StringColumnLocalDateTimeMapper(), new StringColumnZoneIdMapper() };

    private static final String[] PROPERTY_NAMES = new String[]{ "datetime", "zoneid" };

    @Override
    protected ZonedDateTime fromConvertedColumns(Object[] convertedColumns) {

        LocalDateTime datePart = (LocalDateTime) convertedColumns[0];
        ZoneId zoneId = (ZoneId) convertedColumns[1];

        return ZonedDateTime.of(datePart, zoneId);
    }

    @Override
    protected ColumnMapper<?, ?>[] getColumnMappers() {
        return COLUMN_MAPPERS;
    }

    @Override
    protected Object[] toConvertedColumns(ZonedDateTime value) {

        return new Object[] { value.toLocalDateTime(), value.getZone() };
    }

    @Override
    public String[] getPropertyNames() {
        return PROPERTY_NAMES;
    }
}

