
package dto;

import entities.Address;


public class AddressDTO {
    
    private String street;
    private String city;
    private int zipCode;

    public AddressDTO(Address address) {
        this.street = address.getStreet();
        this.city = address.getCity();
        this.zipCode = address.getZipCode();
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getZipCode() {
        return zipCode;
    }

    public void setZipCode(int zipCode) {
        this.zipCode = zipCode;
    }
    
}
