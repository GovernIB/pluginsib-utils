package org.fundaciobit.pluginsib.utils.rest;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;

import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * 
 * @author anadal
 *
 */
@Provider
public class ISO8601TimestampSerializer extends JsonSerializer<Timestamp> {



    @Override
    public void serialize(Timestamp date, JsonGenerator gen, SerializerProvider serializers) throws IOException {

        if (date == null) {
            gen.writeNull();
        } else {
            String s = RestUtils.convertToDateTimeToISO8601(new Date(date.getTime()));
            gen.writeString(s);
        }
        
    }

}
