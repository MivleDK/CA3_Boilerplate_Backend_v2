package facades;

import dto.HobbiesDTO;
import dto.HobbyDTO;
import entities.Hobby;
import errorhandling.MissingInputException;
import errorhandling.NotFoundException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

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

    public long getHobbyCount() {
        EntityManager em = emf.createEntityManager();
        try {
            long hobbyCount = (long) em.createQuery("SELECT COUNT(h) FROM Hobby h").getSingleResult();
            return hobbyCount;
        } finally {
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

    public HobbyDTO getHobby(long id) throws NotFoundException {
        EntityManager em = emf.createEntityManager();
        Hobby hobby = em.find(Hobby.class, id);
        if (hobby == null) {
            throw new NotFoundException("No hobby with provided id found");
        } else {
            try {
                return new HobbyDTO(hobby);
            } finally {
                em.close();
            }
        }
    }

    public HobbyDTO addHobby(String description, String name) throws MissingInputException {
        if (isNameInValid(description, name)) {
            throw new MissingInputException("Description and/or name is missing");
        }
        EntityManager em = emf.createEntityManager();
        Hobby hobby = new Hobby(description, name);
        try {
            em.getTransaction().begin();
            em.persist(hobby);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return new HobbyDTO(hobby);
    }

    public HobbyDTO editHobby(HobbyDTO h) throws  MissingInputException, NotFoundException {
        if (isNameInValid(h.getDescription(), h.getName())) {
            throw new MissingInputException("Description and/or name is missing");
        }
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Hobby hobby = em.find(Hobby.class, h.getId());
            if (hobby == null) {
                throw new NotFoundException("No hobby with provided id found");
            } else {
                hobby.setDescription(h.getDescription());
                hobby.setName(h.getName());
            }
            em.getTransaction().commit();
            return new HobbyDTO(hobby);
        } finally {
            em.close();
        }
    }

     public HobbyDTO deleteHobby(long id) throws NotFoundException {
        EntityManager em = emf.createEntityManager();
        Hobby hobby = em.find(Hobby.class, id);
        if (hobby == null) {
            throw new NotFoundException("Could not delete, provided id does not exist");
        } else {
            try {
                em.getTransaction().begin();
                em.remove(hobby);
                em.getTransaction().commit();
            } finally {
                em.close();
            }
            return new HobbyDTO(hobby);
        }
    }
     
    private static boolean isNameInValid(String description, String name) {
        return (description.length() == 0) || (name.length() == 0);
    }
}
