package org.fundaciobit.pluginsib.utils.rest;

import javax.ws.rs.core.Response.Status;

/**
 * 
 * @author anadal
 * @deprecated Usar {@link RestUtils}
 */
@Deprecated
public class OpenApiException extends RestException {

    public OpenApiException(String message, int status) {
        super(message, status);

    }

    public OpenApiException(String message, Status status) {
        super(message, status);

    }

    public OpenApiException(String message, Throwable cause, int status) {
        super(message, cause, status);

    }

    public OpenApiException(String message, Throwable cause, Status status) throws IllegalArgumentException {
        super(message, cause, status);

    }

    public OpenApiException(String message, Throwable cause) {
        super(message, cause);

    }

    public OpenApiException(String message) {
        super(message);

    }

}
