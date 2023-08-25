package org.fundaciobit.pluginsib.utils.rest;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;

import javax.ws.rs.ext.Provider;

import org.apache.log4j.Logger;

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

    protected static Logger log = Logger.getLogger(ISO8601TimestampSerializer.class);    


    @Override
    public void serialize(Timestamp date, JsonGenerator gen, SerializerProvider serializers) throws IOException {

        
        
        if (date == null) {
            gen.writeNull();
        } else {
            String s = RestUtils.convertToDateTimeToISO8601(new Date(date.getTime()));
            
            log.info("\n\n\n ISO8601TimestampSerializer serialize(Timestamp: " + date + " => " + s +  ")  \n\n\n");
            gen.writeString(s);
        }
        
    }

}
