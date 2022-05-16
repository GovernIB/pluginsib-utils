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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.activation.DataHandler;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author gdeignacio
 */
public class Zips {

    public static DataHandler generateZip(Map<String, DataHandler> documents) throws IOException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(baos);

        for (Map.Entry<String, DataHandler> pair : documents.entrySet()) {
        //for (String key : documents.keySet()) {

            //InputStream is = new ByteArrayInputStream(DataHandlers.dataHandlerToByteArray(documents.get(key)));
            //ZipEntry zipEntry = new ZipEntry(key);
            
            InputStream is = new ByteArrayInputStream(DataHandlers.dataHandlerToByteArray(pair.getValue()));
            ZipEntry zipEntry = new ZipEntry(pair.getKey());
            
            zip.putNextEntry(zipEntry);
            IOUtils.copy(is, zip);
            zip.closeEntry();
            is.close();
        }

        zip.close();
        baos.close();

        return DataHandlers.byteArrayToDataHandler(baos.toByteArray(), "application/zip");

    }

}
