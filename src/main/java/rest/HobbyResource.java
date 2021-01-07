package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dto.HobbiesDTO;
import dto.PersonsDTO;
import entities.Hobby;
import entities.Person;
import errorhandling.NotFoundException;
import facades.HobbyFacade;
import facades.PersonFacade;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import utils.EMF_Creator;

@Path("hobby")
public class HobbyResource {
    
    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    @Context
    private UriInfo context;

    @Context
    SecurityContext securityContext;
    
    private static final HobbyFacade FACADE =  HobbyFacade.getHobbyFacade(EMF);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getHobbyForAll() {
        return "{\"msg\":\"Hello from Hobby\"}";
    }

    //Just to verify if the database is setup
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("count")
    public String countHobbies() {

        EntityManager em = EMF.createEntityManager();
        try {
            TypedQuery<Hobby> query = em.createQuery ("SELECT h from Hobby h", Hobby.class);
            List<Hobby> hobby = query.getResultList();
            return "[" + hobby.size() + "]";
        } finally {
            em.close();
        }
    }

    
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("all")
    public String getAllHobbies() throws NotFoundException {
        HobbiesDTO hsDTO = FACADE.getAllHobbies();
        return GSON.toJson(hsDTO);
    }
    
 
}