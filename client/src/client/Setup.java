/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

/**
 *
 * @author Coryn Scott
 */
public class Setup {
    private RestfulClient rc;
    
    public static void main (String[] args){
        RestfulClient rc = new RestfulClient();
        rc.addUser("coryns", "password", "07709829401", "coryn");
        rc.addUser("jamesm", "password1","07709829402", "james");
        rc.addUser("jakeg","password2", "+447709829403", "jake");
        rc.addUser("justinm", "password3", "+447709829404","justin");
    
}
    
}
