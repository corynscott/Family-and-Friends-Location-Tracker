package restful;

import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 * Class that saves us from having to specify the restful architecture in XML
 * @author Coryn Scott
 */
@ApplicationPath("/")
public class RSApplication extends Application{
   @Override
   public Set<Class<?>> getClasses()
   { 
     final Set<Class<?>> classes = new HashSet<Class<?>>();
     //register root resource 
     classes.add(RSLocation.class); return classes; 
   } 

    /**
     * Do not modify addRestResourceClasses() method.
     * It is automatically re-generated by NetBeans REST support to populate
     * given list with all resources defined in the project.
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(restful.RSLocation.class);
    }
}

