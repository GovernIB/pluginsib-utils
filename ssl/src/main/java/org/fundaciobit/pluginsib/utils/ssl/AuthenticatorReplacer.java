/*
 * Copyright 2017 gdeignacio.
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
package org.fundaciobit.pluginsib.utils.ssl;

import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


/**
 *
 * @author gdeignacio
 */
public class AuthenticatorReplacer {
    
    public static void verifyHost(){
    
          // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            @Override
            public void checkClientTrusted(X509Certificate[] certs, String authType) {
                // Trust always
            }

            @Override
            public void checkServerTrusted(X509Certificate[] certs, String authType) {
                // Trust always
            }
        } };
        // Install the all-trusting trust manager
        SSLContext sc = null;
        try {
            sc = SSLContext.getInstance("SSL");
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(AuthenticatorReplacer.class.getName()).log(Level.SEVERE, null, ex);
        }
        // Create empty HostnameVerifier
        HostnameVerifier hv = new HostnameVerifier() {
            @Override
            public boolean verify(String arg0, SSLSession arg1) {
                return true;
            }
        };
        
        try {
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
        } catch (KeyManagementException ex) {
            Logger.getLogger(AuthenticatorReplacer.class.getName()).log(Level.SEVERE, null, ex);
        }
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        HttpsURLConnection.setDefaultHostnameVerifier(hv);
    
    }
    
    
    public static void setAuthenticator(String user, String password) {

        final String _user = user;
        final String _password = password;

        Authenticator.setDefault(new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(
                        _user,
                        _password.toCharArray()
                );
            }
        });
    }
    

    public static void main(String args[]) throws Exception {
        
        verifyHost();
        
        String arg0 = (args!=null && args.length>0)?args[0]:"";
        String arg1 = (args!=null && args.length>1)?args[1]:"";
        
        final String user = arg0;
        final String password = arg1;
    
        Authenticator.setDefault(new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(
                        user,
                        password.toCharArray()
                );
            }
        });

    }

}
