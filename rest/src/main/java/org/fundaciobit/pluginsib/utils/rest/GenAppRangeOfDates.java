package org.fundaciobit.pluginsib.utils.rest;

import java.util.Date;

import org.fundaciobit.genapp.common.query.Where;

/**
 * 
 * @author anadal
 *
 */
public class GenAppRangeOfDates {

    protected Date startDate;

    protected Date endDate;

    protected Where where;

    public GenAppRangeOfDates() {
        super();
    }

    public GenAppRangeOfDates(Date startDate, Date endDate, Where where) {
        super();
        this.startDate = startDate;
        this.endDate = endDate;
        this.where = where;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Where getWhere() {
        return where;
    }

    public void setWhere(Where where) {
        this.where = where;
    }

}
