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
@JsonPropertyOrder({ "name", "page", "pagesize", "totalpages", "totalcount", "itemsReturned", "nextUrl", "dateDownload",
        "data" })
@Schema(description = "Estructura de dades utilitzada per retornar un llistat de d'elements de informació paginada")
public class ReuseDataPagination<D> extends ReuseDataAllElements<D> implements AbstractPagination<D> {

    @Schema(required = true, description = "Número pàgina. Comença per 1.")
    @JsonProperty("page")
    protected int page;

    @Schema(required = true, description = "Mida de pàgina")
    @JsonProperty("page-size")
    protected int pagesize;

    @Schema(required = true, description = "Número total de pàgines")
    @JsonProperty("total-pages")
    protected int totalpages;

    @Schema(required = true, description = "Numero d'elements retornats")
    @JsonProperty("items-returned")
    protected int itemsReturned;

    @Schema(
            required = false,
            description = "Si hi ha més elements, llavors retorna la URL a la següent pàgina de dades.")
    @JsonProperty("next")
    protected String nextUrl;

    public ReuseDataPagination() {
        super();
    }

    public ReuseDataPagination(List<D> data, int page, int pagesize, int totalpages, int totalcount, String nextUrl,
            String dateDownload, String name) {
        super();
        this.data = data;
        this.page = page;
        this.pagesize = pagesize;
        this.totalpages = totalpages;
        this.totalcount = totalcount;
        this.nextUrl = nextUrl;
        this.dateDownload = dateDownload;
        this.itemsReturned = this.data == null ? 0 : this.data.size();
        this.name = name;
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

    public String getNextUrl() {
        return nextUrl;
    }

    public void setNextUrl(String nextUrl) {
        this.nextUrl = nextUrl;
    }

    public int getItemsReturned() {
        return itemsReturned;
    }

    public void setItemsReturned(int itemsReturned) {
        this.itemsReturned = itemsReturned;
    }

}
