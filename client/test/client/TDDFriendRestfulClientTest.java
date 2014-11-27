/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.util.ArrayList;
import org.apache.http.HttpResponse;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Coryn Scott
 */
public class TDDFriendRestfulClientTest {
    String exp200Result = "HTTP/1.1 200 OK";
    public TDDFriendRestfulClientTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getPotentialFriends method, of class TDDFriendRestfulClient.
     */
    @Test
    public void testCorynGetPotentialFriends() {
        System.out.println("CorynGetPotentialFriends");
        String username = "coryn";
        String data = "{\"usernameNumber\":[{\"@number\":\"+447700\",\"@username\":\"\"},{\"@number\":\"+447709829402\",\"@username\":\"\"},{\"@number\":\"+447709829403\",\"@username\":\"\"},{\"@number\":\"+447709829404\",\"@username\":\"\"}]}";
        TDDFriendRestfulClient instance = new TDDFriendRestfulClient();
        String expResult = "{\"usernameNumber\":[{\"@number\":\"+447709829402\",\"@username\":\"james\"},{\"@number\":\"+447709829403\",\"@username\":\"jake\"},{\"@number\":\"+447709829404\",\"@username\":\"justin\"}]}";
           String result = instance.getPotentialFriends(username, data);
 
        assertEquals(result,expResult);
    }

    public void testJamesmGetPotentialFriends() {
        System.out.println("JamesGetPotentialFriends");
        String username = "james";
        String data = "{\"usernameNumber\":[{\"@number\":\"+447700\",\"@username\":\"\"},\"@username\":\"\"},{\"@number\":\"+447709829402\",\"@username\":\"\"},{\"@number\":\"+447709829403\",\"@username\":\"\"},{\"@number\":\"+447709829404\",\"@username\":\"\"}]}";
            
        TDDFriendRestfulClient instance = new TDDFriendRestfulClient();
        String expResult1 = "\"coryn\":\"+447709829401\"";
        String expResult2 = "\"justin\":\"+447709829404\"";
        String result = instance.getPotentialFriends(username, data);
        assertTrue(result.contains(expResult1));
        assertTrue(result.contains(expResult2));
    }
    /**
     * Test of requestFriendship method, of class TDDFriendRestfulClient.
     */
    @Test
    public void testRequestjjFriendship() {
        System.out.println("requestjamestojakeFriendship");
        String initatiorusername = "james";
        String recipientusername = "jake";
        TDDFriendRestfulClient instance = new TDDFriendRestfulClient();
        String result = instance.requestFriendship(initatiorusername, recipientusername);
        assertEquals(exp200Result, result);
        //test that the request has been added to the pending list
        String resultini = instance.getPendingFriendList(initatiorusername);
        String expResultini = "{\"initiator\":\"james\",\"recipient\":\"jake\"}";
        assertTrue(resultini.contains(expResultini));
        
        String resultrecp = instance.getPendingFriendList(recipientusername);
        String expResultrecp = "{\"initiator\":\"james\",\"recipient\":\"jake\"}";
        assertTrue(resultrecp.contains(expResultrecp));
    }
    
    @Test
    public void testRequestcjFriendship() {
       System.out.println("requestcoryntojamesFriendship");
        String initatiorusername = "coryn";
        String recipientusername = "james";
        TDDFriendRestfulClient instance = new TDDFriendRestfulClient();
        String result = instance.requestFriendship(initatiorusername, recipientusername);
        assertEquals(exp200Result, result);
        //test that the request has been added to the pending list
        String resultini = instance.getPendingFriendList(initatiorusername);
        String expResultini = "{\"initiator\":\"coryn\",\"recipient\":\"james\"}";
        assertTrue(resultini.contains(expResultini));
        
        String resultrecp = instance.getPendingFriendList(recipientusername);
        String expResultrecp = "{\"initiator\":\"coryn\",\"recipient\":\"james\"}";
        assertTrue(resultrecp.contains(expResultrecp));
    }

    /**
     * Test of acceptFriendship method, of class TDDFriendRestfulClient.
     */
    @Test
    public void testAcceptFriendship() {
        System.out.println("acceptjjFriendship");
        String recipientusername = "jake";
        String initatiorusername = "james";
        TDDFriendRestfulClient instance = new TDDFriendRestfulClient();
        String result = instance.acceptFriendship(recipientusername, initatiorusername);
        assertEquals(exp200Result, result);
        //testtheyarenowfriends
        String resultini = instance.getFriendList(initatiorusername);
        String expResultini = "jake";
        assertTrue(resultini.contains(expResultini));
        
        String resultrecp = instance.getFriendList(recipientusername);
        String expResultrecp = "james";
        assertTrue(resultrecp.contains(expResultrecp));
        
        //check they are nolonger pending
        String resultinipend = instance.getPendingFriendList(initatiorusername);
        String expResultinipend = "jake";
        assertFalse(resultinipend.contains(expResultinipend));
        
        String resultrecppend = instance.getPendingFriendList(recipientusername);
        String expResultrecppend = "james";
        assertFalse(resultrecppend.contains(expResultrecppend));
        
    }

    /**
     * Test of declineFriendship method, of class TDDFriendRestfulClient.
     */
    @Test
    public void testDeclineFriendship() {
        System.out.println("declineFriendship");
        String recipientusername = "coryn";
        String initatiorusername = "james";
        TDDFriendRestfulClient instance = new TDDFriendRestfulClient();
        String result = instance.declineFriendship(recipientusername, initatiorusername);
        assertEquals(exp200Result, result);
        
        //testtheyarenotfriends
        String resultini = instance.getFriendList(initatiorusername);
        String expResultini = "james";
        assertFalse(resultini.contains(expResultini));
        
        String resultrecp = instance.getFriendList(recipientusername);
        String expResultrecp = "coryn";
        assertFalse(resultrecp.contains(expResultrecp));
        
        //check they are nolonger pending
        String resultinipend = instance.getPendingFriendList(initatiorusername);
        String expResultinipend = "james";
        assertFalse(resultinipend.contains(expResultinipend));
        
        String resultrecppend = instance.getPendingFriendList(recipientusername);
        String expResultrecppend = "coryn";
        assertFalse(resultrecppend.contains(expResultrecppend));
    }

    

    /**
     * Test of deleteFriend method, of class TDDFriendRestfulClient.
     */
    @Test
    public void testDeleteFriend() {
        System.out.println("deleteFriend");
        String username = "";
        String friendToDeleteUsername = "";
        TDDFriendRestfulClient instance = new TDDFriendRestfulClient();
        String expResult = "";
        String result = instance.deleteFriend(username, friendToDeleteUsername);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getFriendLocations method, of class TDDFriendRestfulClient.
     */
    @Test
    public void testGetFriendLocations() {
        System.out.println("getFriendLocations");
        String username = "james";
        String friendUsername = "jake";
        TDDFriendRestfulClient instance = new TDDFriendRestfulClient();
        String expResult = "jake";
        String result = instance.getFriendLocations(username, friendUsername);
        assertTrue(result.contains(expResult));
    }

    /**
     * Test of getFriendCheckIns method, of class TDDFriendRestfulClient.
     */
    @Test
    public void testGetFriendCheckIns() {
        System.out.println("getFriendCheckIns");
        String username = "james";
        String friendUsername = "jake";
        TDDFriendRestfulClient instance = new TDDFriendRestfulClient();
        String expResult = "jake";
        String result = instance.getFriendCheckIns(username, friendUsername);
        assertTrue(result.contains(expResult));
    }

}