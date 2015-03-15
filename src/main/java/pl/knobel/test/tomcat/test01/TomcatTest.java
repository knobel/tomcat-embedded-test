/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.knobel.test.tomcat.test01;

import java.io.File;
import java.net.MalformedURLException;
import javax.servlet.ServletException;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.loader.WebappLoader;
//import org.apache.catalina.core.StandardContext;
//import org.apache.catalina.WebResourceRoot.ResourceSetType;
import org.apache.catalina.startup.Tomcat;

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
        String stringUtils = libFolder + "commons-lang3-3.3.2.jar";
        Tomcat tomcat = new Tomcat();

        //The port that we should run on can be set into an environment variable
        //Look for that variable and default to 8080 if it isn't there.
        String webPort = System.getenv("PORT");
        if (webPort == null || webPort.isEmpty()) {
            webPort = "8133";
        }

        tomcat.setPort(Integer.valueOf(webPort));
        tomcat.setBaseDir("/tmp/tomcat");

//        tomcat.addWebapp("/", new File(webappDirLocation).getAbsolutePath());
        Context ctx = tomcat.addWebapp("/test", new File(warFile).getAbsolutePath());
        WebappLoader webappLoader = new WebappLoader(ctx.getParentClassLoader());
        webappLoader.addRepository("file:///" + guavaFile);
        webappLoader.addRepository("file:///" + stringUtils);
        ctx.setLoader(webappLoader);


        tomcat.start();
        tomcat.getServer().await();
    }
}
