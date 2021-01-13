package dto;

import entities.Address;
import entities.Hobby;
import entities.Person;
import java.util.ArrayList;
import java.util.List;

public class PersonDTO {

    private String email;
    private String phone;
    private String firstName;
    private String lastName;
    private String street;
    private String city;
    private int zipcode;

    private String password;

    private List<String> hobbyList;
  

    public PersonDTO(Person person) {
        this.email = person.getEmail();
        this.phone = person.getPhone();
        this.firstName = person.getFirstName();
        this.lastName = person.getLastName();
        this.street = person.getAddress().getStreet();
        this.city = person.getAddress().getCity();
        this.zipcode = person.getAddress().getZipCode();
        this.hobbyList = person.getHobbiesAsStrings();
        this.password = person.getUserPass();
    }

    public String getPassword() {
        return password;
    }
    
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public int getZipcode() {
        return zipcode;
    }

    public void setZipcode(int zipcode) {
        this.zipcode = zipcode;
    }

    public List<String> getHobbyList() {
        return hobbyList;
    }

    public void setHobbyList(List<String> hobbyList) {
        this.hobbyList = hobbyList;
    }

}
