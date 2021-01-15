
package rest;

import dto.PersonDTO;
import javax.ws.rs.core.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Disabled;

@Disabled
public class PersonResourceTest {
    
    public PersonResourceTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
    }
    
    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of getInfoForAll method, of class PersonResource.
     */
    @Test
    public void testGetInfoForAll() {
        System.out.println("getInfoForAll");
        PersonResource instance = new PersonResource();
        String expResult = "";
        String result = instance.getInfoForAll();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of allUsers method, of class PersonResource.
     */
    @Test
    public void testAllUsers() {
        System.out.println("allUsers");
        PersonResource instance = new PersonResource();
        String expResult = "";
        String result = instance.allUsers();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getFromUser method, of class PersonResource.
     */
    @Test
    public void testGetFromUser() {
        System.out.println("getFromUser");
        PersonResource instance = new PersonResource();
        String expResult = "";
        String result = instance.getFromUser();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getFromAdmin method, of class PersonResource.
     */
    @Test
    public void testGetFromAdmin() {
        System.out.println("getFromAdmin");
        PersonResource instance = new PersonResource();
        String expResult = "";
        String result = instance.getFromAdmin();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getPersonByEmail method, of class PersonResource.
     */
    @Test
    public void testGetPersonByEmail() throws Exception {
        System.out.println("getPersonByEmail");
        String email = "";
        PersonResource instance = new PersonResource();
        String expResult = "";
        String result = instance.getPersonByEmail(email);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getAllPersons method, of class PersonResource.
     */
    @Test
    public void testGetAllPersons() throws Exception {
        System.out.println("getAllPersons");
        PersonResource instance = new PersonResource();
        String expResult = "";
        String result = instance.getAllPersons();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of addUser method, of class PersonResource.
     */
    @Test
    public void testAddUser() throws Exception {
        System.out.println("addUser");
        String user = "";
        PersonResource instance = new PersonResource();
        PersonDTO expResult = null;
        PersonDTO result = instance.addUser(user);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of updatePerson method, of class PersonResource.
     */
    @Test
    public void testUpdatePerson() throws Exception {
        System.out.println("updatePerson");
        String person = "";
        PersonResource instance = new PersonResource();
        Response expResult = null;
        Response result = instance.updatePerson(person);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of deletePerson method, of class PersonResource.
     */
    @Test
    public void testDeletePerson() throws Exception {
        System.out.println("deletePerson");
        String email = "";
        PersonResource instance = new PersonResource();
        String expResult = "";
        String result = instance.deletePerson(email);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of addHobbyToPerson method, of class PersonResource.
     */
    @Test
    public void testAddHobbyToPerson() throws Exception {
        System.out.println("addHobbyToPerson");
        String email = "";
        long id = 0L;
        PersonResource instance = new PersonResource();
        Response expResult = null;
        Response result = instance.addHobbyToPerson(email, id);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of removeHobbyFromPerson method, of class PersonResource.
     */
    @Test
    public void testRemoveHobbyFromPerson() throws Exception {
        System.out.println("removeHobbyFromPerson");
        String email = "";
        long id = 0L;
        PersonResource instance = new PersonResource();
        Response expResult = null;
        Response result = instance.removeHobbyFromPerson(email, id);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
