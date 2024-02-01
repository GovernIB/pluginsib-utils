package org.fundaciobit.pluginsib.utils.rest;

import java.util.List;

/**
 * 
 * @author anadal
 *
 * @param <D>
 */
@Deprecated
public abstract class OpenDataPagination<D> extends ReuseDataPagination<D> {

    public OpenDataPagination() {
        super();
    }

    public OpenDataPagination(List<D> data, int page, int pagesize, int totalpages, int totalcount, String nextUrl,
            String dateDownload) {
        super(data, page, pagesize, totalpages, totalcount, nextUrl, dateDownload, null);
    }

}
