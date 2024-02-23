package org.fundaciobit.pluginsib.utils.rest;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 
 * @author anadal
 *
 * @param <D>
 */
@JsonPropertyOrder({ "name", "totalcount", "dateDownload", "data" })
@Schema(description = "Estructura de dades utilitzada per retornar un llistat de informació completa sense paginar")
public abstract class ReuseDataAllElements<D> extends RestAllElements<D> {

    @Schema(required = true, description = "Número total d'elements")
    @JsonProperty("total-count")
    protected int totalcount;

    @Schema(
            required = true,
            pattern = RestUtils.DATE_PATTERN_ISO8601_DATE_AND_TIME,
            description = "Data i hora en que s'han retornat les dades. Format ISO-8601")
    @JsonProperty("date-download")
    protected String dateDownload;

    @Schema(required = true, description = "Nom descriptiu del que s'està retornant")
    @JsonProperty("name")
    protected String name;

    public ReuseDataAllElements() {
        super();
    }

    public ReuseDataAllElements(List<D> data, int totalcount, String dateDownload, String name) {
        super(data);
        this.totalcount = totalcount;
        this.dateDownload = dateDownload;
        this.name = name;
    }

    public int getTotalcount() {
        return totalcount;
    }

    public void setTotalcount(int totalcount) {
        this.totalcount = totalcount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateDownload() {
        return dateDownload;
    }

    public void setDateDownload(String dateDownload) {
        this.dateDownload = dateDownload;
    }
}
