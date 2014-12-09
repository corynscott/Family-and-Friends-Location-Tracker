/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clients;

import clients.TestClientLocationAndUser;
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
        String password = "passwors1asd";
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
        String expResult1 = "\"@appUsername\":\"coryn\",\"@checkIn\":\"false\",\"@comment\":\"\",\"@dateandtime\":\"2014-11-17T00:00:00Z\",\"@latitude\":\"001\",\"@longitude\":\"0001\",\"@place\":\"\"";
        String result1 = instance.getUserLocations(username);
        assertTrue(result1.contains(expResult1));
        
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
        String username = "coryn";
        String longitude = "125";
        String latitude = "124";
        String comment = "HEY!";
        String dateandtime = "2014-11-17T00:00:00Z";
        String place = "Sussex";
        TestClientLocationAndUser instance = new TestClientLocationAndUser();
        String expResult = exp201Result;
        String result = instance.addCheckIn(username, longitude, latitude, comment, dateandtime, place);
        assertEquals(expResult, result);
        
        String expResult1 = "\"@appUsername\":\"coryn\",\"@checkIn\":\"true\",\"@comment\":\"HEY!\",\"@dateandtime\":\"2014-11-17T00:00:00Z\",\"@latitude\":\"124\",\"@longitude\":\"125\",\"@place\":\"Sussex\"";     
        String result1 = instance.getUserCheckIns(username);
        System.out.println(result1);
        assertTrue(result1.contains(expResult1));
    }

    /**
     * Test of getUserLocations method, of class TestClientLocationAndUser.
     */
    @Test
    public void testGetUserLocations() {
        System.out.println("getUserLocations");
        String username = "coryn";
        TestClientLocationAndUser instance = new TestClientLocationAndUser();
        String expResulta = "\"@appUsername\":\"coryn\",\"@checkIn\":\"false\",\"@comment\":\"\",\"@dateandtime\":\"2014-11-17T00:00:00Z\",\"@latitude\":\"001\",\"@longitude\":\"0001\",\"@place\":\"\"";
        String expResultb = "\"@appUsername\":\"coryn\",\"@checkIn\":\"true\",\"@comment\":\"HEY!\",\"@dateandtime\":\"2014-11-17T00:00:00Z\",\"@latitude\":\"124\",\"@longitude\":\"125\",\"@place\":\"Sussex\"";     
        String result = instance.getUserLocations(username);
        assertTrue(result.contains(expResulta));
        assertTrue(result.contains(expResultb));
    }

    
    /**
     * Test of getUserLocations method for user who doesn't exist, of class TestClientLocationAndUser.
     */
    @Test
    public void testGetUserLocationsUserDoesntExist() {
        System.out.println("getUserLocations");
        String username = "corynasd";
        TestClientLocationAndUser instance = new TestClientLocationAndUser();
        String expResult = null;
        String result = instance.getUserLocations(username);
        System.out.println(result);
        assertEquals(expResult, result);
    }

    
    /**
     * Test of getUserCheckIns method, of class TestClientLocationAndUser.
     */
    @Test
    public void testGetUserCheckIns() {
        System.out.println("getUserCheckIns");
        String username = "coryn";
        TestClientLocationAndUser instance = new TestClientLocationAndUser();
        String expResult = "\"@appUsername\":\"coryn\",\"@checkIn\":\"true\",\"@comment\":\"HEY!\",\"@dateandtime\":\"2014-11-17T00:00:00Z\",\"@latitude\":\"124\",\"@longitude\":\"125\",\"@place\":\"Sussex\"";     
        
        String result = instance.getUserCheckIns(username);
        assertTrue(result.contains(expResult));
    }

/**
     * Test of getUserCheckIns method username doesnt exist, of class TestClientLocationAndUser.
     */
    @Test
    public void testGetUserCheckInsUserDoesntExist() {
        System.out.println("getUserCheckIns");
        String username = "csc";
        TestClientLocationAndUser instance = new TestClientLocationAndUser();
        String expResult = null;
        String result = instance.getUserCheckIns(username);
        assertEquals(expResult, result);
    
    }
    
}