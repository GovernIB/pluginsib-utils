package org.fundaciobit.pluginsib.utils.rest;

import java.io.IOException;
import java.util.Date;

import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;



/**
 * 
 * @author anadal
 *
 */
@Provider
public class ISO8601DateTimeSerializer extends JsonSerializer<Date> {

    @Override
    public void serialize(Date date, JsonGenerator jgen, SerializerProvider provider)
            throws IOException, JsonProcessingException {

        if (date == null) {
            jgen.writeNull();
        } else {
            String s = RestUtils.convertToDateTimeToISO8601(date);
            jgen.writeString(s);
        }

    }

}
