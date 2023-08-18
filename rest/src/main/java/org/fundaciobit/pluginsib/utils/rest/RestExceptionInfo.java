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
public class RestExceptionInfo {

    @Schema(required = true, description = "")
    protected int code;

    @Schema(required = true, description = "")
    protected String errorMessage;

    @Schema(nullable = false)
    protected String stackTrace;

    @Schema(nullable = false)
    protected String causeException;

    @Schema(nullable = false)
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
