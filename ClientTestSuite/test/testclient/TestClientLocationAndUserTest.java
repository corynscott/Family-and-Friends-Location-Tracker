/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package testclient;

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
public class TestClientLocationAndUserTest {
    
    String exp200Result = "HTTP/1.1 200 OK";
    String exp201Result = "HTTP/1.1 201 Created";
    String exp304Result = "HTTP/1.1 304 Not Modified";
    String exp401Result = "HTTP/1.1 401 Unauthorized";
    String exp409Result = "HTTP/1.1 409 Conflict";
    
    public TestClientLocationAndUserTest() {
    }
    
   

    /**
     * Test of addUser method, of class TestClientLocationAndUser.
     */
    @Test
    public void testAddUser() {
        System.out.println("addUser");
        String name = "coryns";
        String password = "password1";
        String phoneNumber = "0770982800";
        String username = "coryn";
        TestClientLocationAndUser instance = new TestClientLocationAndUser();
        String expResult = exp201Result;
        String result = instance.addUser(name, password, phoneNumber, username);
        assertEquals(expResult, result);
        //Add a user with the same username
        System.out.println("addUser");
        String name1 = "bill";
        String password1 = "password1";
        String phoneNumber1 = "077095500";
        String username1 = "coryn";
        String expResult1 = exp409Result;
        String result1 = instance.addUser(name1, password1, phoneNumber1, username1);
        assertEquals(expResult1, result1);
    }

   
    /**
     * Test of loginUser method with correct credentials, of class TestClientLocationAndUser.
     */
    @Test
    public void testLoginUserSuccessful() {
        System.out.println("loginUser");
        String username = "coryn";
        String password = "password1";
        TestClientLocationAndUser instance = new TestClientLocationAndUser();
        String expResult = exp200Result;
        String result = instance.loginUser(username, password);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of loginUser method with incorrect credentials, of class TestClientLocationAndUser.
     */
    @Test
    public void testLoginUserUnSuccessful() {
        System.out.println("loginUserInCorrectPassoword");
        String username = "coryn";
        String password = "passworsdd";
        TestClientLocationAndUser instance = new TestClientLocationAndUser();
        String expResult = exp401Result;
        String result = instance.loginUser(username, password);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of loginUser method with username that doesnt exist, of class TestClientLocationAndUser.
     */
    @Test
    public void testLoginUserDoesntExist() {
        System.out.println("loginUserWhoDoesntExist");
        String username = "csdfsdf";
        String password = "password";
        TestClientLocationAndUser instance = new TestClientLocationAndUser();
        String expResult = exp401Result;
        String result = instance.loginUser(username, password);
        assertEquals(expResult, result);
    }

    /**
     * Test of addLocation method, of class TestClientLocationAndUser.
     */
    @Test
    public void testAddLocation() {
        System.out.println("addLocation");
        String username = "coryn";
        String longitude = "0001";
        String latitude = "001";
        String comment = "";
        String dateandtime = "2014-11-17T00:00:00Z";
        String place = "";
        TestClientLocationAndUser instance = new TestClientLocationAndUser();
        String expResult = exp201Result;
        String result = instance.addLocation(username, longitude, latitude, comment, dateandtime, place);
        assertEquals(expResult, result);
        
        //Check That it has been added
        String expResult1 = "{\"location\":{\"@appUsername\":\"coryn\",\"@checkIn\":\"false\",\"@comment\":\"\",\"@dateandtime\":\"2014-11-17T00:00:00Z\",\"@latitude\":\"001\",\"@longitude\":\"0001\",\"@place\":\"\",\"@uli\":\"2\"}}";
        String result1 = instance.getUserLocations(username);
        assertEquals(expResult1, result1);
        
    }

    /**
     * Test of addLocation method where User Doesn't Exist, of class TestClientLocationAndUser.
     */
    @Test
    public void testAddLocationUserDoesntExist() {
        System.out.println("addLocationUserDoesntExist");
        String username = "fdsdf";
        String longitude = "001";
        String latitude = "001";
        String comment = "001";
        String dateandtime = "2014-11-17T00:00:00Z";
        String place = "";
        TestClientLocationAndUser instance = new TestClientLocationAndUser();
        String expResult = exp304Result;
        String result = instance.addLocation(username, longitude, latitude, comment, dateandtime, place);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of addCheckIn method, of class TestClientLocationAndUser.
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
        TestClientLocationAndUser instance = new TestClientLocationAndUser();
        String expResult = "";
        String result = instance.addCheckIn(username, longitude, latitude, comment, dateandtime, place);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getUserLocations method, of class TestClientLocationAndUser.
     */
    @Test
    public void testGetUserLocations() {
        System.out.println("getUserLocations");
        String username = "";
        TestClientLocationAndUser instance = new TestClientLocationAndUser();
        String expResult = "";
        String result = instance.getUserLocations(username);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getUserCheckIns method, of class TestClientLocationAndUser.
     */
    @Test
    public void testGetUserCheckIns() {
        System.out.println("getUserCheckIns");
        String username = "";
        TestClientLocationAndUser instance = new TestClientLocationAndUser();
        String expResult = "";
        String result = instance.getUserCheckIns(username);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }


    
}