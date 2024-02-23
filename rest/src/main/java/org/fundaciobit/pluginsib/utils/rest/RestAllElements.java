package org.fundaciobit.pluginsib.utils.rest;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 
 * @author anadal
 *
 * @param <D>
 */
@Schema(description = "Estructura de dades utilitzada per retornar un llistat de informació completa sense paginar")
public abstract class RestAllElements<D> {

    @Schema(
            required = false,
            description = "Elements retornats. Pot retornar un null o una llista buida si no hi ha elements.")
    @JsonProperty("data")
    protected List<D> data;

    public RestAllElements() {
        super();
    }

    public RestAllElements(List<D> data) {
        super();
        this.data = data;
    }

    public List<D> getData() {
        return data;
    }

    public void setData(List<D> data) {
        this.data = data;
    }
}