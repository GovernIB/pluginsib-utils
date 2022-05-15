package org.fundaciobit.pluginsib.utils.ws.connexio;

/**
 *
 * @author gdeignacio
 */
public class BadConfiguredConnectionException extends RuntimeException{
    public BadConfiguredConnectionException(String message){
        super(message);
    }
}