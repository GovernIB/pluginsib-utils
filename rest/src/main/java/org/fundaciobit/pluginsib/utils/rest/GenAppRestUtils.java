package org.fundaciobit.pluginsib.utils.rest;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;
import org.fundaciobit.genapp.common.IGenAppEntity;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.query.Field;
import org.fundaciobit.genapp.common.query.ITableManager;
import org.fundaciobit.genapp.common.query.OrderBy;
import org.fundaciobit.genapp.common.query.Where;


/**
 * 
 * @author anadal
 *
 */
public class GenAppRestUtils {

    private static Logger log = Logger.getLogger(GenAppRestUtils.class);
    
    

    
    public static <D extends IGenAppEntity, P extends AbstractPagination<D>> P createRestPagination(Class<P> classe,
            ITableManager<D, Long> ejb, int page, int pagesize, Where w, OrderBy orderBy) throws I18NException {
        
        
        GenAppEntityConverter<D, D> converter = new GenAppEntityConverter<D, D>() {

            @Override
            public D convert(D genAppEntity) throws RestException {
                return genAppEntity;
            }
        };
        
        
        return createRestPagination(classe, ejb, page, pagesize, w, orderBy, converter);
        
    }
    
    
    /**
     * 
     * @param <D>  Entitat del Model GenApp
     * @param <P>  RestPagination o ReusePagination
     * @param <R>  Rest POJO
     * @param classe
     * @param ejb
     * @param page
     * @param pagesize
     * @param w
     * @param orderBy
     * @return
     * @throws I18NException 
     */
    public static <R, D extends IGenAppEntity, P extends AbstractPagination<R>> P createRestPagination(Class<P> classe,
            ITableManager<D, Long> ejb, int page, int pagesize, Where w, OrderBy orderBy, GenAppEntityConverter<D, R> converter, String name, CharSequence nextQuery) throws I18NException {

        
        P pag = createRestPagination(classe,
                ejb, page, pagesize, w, orderBy, converter);
        
        if (pag instanceof ReuseDataPagination) {
            ReuseDataPagination<R> paginat = (ReuseDataPagination<R>)pag;
            paginat.setName(name);
            paginat.setDateDownload(RestUtils.convertDateToDateTimeISO8601(new Date()));
            paginat.setNextUrl((pag.getPage() >= pag.getTotalpages())?null:nextQuery.toString());
        }
    
        return pag;
    }
    
    
    
    /**
     * 
     * @param <D>  Entitat del Model GenApp
     * @param <P>  RestPagination o ReusePagination
     * @param <R>  Rest POJO
     * @param classe
     * @param ejb
     * @param page
     * @param pagesize
     * @param w
     * @param orderBy
     * @return
     * @throws I18NException 
     */
    public static <R, D extends IGenAppEntity, P extends AbstractPagination<R>> P createRestPagination(Class<P> classe,
            ITableManager<D, Long> ejb, int page, int pagesize, Where w, OrderBy orderBy, GenAppEntityConverter<D, R> converter) throws I18NException {

        final int firstResult = (page - 1) * pagesize;
        final int maxResults = pagesize;
        final List<D> llistatBBDD = ejb.select(w, null, firstResult, maxResults, orderBy);

        final List<R> llistat = new ArrayList<R>();
        
        for (D d : llistatBBDD) {
            llistat.add(converter.convert(d));
        }

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
     * ****
     */
    /*
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
    */

    /**
    *  
     * 
     * @param dataIniciRequest Format ISO-8601 => yyyy-MM-dd
     * @param dataIniciRequestLabel Nom del parametre dataIniciRequest
     * @param dataIniciField
     * @param dataFiRequest Format ISO-8601 => yyyy-MM-dd
     * @param dataFiRequestLabel Nom del parametre dataFiRequest
     * @param dataFiField
     * @param language Idioma del missatge d'error
     * @return Array de dates on [0] és datestart i [1] es dateend
    * @throws RestException
    */
    public static <T extends Date> GenAppRangeOfDates checkRangeOfOnlyDates(final String dataIniciRequest,
            final String dataIniciRequestLabel, final String dataFiRequest, final String dataFiRequestLabel,
            final Field<T> dataField, StringBuilder nextQuery, String language) throws RestException {
        return new CheckRangeOfOnlyDates<T>().checkRangeOfOnlyDates(dataIniciRequest, dataIniciRequestLabel,
                dataFiRequest, dataFiRequestLabel, dataField, nextQuery, language);
    }

    /**
     * 
     * @author anadal
     *
     * @param <T>
     */
    protected static class CheckRangeOfOnlyDates<T extends Date> {

        //@SuppressWarnings("unchecked")
        public Class<T> getTypeParameterClass() {
            Type type = getClass().getGenericSuperclass();
            ParameterizedType paramType = (ParameterizedType) type;
            return (Class<T>) paramType.getActualTypeArguments()[0];
        }

        public GenAppRangeOfDates checkRangeOfOnlyDates(final String dataIniciRequest,
                final String dataIniciRequestLabel, final String dataFiRequest, final String dataFiRequestLabel,
                final Field<T> dataField, StringBuilder nextQuery, String language) throws RestException {

            final Date[] dates = RestUtils.checkRangeOfOnlyDates(dataIniciRequest, dataIniciRequestLabel, dataFiRequest,
                    dataFiRequestLabel, language);

            Date startDate;
            Date endDate;

            startDate = dates[0];
            endDate = dates[1];

            Where w = null;
            if (startDate != null) {
                w = dataField.greaterThanOrEqual(createFromDate(startDate));
                nextQuery.append("&" + dataIniciRequestLabel + "=" + dataIniciRequest);
            }

            if (endDate != null) {
                Where ed = dataField.lessThanOrEqual(createFromDate(endDate));
                w = (w == null) ? ed : Where.AND(w, ed);
                nextQuery.append("&" + dataFiRequestLabel + "=" + dataFiRequest);
            }

            return new GenAppRangeOfDates(startDate, endDate, w);

        }

        private T createFromDate(Date d) {
            Class<T> clazz = getTypeParameterClass();

            try {
                T t = clazz.getDeclaredConstructor().newInstance();
                t.setTime(d.getTime());
                return t;
            } catch (Throwable e) {
                final String msg = "Error instanciant class " + clazz + ": " + e.getMessage();
                throw new RestException(msg, Status.BAD_REQUEST);
            }
        }

    }
}
