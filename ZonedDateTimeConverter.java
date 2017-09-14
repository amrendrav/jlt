package com.tii.php.util.converter;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
*
* @author djeremias
*/
@Converter(autoApply = true)
public class ZonedDateTimeConverter implements AttributeConverter<ZonedDateTime, Timestamp> {
	
   @Override
   public Timestamp convertToDatabaseColumn(ZonedDateTime zdt) {
	   if (zdt == null) {
		   return null;
	   }

       return Timestamp.valueOf(zdt.withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime());
   }

   @Override
   public ZonedDateTime convertToEntityAttribute(Timestamp ts) { 
       if (ts == null) {
           return null;
       }
       
       return ts.toLocalDateTime().atZone(ZoneId.of("UTC"));
   }
   
}
