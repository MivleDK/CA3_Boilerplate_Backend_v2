package facades;

import dto.PersonDTO;
import dto.PersonsDTO;
import entities.Address;
import entities.Hobby;
import entities.Role;
import utils.EMF_Creator;
import entities.Person;
import errorhandling.NotFoundException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import static org.hamcrest.MatcherAssert.assertThat;
import org.hamcrest.Matchers;
import static org.hamcrest.Matchers.everyItem;
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
//Delete existing users and roles to get a "fresh" database
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
    public void testPersonCount() {
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

    @Test
    public void testMakePerson() throws Exception {
        EntityManager em = emf.createEntityManager();

        String email = "ulla@hotmail.com";
        String phone = "123456";
        String firstName = "Ulla";
        String lastName = "Allu";
        String street = "street";
        String city = "city";
        int zipcode = 1234;
        String password = "muffe";
        String hobby1 = "bold";
        String hobby2 = "flyvning";

        Person p = new Person(email, password, phone, firstName, lastName);
        Address a = new Address(street, city, zipcode);
        Hobby h = new Hobby(hobby1, hobby2);

        p.addHobby(h);
        p.setAddress(a);

        System.out.println("Person: " + p);

        PersonDTO pDTO = new PersonDTO(p);

        System.out.println("PersonDTO" + pDTO);

        facade.makePerson(pDTO);

        try {
            em.getTransaction().begin();

            em.find(Person.class, email);

            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @Test
    public void testGetAllPerson() throws NotFoundException {

        PersonsDTO personsDTO = facade.getAllPersons();
        List<PersonDTO> list = personsDTO.getAll();
        System.out.println("Liste af personer: " + list);
        assertThat(list, everyItem(Matchers.hasProperty("email")));
        assertThat(list, Matchers.hasItems(Matchers.<PersonDTO>hasProperty("firstName", is("John")),
                Matchers.<PersonDTO>hasProperty("firstName", is("Villads"))
        ));

    }

    @Test
    public void getPersonByEmail() throws NotFoundException {

        PersonDTO personDTO = facade.getPersonByEmail(p1.getEmail());

        assertEquals("kinkymarkmus@hotmail.com", personDTO.getEmail());
    }

    @Test
    public void testDeletePerson() throws NotFoundException {

        PersonDTO personDTO = facade.deletePerson(p1.getEmail());
        //assertThat(p1.getEmail(), is(not(personDTO.getEmail())));
        assertEquals(2, facade.getUserCount());

    }

    @Test
    public void TestUpdatePerson() throws NotFoundException {

        EntityManager em = emf.createEntityManager();

        String phone = "123456";
        String firstName = "Ulla";
        String lastName = "Allu";

        p1.setFirstName(firstName);
        p1.setLastName(lastName);
        p1.setPhone(phone);
        System.out.println("Person: " + p1);

        PersonDTO pDTO = new PersonDTO(p1);

        System.out.println("PersonDTO: " + pDTO);

        facade.updatePerson(pDTO);

        try {
            em.getTransaction().begin();
            em.merge(p1);
            em.getTransaction().commit();
        } finally {
            em.close();
        }

        assertEquals(p1.getFirstName(), "Ulla");
    }

}
