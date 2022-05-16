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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
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



public class FileSystemUtils {

    protected static final Logger LOG = Logger.getLogger(FileSystemUtils.class.getName());

    private static Path convert2Path() {
        return convert2Path(System.getProperty("user.home"));
    }

    public static Path convert2Path(String path) {
        path = (path == null) ? System.getProperty("user.dir") : path;
        Path p = FileSystems.getDefault().getPath(path);
        return p;
    }

    
    
    
    /*
    public static File crearFitxer(InputStream is, Path path, String filename) {
        
        
        
        File newFile = new File(getFilesPath(), String.valueOf(dstId));
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(newFile, false);
            copy(is, fos);
            fos.flush();
        } catch (Exception e) {
            throw new I18NException(e, "error.createfile",
                    new I18NArgumentString(String.valueOf(dstId)));
        } finally {
            try {
                fos.close();
            } catch (Throwable e) {
            }
        }
        return newFile;
    }
  

  public static File crearFitxer(InputStream is, Long dstId) throws I18NException {
    File newFile = new File(getFilesPath(), String.valueOf(dstId));
    FileOutputStream fos = null;
    try {
      fos = new FileOutputStream(newFile, false); 
      copy(is, fos );
      fos.flush();
    } catch(Exception e) {
      throw new I18NException(e, "error.createfile",
          new I18NArgumentString(String.valueOf(dstId)));
    } finally {
      try { fos.close(); } catch (Throwable e) { }
    }
    return newFile;
  }
  
*/

  

}


