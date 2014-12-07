    /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package locationappserver;

import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 *
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
        resources.add(locationappserver.RSLocation.class);
    }
}
