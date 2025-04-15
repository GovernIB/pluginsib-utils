package org.fundaciobit.pluginsib.utils.ldap;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.SearchResult;

/**
 * @author anadal
 */
public class Test {

    public static void main(String[] args) {

        try {

            

            File f = new File("connection.properties");

            Properties ldapProperties = new Properties();

            if (!f.exists()) {
                System.out.println("No existeix el fitxer de configuració LDAP connection.properties");
                System.exit(-1);
            }

            ldapProperties.load(new FileInputStream(f));

            String username = ldapProperties.getProperty("test.username");
            String password = ldapProperties.getProperty("test.password");

            LDAPUserManager um = new LDAPUserManager(ldapProperties);
            
            

            testCustomFilter(ldapProperties, um);

            testAutenticate(username, password, um);

            testRequestUserInfo(username, um);

            testRole(username, um);
            
            
            testGetAllUsers(um);
            
            
            testGetUserByUsername(username, um);
        

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static void testRole(String username, LDAPUserManager um) throws Exception {
        // 7.- Roles
        List<String> roles = um.getRolesOfUser(username);
        if (roles != null) {
            System.out.println(" ------- rolesInfo(" + username + ") ------- ");
            for (String rol : roles) {
                System.out.println("    - " + rol);
            }
            System.out.println();
        }
    }

    public static void testRequestUserInfo(String username, LDAPUserManager um) throws Exception {
        int i;
        // 3.- Obtenir Usuari

        // 3.1.- Obtenir usuari per Nom

        LDAPUser u = testGetUserByUsername(username, um);

        // 3.2.- Obtenir usuari per NIF
        LDAPUser u2 = um.getUserByAdministrationID(u.getAdministrationID());
        System.out.println("El nom de l'usuari amb NIF [" + u.getAdministrationID() + "] és " + u2.getAdministrationID()
                + " " + LDAPUser.getCorrectSurname(u2));

        // 4.- Existeix usuari?
        boolean existeix = um.userExists(username);
        System.out.println("L'usuari " + username + " existeix ? " + (existeix ? "SI" : "NO"));

        // 5.- Llista només de usernames
        List<String> usernames = um.getAllUserNames();
        System.out.println(" ALL USERNAMES (" + usernames.size() + ")");
        i = 0;
        for (String un : usernames) {
            System.out.println((i++) + ".- " + un);
            if (i > 10) {
                System.out.println("...");
                break;
            }
        }
    }

    private static LDAPUser testGetUserByUsername(String username, LDAPUserManager um) throws Exception {
        LDAPUser u = um.getUserByUsername(username);
        System.out.println("NIF de l'usuari [" + username + "] és: " + u.getAdministrationID());
        String[] members = u.getMemberOf();
        for (int j = 0; j < members.length; j++) {
            System.out.println("     + member: " + members[j]);
        }
        return u;
    }

    public static void testGetAllUsers(LDAPUserManager um) throws Exception {
        // 2.- LLista de Tots els Usuaris
        LDAPUser[] all = um.getUserArray();
        int i = 0;
        System.out.println(" ALL USERS (" + all.length + ")");
        for (LDAPUser u : all) {
            String surnames = LDAPUser.getCorrectSurname(u);

            System.out.println((i++) + ".- " + u.getUserName() + " -> " + u.getName() + " " + surnames + "  ["
                    + u.getEmail() + "]");
            if (i > 10) {
                System.out.println("...");
                break;
            }
        }
    }

    public static void testAutenticate(String username, String password, LDAPUserManager um) {
        // 1.- Mètode per autenticar amb usuari contrasenya
        System.out.println("Authenticate: " + um.authenticateUser(username, password));
        System.out.println("Authenticate amb contrasenya erronia: " + um.authenticateUser(username, password + "22"));
    }

    public static void testCustomFilter(Properties ldapProperties, LDAPUserManager um)
            throws NamingException, Exception {
        // Null get all results.
        // String customFilter = ldapProperties.getProperty(LDAPConstants.LDAP_USERNAME_ATTRIBUTE) + "=e43096*";
        // String customFilter = ldapProperties.getProperty(LDAPConstants.LDAP_USERNAME_ATTRIBUTE) + "=u80*";
        // String customFilter = ldapProperties.getProperty(LDAPConstants.LDAP_ADMINISTRATIONID_ATTRIBUTE) + "=43096845*";

        // String customFilter = ldapProperties.getProperty(LDAPConstants.LDAP_SURNAMES_ATTRIBUTE) + "=Nadal*";
        
        //String customFilter = ldapProperties.getProperty(LDAPConstants.LDAP_NAME_ATTRIBUTE) + "=Antoni";

        String customFilter = "&(" + ldapProperties.getProperty(LDAPConstants.LDAP_NAME_ATTRIBUTE) + "=Antoni)("
                + ldapProperties.getProperty(LDAPConstants.LDAP_SURNAMES_ATTRIBUTE) + "=Nadal*)";

        NamingEnumeration<SearchResult> enumeration = um.searchLDAP(customFilter, null);
        List<String> list = new ArrayList<String>();
        while (enumeration.hasMore()) {
            SearchResult sr = enumeration.next();
            
            System.out.println("============================\n" + sr.getAttributes().toString());
            
            String userName = um.searchResultToUserName(sr.getAttributes());
            list.add(userName);
        }
        Collections.sort(list);
        System.out.println("Resultats de la cerca amb filtre: " + list.size());
    }

}
