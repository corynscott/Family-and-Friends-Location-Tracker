/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clients;

/**
 *
 * @author Coryn Scott
 */
public class Setup {
    private TestClientLocationAndUser rc;
    
    public static void main (String[] args){
        TestClientLocationAndUser rc = new TestClientLocationAndUser();
        rc.addUser("coryns", "password", "07709829401", "coryn");
        rc.addUser("jamesm", "password1","07709829402", "james");
        rc.addUser("jakeg","password2", "+447709829403", "jake");
        rc.addUser("justinm", "password3", "+447709829404","justin");
        rc.addLocation("justin","000341","000205300","justinno comment","2014-11-17T00:00:00Z","pladcea");
        rc.addCheckIn("justin","0001","0002000","HEllo this is james","2014-11-17T00:00:00Z", "placdfeb");
}
    
}
