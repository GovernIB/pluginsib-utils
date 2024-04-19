package org.fundaciobit.pluginsib.utils.rest.adaptpom;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * This class is used to adapt the pom.xml of the api-client project to JBoss 7.2
 */
public class AdaptApiClientPomToJboss72 {

    public static void main(String[] args) {

        System.out.println("\n\n\n");
        System.out.println("========================================");
        System.out.println("AdaptApiClientPomToJboss72");
        if (args == null || args.length == 0) {
            System.err.println("Requerim un argument que sigui la part del servidor de l'api (interna o externa)");
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

        String pom_api = readFile(new File(base, "pom.xml"));

        for (String api : getStringBetween(pom_api, "<output>", "</output>")) {

            api = api.replace("${project.basedir}", base.getAbsolutePath());

            File f = new File(api);
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
                String newContentPom = doReplaces(contentPom);

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

    }
    
    
    protected static String doReplaces(String content) {
        for (String[] replace : REPLACES) {
            content = content.replace(replace[0], replace[1]);
        }
        return content;
    }
    
    

    public static List<String> getStringBetween(String content, String start, String end) {

        List<String> trobades = new ArrayList<String>();

        int from = 0;
        while (true) {
            int startIndex = content.indexOf(start, from);
            if (startIndex == -1) {
                break;
            }
            int endIndex = content.indexOf(end, startIndex);
            if (endIndex == -1) {
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
    
    
    public static String[][] REPLACES = { 
            {"</developers>", "    </developers>\n"
            + "\n"
            + "\n"
            + "    <!-- NOU -->\n"
            + "    <repositories>\n"
            + "        <repository>\n"
            + "            <id>jboss-releases</id>\n"
            + "            <name>JBoss Releases Repository</name>\n"
            + "            <url>\n"
            + "                https://repository.jboss.org/nexus/content/repositories/releases/</url>\n"
            + "            <releases>\n"
            + "                <updatePolicy>never</updatePolicy>\n"
            + "            </releases>\n"
            + "            <snapshots>\n"
            + "                <updatePolicy>never</updatePolicy>\n"
            + "            </snapshots>\n"
            + "        </repository>\n"
            + "        <repository>\n"
            + "            <id>redhat-repository</id>\n"
            + "            <name>Redhat Repository</name>\n"
            + "            <url>https://maven.repository.redhat.com/ga/</url>\n"
            + "        </repository>\n"
            + "    </repositories>\n"
            + "\n"
            + "\n"
            + "    <!-- NOU -->\n"
            + "    <dependencyManagement>\n"
            + "        <dependencies>\n"
            + "            <dependency>\n"
            + "                <groupId>org.jboss.bom</groupId>\n"
            + "                <artifactId>eap-runtime-artifacts</artifactId>\n"
            + "                <version>${jboss-eap.version}</version>\n"
            + "                <type>pom</type>\n"
            + "                <scope>import</scope>\n"
            + "            </dependency>\n"
            + "            <dependency>\n"
            + "                <groupId>org.jboss.bom</groupId>\n"
            + "                <artifactId>jboss-eap-javaee8-with-tools</artifactId>\n"
            + "                <version>${jboss-eap.version}</version>\n"
            + "                <type>pom</type>\n"
            + "                <scope>import</scope>\n"
            + "            </dependency>\n"
            + "        </dependencies>\n"
            + "    </dependencyManagement>"},
           // =====================================================        
            { "<plugins>\n", "<plugins>\n"
                    + "            <!-- NOU -->\n"
                    + "            <!--" },
            // =====================================================
            { "            <plugin>\n"
                    + "                <groupId>org.apache.maven.plugins</groupId>\n"
                    + "                <artifactId>maven-surefire-plugin</artifactId>", "-->\n"
                            + "\n"
                            + "            <!-- NOU -->\n"
                            + "            <plugin>\n"
                            + "                <groupId>org.apache.maven.plugins</groupId>\n"
                            + "                <artifactId>maven-enforcer-plugin</artifactId>\n"
                            + "                <version>3.0.0-M3</version>\n"
                            + "                <configuration>\n"
                            + "                    <rules>\n"
                            + "                        <requireMavenVersion>\n"
                            + "                            <version>3.6.1</version>\n"
                            + "                        </requireMavenVersion>\n"
                            + "                        <requireJavaVersion>\n"
                            + "                            <version>11</version>\n"
                            + "                        </requireJavaVersion>\n"
                            + "                        <banDuplicatePomDependencyVersions />\n"
                            + "                        <dependencyConvergence />\n"
                            + "                    </rules>\n"
                            + "                </configuration>\n"
                            + "                <executions>\n"
                            + "                    <execution>\n"
                            + "                        <goals>\n"
                            + "                            <goal>enforce</goal>\n"
                            + "                        </goals>\n"
                            + "                    </execution>\n"
                            + "                </executions>\n"
                            + "            </plugin>\n"
                            + "\n"
                            + "\n"
                            + "            <plugin>\n"
                            + "                <groupId>org.apache.maven.plugins</groupId>\n"
                            + "                <artifactId>maven-surefire-plugin</artifactId>" },
            // =====================================================
            { "<source>1.8</source>", "" },
            { "<target>1.8</target>", "" },
            // =====================================================
            { "    <properties>\n", "    <properties>\n"
                    + "        <!-- NOU -->\n"
                    + "        <jboss-eap.version>7.2.6.GA</jboss-eap.version>\n"
                    + "        <maven.compiler.release>11</maven.compiler.release>\n"
                    + "        <maven.compiler.target>11</maven.compiler.target>\n"
                    + "        <maven.compiler.source>11</maven.compiler.source>" },
            // =====================================================
            { "<resteasy-version>4.7.6.Final</resteasy-version>", "<!-- NOU <resteasy-version>4.7.6.Final</resteasy-version>-->\n"
                    },
            { "<jackson-version>2.15.2</jackson-version>",  "<!-- NOU <jackson-version>2.15.2</jackson-version>-->"},
            { "<junit-version>4.13</junit-version>", "<!-- NOU <junit-version>4.13</junit-version>-->" },
            // =====================================================
            { "<version>${junit-version}</version>", "<!-- NOU  <version>${junit-version}</version> -->" },
            { "<version>${jackson-version}</version>", "<!-- NOU <version>${jackson-version}</version> -->" },
            { "<version>${resteasy-version}</version>", "<!-- NOU <version>${resteasy-version}</version> -->" },
            { "<version>${jackson-databind-version}</version>", "<!-- NOU ${jackson-databind-version} -->" },
            { "<version>3.0.2</version>", "<version>1.3.9</version><!-- NOU  -->" },
            // =====================================================
            { "    </build>\n"
                    + "    <dependencies>", "    </build>\n"
                            + "    <dependencies>\n"
                    + "\n"
                    + "        <!-- NOU -->\n"
                    + "        <dependency>\n"
                    + "            <groupId>commons-logging</groupId>\n"
                    + "            <artifactId>commons-logging</artifactId>\n"
                    + "            <version>1.2</version>\n"
                    + "            <scope>test</scope>\n"
                    + "        </dependency>\n"
                    + "\n"
                    + "        <!-- NOU -->\n"
                    + "        <dependency>\n"
                    + "            <groupId>javax.mail</groupId>\n"
                    + "            <artifactId>mail</artifactId>\n"
                    + "            <version>1.4.3</version>\n"
                    + "            <scope>test</scope>\n"
                    + "        </dependency>" },
            // =====================================================
            { "        </plugins>", "           <!-- NOU -->\n"
                    + "            <plugin>\n"
                    + "                <artifactId>maven-deploy-plugin</artifactId>\n"
                    + "                <version>2.8.2</version>\n"
                    + "                <configuration>\n"
                    + "                    <altDeploymentRepository>\n"
                    + "                        internal.repo::default::file://${project.build.directory}/mvn-repo</altDeploymentRepository>\n"
                    + "                </configuration>\n"
                    + "            </plugin>\n"
                    + "            <!-- NOU -->\n"
                    + "            <plugin>\n"
                    + "                <groupId>com.github.github</groupId>\n"
                    + "                <artifactId>site-maven-plugin</artifactId>\n"
                    + "                <version>0.12</version>\n"
                    + "                <configuration>\n"
                    + "                    <!-- git commit message -->\n"
                    + "                    <message>Maven artifacts for ${project.version}</message>\n"
                    + "                    <outputDirectory>${project.build.directory}/mvn-repo</outputDirectory>\n"
                    + "                    <noJekyll>true</noJekyll>\n"
                    + "                    <!-- remote branch name -->\n"
                    + "                    <branch>refs/heads/gh-pages</branch>\n"
                    + "                    <includes>\n"
                    + "                        <include>**/*</include>\n"
                    + "                    </includes>\n"
                    + "                    <path>maven</path>\n"
                    + "                    <!-- github repo name -->\n"
                    + "                    <repositoryName>maven</repositoryName>\n"
                    + "                    <!-- github username or organization  -->\n"
                    + "                    <repositoryOwner>GovernIB</repositoryOwner>\n"
                    + "                    <server>github_governib_maven</server>\n"
                    + "                    <merge>true</merge>\n"
                    + "                    <dryRun>false</dryRun>\n"
                    + "                </configuration>\n"
                    + "                <executions>\n"
                    + "                    <execution>\n"
                    + "                        <goals>\n"
                    + "                            <goal>site</goal>\n"
                    + "                        </goals>\n"
                    + "                        <phase>deploy</phase>\n"
                    + "                    </execution>\n"
                    + "                </executions>\n"
                    + "            </plugin>\n"
                    + "        </plugins>" },
            
                
            
            
    };
    

}
