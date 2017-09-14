/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tii.php.util.converter;

import java.sql.Time;
import java.time.LocalTime;
import javax.persistence.AttributeConverter;

/**
 *
 * @author djeremias
 */
public class LocalTimeConverter implements AttributeConverter<LocalTime, Time>{

    @Override
    public Time convertToDatabaseColumn(LocalTime attribute) {
        if (attribute == null) {
            return null;
        }
       
        return Time.valueOf(attribute);
    }

    @Override
    public LocalTime convertToEntityAttribute(Time dbData) {
        if (dbData == null) {
            return null;
        }
        
        return dbData.toLocalTime();
    }
    
}
