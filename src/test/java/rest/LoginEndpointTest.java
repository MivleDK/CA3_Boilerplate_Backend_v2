package rest;

import entities.Address;
import entities.Hobby;
import entities.Person;
import entities.Role;

import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import java.net.URI;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.UriBuilder;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import static org.hamcrest.Matchers.equalTo;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import utils.EMF_Creator;

//@Disabled
public class LoginEndpointTest {

    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";

    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
    private static HttpServer httpServer;
    private static EntityManagerFactory emf;

    static HttpServer startServer() {
        ResourceConfig rc = ResourceConfig.forApplication(new ApplicationConfig());
        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);
    }

    Person p1, p2, p3;
    private Address a1, a2;
    private Hobby h1, h2;

    @BeforeAll
    public static void setUpClass() {
        //This method must be called before you request the EntityManagerFactory
        EMF_Creator.startREST_TestWithDB();
        emf = EMF_Creator.createEntityManagerFactoryForTest();

        httpServer = startServer();
        //Setup RestAssured
        RestAssured.baseURI = SERVER_URL;
        RestAssured.port = SERVER_PORT;
        RestAssured.defaultParser = Parser.JSON;
    }

    @AfterAll
    public static void closeTestServer() {
        //Don't forget this, if you called its counterpart in @BeforeAll
        EMF_Creator.endREST_TestWithDB();

        httpServer.shutdownNow();
    }

    // Setup the DataBase (used by the test-server and this test) in a known state BEFORE EACH TEST
    //TODO -- Make sure to change the EntityClass used below to use YOUR OWN (renamed) Entity class
    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            //Delete existing users and roles to get a "fresh" database
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

            Role userRole = new Role("user");
            Role adminRole = new Role("admin");
            p1.addRole(userRole);
            p2.addRole(adminRole);
            p3.addRole(userRole);
            p3.addRole(adminRole);
            em.persist(userRole);
            em.persist(adminRole);
            em.persist(p1);
            em.persist(p2);
            em.persist(p3);
            em.getTransaction().commit();

        } finally {
            em.close();
        }
    }

    //This is how we hold on to the token after login, similar to that a client must store the token somewhere
    private static String securityToken;

    //Utility method to login and set the returned securityToken
    private static void login(String role, String password) {
        String json = String.format("{username: \"%s\", password: \"%s\"}", role, password);
        securityToken = given()
                .contentType("application/json")
                .body(json)
                //.when().post("/api/login")
                .when().post("/login")
                .then()
                .extract().path("token");
        //System.out.println("TOKEN ---> " + securityToken);
    }

    private void logOut() {
        securityToken = null;
    }

    @Test
    public void serverIsRunning() {
        given().when().get("/info").then().statusCode(200);
    }

    @Test
    public void testRestNoAuthenticationRequired() {
        given()
                .contentType("application/json")
                .when()
                .get("/info/").then()
                .statusCode(200)
                .body("msg", equalTo("Hello anonymous"));
    }

    @Test
    public void testRestForAdmin() {
        login("villads@gmail.com", "secretpassword");
        given()
                .contentType("application/json")
                .accept(ContentType.JSON)
                .header("x-access-token", securityToken)
                .when()
                .get("/info/admin").then()
                .statusCode(200)
                .body("msg", equalTo("Hello to (admin) User: " + p2.getEmail()));
    }

    @Test
    public void testRestForUser() {
        login("kinkymarkmus@hotmail.com", "secretpassword");
        given()
                .contentType("application/json")
                .header("x-access-token", securityToken)
                .when()
                .get("/info/user").then()
                .statusCode(200)
                .body("msg", equalTo("Hello to User: " + p1.getEmail()));
    }

    @Test
    public void testAutorizedUserCannotAccesAdminPage() {
        login("kinkymarkmus@hotmail.com", "secretpassword");
        given()
                .contentType("application/json")
                .header("x-access-token", securityToken)
                .when()
                .get("/info/admin").then() //Call Admin endpoint as user
                .statusCode(401);
    }

    @Test
    public void testAutorizedAdminCannotAccesUserPage() {
        login("villads@gmail.com", "secretpassword");
        given()
                .contentType("application/json")
                .header("x-access-token", securityToken)
                .when()
                .get("/info/user").then() //Call Person endpoint as Admin
                .statusCode(401);
    }

    @Test
    public void testRestForMultiRole1() {
        login("Mike@litoris.com", "secretpassword");
        given()
                .contentType("application/json")
                .accept(ContentType.JSON)
                .header("x-access-token", securityToken)
                .when()
                .get("/info/admin").then()
                .statusCode(200)
                .body("msg", equalTo("Hello to (admin) User: " + p3.getEmail()));
    }

    @Test
    public void testRestForMultiRole2() {
        login("Mike@litoris.com", "secretpassword");
        given()
                .contentType("application/json")
                .header("x-access-token", securityToken)
                .when()
                .get("/info/user").then()
                .statusCode(200)
                .body("msg", equalTo("Hello to User: " + p3.getEmail()));
    }

    @Test
    public void userNotAuthenticated() {
        logOut();
        given()
                .contentType("application/json")
                .when()
                .get("/info/user").then()
                .statusCode(403)
                .body("code", equalTo(403))
                .body("message", equalTo("Not authenticated - do login"));
    }

    @Test
    public void adminNotAuthenticated() {
        logOut();
        given()
                .contentType("application/json")
                .when()
                .get("/info/user").then()
                .statusCode(403)
                .body("code", equalTo(403))
                .body("message", equalTo("Not authenticated - do login"));
    }

}
