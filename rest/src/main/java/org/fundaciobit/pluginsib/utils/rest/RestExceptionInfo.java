package org.fundaciobit.pluginsib.utils.rest;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 
 * @author anadal
 *
 */
// Això serveix per evitar la impressió de valors nulls
@JsonInclude(Include.NON_NULL)
@Schema(description = "Estructura de dades utilitzada per passar informació d'un error")
public class RestExceptionInfo {

    @Schema(required = true, description = "Codi de HTTP de l'error. Veure https://en.wikipedia.org/wiki/List_of_HTTP_status_codes.")
    protected int code;

    @Schema(required = true, description = "Missatge de l'error")
    protected String errorMessage;

    @Schema(nullable = false, description = "Stacktrace de l'excepció si n'hi hagués.")
    protected String stackTrace;

    @Schema(nullable = false, description = "Tipus de l'excepció origen (cause) si n'hi hagués.")
    protected String causeException;

    @Schema(nullable = false, description = "Stacktrace de l'excepció origen (cause) si n'hi hagués.")
    protected String causeStackTrace;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getStackTrace() {
        return stackTrace;
    }

    public void setStackTrace(String stackTrace) {
        this.stackTrace = stackTrace;
    }

    public String getCauseException() {
        return causeException;
    }

    public void setCauseException(String causeException) {
        this.causeException = causeException;
    }

    public String getCauseStackTrace() {
        return causeStackTrace;
    }

    public void setCauseStackTrace(String causeStackTrace) {
        this.causeStackTrace = causeStackTrace;
    }

}
