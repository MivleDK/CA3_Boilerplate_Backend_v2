package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dto.HobbyDTO;
import dto.PersonDTO;
import dto.PersonsDTO;
import entities.Person;
import errorhandling.NotFoundException;
import facades.PersonFacade;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import utils.EMF_Creator;

@Path("info")
public class PersonResource {
    
    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    @Context
    private UriInfo context;

    @Context
    SecurityContext securityContext;
    
    private static final PersonFacade FACADE =  PersonFacade.getPersonFacade(EMF);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getInfoForAll() {
        return "{\"msg\":\"Hello anonymous\"}";
    }

    //Just to verify if the database is setup
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("count")
    public String allUsers() {

        EntityManager em = EMF.createEntityManager();
        try {
            TypedQuery<Person> query = em.createQuery ("select u from Person u", entities.Person.class);
            List<Person> users = query.getResultList();
            return "[" + users.size() + "]";
        } finally {
            em.close();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("user")
    @RolesAllowed("user")
    public String getFromUser() {
        String thisuser = securityContext.getUserPrincipal().getName();
        return "{\"msg\": \"Hello to User: " + thisuser + "\"}";
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("admin")
    @RolesAllowed("admin")
    public String getFromAdmin() {
        String thisuser = securityContext.getUserPrincipal().getName();
        return "{\"msg\": \"Hello to (admin) User: " + thisuser + "\"}";
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("person/{email}")
    public String getPersonByEmail(@PathParam("email") String email) throws NotFoundException {

        return GSON.toJson(FACADE.getPersonByEmail(email));

    }
    
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("person/all")
    public String getAllPersons() throws NotFoundException {
        PersonsDTO psDTO = FACADE.getAllPersons();
        return GSON.toJson(psDTO);
    }
    
    
    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public PersonDTO addUser(String user) throws Exception {
        PersonDTO u = GSON.fromJson(user, PersonDTO.class);
        PersonDTO newPerson = FACADE.makePerson(u);
        return newPerson;
    }    
    
    @PUT
    @Path("person")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response updatePerson(String person) throws NotFoundException{
        PersonDTO personDTO = GSON.fromJson(person, PersonDTO.class);
        FACADE.updatePerson(personDTO);
        return Response.status(Response.Status.OK).entity("Person updated OK").build();
    }    
    
    @DELETE
    @Path("delete/{email}")
    @Produces({MediaType.APPLICATION_JSON})
    public String deletePerson(@PathParam("email") String email) throws NotFoundException {
        PersonDTO personDelete = FACADE.deletePerson(email);
        return GSON.toJson(personDelete);
    }
    
    @PUT
    @Path("addHobby/{email}/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response addHobbyToPerson(@PathParam("email")String email, @PathParam("id")long id) throws NotFoundException{
        
        FACADE.addHobbyToPerson(email, id);
        
        return Response.status(Response.Status.OK).entity("Person updated OK").build();
    }  
    
    @PUT
    @Path("removeHobby/{email}/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response removeHobbyFromPerson(@PathParam("email")String email, @PathParam("id")long id) throws NotFoundException{
        
        FACADE.removeHobbyFromPerson(email, id);
        
        return Response.status(Response.Status.OK).entity("Person updated OK").build();
    }  
}