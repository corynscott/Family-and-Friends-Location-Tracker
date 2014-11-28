/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

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
public class RestfulClientTest {
    
    public RestfulClientTest() {
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
     * Test of addUser method, of class RestfulClient.
     */
    @Test
    public void testAddUser() {
        System.out.println("addUser");
        String name = "hanson";
        String password = "password";
        String phoneNumber = "077092525";
        String username = "han";
        RestfulClient instance = new RestfulClient();
        instance.addUser(name, password, phoneNumber, username);
        instance.loginUser(username, password);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of loginUser method, of class RestfulClient.
     */
    @Test
    public void testLoginUser() {
        System.out.println("loginUser");
        String username = "";
        String password = "";
        RestfulClient instance = new RestfulClient();
        instance.loginUser(username, password);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of addLocation method, of class RestfulClient.
     */
    @Test
    public void testAddLocation() {
        System.out.println("addLocation");
        String username = "";
        String longitude = "";
        String latitude = "";
        String comment = "";
        String dateandtime = "";
        String place = "";
        RestfulClient instance = new RestfulClient();
        instance.addLocation(username, longitude, latitude, comment, dateandtime, place);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of addCheckIn method, of class RestfulClient.
     */
    @Test
    public void testAddCheckIn() {
        System.out.println("addCheckIn");
        String username = "";
        String longitude = "";
        String latitude = "";
        String comment = "";
        String dateandtime = "";
        String place = "";
        RestfulClient instance = new RestfulClient();
        instance.addCheckIn(username, longitude, latitude, comment, dateandtime, place);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getUserLocations method, of class RestfulClient.
     */
    @Test
    public void testGetUserLocations() {
        System.out.println("getUserLocations");
        String username = "";
        RestfulClient instance = new RestfulClient();
        instance.getUserLocations(username);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getUserCheckIns method, of class RestfulClient.
     */
    @Test
    public void testGetUserCheckIns() {
        System.out.println("getUserCheckIns");
        String username = "";
        RestfulClient instance = new RestfulClient();
        instance.getUserCheckIns(username);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getRequest method, of class RestfulClient.
     */
    @Test
    public void testGetRequest() {
        System.out.println("getRequest");
        String subURL = "";
        RestfulClient instance = new RestfulClient();
        HttpResponse expResult = null;
        HttpResponse result = instance.getRequest(subURL);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of postRequest method, of class RestfulClient.
     */
    @Test
    public void testPostRequest() throws Exception {
        System.out.println("postRequest");
        String subURL = "";
        String data = "";
        RestfulClient instance = new RestfulClient();
        HttpResponse expResult = null;
        HttpResponse result = instance.postRequest(subURL, data);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of processResponse method, of class RestfulClient.
     */
    @Test
    public void testProcessResponse() {
        System.out.println("processResponse");
        HttpResponse response = null;
        RestfulClient instance = new RestfulClient();
        instance.processResponse(response);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of main method, of class RestfulClient.
     */
    @Test
    public void testMain() {
        System.out.println("main");
        String[] args = null;
        RestfulClient.main(args);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}