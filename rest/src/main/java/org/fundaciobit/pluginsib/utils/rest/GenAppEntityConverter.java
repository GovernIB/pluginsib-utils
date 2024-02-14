package org.fundaciobit.pluginsib.utils.rest;

import org.fundaciobit.genapp.common.IGenAppEntity;

/**
 * 
 * Converteix una entitat GenAPP a un PoJO normalment utilitzat en REST
 * @author anadal
 *
 * @param <S> 
 * @param <R> Rest POJO
 */
public interface GenAppEntityConverter<S extends IGenAppEntity,R> {

    /**
     * 
     * @param genAppEntity
     * @return
     * @throws RestException
     */
    public R convert(S genAppEntity) throws RestException;
}
