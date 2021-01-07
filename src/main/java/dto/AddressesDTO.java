
package dto;


import entities.Address;
import java.util.ArrayList;
import java.util.List;


public class AddressesDTO {
    
    List<AddressDTO> all = new ArrayList();

    public AddressesDTO(List<Address> addressEntities) {
        addressEntities.forEach((a) -> {
            all.add(new AddressDTO(a));
        });
    }

    public List<AddressDTO> getAll() {
        return all;
    }

    public void setAll(List<AddressDTO> all) {
        this.all = all;
    }
    
}
