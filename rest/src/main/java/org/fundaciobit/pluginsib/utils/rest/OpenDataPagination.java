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
@JsonPropertyOrder({ "page", "pagesize", "totalpages", "totalcount", "itemsReturned", "nextUrl", "dateDownload",
        "data" })
@Schema(description = "Estructura de dades utilitzada per retornar informació paginada de Dades Obertes")
public abstract class OpenDataPagination<D> extends AbstractPagination<D> {

    @Schema(required = true, description = "Elements retornats")
    @JsonProperty("data")
    protected List<D> data;

    @Schema(required = true, description = "Número pàgina. Comença per 1.")
    @JsonProperty("page")
    protected int page;

    @Schema(required = true, description = "Mida de pàgina")
    @JsonProperty("page-size")
    protected int pagesize;

    @Schema(required = true, description = "Número total de pàgines")
    @JsonProperty("total-pages")
    protected int totalpages;

    @Schema(required = true, description = "Numero total d'elements")
    @JsonProperty("total-count")
    protected int totalcount;

    @Schema(required = true, description = "Numero d'elements retornats")
    @JsonProperty("items-returned")
    protected int itemsReturned;

    @Schema(
            required = false,
            description = "Si hi ha més elements, llavors retorna la URL a la següent pàgina de dades.")
    @JsonProperty("next")
    protected String nextUrl;

    @Schema(
            required = true,
            pattern = RestUtils.DATE_PATTERN_ISO8601_DATE_AND_TIME,
            description = "Data i hora en que s'han retornat les dades. Format ISO-8601")
    @JsonProperty("date-download")
    protected String dateDownload;

    public OpenDataPagination() {
        super();
    }

    public OpenDataPagination(List<D> data, int page, int pagesize, int totalpages, int totalcount, String nextUrl,
            String dateDownload) {
        super();
        this.data = data;
        this.page = page;
        this.pagesize = pagesize;
        this.totalpages = totalpages;
        this.totalcount = totalcount;
        this.nextUrl = nextUrl;
        this.dateDownload = dateDownload;
        this.itemsReturned = this.data == null ? 0 : this.data.size();
    }

    @Override
    public int getPagesize() {
        return pagesize;
    }

    @Override
    public void setPagesize(int pagesize) {
        this.pagesize = pagesize;
    }

    @Override
    public int getPage() {
        return page;
    }

    @Override
    public void setPage(int page) {
        this.page = page;
    }

    @Override
    public int getTotalpages() {
        return totalpages;
    }

    @Override
    public void setTotalpages(int totalpages) {
        this.totalpages = totalpages;
    }

    @Override
    public List<D> getData() {
        return data;
    }

    @Override
    public void setData(List<D> data) {
        this.data = data;
        this.itemsReturned = this.data == null ? 0 : this.data.size();
    }

    @Override
    public int getTotalcount() {
        return totalcount;
    }

    @Override
    public void setTotalcount(int totalcount) {
        this.totalcount = totalcount;
    }

    public String getNextUrl() {
        return nextUrl;
    }

    public void setNextUrl(String nextUrl) {
        this.nextUrl = nextUrl;
    }

    public String getDateDownload() {
        return dateDownload;
    }

    public void setDateDownload(String dateDownload) {
        this.dateDownload = dateDownload;
    }

    public int getItemsReturned() {
        return itemsReturned;
    }

    public void setItemsReturned(int itemsReturned) {
        this.itemsReturned = itemsReturned;
    }

}
