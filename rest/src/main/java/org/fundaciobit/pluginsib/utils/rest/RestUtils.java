package org.fundaciobit.pluginsib.utils.rest;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.SignStyle;
import java.time.temporal.ChronoField;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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

    public static final String DATE_PATTERN_ISO8601_ONLYDATE = "^(?:[1-9]\\d{3}-(?:(?:0[1-9]|1[0-2])-(?:0[1-9]|1\\d|2[0-8])|(?:0[13-9]|1[0-2])-(?:29|30)|(?:0[13578]|1[02])-31)|(?:[1-9]\\d(?:0[48]|[2468][048]|[13579][26])|(?:[2468][048]|[13579][26])00)-02-29))$";

    public static final String DATE_PATTERN_ISO8601_DATE_AND_TIME = "^(?:[1-9]\\d{3}-(?:(?:0[1-9]|1[0-2])-(?:0[1-9]|1\\d|2[0-8])|(?:0[13-9]|1[0-2])-(?:29|30)|(?:0[13578]|1[02])-31)|(?:[1-9]\\d(?:0[48]|[2468][048]|[13579][26])|(?:[2468][048]|[13579][26])00)-02-29)T(?:[01]\\d|2[0-3]):[0-5]\\d:[0-5]\\d(?:Z|[+-][01]\\d:[0-5]\\d)$";

    public static String convertDateToOnlyDateISO8601(Date dateToConvert) {

        if (dateToConvert == null) {
            return null;
        }

        DateTimeFormatter parser = new DateTimeFormatterBuilder().appendValue(ChronoField.YEAR, 4, 4, SignStyle.NEVER)
                .appendPattern("-MM-dd").toFormatter(Locale.getDefault());

        OffsetDateTime odt2 = dateToConvert.toInstant().atOffset(ZoneOffset.UTC);

        return parser.format(odt2);

    }

    @Deprecated
    public static String convertToDateTimeToISO8601(Date dateToConvert) {
        return convertDateToDateTimeISO8601(dateToConvert);
    }

    public static String convertDateToDateTimeISO8601(Date dateToConvert) {

        if (dateToConvert == null) {
            return null;
        }

        DateTimeFormatter parser = new DateTimeFormatterBuilder().appendValue(ChronoField.YEAR, 4, 4, SignStyle.NEVER)
                .appendPattern("-MM-dd'T'HH:mm:ssXXX").toFormatter(Locale.getDefault());

        OffsetDateTime odt2 = dateToConvert.toInstant().atOffset(ZoneOffset.UTC);

        return parser.format(odt2);

    }

    /**
     * 
     * @param inputDate
     * @param paramName
     * @return
     * @throws RestException
     * @deprecate use {@link #parseOnlyDate(String, String)}
     */
    @Deprecated
    public static Date parseDate(final String inputDate, final String paramName) throws RestException {
        return parseOnlyDateISO8601ToDate(inputDate, paramName, "ca");
    }

    @Deprecated
    public static Date parseOnlyDate(final String inputDate, final String paramName) throws RestException {
        return parseOnlyDateISO8601ToDate(inputDate, paramName, "ca");
    }

    public static Date parseOnlyDateISO8601ToDate(final String inputDate, final String paramName, String language)
            throws RestException {
        Date date;
        if (StringUtils.isBlank(inputDate)) {
            date = null;
        } else {

            try {
                LocalDate myLocalDate = LocalDate.parse(inputDate);
                ZonedDateTime zdt = myLocalDate.atStartOfDay(ZoneOffset.UTC);
                Instant instant = zdt.toInstant();
                date = java.util.Date.from(instant);
            } catch (Throwable pe) {
                final String msg;
                if ("es".equalsIgnoreCase(language)) {
                    msg = "Error en el formato (" + inputDate + ") del parametre de tipo fecha con nombre " + paramName
                            + ": " + pe.getMessage();
                } else {
                    msg = "Error en el format (" + inputDate + ") del paràmetre de tipus data amb nom " + paramName
                            + ": " + pe.getMessage();
                }
                throw new RestException(msg, pe, Status.BAD_REQUEST);
            }
        }
        return date;
    }

    public static Date parseDateTimeISO8601ToDate(final String inputDate, final String paramName, String language)
            throws RestException {
        Date date;
        if (StringUtils.isBlank(inputDate)) {
            date = null;
        } else {

            try {
                LocalDateTime myLocalDate = LocalDateTime.parse(inputDate, DateTimeFormatter.ISO_DATE_TIME);
                date = Date.from(myLocalDate.atZone(ZoneOffset.UTC).toInstant());
            } catch (Throwable pe) {
                final String msg;
                if ("es".equalsIgnoreCase(language)) {
                    msg = "Error en el formato (" + inputDate + ") del parametre de tipo fecha con nombre " + paramName
                            + ": " + pe.getMessage();
                } else {
                    msg = "Error en el format (" + inputDate + ") del paràmetre de tipus data amb nom " + paramName
                            + ": " + pe.getMessage();
                }
                throw new RestException(msg, pe, Status.BAD_REQUEST);
            }
        }
        return date;
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

    @Deprecated
    public static Date[] checkRangeOfOnlyDates(final String dataIniciRequest, final String dataIniciRequestLabel,
            final String dataFiRequest, final String dataFiRequestLabel) {
        return checkRangeOfOnlyDates(dataIniciRequest, dataIniciRequestLabel, dataFiRequest, dataFiRequestLabel, "ca");
    }

    /**
     * 
     * @param dataIniciRequest Format ISO-8601 => yyyy-MM-dd
     * @param dataIniciRequestLabel Nom del parametre dataIniciRequest
     * @param dataFiRequest Format ISO-8601 => yyyy-MM-dd
     * @param dataFiRequestLabel Nom del parametre dataFiRequest
     * @param language Idioma del missatge d'error
     * @return Array de dates on [0] és datestart i [1] es dateend
     */
    public static Date[] checkRangeOfOnlyDates(final String dataIniciRequest, final String dataIniciRequestLabel,
            final String dataFiRequest, final String dataFiRequestLabel, String language) {
        final Date[] dates;
        {

            Date dateStart = parseOnlyDateISO8601ToDate(dataIniciRequest, dataIniciRequestLabel, language);

            Date dateEnd = parseOnlyDateISO8601ToDate(dataFiRequest, dataFiRequestLabel, language);

            // Comprovar que la data d'inici és anterior a la de final
            if (dateStart != null && dateEnd != null) {
                if (dateStart.getTime() >= dateEnd.getTime()) {
                    final String msg;
                    if ("es".equals(language)) {
                        msg = "La fecha de inicio debe ser menor que la fecha de fin (" + dataIniciRequest + " | "
                                + dataFiRequest + ")";
                    } else {
                        msg = "La data d'inici ha de ser menor que la data de fi (" + dataIniciRequest + " | "
                                + dataFiRequest + ")";
                    }

                    throw new RestException(msg, Status.BAD_REQUEST);
                }
            }

            dates = new Date[] { dateStart, dateEnd };
        }
        return dates;
    }

    public static String formatOffsetDateTimeToLocalTime(OffsetDateTime odt) {

        if (odt == null) {
            return null;
        }

        return odt.atZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime().toString();
    }

    public static Date atEndOfDay(final Date date) {
        return DateUtils.addMilliseconds(DateUtils.ceiling(date, Calendar.DAY_OF_MONTH), -1);
    }

    public static Date atStartOfDay(final Date date) {
        return DateUtils.truncate(date, Calendar.DAY_OF_MONTH);
    }
}
