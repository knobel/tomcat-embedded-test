/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.knobel.test.tomcat.test01;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashSet;
import java.util.Set;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import org.apache.catalina.Container;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.Service;
import org.apache.catalina.loader.WebappLoader;
//import org.apache.catalina.core.StandardContext;
//import org.apache.catalina.WebResourceRoot.ResourceSetType;
import org.apache.catalina.startup.Tomcat;
import org.apache.naming.resources.VirtualDirContext;
import org.apache.tomcat.JarScannerCallback;
import org.apache.tomcat.util.scan.StandardJarScanner;

/**
 *
 * @author michalknobel
 */
public class TomcatTest {

    public static void main(String[] args) throws ServletException, LifecycleException, MalformedURLException {
//        String webappDirLocation = "/Users/michalknobel/NetBeansProjects/servlet-test01/src/main/webapp";
        String warFile = "/Users/michalknobel/NetBeansProjects/servlet-test01/target/servlet-test01-1.0-SNAPSHOT.war";
        String libFolder = "/Users/michalknobel/";
        String guavaFile = libFolder + "guava-18.0.jar";
        Tomcat tomcat = new Tomcat();

        //The port that we should run on can be set into an environment variable
        //Look for that variable and default to 8080 if it isn't there.
        String webPort = System.getenv("PORT");
        if (webPort == null || webPort.isEmpty()) {
            webPort = "8122";
        }

        tomcat.setPort(Integer.valueOf(webPort));
        tomcat.setBaseDir("/tmp/tomcat");

//        tomcat.addWebapp("/", new File(webappDirLocation).getAbsolutePath());
        Context ctx = tomcat.addWebapp("/test", new File(warFile).getAbsolutePath());
        WebappLoader webappLoader = new WebappLoader(ctx.getParentClassLoader());
        webappLoader.addRepository("file:///" +guavaFile);
        ctx.setLoader(webappLoader);
       
//        tomcat.getService().getContainer().getLoader().addRepository(libFolder);
//        ctx.addResourceJarUrl(new URL("file:///" + guavaFile));

//        File additionWebInfClasses = new File(guavaFile);
//        VirtualDirContext resources = new VirtualDirContext();
//        resources.setExtraResourcePaths("/WEB-INF/lib=" + additionWebInfClasses);
//        ctx.setResources(resources);
//        List<URL> repo = new ArrayList<URL>();
//        repo.add(new URL("file:///" + libFolder));
//        URL[] reporArr = new URL[repo.size()];
//        reporArr = repo.toArray(reporArr);
//        tomcat.getService().getContainer().setLoader(new WebappLoader());
//        tomcat.getService().getContainer().get.addRepository("file:///" + libFolder);
        tomcat.start();
        tomcat.getServer().await();
    }

   
    }

 class TomcatWithFastJarScanner extends Tomcat {

        @Override
        public void start() throws LifecycleException {
            Service[] findServices = getServer().findServices();
            for (Service s : findServices) {
                Container[] findChildren = s.getContainer().findChildren();
                for (Container c : findChildren) {
                    ((Context) c).setJarScanner(new FastJarScanner());
                }
            }
            super.start();
        }
        }

    class FastJarScanner extends StandardJarScanner {

        public void scan(ServletContext context, ClassLoader classloader,
                JarScannerCallback callback, Set<String> jarsToSkip) {
            jarsToSkip = new HashSet<String>();

            URL[] urLs = ((URLClassLoader) classloader.getParent()).getURLs();
            for (URL u : urLs) {
                System.out.println("URL: " + u.toString());
            }
//        .each {
//            def jar = it.path.find(/[^\/]+\.jar$/)
//            if(!jar) return
//            for(String inclusionPattern : jarsToInclude) {
//                if(jar.find(inclusionPattern))  
//                    println "including jar: " + jar
//                else jarsToSkip.add(jar)
//            }
//        }

            super.scan(context, classloader, callback, jarsToSkip);
        }
    }

