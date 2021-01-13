package facades;

import dto.AddressDTO;
import dto.AddressesDTO;
import entities.Address;
import errorhandling.NotFoundException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class AddressFacade {

    private static EntityManagerFactory emf;
    private static AddressFacade instance;

    private AddressFacade() {
    }

    /**
     *
     * @param _emf
     * @return the instance of this facade.
     */
    public static AddressFacade getAddressFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new AddressFacade();
        }
        return instance;
    }

    public long getAddressCount() {
        EntityManager em = emf.createEntityManager();
        try {
            long addressCount = (long) em.createQuery("SELECT COUNT(a) FROM Address a").getSingleResult();
            return addressCount;
        } finally {
            em.close();
        }
    }

    public AddressDTO getAddressById(long id) throws NotFoundException {
        EntityManager em = emf.createEntityManager();

        Address address = em.find(Address.class, id);
       
        if (address == null) {
            throw new NotFoundException("No address found with the given ID");
        } else {
            try {
                return new AddressDTO(address);
            } finally {
                em.close();
            }
        }

    }

    public AddressesDTO getAllAddresses() throws NotFoundException {

        EntityManager em = emf.createEntityManager();
        AddressesDTO asDTO;
        try {
            asDTO = new AddressesDTO(em.createQuery("SELECT a FROM Address a").getResultList());
        } catch (Exception e) {
            throw new NotFoundException("No connection to the database");
        } finally {
            em.close();
        }
        return asDTO;

    }

}
