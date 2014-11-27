/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package friend;

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
public class UsernameNumberTest {
    
    public UsernameNumberTest() {
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
     * Test of getUsername method, of class UsernameNumber.
     */
    @Test
    public void testGetUsername() {
        System.out.println("getUsername");
        UsernameNumber instance = new UsernameNumber();
        String expResult = "";
        String result = instance.getUsername();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setUsername method, of class UsernameNumber.
     */
    @Test
    public void testSetUsername() {
        System.out.println("setUsername");
        String username = "";
        UsernameNumber instance = new UsernameNumber();
        instance.setUsername(username);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getNumber method, of class UsernameNumber.
     */
    @Test
    public void testGetNumber() {
        System.out.println("getNumber");
        UsernameNumber instance = new UsernameNumber();
        String expResult = "";
        String result = instance.getNumber();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setNumber method, of class UsernameNumber.
     */
    @Test
    public void testSetNumber() {
        System.out.println("setNumber");
        String number = "";
        UsernameNumber instance = new UsernameNumber();
        instance.setNumber(number);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}