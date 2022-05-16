/*
 * Copyright 2016 gdeignacio.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.fundaciobit.pluginsib.utils.commons;

import java.sql.Timestamp;
import java.util.GregorianCalendar;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 *
 * @author gdeignacio
 */
public class GregorianCalendars {
    
    public static XMLGregorianCalendar timestampToXMLGregorianCalendar(Timestamp t){
        if (t == null) return null;

        GregorianCalendar gc = new GregorianCalendar();
        gc.setTimeInMillis(t.getTime());
        try {
            return DatatypeFactory.newInstance().newXMLGregorianCalendar(gc);
        } catch (DatatypeConfigurationException ex) {
            return null;
        }
    }

    public static Timestamp xmlGregorianCalendarToTimestamp(XMLGregorianCalendar xgc){
        if (xgc == null) return null;
        GregorianCalendar gc = xgc.toGregorianCalendar();
        return new Timestamp(gc.getTimeInMillis());

    }
    
}
