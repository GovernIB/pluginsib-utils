package org.fundaciobit.pluginsib.utils.rest;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

/** Deserialitzador de Timestamp emprant el format ISO8601 
 * Ha de poder parsejar formats com ls standars ISO8601 així com formats amb milisegons, nanosegons, ...
 *com per exemple 2026-06-02T13:38:18.5768656+02:00
 *
 * @author anadal (u80067)
 * 14 may 2026 10:14:04
 */
public class ISO8601TimestampDeserializer extends JsonDeserializer<Timestamp> {
    @Override
    public Timestamp deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {

        String text = p.getText();
        if (text == null || text.isEmpty()) {
            return null;
        }

        try {
            // Intentar parsejar com a OffsetDateTime amb format flexible
            OffsetDateTime odt = OffsetDateTime.parse(text, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
            return Timestamp.from(odt.toInstant());
        } catch (Exception e) {
            // Si falla, intentar parsejar amb RestUtils
            try {

                String fieldName = p.getCurrentName();

                Date date = RestUtils.parseDateTimeISO8601ToDate(text, fieldName, "ca");
                if (date == null) {
                    return null;
                }

                return new Timestamp(date.getTime());
            } catch (Exception ex) {
                // Si falla, intentar parsejar com a Timestamp estàndard
                return Timestamp.valueOf(text);
            }

        }
    }
}