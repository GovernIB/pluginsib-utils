package org.fundaciobit.pluginsib.utils.rest;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

/**
 * 
 * @author anadal
 *
 */
public class RestUtils {

    public static final String MIME_APPLICATION_JSON = "application/json";
    
    public static final String MIME_APPLICATION_PDF = "application/pdf";

    public static String convertToDateTimeToISO8601(Date dateToConvert) {

        if (dateToConvert == null) {
            return null;
        }

        LocalDateTime ldt = Instant.ofEpochMilli(dateToConvert.getTime()).atZone(ZoneId.systemDefault())
                .truncatedTo(ChronoUnit.SECONDS).toLocalDateTime();

        return ldt.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

    }

    public static Date parseDate(final String inputDate, final String paramName) throws RestException {
        Date dateStart;
        if (StringUtils.isBlank(inputDate)) {
            dateStart = null;
        } else {

            try {

                LocalDate myLocalDate = LocalDate.parse(inputDate);

                ZonedDateTime zdt = myLocalDate.atStartOfDay(ZoneId.systemDefault());

                Instant instant = zdt.toInstant();
                dateStart = java.util.Date.from(instant);

            } catch (Throwable pe) {
                final String msg = "Error en el format del paràmetre de tipus date amb nom " + paramName + ": "
                        + pe.getMessage();
                throw new RestException(msg, pe, Status.BAD_REQUEST);
            }
        }
        return dateStart;
    }

    public static String checkLanguage(String language) {
        if (StringUtils.isBlank(language)) {
            language = "ca";
        } else {
            if (!"es".equals(language) && !"ca".equals(language)) {
                language = "ca";
            }
        }
        return language;
    }

    /**
     * 
     * @param dataIniciRequest Format ISO-8601 => yyyy-MM-dd
     * @param dataIniciRequestLabel Nom del parametre dataIniciRequest
     * @param dataFiRequest Format ISO-8601 => yyyy-MM-dd
     * @param dataFiRequestLabel Nom del parametre dataFiRequest
     * @return
     */
    public static Date[] checkRangeOfOnlyDates(final String dataIniciRequest, final String dataIniciRequestLabel,

            final String dataFiRequest, final String dataFiRequestLabel) {
        final Date[] dates;
        {

            Date dateStart = parseDate(dataIniciRequest, dataIniciRequestLabel);

            Date dateEnd = parseDate(dataFiRequest, dataFiRequestLabel);

            if (dateStart == null) {
                Calendar cal = Calendar.getInstance();
                if (dateEnd == null) {
                    dateEnd = cal.getTime();
                } else {
                    cal.setTime(dateEnd);
                }
                cal.add(Calendar.MONTH, -1);
                dateStart = cal.getTime();
            } else {
                Calendar cal = Calendar.getInstance();
                if (dateEnd == null) {
                    cal.setTime(dateStart);
                    cal.add(Calendar.MONTH, +1);
                    dateEnd = cal.getTime();
                } else {
                    // OK Cap dels dos és null
                }
            }

            // Comprovar que la data d'inici és anterior a la de final

            if (dateStart.getTime() >= dateEnd.getTime()) {
                // XYZ ZZZ Traduccio
                final String msg = "La data d'inici ha de ser menor que la data de fi (" + dataIniciRequest + " | "
                        + dataFiRequest + ")";

                throw new RestException(msg, Status.BAD_REQUEST);

            }

            dates = new Date[] { dateStart, dateEnd };
        }
        return dates;
    }

    public static void main(String[] args) {
        try {

            System.out.println(convertToDateTimeToISO8601(new Date()));

            Date date = parseDate("2023-02-23", "prova");
            System.out.println(date);

            //            date = parseDate("12-02-2023", "prova");
            //            System.err.println(date);

        } catch (Throwable th) {
            th.printStackTrace(System.err);
        }
        System.out.println("FINAL");
    }

    public static Date atEndOfDay(final Date date) {
        return DateUtils.addMilliseconds(DateUtils.ceiling(date, Calendar.DAY_OF_MONTH), -1);
    }

    public static Date atStartOfDay(final Date date) {
        return DateUtils.truncate(date, Calendar.DAY_OF_MONTH);
    }
}
