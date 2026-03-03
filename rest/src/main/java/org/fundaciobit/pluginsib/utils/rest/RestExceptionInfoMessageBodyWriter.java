package org.fundaciobit.pluginsib.utils.rest;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * @author anadal (u80067)
 * 3 mar 2026 8:34:17
 */
@Provider
public class RestExceptionInfoMessageBodyWriter implements MessageBodyWriter<RestExceptionInfo> {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return RestExceptionInfo.class.isAssignableFrom(type);
    }

    @Override
    public void writeTo(RestExceptionInfo info, Class<?> type, Type genericType, Annotation[] annotations,
            MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream)
            throws IOException {
        // Comentari: serialitzam l'objecte d'error a JSON.
        httpHeaders.putSingle("Content-Type", MediaType.APPLICATION_JSON);
        MAPPER.writeValue(entityStream, info);
    }

    @Override
    public long getSize(RestExceptionInfo info, Class<?> type, Type genericType, Annotation[] annotations,
            MediaType mediaType) {
        return -1;
    }
}
