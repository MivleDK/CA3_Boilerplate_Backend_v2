package facades;

import dto.HobbyDTO;
import dto.PersonDTO;
import dto.PersonsDTO;
import entities.Address;
import entities.Hobby;
import entities.Person;
import entities.Role;
import errorhandling.NotFoundException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
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

                TypedQuery<Address> addressList = em.createQuery("SELECT a FROM Address a", Address.class);
                List<Address> resultList = addressList.getResultList();

                boolean flag = true;

                for (int i = 0; i < resultList.size(); i++) {
                    if (person.getStreet().equalsIgnoreCase(resultList.get(i).getStreet())
                            && person.getCity().equalsIgnoreCase(resultList.get(i).getCity())
                            && person.getZipcode() == resultList.get(i).getZipCode()) {
                        newPerson.setAddress(resultList.get(i));
                        flag = false;
                    }
                }
                if (flag) {
                    Address newAddress = new Address(person.getStreet(), person.getCity(), person.getZipcode());
                    newPerson.setAddress(newAddress);
                }

                Hobby hobby = new Hobby("None", "none");

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

    public void updatePerson(PersonDTO p) throws NotFoundException {
        EntityManager em = emf.createEntityManager();
        Person person = em.find(Person.class, p.getEmail());
        if (person == null) {
            throw new NotFoundException("No person found");
        }
        person.setFirstName(p.getFirstName());
        person.setLastName(p.getLastName());
        person.setPhone(p.getPhone());

//        Address a1 = em.find(Address.class, person.getAddress().getId());
        TypedQuery<Address> addressList = em.createQuery("SELECT a FROM Address a", Address.class);
        List<Address> resultList = addressList.getResultList();

        boolean flag = true;

        for (int i = 0; i < resultList.size(); i++) {
            if (p.getStreet().equalsIgnoreCase(resultList.get(i).getStreet())
                    && p.getCity().equalsIgnoreCase(resultList.get(i).getCity())
                    && p.getZipcode() == resultList.get(i).getZipCode()) {
                person.setAddress(resultList.get(i));
                flag = false;
            }
        }
        if (flag) {
            Address newAddress = new Address(p.getStreet(), p.getCity(), p.getZipcode());
            person.setAddress(newAddress);
        }

        try {
            em.getTransaction().begin();
            em.merge(person);
            em.getTransaction().commit();
        } finally {
            em.close();
        }

//        //DELETE 
//        for (int i = 0; i < resultList.size(); i++) {
//            System.out.println();
//            System.out.println("RESULT LIST ID: " + resultList.get(i).getId() + " PERSON LIST SIZE IS: " + resultList.get(i).getPersonList().size());
//        }
//
//        //Update list of addresses
//        resultList = em.createQuery("SELECT a FROM Address a", Address.class).getResultList();
//        
//        //Clean up addresses in DB
//        for (int i = 0; i < resultList.size(); i++) {
//            if (resultList.get(i).getPersonList().isEmpty()) {
//
//                Long id = resultList.get(i).getId();
//                System.out.println(id);
//
//                em.getTransaction().begin();
//
//                em.remove(em.find(Address.class, id));
////                TypedQuery<Address> delAddress;
////                delAddress = em.createQuery("DELETE FROM Address WHERE id LIKE :id", Address.class);
////                delAddress.setParameter("id", id);
//                em.getTransaction().commit();
//                resultList.remove(i);
//            }
//        }
    }

//    private List<Address> addressList() {
//        EntityManager em = emf.createEntityManager();
//        TypedQuery<Address> addressList = (TypedQuery<Address>) em.createQuery("SELECT a FROM Address a", Address.class);
//        List<Address> resultList = addressList.getResultList();
//        return resultList;
//    }
    public PersonDTO deletePerson(String email) throws NotFoundException {
        EntityManager em = emf.createEntityManager();
        Person person = em.find(Person.class, email);
        if (person == null) {
            throw new NotFoundException("Could not delete, provided id does not exist");
        } else {
            try {
                em.getTransaction().begin();
                em.remove(person);
                em.getTransaction().commit();
            } finally {
                em.close();
            }
            return new PersonDTO(person);
        }
    }

    public void addHobbyToPerson(String email, long id) throws NotFoundException {
        EntityManager em = emf.createEntityManager();

        Person person = em.find(Person.class, email);
        Hobby hobby = em.find(Hobby.class, id);

        person.addHobby(hobby);

        if (person == null) {
            throw new NotFoundException("No person found");
        }
        try {
            em.getTransaction().begin();
            em.merge(person);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public void removeHobbyFromPerson(String email, long id) throws NotFoundException {
        EntityManager em = emf.createEntityManager();

        Person person = em.find(Person.class, email);
        Hobby hobby = em.find(Hobby.class, id);

        person.removeHobby(hobby);

        if (person == null) {
            throw new NotFoundException("No person found");
        }
        try {
            em.getTransaction().begin();
            em.merge(person);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
}
