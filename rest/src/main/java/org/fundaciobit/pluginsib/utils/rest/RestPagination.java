package org.fundaciobit.pluginsib.utils.rest;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 
 * @author anadal
 *
 * @param <D>
 */
@Schema(description = "Estructura de dades utilitzada per passar informació paginada d'un llistat")
public abstract class RestPagination<D> {

    @Schema(required=true, description = "Mida de pàgina")
    protected int pagesize;
    @Schema(required=true, description = "Número pàgina. Comença per 1.")
    protected int page;
    @Schema(required=true, description = "Número total de pàgines")
    protected int totalpages;
    @Schema(required=true, description = "Numero total d'elements")
    protected int totalcount;
    @Schema(required=true, description = "Elements retornats")
    protected List<D> data;

    public RestPagination() {
        super();
    }
    

    public RestPagination(int pagesize, int page, int totalpages, int totalcount, List<D> data) {
        super();
        this.pagesize = pagesize;
        this.page = page;
        this.totalpages = totalpages;
        this.totalcount = totalcount;
        this.data = data;
    }

    public int getPagesize() {
        return pagesize;
    }

    public void setPagesize(int pagesize) {
        this.pagesize = pagesize;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotalpages() {
        return totalpages;
    }

    public void setTotalpages(int totalpages) {
        this.totalpages = totalpages;
    }

    public List<D> getData() {
        return data;
    }

    public void setData(List<D> data) {
        this.data = data;
    }

    public int getTotalcount() {
        return totalcount;
    }

    public void setTotalcount(int totalcount) {
        this.totalcount = totalcount;
    }

}
