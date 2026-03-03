package org.fundaciobit.pluginsib.utils.rest;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 * 
 * @author anadal
 * 13 ene 2025 12:27:26
 */
public class RestException extends WebApplicationException {

    public static boolean exportStackTrace = false;
    public static boolean exportStackTraceCause = false;

    protected final RestExceptionInfo info;

    /**
     * Llançarà un statuscode 500 (InternalServerError)
     * @param errorMessage
     */
    public RestException(String errorMessage) {
        this(Status.INTERNAL_SERVER_ERROR, null, errorMessage, null, null);
    }

    public RestException(Status status, String errorMessage) {
        this(status, null, errorMessage, null, null);
    }

    public RestException(Status status, String errorMessage, Throwable cause) {
        this(status, null, errorMessage, cause, null);
    }

    /**
     * Llançarà un statuscode 500 (InternalServerError)
     * @param errorMessage
     * @param cause
     */
    public RestException(String errorMessage, Throwable cause) {
        this(Status.INTERNAL_SERVER_ERROR, null, errorMessage, cause, null);
    }

    /**
     * Error de validació en un camp o objecte. Llançarà un statuscode 400 (BadRequest)
     * @param errorMessage
     * @param field
     */
    public RestException(String errorMessage, String field) {
        this(Status.BAD_REQUEST, null, errorMessage, null, field);
    }

    public RestException(Status status, String errorMessage, String field) {
        this(status, null, errorMessage, null, field);
    }

    public RestException(Status status, Integer errorCode, String errorMessage, Throwable cause, String field) {
        super(errorMessage, cause, status);
        info = new RestExceptionInfo(errorCode, errorMessage);
        if (exportStackTrace) {
            info.setStackTrace(ExceptionUtils.getStackTrace(this));
        }
        if (exportStackTraceCause && cause != null) {
            info.setStackTrace(ExceptionUtils.getStackTrace(cause));
        }
        info.setField(field);
    }

    @Override
    public Response getResponse() {
        return Response.status(super.getResponse().getStatus()).entity(info).build();
    }

    public RestExceptionInfo getInfo() {
        return info;
    }

}
