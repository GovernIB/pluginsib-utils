package org.fundaciobit.pluginsib.utils.rest;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 
 * @author anadal
 * 13 ene 2025 12:41:03
 */
// Això serveix per evitar la impressió de valors nulls
@JsonInclude(Include.NON_NULL)
@Schema(description = "Estructura de dades utilitzada per passar informació d'un error")
public class RestExceptionInfo {

    @Schema(description = "Codi intern de l'error. Si l'Aplicació no gestiona codis d'error llavors val null.")
    protected Integer errorCode;

    @Schema(required = true, description = "Missatge de l'error")
    protected String errorMessage;

    @Schema(description = "Stacktrace de l'excepció")
    protected String stackTrace;

    @Schema(description = "Stacktrace de l'excepció causant de l'error si n'hi hagués.")
    protected String stackTraceCause;

    @Schema(description = "Indica el camp en que hi ha un error de validació.")
    protected String field;

    public RestExceptionInfo(String errorMessage) {
        super();
        this.errorMessage = errorMessage;
    }

    public RestExceptionInfo(Integer errorCode, String errorMessage) {
        super();
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public RestExceptionInfo(String errorMessage, String field) {
        super();
        this.errorMessage = errorMessage;
        this.field = field;
    }

    public RestExceptionInfo(Integer errorCode, String errorMessage, String stackTrace, String stackTraceCause) {
        super();
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.stackTrace = stackTrace;
        this.stackTraceCause = stackTraceCause;

    }

    public RestExceptionInfo(Integer errorCode, String errorMessage, String stackTrace, String stackTraceCause,
            String field) {
        super();
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.stackTrace = stackTrace;
        this.stackTraceCause = stackTraceCause;
        this.field = field;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
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

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getStackTraceCause() {
        return stackTraceCause;
    }

    public void setStackTraceCause(String stackTraceCause) {
        this.stackTraceCause = stackTraceCause;
    }

}
