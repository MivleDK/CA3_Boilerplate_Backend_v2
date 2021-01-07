
package dto;

import entities.Hobby;
import java.util.ArrayList;
import java.util.List;


public class HobbiesDTO {
    
    List<HobbyDTO> all = new ArrayList();

    public HobbiesDTO(List<Hobby> hobbiesEntities) {
        hobbiesEntities.forEach((h) -> {
            all.add(new HobbyDTO(h));
        });
    }

    public List<HobbyDTO> getAll() {
        return all;
    }

    public void setAll(List<HobbyDTO> all) {
        this.all = all;
    }
    

}
