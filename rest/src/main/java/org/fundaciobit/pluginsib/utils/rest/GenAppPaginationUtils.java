package org.fundaciobit.pluginsib.utils.rest;

import java.util.List;

import org.apache.log4j.Logger;
import org.fundaciobit.genapp.common.IGenAppEntity;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.query.ITableManager;
import org.fundaciobit.genapp.common.query.OrderBy;
import org.fundaciobit.genapp.common.query.Where;

/**
 * 
 * @author anadal
 *
 */
public class GenAppPaginationUtils {

    private static Logger log = Logger.getLogger(GenAppPaginationUtils.class);

    /**
     * 
     * @param <D>
     * @param <P>
     * @param classe
     * @param ejb
     * @param page
     * @param pagesize
     * @param w
     * @param orderBy
     * @return
     * @throws I18NException 
     */
    public static <D extends IGenAppEntity, P extends AbstractPagination<D>> P createRestPagination(Class<P> classe,
            ITableManager<D, Long> ejb, int page, int pagesize, Where w, OrderBy orderBy) throws I18NException {

        final int firstResult = (page - 1) * pagesize;
        final int maxResults = pagesize;
        final List<D> llistat = ejb.select(w, null, firstResult, maxResults, orderBy);

        long countTotal = ejb.count(w);

        // PAGINACIO
        final int pageSizeOutput = pagesize;
        final int pageOutput = page;
        final int totalPages = (int) (countTotal / pagesize) + ((countTotal % pagesize == 0) ? 0 : 1);

        P paginacio;
        try {
            paginacio = classe.getConstructor().newInstance();
        } catch (Throwable e) {
            String msg = "Error instanciant un objecte de la classe " + classe + ": " + e.getMessage();
            log.error(msg, e);
            throw new I18NException("genapp.comodi", msg);
        }
        paginacio.setPagesize(pageSizeOutput);
        paginacio.setPage(pageOutput);
        paginacio.setTotalpages(totalPages);
        paginacio.setTotalcount((int) countTotal);
        paginacio.setData(llistat);
        return paginacio;
    }
}
