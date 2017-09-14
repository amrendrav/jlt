package com.tii.php.util.converter;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
*
* @author djeremias
*/
@Converter(autoApply = true)
public class LocalDateTimeConverter implements AttributeConverter<LocalDateTime, Timestamp> {
	
   @Override
   public Timestamp convertToDatabaseColumn(LocalDateTime ldt) {
	   if(ldt == null) {
   			return null;
	   }
	   
       return Timestamp.valueOf(ldt);
   }

   @Override
   public LocalDateTime convertToEntityAttribute(Timestamp ts) { 
       if (ts == null) {
           return null;
       }
       
       // ensure timestamp is returned in form of localdatetime utc
       ZonedDateTime zdt = ZonedDateTime.of(ts.toLocalDateTime(), ZoneOffset.UTC);
       return zdt.toLocalDateTime();
   }
   
}
