package com.tii.php.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.tii.php.exception.TiiBusinessException;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import org.apache.commons.lang.StringUtils;


/**
 *
 * @author djeremias
 */
public class ZonedDateTimeDeserializer extends JsonDeserializer<ZonedDateTime> {

    @Override
    public ZonedDateTime deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
       ZonedDateTime zdt = null;
       DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss VV");
       String dateString = jp.getText();
       if (StringUtils.isNotBlank(dateString)) {
            zdt = ZonedDateTime.parse(dateString, format);
       } else {
           throw new TiiBusinessException("No valid ZonedDateTime with zone id formatted string passed into ZonedDateTimeDeserializer");
       }
       
       return zdt;
    }
    
}
