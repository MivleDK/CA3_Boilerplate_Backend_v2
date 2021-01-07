package facades;

import dto.HobbiesDTO;
import dto.PersonDTO;
import dto.PersonsDTO;
import entities.Person;
import errorhandling.NotFoundException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import security.errorhandling.AuthenticationException;


public class HobbyFacade {

    private static EntityManagerFactory emf;
    private static HobbyFacade instance;

    private HobbyFacade() {
    }

    /**
     *
     * @param _emf
     * @return the instance of this facade.
     */
    public static HobbyFacade getHobbyFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new HobbyFacade();
        }
        return instance;
    }

    public long getHobbyCount(){
        EntityManager em = emf.createEntityManager();
        try{
            long hobbyCount = (long)em.createQuery("SELECT COUNT(h) FROM Hobby h").getSingleResult();
            return hobbyCount;
        }finally{  
            em.close();
        }
    }    
    
    
    public HobbiesDTO getAllHobbies() throws NotFoundException {

        EntityManager em = emf.createEntityManager();
        HobbiesDTO hsDTO;
        try {
            hsDTO = new HobbiesDTO(em.createQuery("SELECT h FROM Hobby h").getResultList());
        } catch (Exception e) {
            throw new NotFoundException("No connection to the database");
        } finally {
            em.close();
        }
        return hsDTO;
    }

}
