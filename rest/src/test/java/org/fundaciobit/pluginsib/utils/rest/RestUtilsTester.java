package org.fundaciobit.pluginsib.utils.rest;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Date;

/**
 * 
 * @author anadal
 *
 */
public class RestUtilsTester {


    public static void main(String[] args) {
        try {

            String lang = "ca";
            String param = "startDate";
            
            Date date = new Date();
            System.out.println("Print DATE => " + date);

            System.out.println();
            String dateStrDateTime = RestUtils.convertDateToDateTimeISO8601(date);
            System.out.println("convertDateToDateTimeISO8601 => " + dateStrDateTime);

            String dateStrOnlyDate = RestUtils.convertDateToOnlyDateISO8601(date);
            System.out.println("convertDateToOnlyDateISO8601 => " + dateStrOnlyDate);
            //

            //System.out.println(odt.toZonedDateTime().toString());

            {

                // If you want a LocalDateTime, you can get it from `odt`
                /*
                OffsetDateTime odt = OffsetDateTime.parse(dateStr);
                LocalDateTime ldt = odt.toLocalDateTime();
                */
                //OffsetDateTime odt = OffsetDateTime.now();
                //OffsetDateTime odt = OffsetDateTime.parse(dateStr);

                {
                    OffsetDateTime odt = date.toInstant().atOffset(ZoneOffset.UTC);
                    System.out.println("Data Local (desde Date) -> "
                            + RestUtils.formatOffsetDateTimeToLocalTime(odt));
                }

                {
                    OffsetDateTime odt = OffsetDateTime.parse(dateStrDateTime);
                    System.out.println("Data Local (desde String ISO8601) -> "
                            + RestUtils.formatOffsetDateTimeToLocalTime(odt));
                }
                
                
                {
                    
                    System.out.println("parseOnlyDateISO8601ToDate => " +  RestUtils.parseOnlyDateISO8601ToDate(dateStrOnlyDate, param, lang));
                    
                    System.out.println("parseDateTimeISO8601ToDate => " +  RestUtils.parseDateTimeISO8601ToDate(dateStrDateTime, param, lang));
                    
                    
                    
                }
                
            }

        } catch (Throwable th) {
            th.printStackTrace(System.err);
        }
        System.out.println("FINAL");
    }

}