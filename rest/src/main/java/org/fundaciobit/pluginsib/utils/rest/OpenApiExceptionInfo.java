package org.fundaciobit.pluginsib.utils.rest;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;


/**
 * 
 * @author anadal
 * @deprecated Usar {@link RestExceptionInfo}
 */
// Això serveix per evitar la impressió de valors nulls
@JsonInclude(Include.NON_NULL)
@Deprecated
public class OpenApiExceptionInfo extends RestExceptionInfo {

}
