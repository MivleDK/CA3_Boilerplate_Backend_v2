package facades;

import dto.PersonDTO;
import dto.PersonsDTO;
import entities.Address;
import entities.Hobby;
import entities.Person;
import entities.Role;
import errorhandling.NotFoundException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import security.errorhandling.AuthenticationException;

public class PersonFacade {

    private static EntityManagerFactory emf;
    private static PersonFacade instance;

    private PersonFacade() {
    }

    /**
     *
     * @param _emf
     * @return the instance of this facade.
     */
    public static PersonFacade getPersonFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new PersonFacade();
        }
        return instance;
    }

    public long getUserCount() {
        EntityManager em = emf.createEntityManager();
        try {
            long userCount = (long) em.createQuery("SELECT COUNT(u) FROM Person u").getSingleResult();
            return userCount;
        } finally {
            em.close();
        }
    }

    public Person getVeryfiedUser(String username, String password) throws AuthenticationException {
        EntityManager em = emf.createEntityManager();
        Person user;
        try {
            user = em.find(Person.class, username);
            if (user == null || !user.verifyPassword(password)) {
                throw new AuthenticationException("Invalid user name or password");
            }
        } finally {
            em.close();
        }
        return user;
    }

    public PersonDTO getPersonByEmail(String email) throws NotFoundException {
        EntityManager em = emf.createEntityManager();

        Person user;
        PersonDTO pDTO;

        try {
            user = em.find(Person.class, email);
            if (user == null) {
                throw new NotFoundException("Invalid user name or email");
            }
        } finally {
            em.close();
        }
        return new PersonDTO(user);

    }

    public PersonsDTO getAllPersons() throws NotFoundException {

        EntityManager em = emf.createEntityManager();
        PersonsDTO psDTO;
        try {
            psDTO = new PersonsDTO(em.createQuery("SELECT p FROM Person p").getResultList());
        } catch (Exception e) {
            throw new NotFoundException("No connection to the database");
        } finally {
            em.close();
        }
        return psDTO;

    }

    public PersonDTO makePerson(PersonDTO person) throws AuthenticationException {

        String email = person.getEmail();
        String userPass = person.getPassword();
        String phone = person.getPhone();
        String fName = person.getFirstName();
        String lName = person.getLastName();

        Person newPerson;

        EntityManager em = emf.createEntityManager();

        try {
            newPerson = em.find(Person.class, email);
            if (newPerson == null && email.length() > 0 && userPass.length() > 0) {
                newPerson = new Person(email, userPass, phone, fName, lName);
                Role userRole = em.find(Role.class, "user");
                newPerson.addRole(userRole);
                Address address = new Address("", "", 0);
                Hobby hobby = new Hobby("None", "none");
                newPerson.setAddress(address);
                newPerson.addHobby(hobby);

                em.getTransaction().begin();
                em.persist(newPerson);
                em.getTransaction().commit();
            } else {
                if ((email.length() == 0 || userPass.length() == 0)) {
                    throw new AuthenticationException("Missing input");
                }
                if (newPerson.getEmail().equalsIgnoreCase(person.getEmail())) {
                    throw new AuthenticationException("User exist");
                }
            }
        } finally {
            em.close();
        }
        return new PersonDTO(newPerson);
    }
}
