package facades;

import entities.Address;
import entities.Hobby;
import entities.Role;
import utils.EMF_Creator;
import entities.Person;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import security.errorhandling.AuthenticationException;

//Uncomment the line below, to temporarily disable this test
//@Disabled
public class PersonFacadeTest {

    private static EntityManagerFactory emf;
    //private static FacadeExample facade;
    private static PersonFacade facade;
    private Person p1, p2, p3;
    private Role r1, r2;
    private Address a1, a2;
    private Hobby h1, h2;

    public PersonFacadeTest() {
    }

    @BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        facade = PersonFacade.getPersonFacade(emf);
    }

    @AfterAll
    public static void tearDownClass() {
//        Clean up database after test is done or use a persistence unit with drop-and-create to start up clean on every test
    }

    // Setup the DataBase in a known state BEFORE EACH TEST
    //TODO -- Make sure to change the code below to use YOUR OWN entity class
    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        try {
//            Delete existing users and roles to get a "fresh" database
            em.getTransaction().begin();
            em.createQuery("delete from Person").executeUpdate();
            em.createQuery("delete from Role").executeUpdate();
            em.createQuery("delete from Address").executeUpdate();

            p1 = new Person("kinkymarkmus@hotmail.com", "secretpassword", "13467964", "John", "Illermand");
            p2 = new Person("villads@gmail.com", "secretpassword", "65478931", "Villads", "Markmus");
            p3 = new Person("Mike@litoris.com", "secretpassword", "32132112", "Willy", "Stroker");

            a1 = new Address("Gøgeholmvej 2", "Helsingør", 3000);
            a2 = new Address("Slingrevænget 55", "Birkerød", 3460);

            h1 = new Hobby("Vandpolo", "Husk at holde vejret");
            h2 = new Hobby("Full Contact Petanque", "Bring your own equipment (And bandages!)");

            p1.setAddress(a1);
            p2.setAddress(a2);
            p3.setAddress(a1);

            p1.addHobby(h1);
            p2.addHobby(h2);
            p3.addHobby(h2);
            p3.addHobby(h1);

            r1 = new Role("user");
            r2 = new Role("admin");
            p1.addRole(r1);
            p2.addRole(r2);
            p3.addRole(r1);
            p3.addRole(r2);
            em.persist(r1);
            em.persist(r2);
            em.persist(p1);
            em.persist(p2);
            em.persist(p3);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @AfterEach
    public void tearDown() {
//        Remove any data after each test was run
    }

    @Test
    public void testAFacadeMethod() {
        assertEquals(3, facade.getUserCount(), "Expects three rows in the database");
    }

    @Test
    public void testGetVeryfiedUser() throws AuthenticationException {
        String pass = p1.getUserPass();

        assertEquals(p1.getEmail(), "kinkymarkmus@hotmail.com");
        assertEquals(p1.getUserPass(), pass);
        assertThat(p1.getEmail(), is(not("pollemand")));
        assertThat(p1.getUserPass(), is(not("lilleGrimTomat")));
    }

    @Test
    public void testGetRoleList() {
        assertEquals(p1.getRolesAsStrings().get(0), r1.getRoleName());
    }
}
