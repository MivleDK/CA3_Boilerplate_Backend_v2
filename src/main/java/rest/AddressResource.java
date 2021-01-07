package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dto.AddressesDTO;
import entities.Address;
import errorhandling.NotFoundException;
import facades.AddressFacade;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import utils.EMF_Creator;

@Path("address")
public class AddressResource {
    
    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    
    @Context
    private UriInfo context;
    
    private static final AddressFacade FACADE =  AddressFacade.getAddressFacade(EMF);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getHobbyForAll() {
        return "{\"msg\":\"Hello from Addresses\"}";
    }

    //Just to verify if the database is setup
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("count")
    public String countAddresses() {

        EntityManager em = EMF.createEntityManager();
        try {
            TypedQuery<Address> query = em.createQuery ("SELECT a from Address a", Address.class);
            List<Address> address = query.getResultList();
            return "[" + address.size() + "]";
        } finally {
            em.close();
        }
    }

    
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("all")
    public String getAllAddresses() throws NotFoundException {
        AddressesDTO aDTO = FACADE.getAllAddresses();
        return GSON.toJson(aDTO);
    }
    
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public String getPersonByEmail(@PathParam("id") long id) throws NotFoundException {
        return GSON.toJson(FACADE.getAddressById(id));
    }
 
}