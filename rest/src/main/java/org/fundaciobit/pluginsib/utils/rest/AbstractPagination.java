package org.fundaciobit.pluginsib.utils.rest;

import java.util.List;

/**
 * 
 * @author anadal
 *
 */
public interface AbstractPagination<D> {

    public abstract int getPagesize();

    public abstract void setPagesize(int pagesize);

    public abstract int getPage();

    public abstract void setPage(int page);

    public abstract int getTotalpages();

    public abstract void setTotalpages(int totalpages);

    public abstract List<D> getData();

    public abstract void setData(List<D> data);

    public abstract int getTotalcount();

    public abstract void setTotalcount(int totalcount);
}
