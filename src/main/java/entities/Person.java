package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.mindrot.jbcrypt.BCrypt;

@Entity
@Table(name = "persons")
@NamedQuery(name = "Person.deleteAllRows", query = "DELETE from Person")
public class Person implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "email", length = 50)
    private String email;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "user_pass")
    private String userPass;

    @JoinTable(name = "user_roles", joinColumns = {
        @JoinColumn(name = "email", referencedColumnName = "email")}, inverseJoinColumns = {
        @JoinColumn(name = "role_name", referencedColumnName = "role_name")})
    @ManyToMany
    private List<Role> roleList = new ArrayList<>();


    @JoinTable(name = "person_hobbies", joinColumns = {
        @JoinColumn(name = "email", referencedColumnName = "email")}, inverseJoinColumns = {
        @JoinColumn(name = "id", referencedColumnName = "id")})
    @ManyToMany(cascade = CascadeType.PERSIST)

    
    private List<Hobby> hobbyList;

    @Column(name = "phone", length = 11)
    private String phone;

    @Column(name = "firstname", length = 30)
    private String firstName;

    @Column(name = "lastname", length = 30)
    private String lastName;

    @JoinColumn(name = "address_id")
    @ManyToOne(cascade = CascadeType.PERSIST)
    private Address address;

    public Person() {
    }

    public Person(String email, String userPass, String phone, String firstName, String lastName) {
        this.phone = phone;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.userPass = BCrypt.hashpw(userPass, BCrypt.gensalt(12));
        this.hobbyList = new ArrayList<>();
    }

    //TODO Change when password is hashed
    public boolean verifyPassword(String pw) {
        return (BCrypt.checkpw(pw, this.userPass));
    }

    public Person(String userName, String userPass) {
        this.email = userName;
        this.userPass = BCrypt.hashpw(userPass, BCrypt.gensalt(12));
    }
    
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserPass() {
        return this.userPass;
    }

    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }

    public List<String> getHobbiesAsStrings() {
        if (hobbyList.isEmpty()) {
            return null;
        }
        List<String> hobbiesAsStrings = new ArrayList<>();
        hobbyList.forEach((hobby) -> {
            hobbiesAsStrings.add(hobby.getName());
        });
        return hobbiesAsStrings;
    }

    public List<Hobby> getHobbyList() {
        return hobbyList;
    }

    public void addHobby(Hobby hobby) {
        if(hobby != null){
            this.hobbyList.add(hobby);
            hobby.getPersonList().add(this);
        }

    }
    
    public void removeHobby(Hobby hobby) {
        if(hobby != null){
            hobbyList.remove(hobby);
            hobby.getPersonList().remove(this);
        }

    }

    public void setHobbyList(List<Hobby> hobbyList) {
        this.hobbyList = hobbyList;
    }

    public List<String> getRolesAsStrings() {
        if (roleList.isEmpty()) {
            return null;
        }
        List<String> rolesAsStrings = new ArrayList<>();
        roleList.forEach((role) -> {
            rolesAsStrings.add(role.getRoleName());
        });
        return rolesAsStrings;
    }

    public List<Role> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<Role> roleList) {
        this.roleList = roleList;
    }

    public void addRole(Role userRole) {
        roleList.add(userRole);
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

    public void setAddress(Address address) {
        this.address = address;
    }

    public Address getAddress() {
        return address;
    }

}
