package org.fundaciobit.pluginsib.utils.rest.pomadaptertojboss72;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * This class is used to adapt the pom.xml of the api-client project to JBoss 7.2 or 7.4
 * 
 * Usar des de línia de comandes:  -Djboss=7.2 o -Djboss=7.4
 * 
 */
public class AdaptApiClientPomToJboss72 {

    public static void main(String[] args) {

        System.out.println("\n\n\n");
        System.out.println("========================================");
        System.out.println("AdaptApiClientPomToJboss72");
        if (args == null || args.length == 0) {
            System.err.println("Requerim un argument que sigui la ruta al projecte api interna/externa del servidor");
            System.exit(-1);
        }
        System.out.println("Base Path: " + args[0]);
        try {
            adaptApiClientPomToJboss72(new File(args[0]));
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
        System.out.println("========================================");
        System.out.println("\n\n\n");

    }

    public static void adaptApiClientPomToJboss72(File base) throws Exception {

        // Read file content without libraries
        System.out.println("Base " + base);

        // Llegim el pom.xml del projecte api interna/externa del servidor
        String pom_api = readFile(new File(base, "pom.xml"));

        List<InfoApi> infoApis = getApisInfo(pom_api);

        for (InfoApi info : infoApis) {

            String api = info.getOutput();

            api = api.trim().replace("${project.basedir}", base.getAbsolutePath());

            File f = new File(api);
            System.out.println("\n\n-------------------------------------------------------");
            System.out.println("Processant " + f.getAbsolutePath());
            if (f.exists()) {

                String ignorefilesContent;
                File ignorefiles = new File(f, ".openapi-generator-ignore");
                ignorefilesContent = readFile(ignorefiles);

                if (ignorefilesContent.contains("#_POM.XML_JA_PROCESSAT")) {
                    System.out.println("Ja adaptat: " + f.getAbsolutePath());
                    continue;
                }

                // Fer reemplaços
                File pom = new File(f, "pom.xml");
                String contentPom = readFile(pom);
                String newContentPom = doReplaces(contentPom, info);

                {
                    FileOutputStream fos = new FileOutputStream(pom);
                    fos.write(newContentPom.getBytes());
                    fos.flush();
                    fos.close();
                }

                {
                    FileOutputStream fos = new FileOutputStream(new File(f, "pom_backup.xml"));
                    fos.write(contentPom.getBytes());
                    fos.flush();
                    fos.close();
                }

                // Canviar ignore files
                ignorefilesContent = ignorefilesContent + "\n" + "#_POM.XML_JA_PROCESSAT" + "\n" + "pom.xml" + "\n"
                        + ".gitignore" + "\n";

                {
                    FileOutputStream fos = new FileOutputStream(ignorefiles);
                    fos.write(ignorefilesContent.getBytes());
                    fos.flush();
                    fos.close();
                }

                new File(f, ".gitignore").delete();

                System.out.println("Processat: " + f.getAbsolutePath());

            }

        }

        System.out.println("\n\n-------------------------------------------------------");
        System.out.println("Generant fitxers Open API per Swagger UI ... ");
        StringBuilder swaggerui_urls = new StringBuilder();

        List<String> noms = getStringBetween(pom_api, "<contextId>", "</contextId>");

        int index = 0;
        for (String openapijson : getStringBetween(pom_api, "<outputFileName>", "</outputFileName>")) {

            // openapijson = ${project.basedir}/openapi/openapi_revisors_v1.json

            openapijson = openapijson + ".json";

            if (swaggerui_urls.length() != 0) {
                swaggerui_urls.append(",\n");
            }
            System.out.println("    - " + noms.get(index) + " (" + openapijson + ")");

            swaggerui_urls.append("    {\n" + "        \"url\": \"./" + openapijson + "\",\n" + "        \"name\": \""
                    + noms.get(index) + "\"\n" + "    }\n");

            index++;

        }

        writeFile(new File(base, "openapi/swaggerui_urls.json"), "[\n" + swaggerui_urls.toString() + "]");

    }

    protected static List<InfoApi> getApisInfo(String pom_api) {

        List<InfoApi> apis = new ArrayList<InfoApi>();

        for (String api : getStringBetween(pom_api, "<output>", "</output>")) {

            InfoApi info = new InfoApi();
            info.setOutput(api);

            //System.out.println("Output: " + api);

            int i = pom_api.indexOf(api);

            final String artifactId = "<artifactId>";
            int a_start = pom_api.indexOf(artifactId, i);
            int a_end = pom_api.indexOf("</artifactId>", a_start);

            System.out.println("ArtifactId: |" + pom_api.substring(a_start + artifactId.length(), a_end) + "|");
            info.setArtifactId(pom_api.substring(a_start + artifactId.length(), a_end).trim());

            int ad_start = pom_api.indexOf("<artifactDescription>", a_end);
            int ad_end = pom_api.indexOf("</artifactDescription>", ad_start);

            System.out.println("ArtifactDescription: |"
                    + pom_api.substring(ad_start + "<artifactDescription>".length(), ad_end) + "|\n");
            info.setArtifactDescription(pom_api.substring(ad_start + "<artifactDescription>".length(), ad_end).trim());

            apis.add(info);

        }

        return apis;

    }

    protected static String doReplaces(String content, InfoApi info) {
        for (String[] replace : REPLACES) {

            String replaceWith = replace[1];

            if (replaceWith == null) {
                replaceWith = "<!-- NOU " + replace[0] + " -->";
            }

            content = content.replace(replace[0], replaceWith);
        }

        content = content.replace("<name>" + info.getArtifactId() + "</name>",
                "<name>" + info.getArtifactDescription() + "</name>");

        return content;
    }

    public static List<String> getStringBetween(String content, String start, String end) {

        List<String> trobades = new ArrayList<String>();
        //System.out.println("Cercant : " + start);

        int from = 0;
        while (true) {
            int startIndex = content.indexOf(start, from);
            if (startIndex == -1) {
                //System.out.println("Ja no trobam més start: " + start);
                break;
            }
            int endIndex = content.indexOf(end, startIndex);
            if (endIndex == -1) {
                //System.out.println("Ja no trobam més end: " + start);
                break;
            }

            trobades.add(content.substring(startIndex + start.length(), endIndex));
            from = endIndex + end.length();
        }

        return trobades;
    }

    protected static String readFile(File file) throws FileNotFoundException {
        Scanner sc = new Scanner(file);
        // we just need to use \\Z as delimiter
        sc.useDelimiter("\\Z");
        String content = sc.next();
        sc.close();
        content = content.replace("\n", "\n");
        return content;
    }

    /** Write a text to a file */
    protected static void writeFile(File file, String content) throws Exception {
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(content.getBytes());
        fos.flush();
        fos.close();
    }

    public static String[][] REPLACES = { { "<project xmlns=\"http://maven.apache.org/POM/4.0.0\"",

            "<!--                                                                     -->\n"
                    + "<!--                                                                     -->\n"
                    + "<!--             IMPORTANT: NO MODIFICAR AQUEST FITXER!!!!!!!            -->\n"
                    + "<!--             ============================================            -->\n"
                    + "<!--  Aquest fitxer s'ha generat emprant el plugin maven de OpenApiTools -->\n"
                    + "<!--  org.openapitools::openapi-generator-maven-plugin i modificat per   -->\n" + "<!--  "
                    + AdaptApiClientPomToJboss72.class.getName() + " -->\n"
                    + "<!--  de PluginsIB-Utils-Rest. NO modificar-ho a ma !!!!!                -->\n"
                    + "<!--                                                                     -->\n"
                    + "<project xmlns=\"http://maven.apache.org/POM/4.0.0\"" },
            // =====================================================
            { "</developers>", "</developers>\n" + "\n" + "\n" + "    <!-- NOU -->\n" + "    <repositories>\n"
            /*
                    + "        <repository>\n" + "            <id>jboss-releases</id>\n"
                    + "            <name>JBoss Releases Repository</name>\n" + "            <url>\n"
                    + "                https://repository.jboss.org/nexus/content/repositories/releases/</url>\n"
                    + "            <releases>\n" + "                <updatePolicy>never</updatePolicy>\n"
                    + "            </releases>\n" + "            <snapshots>\n"
                    + "                <updatePolicy>never</updatePolicy>\n" + "            </snapshots>\n"
                    + "        </repository>\n" + "        <repository>\n" + "            <id>redhat-repository</id>\n"
                    + "            <name>Redhat Repository</name>\n"
                    + "            <url>https://maven.repository.redhat.com/ga/</url>\n" + "        </repository>\n"
                    + "    </repositories>\n" + "\n" + "\n" + "    <!-- NOU -->\n" + "    <dependencyManagement>\n"
                    + "        <dependencies>\n" + "            <dependency>\n"
                    + "                <groupId>org.jboss.bom</groupId>\n"
                    + "                <artifactId>eap-runtime-artifacts</artifactId>\n"
                    + "                <version>${jboss-eap.version}</version>\n" + "                <type>pom</type>\n"
                    + "                <scope>import</scope>\n" + "            </dependency>\n"
                    + "            <dependency>\n" + "                <groupId>org.jboss.bom</groupId>\n"
                    + "                <artifactId>jboss-eap-javaee8-with-tools</artifactId>\n"
                    + "                <version>${jboss-eap.version}</version>\n" + "                <type>pom</type>\n"
                    + "                <scope>import</scope>\n" + "            </dependency>\n"*/
                    + "        <repository>\n" + "            <id>github-governib-maven-repos</id>\n"
                    + "            <name>GitHub GovernIB Maven Repository</name>\n"
                    + "            <url>https://governib.github.io/maven/maven/</url>\n" + "            <snapshots>\n"
                    + "                <enabled>true</enabled>\n"
                    + "                <updatePolicy>daily</updatePolicy>\n" + "            </snapshots>\n"
                    + "            <releases>\n" + "                <enabled>true</enabled>\n"
                    + "                <updatePolicy>never</updatePolicy>\n" + "            </releases>\n"
                    + "        </repository>\n" + "    </repositories>\n" },
            //+ "        </dependencies>\n" + "    </dependencyManagement>" },
            // =====================================================        
            { "<plugins>\n", "<plugins>\n" + "            <!-- NOU -->\n" + "            <!--" },
            // =====================================================
            { "            <plugin>\n" + "                <groupId>org.apache.maven.plugins</groupId>\n"
                    + "                <artifactId>maven-surefire-plugin</artifactId>",
                    "-->\n" + "\n" + "            <!-- NOU -->\n" + "            <plugin>\n"
                            + "                <groupId>org.apache.maven.plugins</groupId>\n"
                            + "                <artifactId>maven-enforcer-plugin</artifactId>\n"
                            //+ "                <version>3.0.0-M3</version>\n"
                            + "                <configuration>\n" + "                    <rules>\n"
                            + "                        <requireMavenVersion>\n"
                            + "                            <version>3.6.1</version>\n"
                            + "                        </requireMavenVersion>\n"
                            + "                        <requireJavaVersion>\n"
                            + "                            <version>11</version>\n"
                            + "                        </requireJavaVersion>\n"
                            + "                        <banDuplicatePomDependencyVersions />\n"
                            + "                        <dependencyConvergence />\n" + "                    </rules>\n"
                            + "                </configuration>\n" + "                <executions>\n"
                            + "                    <execution>\n" + "                        <goals>\n"
                            + "                            <goal>enforce</goal>\n"
                            + "                        </goals>\n" + "                    </execution>\n"
                            + "                </executions>\n" + "            </plugin>\n" + "\n" + "\n"
                            + "            <plugin>\n" + "                <groupId>org.apache.maven.plugins</groupId>\n"
                            + "                <artifactId>maven-surefire-plugin</artifactId>" },
            // =====================================================
            { "<source>1.8</source>", "" }, { "<target>1.8</target>", "" },

            // ====================================================
            { "            <plugin>\n" + "                <groupId>org.apache.maven.plugins</groupId>\n"
                    + "                <artifactId>maven-javadoc-plugin</artifactId>\n"
                    + "                <version>3.3.2</version>\n" + "                <configuration>\n"
                    + "                    <doclint>none</doclint>\n" + "                    \n"
                    + "                </configuration>\n" + "                <executions>\n"
                    + "                    <execution>\n" + "                        <id>attach-javadocs</id>\n"
                    + "                        <goals>\n" + "                            <goal>jar</goal>\n"
                    + "                        </goals>\n" + "                    </execution>\n"
                    + "                </executions>\n" + "            </plugin>", "" },
            // =====================================================
            { "<project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>",
                    "<!-- NOU <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding> -->" },
            // =====================================================
            { "<resteasy-version>4.7.6.Final</resteasy-version>",
                    "<!-- NOU <resteasy-version>4.7.6.Final</resteasy-version> -->\n" },
            { "<jackson-version>2.15.2</jackson-version>", "<!-- NOU <jackson-version>2.15.2</jackson-version>-->" },
            { "<jackson-version>2.17.1</jackson-version>", "<!-- NOU <jackson-version>2.17.1</jackson-version>-->" },
            { "<jackson-databind-version>2.17.1</jackson-databind-version>",
                    "<!-- NOU <jackson-databind-version>2.17.1</jackson-databind-version>-->" },
            { "            <artifactId>junit</artifactId>\n" + "            <junit-version>4.13</junit-version>",
                    "            <artifactId>junit</artifactId>\n"
                            + "            <!-- NOU <junit-version>4.13</junit-version>-->" },
            // =====================================================
            { "<version>${jackson-version}</version>", "<!-- NOU <version>${jackson-version}</version> -->" },
            { "<version>${resteasy-version}</version>", "<!-- NOU <version>${resteasy-version}</version> -->" },
            { "<version>${jackson-databind-version}</version>", "<!-- NOU ${jackson-databind-version} -->" },
            //{ "<version>3.0.2</version>", "<version>1.3.9</version><!-- NOU  -->" },
            //{ "<goal>test-jar</goal>", "<!-- NOU <goal>test-jar</goal> -->" },
            // =====================================================
            { "Unlicense", "European Union Public Licence (EUPL v1.2)" },
            // =====================================================
            { "            <plugin>\n" + "                <artifactId>maven-dependency-plugin</artifactId>\n"
                    + "                <executions>\n" + "                    <execution>\n"
                    + "                        <phase>package</phase>\n" + "                        <goals>\n"
                    + "                            <goal>copy-dependencies</goal>\n"
                    + "                        </goals>\n" + "                        <configuration>\n"
                    + "                            <outputDirectory>${project.build.directory}/lib</outputDirectory>\n"
                    + "                        </configuration>\n" + "                    </execution>\n"
                    + "                </executions>\n" + "            </plugin>", "" },
            // ====================================================
            { "                    <execution>\n" + "                        <id>add_test_sources</id>\n"
                    + "                        <phase>generate-test-sources</phase>\n"
                    + "                        <goals>\n" + "                            <goal>add-test-source</goal>\n"
                    + "                        </goals>\n" + "                        <configuration>\n"
                    + "                            <sources>\n"
                    + "                                <source>src/test/java</source>\n"
                    + "                            </sources>\n" + "                        </configuration>\n"
                    + "                    </execution>", "" },
            // =====================================================
            { "    </build>\n" + "    <dependencies>",
                    "    </build>\n" + "    <dependencies>\n" + "\n" + "        <!-- NOU -->\n"
                            + "        <dependency>\n" + "            <groupId>org.jboss.logging</groupId>\n"
                            + "            <artifactId>commons-logging-jboss-logging</artifactId>\n"
                            + "        </dependency>\n"
                            /*
                            + "        <!-- NOU -->\n" + "        <dependency>\n"
                            + "            <groupId>com.sun.mail</groupId>\n"
                            + "            <artifactId>javax.mail</artifactId>\n" + "        </dependency>" */
                            },
                            
            // =====================================================
            { "        </plugins>", "           <!-- NOU -->\n" + "            <plugin>\n"
                    + "                <artifactId>maven-deploy-plugin</artifactId>\n"
                    //+ "                <version>2.8.2</version>\n"
                    + "                <configuration>\n" + "                    <altDeploymentRepository>\n"
                    + "                        internal.repo::default::file://${project.build.directory}/mvn-repo</altDeploymentRepository>\n"
                    + "                </configuration>\n" + "            </plugin>\n" + "            <!-- NOU -->\n"
                    + "            <plugin>\n" + "                <groupId>com.github.github</groupId>\n"
                    + "                <artifactId>site-maven-plugin</artifactId>\n"
                    + "                <version>0.12</version>\n" + "                <configuration>\n"
                    + "                    <!-- git commit message -->\n"
                    + "                    <message>Maven artifacts for ${project.version}</message>\n"
                    + "                    <outputDirectory>${project.build.directory}/mvn-repo</outputDirectory>\n"
                    + "                    <noJekyll>true</noJekyll>\n"
                    + "                    <!-- remote branch name -->\n"
                    + "                    <branch>refs/heads/gh-pages</branch>\n" + "                    <includes>\n"
                    + "                        <include>**/*</include>\n" + "                    </includes>\n"
                    + "                    <path>maven</path>\n" + "                    <!-- github repo name -->\n"
                    + "                    <repositoryName>maven</repositoryName>\n"
                    + "                    <!-- github username or organization  -->\n"
                    + "                    <repositoryOwner>GovernIB</repositoryOwner>\n"
                    + "                    <server>github_governib_maven</server>\n"
                    + "                    <merge>true</merge>\n" + "                    <dryRun>false</dryRun>\n"
                    + "                </configuration>\n" + "                <executions>\n"
                    + "                    <execution>\n" + "                        <goals>\n"
                    + "                            <goal>site</goal>\n" + "                        </goals>\n"
                    + "                        <phase>deploy</phase>\n" + "                    </execution>\n"
                    + "                </executions>\n" + "            </plugin>\n" + "        </plugins>" },
            // =====================================================
            { "    <scm>\n"
                    + "        <connection>scm:git:git@github.com:openapitools/openapi-generator.git</connection>\n"
                    + "        <developerConnection>scm:git:git@github.com:openapitools/openapi-generator.git</developerConnection>\n"
                    + "        <url>https://github.com/openapitools/openapi-generator</url>\n" + "    </scm>",
                    "    <!-- NOU\n" + "    <scm>\n"
                            + "        <connection>scm:git:git@github.com:openapitools/openapi-generator.git</connection>\n"
                            + "        <developerConnection>scm:git:git@github.com:openapitools/openapi-generator.git</developerConnection>\n"
                            + "        <url>https://github.com/openapitools/openapi-generator</url>\n" + "    </scm>\n"
                            + "    -->\n" + "    \n" + "    <!-- NOU -->\n" + "    <parent>\n"
                            + "        <artifactId>caib-artifacts-jdk11-jboss72or74-with-github-governib-distribution</artifactId>\n"
                            + "        <groupId>es.caib.maven</groupId>\n"
                            + "        <version>1.0.1-SNAPSHOT</version>\n" + "        <relativePath></relativePath>\n"
                            + "    </parent>" },
            // =====================================================
            { "            <plugin>\n" + "                <groupId>org.apache.maven.plugins</groupId>\n"
                    + "                <artifactId>maven-compiler-plugin</artifactId>\n"
                    + "                <version>2.5.1</version>\n" + "                <configuration>\n"
                    + "                   \n" + "                   \n" + "                </configuration>\n"
                    + "            </plugin>",
                    "            <!-- NOU -->    \n" + "            <!--\n" + "            <plugin>\n"
                            + "                <groupId>org.apache.maven.plugins</groupId>\n"
                            + "                <artifactId>maven-compiler-plugin</artifactId>\n"
                            + "                <version>2.5.1</version>\n" + "                <configuration>\n"
                            + "                </configuration>\n" + "            </plugin>\n" + "            -->" },
            // =====================================================            
            { "            <plugin>\n" + "                <groupId>org.apache.maven.plugins</groupId>\n"
                    + "                <artifactId>maven-surefire-plugin</artifactId>\n"
                    + "                <version>2.12</version>\n" + "                <configuration>\n"
                    + "                    <systemProperties>\n" + "                        <property>\n"
                    + "                            <name>loggerPath</name>\n"
                    + "                            <value>conf/log4j.properties</value>\n"
                    + "                        </property>\n" + "                    </systemProperties>\n"
                    + "                    <argLine>-Xms512m -Xmx1500m</argLine>\n"
                    + "                    <parallel>methods</parallel>\n"
                    + "                    <forkMode>pertest</forkMode>\n" + "                </configuration>\n"
                    + "            </plugin>",
                    "            <plugin>\n" + "                <groupId>org.apache.maven.plugins</groupId>\n"
                            + "                <artifactId>maven-surefire-plugin</artifactId>\n"
                            + "                <!-- NOU <version>2.12</version> -->\n"
                            + "                <configuration>\n" + "                    <systemProperties>\n"
                            + "                        <property>\n"
                            + "                            <name>loggerPath</name>\n"
                            + "                            <value>conf/log4j.properties</value>\n"
                            + "                        </property>\n" + "                    </systemProperties>\n"
                            + "                    <argLine>-Xms512m -Xmx1500m</argLine>\n"
                            + "                    <!-- NOU <parallel>methods</parallel> -->\n"
                            + "                    <!-- NOU <forkMode>pertest</forkMode> -->\n"
                            + "                </configuration>\n" + "            </plugin>" },
            // =====================================================
            { "                <artifactId>maven-surefire-plugin</artifactId>\n"
                    + "                <version>2.22.2</version>",
                    "                <artifactId>maven-surefire-plugin</artifactId>\n"
                            + "                <!-- NOU <version>2.22.2</version> -->" },
            // =====================================================   
            { "            <plugin>\n" + "                <groupId>org.apache.maven.plugins</groupId>\n"
                    + "                <artifactId>maven-jar-plugin</artifactId>\n"
                    + "                <version>2.6</version>\n" + "                <executions>\n"
                    + "                    <execution>\n" + "                        <goals>\n"
                    + "                            <goal>jar</goal>\n"
                    + "                            <goal>test-jar</goal>\n" + "                        </goals>\n"
                    + "                    </execution>\n" + "                </executions>\n"
                    + "                <configuration>\n" + "                </configuration>\n"
                    + "            </plugin>",
                    "            <!-- NOU -->\n" + "            <!--\n" + "            <plugin>\n"
                            + "                <groupId>org.apache.maven.plugins</groupId>\n"
                            + "                <artifactId>maven-jar-plugin</artifactId>\n"
                            + "                <version>2.6</version>\n" + "                <executions>\n"
                            + "                    <execution>\n" + "                        <goals>\n"
                            + "                            <goal>jar</goal>\n"
                            + "                            <goal>test-jar</goal>\n"
                            + "                        </goals>\n" + "                    </execution>\n"
                            + "                </executions>\n" + "                <configuration>\n"
                            + "                </configuration>\n" + "            </plugin>\n" + "            -->" },

            // =====================================================   

            { "        <dependency>\n" + "            <groupId>com.github.joschi.jackson</groupId>\n"
                    + "            <artifactId>jackson-datatype-threetenbp</artifactId>\n"
                    + "            <version>${threetenbp-version}</version>\n" + "        </dependency>\n"
                    + "        <dependency>\n" + "            <groupId>jakarta.annotation</groupId>\n"
                    + "            <artifactId>jakarta.annotation-api</artifactId>\n"
                    + "            <version>${jakarta-annotation-version}</version>\n"
                    + "            <scope>provided</scope>\n" + "        </dependency>",
                    "        <!-- NOU -->\n" + "        <!--\n" + "        <dependency>\n"
                            + "            <groupId>com.github.joschi.jackson</groupId>\n"
                            + "            <artifactId>jackson-datatype-threetenbp</artifactId>\n"
                            + "            <version>${threetenbp-version}</version>\n" + "        </dependency>\n"
                            + "        <dependency>\n" + "            <groupId>jakarta.annotation</groupId>\n"
                            + "            <artifactId>jakarta.annotation-api</artifactId>\n"
                            + "            <version>${jakarta-annotation-version}</version>\n"
                            + "            <scope>provided</scope>\n" + "        </dependency>\n" + "        -->" },

            // =====================================================   
            { "<jakarta-annotation-version>1.3.5</jakarta-annotation-version>", null },
            //"<!-- NOU " + "<jakarta-annotation-version>1.3.5</jakarta-annotation-version>" + " -->" },
            // =====================================================
            { "<threetenbp-version>2.9.10</threetenbp-version>", null },
            // "<!-- NOU " + "<threetenbp-version>2.9.10</threetenbp-version>" + " -->" },
            // =====================================================
            { "<maven-plugin-version>1.0.0</maven-plugin-version>", null },
            //"<!-- NOU " + "<maven-plugin-version>1.0.0</maven-plugin-version>" + " -->" },

            // =====================================================     

            { "<dependency>\n" + "            <groupId>com.google.code.findbugs</groupId>\n"
                    + "            <artifactId>jsr305</artifactId>\n" + "            <version>3.0.2</version>\n"
                    + "        </dependency>", null },
            
            // =====================================================     

            { "    </properties>\n", "    </properties>\n"
                    + "    <profiles>\n"
                    + "        <!-- Profile jboss72: actiu per defecte -->\n"
                    + "        <profile>\n"
                    + "            <id>jboss72_def</id>\n"
                    + "            <activation>\n"
                    + "                <property>\n"
                    + "                    <name>!jboss</name>\n"
                    + "                </property>\n"
                    + "            </activation>\n"
                    + "            <dependencies>\n"
                    + "                <!-- Dependències específiques per a JBoss 7.2 -->\n"
                    + "                <dependency>\n"
                    + "                    <groupId>com.sun.mail</groupId>\n"
                    + "                    <artifactId>javax.mail</artifactId>\n"
                    + "                </dependency>\n"
                    + "            </dependencies>\n"
                    + "        </profile>\n"
                    + "        <!-- Profile jboss72: actiu per propietat -->\n"
                    + "        <profile>\n"
                    + "            <id>jboss72_explicit</id>\n"
                    + "            <activation>\n"
                    + "                <property>\n"
                    + "                    <name>jboss</name>\n"
                    + "                    <value>7.2</value>\n"
                    + "                </property>\n"
                    + "            </activation>\n"
                    + "            <dependencies>\n"
                    + "                <!-- Dependències específiques per a JBoss 7.2 -->\n"
                    + "                <dependency>\n"
                    + "                    <groupId>com.sun.mail</groupId>\n"
                    + "                    <artifactId>javax.mail</artifactId>\n"
                    + "                </dependency>\n"
                    + "            </dependencies>\n"
                    + "        </profile>\n"
                    + "        <!-- Profile jboss74: actiu per propietat -->\n"
                    + "        <profile>\n"
                    + "            <id>jboss74_explicit</id>\n"
                    + "            <activation>\n"
                    + "                <property>\n"
                    + "                    <name>jboss</name>\n"
                    + "                    <value>7.4</value>\n"
                    + "                </property>\n"
                    + "            </activation>\n"
                    + "            <dependencies>\n"
                    + "                <!-- Dependències específiques per a JBoss 7.4 -->\n"
                    + "                <dependency>\n"
                    + "                    <groupId>com.google.code.findbugs</groupId>\n"
                    + "                    <artifactId>jsr305</artifactId>\n"
                    + "                    <version>3.0.2</version>\n"
                    + "                </dependency>\n"
                    + "            </dependencies>\n"
                    + "        </profile>\n"
                    + "    </profiles>\n" }

    };

    /**
     * Info API
     * @author anadal
     * 12 sept 2024 9:27:04
     */
    public static class InfoApi {
        protected String output;
        protected String artifactId;
        protected String artifactDescription;

        public String getOutput() {
            return output;
        }

        public void setOutput(String output) {
            this.output = output;
        }

        public String getArtifactId() {
            return artifactId;
        }

        public void setArtifactId(String artifactId) {
            this.artifactId = artifactId;
        }

        public String getArtifactDescription() {
            return artifactDescription;
        }

        public void setArtifactDescription(String artifactDescription) {
            this.artifactDescription = artifactDescription;
        }

    }

}
