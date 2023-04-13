package org.fundaciobit.pluginsib.utils.ldap;

import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import org.apache.log4j.Logger;

/**
 * LDAP User Manager
 * @author anadal
 * 
 */
public class LDAPUserManager implements LDAPConstants, Serializable {

    private Logger log = Logger.getLogger(this.getClass());

    /**
     * 
     */
    private static final long serialVersionUID = 782306531064001518L;

    /**
     * LDAP Properties.
     */
    private final Properties ldapProperties;

    /**
     * Initializes the configuration instance from the specified context wrapper.
     */
    public LDAPUserManager(File f) throws Exception {
        Properties prop = new Properties();
        prop.load(new FileInputStream(f));
        this.ldapProperties = prop;
    }

    /**
     * Initializes the configuration.
     */
    public LDAPUserManager(Properties ldapProp) {
        ldapProperties = ldapProp;
    }

    public LDAPUser getUserByUsername(String userName) throws Exception {
        SearchResult result = internalGetUser(LDAP_USERNAME_ATTRIBUTE, userName);
        if (result == null) {
            return null;
        } else {
            return convertAttributesToLdapUser(result.getAttributes());
        }
    }

    public LDAPUser getUserByAdministrationID(String nif) throws Exception {
        SearchResult result = internalGetUser(LDAP_ADMINISTRATIONID_ATTRIBUTE, nif);
        if (result == null) {
            return null;
        } else {
            return convertAttributesToLdapUser(result.getAttributes());
        }
    }

    /**
     *
     * @return SearchResult associated to user <code>userName</code>.
     */
    private SearchResult internalGetUser(String attribute, String value) throws Exception {
        SearchResult result = null;
        try {
            String usernameAttribute = ldapProperties.getProperty(attribute);
            String userFilter = usernameAttribute + "=" + value;
            NamingEnumeration<SearchResult> answer = searchLDAP(userFilter);
            if (answer.hasMore()) {
                result = answer.next();
            }
        } catch (Exception e) {
            String msg = "Unknown error searching " + attribute + " = " + value + " in LDAP server: " + e.getMessage();
            log.error(msg, e);
            throw new Exception(msg, e);
        }
        return result;
    }

    /**
     * Convert SearchResult into User instance.
     * 
     * @param ldapUser
     *          List of attributes of user i LDAP server.
     * @return Instance of User.
     * @throws Exception
     *           If error.
     */
    private LDAPUser convertAttributesToLdapUser(Attributes attrib) throws Exception {

        //Attributes attrib = ldapUser.getAttributes();
        //SearchResult ldapUser

        // TODO Imprimir tots els attributs

        String userName = searchResultToUserName(attrib); // ldapUser);

        LDAPUser user = new LDAPUser();
        user.setUserName(userName);

        String givenNameKey = ldapProperties.getProperty(LDAP_NAME_ATTRIBUTE);
        Attribute givenName = attrib.get(givenNameKey);
        if (givenName == null) {
            user.setName(null);
        } else {
            user.setName((String) givenName.get());
        }

        String surname1Key = ldapProperties.getProperty(LDAP_SURNAME1_ATTRIBUTE);
        String surname2Key = ldapProperties.getProperty(LDAP_SURNAME2_ATTRIBUTE);

        if (surname1Key == null || surname2Key == null) {
            //  Els llinatges es troben només a dins una sola clau 
            String surnameKey = ldapProperties.getProperty(LDAP_SURNAMES_ATTRIBUTE);

            Attribute surname = attrib.get(surnameKey);
            if (surname == null) {
                user.setSurnames(null);
            } else {
                user.setSurnames((String) surname.get());
            }
        } else {
            // Està definit el llinatge emprant dos camps: llinatge1 i llinatge2
            Attribute surname1 = attrib.get(surname1Key);
            if (surname1 == null) {
                user.setSurname1(null);
            } else {
                user.setSurname1((String) surname1.get());
            }

            Attribute surname2 = attrib.get(surname2Key);
            if (surname2 == null) {
                user.setSurname2(null);
            } else {
                user.setSurname2((String) surname2.get());
            }
        }

        String memberOfKey = ldapProperties.getProperty(LDAP_MEMBEROF_ATTRIBUTE);
        Attribute memberOfAtt = attrib.get(memberOfKey);
        if (memberOfAtt == null) {
            user.setMemberOf(null);
        } else {
            String prefix = ldapProperties.getProperty(PREFIX_ROLE_MATCH_MEMBEROF);
            if (prefix == null) {
                prefix = "";
            }
            String suffix = ldapProperties.getProperty(SUFFIX_ROLE_MATCH_MEMBEROF);
            if (suffix == null) {
                suffix = "";
            }
            int num = memberOfAtt.size();
            List<String> values = new ArrayList<String>(num);
            List<String> roles = new ArrayList<String>(num);
            for (int i = 0; i < num; i++) {
                String memberOfOk = (String) memberOfAtt.get(i);
                values.add(memberOfOk);
                if (memberOfOk.endsWith(suffix) && memberOfOk.startsWith(prefix)) {
                    String role = memberOfOk.substring(prefix.length(), memberOfOk.length() - suffix.length());
                    roles.add(role);
                }
            }
            user.setMemberOf(values.toArray(new String[values.size()]));
            user.setRoles(roles.toArray(new String[roles.size()]));
        }

        String mailKey = ldapProperties.getProperty(LDAP_EMAIL_ATTRIBUTE);
        Attribute mail = attrib.get(mailKey);
        if (mail == null) {
            user.setEmail(null);
        } else {
            user.setEmail((String) mail.get());
        }

        String departmentKey = ldapProperties.getProperty(LDAP_DEPARTMENT_ATTRIBUTE);
        Attribute department = attrib.get(departmentKey);
        if (department == null) {
            user.setDepartment(null);
        } else {
            user.setDepartment((String) department.get());
        }

        String telephoneKey = ldapProperties.getProperty(LDAP_TELEPHONE_ATTRIBUTE);
        Attribute telephone = attrib.get(telephoneKey);
        if (telephone == null) {
            user.setTelephoneNumber(null);
        } else {
            user.setTelephoneNumber((String) telephone.get());
        }

        String nifKey = ldapProperties.getProperty(LDAP_ADMINISTRATIONID_ATTRIBUTE);
        Attribute nif = attrib.get(nifKey);
        if (nif == null) {
            user.setAdministrationID(null);
        } else {
            user.setAdministrationID((String) nif.get());
        }

        return user;

    }

    /**
     * Obtains username from SearchResult.
     * 
     * @param ldapUser
     *          Instance of SearchResult
     * @return Username
     * @throws Exception
     *           If error.
     */
    private String searchResultToUserName(Attributes attrib) throws Exception {

        // SearchResult ldapUser
        String userNameAttribKey = ldapProperties.getProperty(LDAP_USERNAME_ATTRIBUTE);
        //Attributes attrib = ldapUser.getAttributes();
        Attribute userNameAttrib = attrib.get(userNameAttribKey);
        if (userNameAttrib == null) {
            throw new Exception("Cannot convert 'SearchResult' into 'User' due attribute " + "[" + userNameAttribKey
                    + "] is not in ldap user attributes: [" + attrib.toString() + "]", new Exception()); // ldapUser.toString()
        }
        String userName = (String) userNameAttrib.get();
        if (userName == null) {
            throw new Exception("Cannot convert 'SearchResult' into 'User' due attribute " + "["
                    + LDAP_USERNAME_ATTRIBUTE + "] has value null: [" + attrib.toString() + "]", new Exception()); // ldapUser.toString()
        }
        return userName;
    }

    /**
     * Tells if an user exists in the system.
     * 
     * @param userName
     *          a String with the name of the user to verify.
     * @return True if the user exists. False, otherwise.
     * @throws Exception
     *           in case of error
     */
    public boolean userExists(String userName) throws Exception {
        return (getUserByUsername(userName) != null);
    }

    /**
     * Gets all the user names of the system.
     * 
     * @return an String[] with the users of the system.
     * @throws Exception
     *           in case of error
     */
    public List<String> getAllUserNames() throws Exception {

        NamingEnumeration<SearchResult> enumeration = searchLDAP(null); // Null get all results.
        List<String> list = new ArrayList<String>();
        while (enumeration.hasMore()) {
            SearchResult sr = enumeration.next();
            String userName = searchResultToUserName(sr.getAttributes());
            list.add(userName);
        }
        Collections.sort(list);
        return list;

    }

    public List<LDAPUser> getUsersByRol(String rolName) throws Exception {

        String memberOfKey = ldapProperties.getProperty(LDAP_MEMBEROF_ATTRIBUTE);

        if (memberOfKey == null) {
            throw new Exception(
                    "You need define " + LDAP_MEMBEROF_ATTRIBUTE + " property to execute method getUsersByRol(...)");
        }

        String prefix = ldapProperties.getProperty(PREFIX_ROLE_MATCH_MEMBEROF);
        if (prefix == null) {
            prefix = "";
        }
        String suffix = ldapProperties.getProperty(SUFFIX_ROLE_MATCH_MEMBEROF);
        if (suffix == null) {
            suffix = "";
        }

        // Exemple "memberOf=cn=" + rolName.toUpperCase() + ",dc=caib,dc=es"
        NamingEnumeration<SearchResult> enumeration = searchLDAP(
                memberOfKey + "=" + prefix + rolName.toUpperCase() + suffix);

        List<LDAPUser> users = new ArrayList<LDAPUser>();

        while (enumeration.hasMore()) {
            SearchResult sr = enumeration.next();

            Attributes attrib = sr.getAttributes();

            users.add(convertAttributesToLdapUser(attrib));

        }

        return users;

    }

    /**
     * Gets all the users of the system.
     * 
     * @return an UserData[] with the users of the system.
     * @throws Exception
     *           in case of error
     */
    public LDAPUser[] getUserArray() throws Exception {

        NamingEnumeration<SearchResult> enumeration = searchLDAP(null); // Null get all results.
        List<LDAPUser> list = new ArrayList<LDAPUser>();
        while (enumeration.hasMore()) {
            SearchResult sr = enumeration.next();
            LDAPUser usr = convertAttributesToLdapUser(sr.getAttributes());
            list.add(usr);
        }
        Collections.sort(list);
        return list.toArray(new LDAPUser[0]);
    }

    // ====================================================================
    // ====================================================================
    // ================ L D A P M E T H O D S =========================
    // ====================================================================
    // ====================================================================

    /**
     * @return A copy of all LDAP properties.
     */
    public Properties getLdapProperties() {
        return new Properties(ldapProperties);
    }

    /**
     * Get LDAP property.
     * @param ldapProperty Name of LDAp Property.
     * @return value for detailed LDAP property.
     */
    public String getLdapProperty(String ldapProperty) {
        return ldapProperties.getProperty(ldapProperty);
    }

    /**
     * @return Instance of InitialDirContext for ldap administrator.
     */
    public InitialDirContext getInitialDirContext() throws NamingException {
        return getAdminInitialDirContext();
    }

    /**
     * Create an instance of InitialDirContext for ldap administrator user.
     * @return New instance of InitialDirContext.
     */
    public InitialDirContext getAdminInitialDirContext() throws NamingException {
        String securityPrincipal = ldapProperties.getProperty(LDAP_SECURITYPRINCIPAL);
        String securityCredentials = ldapProperties.getProperty(LDAP_SECURITYCREDENTIALS);

        return getInitialDirContext(securityPrincipal, securityCredentials);
    }

    /**
     * Create an instance of InitialDirContext for <code>securityPrincipal</code> user
     * using <code>securityCredentials</code> as Credentials.
     * @param securityPrincipal User Context
     * @param securityCredentials Credentials used to authenticate user(password).
     * @return New instance of InitialDirContext.
     */
    public InitialDirContext getInitialDirContext(String securityPrincipal, String securityCredentials)
            throws NamingException {
        // Get properties
        String providerURL = ldapProperties.getProperty(LDAP_PROVIDERURL);
        String securityAuthentication;
        securityAuthentication = ldapProperties.getProperty(LDAP_SECURITYAUTHENTICATION);
        // Map of values
        Hashtable<String, String> env = new Hashtable<String, String>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, providerURL);
        env.put(Context.SECURITY_PRINCIPAL, securityPrincipal);
        env.put(Context.SECURITY_CREDENTIALS, securityCredentials);
        env.put(Context.SECURITY_AUTHENTICATION, securityAuthentication);
        // Return Connection
        return new InitialDirContext(env);
    }

    /**
     * Search items.
     * @param customFilter Filter to apply. If null then select all.
     * @return Results of the Search
     * @throws NamingException if an error ocurrs
     */
    public NamingEnumeration<SearchResult> searchLDAP(String customFilter) throws NamingException {
        InitialDirContext ctx = getInitialDirContext();
        String fullFilter;
        String searchFilter = ldapProperties.getProperty(LDAP_SEARCHFILTER);
        if (customFilter != null && customFilter.trim().length() == 0) {
            customFilter = null;
        }
        if (searchFilter != null && searchFilter.trim().length() == 0) {
            searchFilter = null;
        }

        if (searchFilter == null) {
            if (customFilter == null) {
                fullFilter = "(objectclass=*)"; // Select All
            } else {
                fullFilter = customFilter.trim();
            }
        } else {
            if (customFilter == null) {
                fullFilter = searchFilter;
            } else {
                fullFilter = "(&(" + customFilter + ")(" + searchFilter + "))";
            }
        }

        SearchControls sc = new SearchControls();
        String searchScope = ldapProperties.getProperty(LDAP_SEARCHSCOPE);
        if (LDAP_SEARCHSCOPE_ONELEVEL.equals(searchScope)) {
            sc.setSearchScope(SearchControls.ONELEVEL_SCOPE);
        } else {
            if (LDAP_SEARCHSCOPE_SUBTREE.equals(searchScope)) {
                sc.setSearchScope(SearchControls.SUBTREE_SCOPE);
            }
        }
        //
        String additionalAttributesProperty = ldapProperties.getProperty(LDAP_ADDITIONAL_ATTRIBUTES);
        if (additionalAttributesProperty != null && !additionalAttributesProperty.isEmpty()) {
            String[] additionalAttributes = additionalAttributesProperty.split(",");
            sc.setReturningAttributes(additionalAttributes);
        }

        String usersContextDN = ldapProperties.getProperty(LDAP_USERSCONTEXTDN);
        NamingEnumeration<SearchResult> answer;
        answer = ctx.search(usersContextDN, fullFilter, sc);

        return answer;
    }

    /**
     * Authenticate user.
     * @param username User name
     * @param password Password.
     * @return true, if we have authenticate this user and its password.
     *
     */
    public boolean authenticateUser(String username, String password) {
        try {
            String key = ldapProperties.getProperty(LDAP_USERNAME_ATTRIBUTE);
            String filter = key + "=" + username; // key value

            NamingEnumeration<SearchResult> result = searchLDAP(filter);

            if (!result.hasMore()) {
                return false;
            }

            SearchResult sr = result.next();

            String securityPrincipal = sr.getNameInNamespace();
            getInitialDirContext(securityPrincipal, password);

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public List<String> getRolesOfUser(String username) throws Exception {
        LDAPUser ldapUser = getUserByUsername(username);

        if (ldapUser == null) {
            return null;
        }

        List<String> roles = new ArrayList<String>();
        String[] members = ldapUser.getMemberOf();

        if (members != null) {

            String prefix = ldapProperties.getProperty(PREFIX_ROLE_MATCH_MEMBEROF);
            String suffix = ldapProperties.getProperty(SUFFIX_ROLE_MATCH_MEMBEROF);

            if (prefix == null) {
                prefix = "";
            }
            if (suffix == null) {
                suffix = "";
            }

            for (String memberOf : members) {
                try {

                    if (memberOf.startsWith(prefix) && memberOf.endsWith(suffix)) {
                        String role = memberOf.substring(prefix.length(), memberOf.length() - suffix.length());
                        roles.add(role);
                    }
                } catch (Exception e) {
                    e.printStackTrace(System.err);
                }
            }

        } // Final de if

        return roles;
    }

}
