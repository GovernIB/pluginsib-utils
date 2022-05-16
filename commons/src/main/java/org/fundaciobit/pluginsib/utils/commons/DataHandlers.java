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

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.util.ByteArrayDataSource;
import org.apache.commons.io.IOUtils;


/**
 *
 * @author gdeignacio
 */
public class DataHandlers {

    public static byte[] dataHandlerToByteArray(DataHandler dataHandler) throws IOException {
        
        InputStream is = dataHandler.getInputStream();
        return IOUtils.toByteArray(is);
        
    }

    public static DataHandler byteArrayToDataHandler(byte[] arrayByte, String mimetype) {

        ByteArrayDataSource dataSource = new ByteArrayDataSource(arrayByte, mimetype);
        return new DataHandler(dataSource);

    }

    public static DataHandler byteArrayToDataHandler(byte[] arrayByte) {

        InputStream is = new BufferedInputStream(new ByteArrayInputStream(arrayByte));

        String mimetype = "";

        try {
            mimetype = URLConnection.guessContentTypeFromStream(is);
        } catch (IOException ex) {
            Logger.getLogger(DataHandlers.class.getName()).log(Level.SEVERE, null, ex);
        }

        DataSource dataSource = new ByteArrayDataSource(arrayByte, mimetype);

        return new DataHandler(dataSource);

    }

}
